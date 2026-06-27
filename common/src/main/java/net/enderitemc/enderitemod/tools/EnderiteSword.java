package net.enderitemc.enderitemod.tools;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.component.EnderiteChargeComponent;
import net.enderitemc.enderitemod.component.EnderiteDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;
import org.jspecify.annotations.Nullable;

public class EnderiteSword extends Item {

    public EnderiteSword(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings) {
        super(settings.sword(material, attackDamage, attackSpeed));
    }

    public static int getMaxDistance() {
        return Math.max(Math.min(EnderiteMod.CONFIG.tools.enderiteSwordTeleportDistance, 256), 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);
        if(entity instanceof ServerPlayer cpe && cpe.isShiftKeyDown()) {
            RandomSource random = RandomSource.create();
            if (EquipmentSlot.MAINHAND.equals(slot) && random.nextFloat() > 0.25f) {
                // When player sneaking, sword in mainhand and every other tick
                Vec3 eyePos = cpe.getEyePosition();
                Vec3 lookVec = cpe.getLookAngle();
                Vec3 targetPos = eyePos.add(lookVec.scale(getMaxDistance()));
                Vec3 validTeleportPos = findTeleportTarget((ServerLevel) world, cpe, cpe.getEyePosition(), lookVec, targetPos, getMaxDistance());

                if(validTeleportPos != null) {
                    // Display target position with portal particles
                    world.sendParticles(
                        cpe,
                        ParticleTypes.PORTAL,
                        false, false,
                        validTeleportPos.x, validTeleportPos.y, validTeleportPos.z,
                        1,
                        0, 0, 0,
                        .5
                    );
                }
            }
        }
    }

    @Override
    public InteractionResult use(Level world, Player playerEntity, InteractionHand hand) {
        // 1. Shared Condition: Must be sneaking
        if (!playerEntity.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        ItemStack stack = playerEntity.getItemInHand(hand);
        int currentCharge = stack.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0).intValue();
        boolean isCreative = playerEntity.getAbilities().instabuild;

        // 2. Shared Condition: Must have charge
        if (currentCharge <= 0 && !isCreative) {
            return InteractionResult.PASS;
        }

        // 3. Client Side: Animation Trigger
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        // --- SERVER SIDE CALCULATION ---
        Vec3 eyePos = playerEntity.getEyePosition();
        Vec3 lookVec = playerEntity.getLookAngle();
        Vec3 targetPos = eyePos.add(lookVec.scale(getMaxDistance()));

        Vec3 validTeleportPos = findTeleportTarget(world, playerEntity, eyePos, lookVec, targetPos, getMaxDistance());

        if (validTeleportPos != null) {
            // FIX 1: Prevent consuming charge if the target is the player's current location
            // (e.g. looking at feet or stuck in a hole)
            if (validTeleportPos.distanceTo(Vec3.atLowerCornerOf(playerEntity.blockPosition())) < 2.0) {
                return InteractionResult.FAIL;
            }

            // FIX 2: Only consume charge if the teleport actually succeeds
            // The teleport method returns 'true' only if the entity was successfully moved.
            if (playerEntity.randomTeleport(validTeleportPos.x, validTeleportPos.y, validTeleportPos.z, true)) {

                // Audio & Feedback
                world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                    SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
                playerEntity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);

                // Cooldown & Inventory
                playerEntity.getCooldowns().addCooldown(stack, 30);

                if (!isCreative) {
                    stack.set(EnderiteDataComponents.TELEPORT_CHARGE.get(), EnderiteChargeComponent.of(currentCharge - 1));
                }

                world.broadcastEntityEvent(playerEntity, (byte) 46);

                return InteractionResult.CONSUME;
            }
        }

        // Failure: Calculation failed or Teleport failed
        return InteractionResult.FAIL;
    }

    /**
     * Scans for a valid standing spot near the target position.
     */
    private Vec3 findTeleportTarget(Level world, Player player, Vec3 startPos, Vec3 direction, Vec3 targetPos, double maxDist) {
        BlockPos rawTarget = BlockPos.containing(targetPos);

        int worldMinY = world.getMinY();
        int worldMaxY = world.getMaxY();

        // Nether/Roof Logic
        if (world instanceof ServerLevel serverWorld && RespawnAnchorBlock.canSetSpawn(serverWorld, player.blockPosition())) {
            worldMaxY = 127;
        }

        int targetY = Mth.clamp(rawTarget.getY(), worldMinY, worldMaxY);

        // Clamp distance checks to target pos
        int minY = Math.max((int) (rawTarget.getY()-maxDist/2), worldMinY);
        int maxY = Math.min((int) (rawTarget.getY()+maxDist/2), worldMaxY);

        // Use Mutable BlockPos for performance in loops
        BlockPos.MutableBlockPos searchDown = new BlockPos.MutableBlockPos(rawTarget.getX(), targetY, rawTarget.getZ());
        BlockPos.MutableBlockPos searchUp = new BlockPos.MutableBlockPos(rawTarget.getX(), targetY + 1, rawTarget.getZ());

        double currentDist = maxDist;

        // Loop while we still have room to search (Backwards)
        while (currentDist > 2) {

            // 1. Check "Near" (Retracting the ray back towards player)
            currentDist--;
            Vec3 backPos = startPos.add(direction.scale(currentDist));
            BlockPos backBlockPos = BlockPos.containing(backPos);

            if (backBlockPos.getY() >= worldMinY && backBlockPos.getY() <= worldMaxY && checkBlocks(world, backBlockPos)) {
                return new Vec3(backPos.x, Math.max(backPos.y, minY + 1), backPos.z);
            }
        }

        // Backup 1: Loop while we still have room to search (Up, Down)
        while (searchDown.getY() > minY || searchUp.getY() < maxY) {

            // 2. Check Downwards
            if (searchDown.getY() > minY) {
                if (checkBlocks(world, searchDown)) {
                    return Vec3.atBottomCenterOf(searchDown);
                }
                searchDown.move(Direction.DOWN);
            }

            // 3. Check Upwards
            if (searchUp.getY() < maxY) {
                if (checkBlocks(world, searchUp)) {
                    return Vec3.atBottomCenterOf(searchUp);
                }
                searchUp.move(Direction.UP);
            }
        }

        return null;
    }

    protected boolean checkBlocks(Level world, BlockPos pos) {
        // Ensure floor is solid, and two blocks above are not solid (Head and Feet space)
        return world.getBlockState(pos.below()).blocksMotion()
            && !world.getBlockState(pos).blocksMotion()
            && !world.getBlockState(pos.above()).blocksMotion();
    }

}