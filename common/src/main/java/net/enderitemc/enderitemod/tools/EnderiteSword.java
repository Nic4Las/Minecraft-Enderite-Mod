package net.enderitemc.enderitemod.tools;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.component.EnderiteChargeComponent;
import net.enderitemc.enderitemod.component.EnderiteDataComponents;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Math;
import org.jspecify.annotations.Nullable;

public class EnderiteSword extends Item {

    public EnderiteSword(ToolMaterial material, int attackDamage, float attackSpeed, Item.Settings settings) {
        super(settings.sword(material, attackDamage, attackSpeed));
    }

    public static int getMaxDistance() {
        return Math.max(Math.min(EnderiteMod.CONFIG.tools.enderiteSwordTeleportDistance, 256), 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);
        if(entity instanceof ServerPlayerEntity cpe && cpe.isSneaking()) {
            Random random = Random.create();
            if (EquipmentSlot.MAINHAND.equals(slot) && random.nextFloat() > 0.25f) {
                // When player sneaking, sword in mainhand and every other tick
                Vec3d eyePos = cpe.getEyePos();
                Vec3d lookVec = cpe.getRotationVector();
                Vec3d targetPos = eyePos.add(lookVec.multiply(getMaxDistance()));
                Vec3d validTeleportPos = findTeleportTarget((ServerWorld) world, cpe, cpe.getEyePos(), lookVec, targetPos, getMaxDistance());

                if(validTeleportPos != null) {
                    // Display target position with portal particles
                    world.spawnParticles(
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
    public ActionResult use(World world, PlayerEntity playerEntity, Hand hand) {
        // 1. Shared Condition: Must be sneaking
        if (!playerEntity.isSneaking()) {
            return ActionResult.PASS;
        }

        ItemStack stack = playerEntity.getStackInHand(hand);
        int currentCharge = stack.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0).intValue();
        boolean isCreative = playerEntity.getAbilities().creativeMode;

        // 2. Shared Condition: Must have charge
        if (currentCharge <= 0 && !isCreative) {
            return ActionResult.PASS;
        }

        // 3. Client Side: Animation Trigger
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        // --- SERVER SIDE CALCULATION ---
        Vec3d eyePos = playerEntity.getEyePos();
        Vec3d lookVec = playerEntity.getRotationVector();
        Vec3d targetPos = eyePos.add(lookVec.multiply(getMaxDistance()));

        Vec3d validTeleportPos = findTeleportTarget(world, playerEntity, eyePos, lookVec, targetPos, getMaxDistance());

        if (validTeleportPos != null) {
            // FIX 1: Prevent consuming charge if the target is the player's current location
            // (e.g. looking at feet or stuck in a hole)
            if (validTeleportPos.distanceTo(Vec3d.of(playerEntity.getBlockPos())) < 2.0) {
                return ActionResult.FAIL;
            }

            // FIX 2: Only consume charge if the teleport actually succeeds
            // The teleport method returns 'true' only if the entity was successfully moved.
            if (playerEntity.teleport(validTeleportPos.x, validTeleportPos.y, validTeleportPos.z, true)) {

                // Audio & Feedback
                world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                playerEntity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

                // Cooldown & Inventory
                playerEntity.getItemCooldownManager().set(stack, 30);

                if (!isCreative) {
                    stack.set(EnderiteDataComponents.TELEPORT_CHARGE.get(), EnderiteChargeComponent.of(currentCharge - 1));
                }

                world.sendEntityStatus(playerEntity, (byte) 46);

                return ActionResult.CONSUME;
            }
        }

        // Failure: Calculation failed or Teleport failed
        return ActionResult.FAIL;
    }

    /**
     * Scans for a valid standing spot near the target position.
     */
    private Vec3d findTeleportTarget(World world, PlayerEntity player, Vec3d startPos, Vec3d direction, Vec3d targetPos, double maxDist) {
        BlockPos rawTarget = BlockPos.ofFloored(targetPos);

        int worldMinY = world.getBottomY();
        int worldMaxY = world.getTopYInclusive();

        // Nether/Roof Logic
        if (world instanceof ServerWorld serverWorld && RespawnAnchorBlock.isUsable(serverWorld, player.getBlockPos())) {
            worldMaxY = 127;
        }

        int targetY = MathHelper.clamp(rawTarget.getY(), worldMinY, worldMaxY);

        // Clamp distance checks to target pos
        int minY = Math.max((int) (rawTarget.getY()-maxDist/2), worldMinY);
        int maxY = Math.min((int) (rawTarget.getY()+maxDist/2), worldMaxY);

        // Use Mutable BlockPos for performance in loops
        BlockPos.Mutable searchDown = new BlockPos.Mutable(rawTarget.getX(), targetY, rawTarget.getZ());
        BlockPos.Mutable searchUp = new BlockPos.Mutable(rawTarget.getX(), targetY + 1, rawTarget.getZ());

        double currentDist = maxDist;

        // Loop while we still have room to search (Backwards)
        while (currentDist > 2) {

            // 1. Check "Near" (Retracting the ray back towards player)
            currentDist--;
            Vec3d backPos = startPos.add(direction.multiply(currentDist));
            BlockPos backBlockPos = BlockPos.ofFloored(backPos);

            if (backBlockPos.getY() >= worldMinY && backBlockPos.getY() <= worldMaxY && checkBlocks(world, backBlockPos)) {
                return new Vec3d(backPos.x, Math.max(backPos.y, minY + 1), backPos.z);
            }
        }

        // Backup 1: Loop while we still have room to search (Up, Down)
        while (searchDown.getY() > minY || searchUp.getY() < maxY) {

            // 2. Check Downwards
            if (searchDown.getY() > minY) {
                if (checkBlocks(world, searchDown)) {
                    return Vec3d.ofBottomCenter(searchDown);
                }
                searchDown.move(Direction.DOWN);
            }

            // 3. Check Upwards
            if (searchUp.getY() < maxY) {
                if (checkBlocks(world, searchUp)) {
                    return Vec3d.ofBottomCenter(searchUp);
                }
                searchUp.move(Direction.UP);
            }
        }

        return null;
    }

    protected boolean checkBlocks(World world, BlockPos pos) {
        // Ensure floor is solid, and two blocks above are not solid (Head and Feet space)
        return world.getBlockState(pos.down()).blocksMovement()
            && !world.getBlockState(pos).blocksMovement()
            && !world.getBlockState(pos.up()).blocksMovement();
    }

}