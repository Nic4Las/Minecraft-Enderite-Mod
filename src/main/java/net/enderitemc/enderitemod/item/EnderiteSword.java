package net.enderitemc.enderitemod.item;

import net.minecraft.world.item.SwordItem;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class EnderiteSword extends SwordItem {

    public int superAufladung;

    public EnderiteSword(Tier material, int attackDamage, float attackSpeed, Properties settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {

        if (playerEntity.isShiftKeyDown()) {
            Double distance = 30.0d;
            double yaw = (double) playerEntity.getYRot();
            double pitch = (double) playerEntity.getXRot();

            // x: 1 = -90, -1 = 90
            // y: 1 = -90, -1 = 90
            // z: 1 = 0, -1 = 180
            double temp = Math.cos(Math.toRadians(pitch));
            double dX = temp * -Math.sin(Math.toRadians(yaw));
            double dY = -Math.sin(Math.toRadians(pitch));
            double dZ = temp * Math.cos(Math.toRadians(yaw));
            Vec3 position = playerEntity.position().add(0,
                    playerEntity.getEyeHeight() - playerEntity.getMyRidingOffset(), 0);
            Vec3 endPosition = new Vec3(position.x + dX * distance, position.y + dY * distance,
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
            if (world.isAreaLoaded(blockPos, 1) && (slot > 0 || playerEntity.getAbilities().instabuild)) {
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
                    if (!playerEntity.getAbilities().instabuild) {
                        playerEntity.getItemInHand(hand).getTag().putInt("teleport_charge", slot - 1);
                    }
                    world.broadcastEntityEvent(playerEntity, (byte) 46);
                    playerEntity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    playerEntity.fallDistance = 0;
                }
            } else {
                return new InteractionResultHolder<>(InteractionResult.FAIL, playerEntity.getItemInHand(hand));
            }

        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, playerEntity.getItemInHand(hand));
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerEntity.getItemInHand(hand));
    }

    protected boolean checkBlocks(Level world, BlockPos pos) {
        if (world.getBlockState(pos.below()).getMaterial().blocksMotion()
                && !world.getBlockState(pos).getMaterial().blocksMotion()
                && !world.getBlockState(pos.above()).getMaterial().blocksMotion()) {
            return true;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, worldIn, tooltip, flagIn);
        if (itemStack.getOrCreateTag().contains("teleport_charge")) {
            String charge = itemStack.getTag().get("teleport_charge").toString();
            tooltip.add(new TranslatableComponent("item.enderitemod.enderite_sword.charge")
                    .withStyle(new ChatFormatting[] { ChatFormatting.DARK_AQUA })
                    .append(new TextComponent(": " + charge)));
        } else {
            tooltip.add(new TranslatableComponent("item.enderitemod.enderite_sword.charge")
                    .withStyle(new ChatFormatting[] { ChatFormatting.DARK_AQUA })
                    .append(new TextComponent(":0")));
        }

        tooltip.add(new TranslatableComponent("item.enderitemod.enderite_sword.tooltip1")
                .withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC }));
        tooltip.add(new TranslatableComponent("item.enderitemod.enderite_sword.tooltip2")
                .withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC }));
        tooltip.add(new TranslatableComponent("item.enderitemod.enderite_sword.tooltip3")
                .withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC }));
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level world, Player player) {
    }

}