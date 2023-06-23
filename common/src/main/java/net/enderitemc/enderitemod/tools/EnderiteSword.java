package net.enderitemc.enderitemod.tools;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EnderiteSword extends SwordItem {

    public int superAufladung;

    public EnderiteSword(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        if (playerEntity.isSneaking()) {
            Double distance = 30.0d;
            double yaw = (double) playerEntity.headYaw;
            double pitch = (double) playerEntity.getPitch();

            // x: 1 = -90, -1 = 90
            // y: 1 = -90, -1 = 90
            // z: 1 = 0, -1 = 180
            double temp = Math.cos(Math.toRadians(pitch));
            double dX = temp * -Math.sin(Math.toRadians(yaw));
            double dY = -Math.sin(Math.toRadians(pitch));
            double dZ = temp * Math.cos(Math.toRadians(yaw));
            Vec3d position = playerEntity.getPos().add(0, playerEntity.getEyeY() - playerEntity.getY(), 0);
            Vec3d endPosition = new Vec3d(position.x + dX * distance, position.y + dY * distance,
                    position.z + dZ * distance);
            BlockPos blockPos = BlockPos.ofFloored(endPosition.x, endPosition.y, endPosition.z);

            BlockPos[] blockPoses = { blockPos, blockPos.up(), blockPos };

            double down = endPosition.y;
            double maxDown = down - distance - 1 > world.getBottomY() ? down - distance - 1 : world.getBottomY();
            double up = endPosition.y + 1;
            double maxUp = 128;
            if (playerEntity.getEntityWorld().getDimension().respawnAnchorWorks()) {
                maxUp = up + distance - 1 < 127 ? up + distance - 1 : 127;
            } else {
                maxUp = up + distance - 1 < world.getTopY() ? up + distance - 1 : world.getTopY();
            }
            double near = distance;

            int slot = 0;
            if (playerEntity.getStackInHand(hand).getNbt().contains("teleport_charge")) {
                slot = Integer.parseInt(playerEntity.getStackInHand(hand).getNbt().get("teleport_charge").asString());

            }

            // Check to Teleport
            if (world.isChunkLoaded(blockPos) && (slot > 0 || playerEntity.getAbilities().creativeMode)) {
                int foundSpace = 0;

                while (foundSpace == 0 && (blockPoses[0].getY() > maxDown || blockPoses[1].getY() < maxUp)) {
                    // CheckDown
                    if (blockPoses[0].getY() > maxDown) {
                        if (checkBlocks(world, blockPoses[0])) {
                            foundSpace = 1;
                        } else {
                            --down;
                            blockPoses[0] = blockPoses[0].down();
                        }
                    }
                    // CheckUp
                    if (blockPoses[1].getY() < maxUp) {
                        if (checkBlocks(world, blockPoses[1])) {
                            foundSpace = 2;
                        } else {
                            ++up;
                            blockPoses[1] = blockPoses[1].up();
                        }
                    }
                    // CheckNear
                    if (near > 2) {
                        if (checkBlocks(world, blockPoses[2])) {
                            foundSpace = 3;
                        } else {
                            --near;
                            blockPoses[2] = blockPoses[2].add((int) Math.floor(-dX),
                                    (int) Math.floor(-dY),
                                    (int) Math.floor(-dZ));
                        }
                    }
                }
                if (foundSpace == 0 && !world.getBlockState(blockPos).blocksMovement()
                        && !world.getBlockState(blockPos.up()).blocksMovement()) {
                    foundSpace = 4;
                }
                // world.rayTraceBlock(position, endPosition, blockPos, playerEntity.shape,
                // state)

                // System.out.printf("| %s, %s, %s, %s |\r\n", foundSpace, down, up, near);

                if (foundSpace > 0 && (position.y + dY * near) < maxUp && (position.y + dY * near) > maxDown) {
                    playerEntity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    switch (foundSpace) {
                        case 1: // Down
                            playerEntity.teleport(endPosition.x, down > maxDown ? down : maxDown, endPosition.z, true);
                            break;
                        case 2: // Up
                            playerEntity.teleport(endPosition.x, up < maxUp ? up : maxUp, endPosition.z, true);
                            break;
                        case 4: // Air
                            near = distance / 2;
                        case 3: // Near
                            down = position.y + dY * near;
                            down = down > world.getBottomY() ? down : world.getBottomY() + 1;
                            playerEntity.teleport(position.x + dX * near, down,
                                    position.z + dZ * near, true);
                            break;
                    }
                    playerEntity.getItemCooldownManager().set(this, 30);
                    // if (!playerEntity.getAbilities().creativeMode) {
                    // playerEntity.inventory.getStack(slot).decrement(1);
                    // }
                    if (!playerEntity.getAbilities().creativeMode) {
                        playerEntity.getStackInHand(hand).getNbt().putInt("teleport_charge", slot - 1);
                    }
                    world.sendEntityStatus(playerEntity, (byte) 46);
                    world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    playerEntity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            } else {
                return new TypedActionResult<>(ActionResult.FAIL, playerEntity.getStackInHand(hand));
            }

        } else {
            return new TypedActionResult<>(ActionResult.FAIL, playerEntity.getStackInHand(hand));
        }

        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
    }

    protected boolean checkBlocks(World world, BlockPos pos) {
        if (world.getBlockState(pos.down()).blocksMovement()
                && !world.getBlockState(pos).blocksMovement()
                && !world.getBlockState(pos.up()).blocksMovement()) {
            return true;
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (itemStack.getNbt().contains("teleport_charge")) {
            String charge = itemStack.getNbt().get("teleport_charge").toString();
            tooltip.add(Text.translatable("item.enderitemod.enderite_sword.charge")
                    .formatted(new Formatting[] { Formatting.DARK_AQUA }).append(Text.literal(": " + charge)));
        } else {
            tooltip.add(Text.translatable("item.enderitemod.enderite_sword.charge")
                    .formatted(new Formatting[] { Formatting.DARK_AQUA }).append(Text.literal(": 0")));
        }
        tooltip.add(Text.translatable("item.enderitemod.enderite_sword.tooltip1")
                .formatted(new Formatting[] { Formatting.GRAY, Formatting.ITALIC }));
        tooltip.add(Text.translatable("item.enderitemod.enderite_sword.tooltip2")
                .formatted(new Formatting[] { Formatting.GRAY, Formatting.ITALIC }));
        tooltip.add(Text.translatable("item.enderitemod.enderite_sword.tooltip3")
                .formatted(new Formatting[] { Formatting.GRAY, Formatting.ITALIC }));

    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
    }

}