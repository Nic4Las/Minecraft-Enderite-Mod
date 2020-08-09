package net.enderitemc.enderitemod.item;

import net.minecraft.item.SwordItem;

import java.util.List;

import com.mojang.brigadier.LiteralMessage;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class EnderiteSword extends SwordItem {

    public int superAufladung;

    public EnderiteSword(IItemTier material, int attackDamage, float attackSpeed, Properties settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerEntity, Hand hand) {

        if (playerEntity.isSneaking()) {
            Double distance = 30.0d;
            double yaw = (double) playerEntity.rotationYawHead;
            double pitch = (double) playerEntity.rotationPitch;

            // x: 1 = -90, -1 = 90
            // y: 1 = -90, -1 = 90
            // z: 1 = 0, -1 = 180
            double temp = Math.cos(Math.toRadians(pitch));
            double dX = temp * -Math.sin(Math.toRadians(yaw));
            double dY = -Math.sin(Math.toRadians(pitch));
            double dZ = temp * Math.cos(Math.toRadians(yaw));
            Vector3d position = playerEntity.getPositionVec().add(0,
                    playerEntity.getEyeHeight() - playerEntity.getYOffset(), 0);
            Vector3d endPosition = new Vector3d(position.x + dX * distance, position.y + dY * distance,
                    position.z + dZ * distance);
            BlockPos blockPos = new BlockPos(endPosition.x, endPosition.y, endPosition.z);

            BlockPos[] blockPoses = { blockPos, blockPos.up(), blockPos };

            double down = endPosition.y;
            double maxDown = down - distance - 1 > 0 ? down - distance - 1 : 0;
            double up = endPosition.y + 1;
            double maxUp = 128;
            if (playerEntity.getEntityWorld().func_230315_m_().func_236046_h_()) {
                maxUp = up + distance - 1 < 127 ? up + distance - 1 : 127;
            } else {
                maxUp = up + distance - 1 < 255 ? up + distance - 1 : 255;
            }
            double near = distance;

            int slot = 0;
            if (playerEntity.getHeldItem(hand).getTag().contains("teleport_charge")) {
                slot = Integer.parseInt(playerEntity.getHeldItem(hand).getTag().get("teleport_charge").toString());

            }

            // Check to Teleport
            if (world.isAreaLoaded(blockPos, 1) && (slot > 0 || playerEntity.abilities.isCreativeMode)) {
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
                            blockPoses[2] = blockPoses[2].add(-dX, -dY, -dZ);
                        }
                    }
                }
                if (foundSpace == 0 && !world.getBlockState(blockPos).getMaterial().blocksMovement()
                        && !world.getBlockState(blockPos.up()).getMaterial().blocksMovement()) {
                    foundSpace = 4;
                }
                // world.rayTraceBlock(position, endPosition, blockPos, playerEntity.shape,
                // state)

                // System.out.printf("| %s, %s, %s, %s |\r\n", foundSpace, down, up, near);

                if (foundSpace > 0 && (position.y + dY * near) < maxUp && (position.y + dY * near) > maxDown) {
                    playerEntity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    switch (foundSpace) {
                        case 1: // Down
                            playerEntity.teleportKeepLoaded(endPosition.x, down > maxDown ? down : maxDown,
                                    endPosition.z);
                            break;
                        case 2: // Up
                            playerEntity.teleportKeepLoaded(endPosition.x, up < maxUp ? up : maxUp, endPosition.z);
                            break;
                        case 4: // Air
                            near = distance / 2;
                        case 3: // Near
                            playerEntity.teleportKeepLoaded(position.x + dX * near, position.y + dY * near,
                                    position.z + dZ * near);
                            break;
                    }
                    playerEntity.getCooldownTracker().setCooldown(this, 30);
                    // if (!playerEntity.abilities.creativeMode) {
                    // playerEntity.inventory.getStack(slot).decrement(1);
                    // }
                    if (!playerEntity.abilities.isCreativeMode) {
                        playerEntity.getHeldItem(hand).getTag().putInt("teleport_charge", slot - 1);
                    }
                    world.setEntityState(playerEntity, (byte) 46);
                    playerEntity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }

        } else

        {

        }

        return new ActionResult<>(ActionResultType.SUCCESS, playerEntity.getHeldItem(hand));
    }

    protected boolean checkBlocks(World world, BlockPos pos) {
        if (world.getBlockState(pos.down()).getMaterial().blocksMovement()
                && !world.getBlockState(pos).getMaterial().blocksMovement()
                && !world.getBlockState(pos.up()).getMaterial().blocksMovement()) {
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack itemStack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(itemStack, worldIn, tooltip, flagIn);
        if (itemStack.getTag().contains("teleport_charge")) {
            String charge = itemStack.getTag().get("teleport_charge").toString();
            tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.charge")
                    .func_240701_a_(new TextFormatting[] { TextFormatting.DARK_AQUA })
                    .func_230529_a_(new StringTextComponent(": " + charge)));
        } else {
            tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.charge")
                    .func_240701_a_(new TextFormatting[] { TextFormatting.DARK_AQUA })
                    .func_230529_a_(new StringTextComponent(":0")));
        }

        tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.tooltip1")
                .func_240701_a_(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
        tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.tooltip2")
                .func_240701_a_(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
        tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.tooltip3")
                .func_240701_a_(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
    }

    @Override
    public void onCreated(ItemStack stack, World world, PlayerEntity player) {
    }

}