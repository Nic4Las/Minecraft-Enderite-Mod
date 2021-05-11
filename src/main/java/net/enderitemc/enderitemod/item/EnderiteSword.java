package net.enderitemc.enderitemod.item;

import net.minecraft.item.SwordItem;

import java.util.List;

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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class EnderiteSword extends SwordItem {

    public int superAufladung;

    public EnderiteSword(IItemTier material, int attackDamage, float attackSpeed, Properties settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        if (playerEntity.isShiftKeyDown()) {
            Double distance = 30.0d;
            double yaw = (double) playerEntity.yHeadRot;
            double pitch = (double) playerEntity.xRot;

            // x: 1 = -90, -1 = 90
            // y: 1 = -90, -1 = 90
            // z: 1 = 0, -1 = 180
            double temp = Math.cos(Math.toRadians(pitch));
            double dX = temp * -Math.sin(Math.toRadians(yaw));
            double dY = -Math.sin(Math.toRadians(pitch));
            double dZ = temp * Math.cos(Math.toRadians(yaw));
            Vector3d position = playerEntity.position().add(0,
                    playerEntity.getEyeHeight() - playerEntity.getMyRidingOffset(), 0);
            Vector3d endPosition = new Vector3d(position.x + dX * distance, position.y + dY * distance,
                    position.z + dZ * distance);
            BlockPos blockPos = new BlockPos(endPosition.x, endPosition.y, endPosition.z);

            BlockPos[] blockPoses = { blockPos, blockPos.above(), blockPos };

            double down = endPosition.y;
            double maxDown = down - distance - 1 > 0 ? down - distance - 1 : 0;
            double up = endPosition.y + 1;
            double maxUp = 128;
            // if (playerEntity.getCommandSenderWorld().dimensionType().createDragonFight())
            // {
            // maxUp = up + distance - 1 < 127 ? up + distance - 1 : 127;
            // } else {
            maxUp = up + distance - 1 < 255 ? up + distance - 1 : 255;
            // }
            double near = distance;

            int slot = 0;
            if (playerEntity.getItemInHand(hand).getTag().contains("teleport_charge")) {
                slot = Integer.parseInt(playerEntity.getItemInHand(hand).getTag().get("teleport_charge").toString());

            }

            // Check to Teleport
            if (world.isAreaLoaded(blockPos, 1) && (slot > 0 || playerEntity.abilities.instabuild)) {
                int foundSpace = 0;

                while (foundSpace == 0 && (blockPoses[0].getY() > maxDown || blockPoses[1].getY() < maxUp)) {
                    // CheckDown
                    if (blockPoses[0].getY() > maxDown) {
                        if (checkBlocks(world, blockPoses[0])) {
                            foundSpace = 1;
                        } else {
                            --down;
                            blockPoses[0] = blockPoses[0].below();
                        }
                    }
                    // CheckUp
                    if (blockPoses[1].getY() < maxUp) {
                        if (checkBlocks(world, blockPoses[1])) {
                            foundSpace = 2;
                        } else {
                            ++up;
                            blockPoses[1] = blockPoses[1].above();
                        }
                    }
                    // CheckNear
                    if (near > 2) {
                        if (checkBlocks(world, blockPoses[2])) {
                            foundSpace = 3;
                        } else {
                            --near;
                            blockPoses[2] = blockPoses[2].offset(-dX, -dY, -dZ);
                        }
                    }
                }
                if (foundSpace == 0 && !world.getBlockState(blockPos).getMaterial().blocksMotion()
                        && !world.getBlockState(blockPos.above()).getMaterial().blocksMotion()) {
                    foundSpace = 4;
                }
                // world.rayTraceBlock(position, endPosition, blockPos, playerEntity.shape,
                // state)

                // System.out.printf("| %s, %s, %s, %s |\r\n", foundSpace, down, up, near);

                if (foundSpace > 0 && (position.y + dY * near) < maxUp && (position.y + dY * near) > maxDown) {
                    playerEntity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    switch (foundSpace) {
                        case 1: // Down
                            playerEntity.teleportToWithTicket(endPosition.x, down > maxDown ? down : maxDown,
                                    endPosition.z);
                            break;
                        case 2: // Up
                            playerEntity.teleportToWithTicket(endPosition.x, up < maxUp ? up : maxUp, endPosition.z);
                            break;
                        case 4: // Air
                            near = distance / 2;
                        case 3: // Near
                            playerEntity.teleportToWithTicket(position.x + dX * near, position.y + dY * near,
                                    position.z + dZ * near);
                            break;
                    }
                    playerEntity.getCooldowns().addCooldown(this, 30);
                    // if (!playerEntity.abilities.creativeMode) {
                    // playerEntity.inventory.getStack(slot).decrement(1);
                    // }
                    if (!playerEntity.abilities.instabuild) {
                        playerEntity.getItemInHand(hand).getTag().putInt("teleport_charge", slot - 1);
                    }
                    world.broadcastEntityEvent(playerEntity, (byte) 46);
                    playerEntity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    playerEntity.fallDistance = 0;
                }
            } else {
                return new ActionResult<>(ActionResultType.FAIL, playerEntity.getItemInHand(hand));
            }

        } else {
            return new ActionResult<>(ActionResultType.FAIL, playerEntity.getItemInHand(hand));
        }

        return new ActionResult<>(ActionResultType.SUCCESS, playerEntity.getItemInHand(hand));
    }

    protected boolean checkBlocks(World world, BlockPos pos) {
        if (world.getBlockState(pos.below()).getMaterial().blocksMotion()
                && !world.getBlockState(pos).getMaterial().blocksMotion()
                && !world.getBlockState(pos.above()).getMaterial().blocksMotion()) {
            return true;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(itemStack, worldIn, tooltip, flagIn);
        if (itemStack.getOrCreateTag().contains("teleport_charge")) {
            String charge = itemStack.getTag().get("teleport_charge").toString();
            tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.charge")
                    .withStyle(new TextFormatting[] { TextFormatting.DARK_AQUA })
                    .append(new StringTextComponent(": " + charge)));
        } else {
            tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.charge")
                    .withStyle(new TextFormatting[] { TextFormatting.DARK_AQUA })
                    .append(new StringTextComponent(":0")));
        }

        tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.tooltip1")
                .withStyle(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
        tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.tooltip2")
                .withStyle(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
        tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.tooltip3")
                .withStyle(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
    }

    @Override
    public void onCraftedBy(ItemStack stack, World world, PlayerEntity player) {
    }

}