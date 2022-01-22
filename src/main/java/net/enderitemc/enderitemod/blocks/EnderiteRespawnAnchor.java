package net.enderitemc.enderitemod.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion.DestructionType;

public class EnderiteRespawnAnchor extends RespawnAnchorBlock {

    public EnderiteRespawnAnchor(AbstractBlock.Settings settings) {
        super(settings);
    }

    private static boolean isChargeItem(ItemStack stack) {
        return stack.getItem() == Items.ENDER_PEARL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (hand == Hand.MAIN_HAND && !isChargeItem(itemStack) && isChargeItem(player.getStackInHand(Hand.OFF_HAND))) {
            return ActionResult.PASS;
        } else if (isChargeItem(itemStack) && canCharge(state)) {
            charge(world, pos, state);
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            return ActionResult.success(world.isClient);
        } else if ((Integer) state.get(CHARGES) == 0) {
            return ActionResult.PASS;
        } else if (!isNether(world)) {
            if (!world.isClient) {
                this.explode(state, world, pos);
            }

            return ActionResult.success(world.isClient);
        } else {
            if (!world.isClient) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                if (serverPlayerEntity.getSpawnPointDimension() != world.getRegistryKey()
                        || !serverPlayerEntity.getSpawnPointPosition().equals(pos)) {
                    serverPlayerEntity.setSpawnPoint(world.getRegistryKey(), pos, 0.0f, false, true);
                    world.playSound((PlayerEntity) null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
                            (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.CONSUME;
        }
    }

    public static boolean isNether(World world) {
        return world.getDimension().hasEnderDragonFight();
    }

    private static boolean canCharge(BlockState state) {
        return (Integer) state.get(CHARGES) < 4;
    }

    private void explode(BlockState state, World world, final BlockPos explodedPos) {
        world.removeBlock(explodedPos, false);
        world.createExplosion(null, explodedPos.getX(), explodedPos.getY(), explodedPos.getZ(), 6.9F, true,
                DestructionType.DESTROY);
    }

}