package net.enderitemc.enderitemod.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class EnderiteRespawnAnchor extends RespawnAnchorBlock {

    public EnderiteRespawnAnchor(Properties properties) {
        super(properties);
    }

    private static boolean isChargeItem(ItemStack stack) {
        return stack.getItem() == Items.ENDER_PEARL;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (hand == InteractionHand.MAIN_HAND && !isChargeItem(itemStack) && isChargeItem(player.getItemInHand(InteractionHand.OFF_HAND))) {
            return InteractionResult.PASS;
        } else if (isChargeItem(itemStack) && canCharge(state)) {
            charge(world, pos, state);
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        } else if ((Integer) state.getValue(CHARGE) == 0) {
            return InteractionResult.PASS;
        } else if (!isNether(world)) {
            if (!world.isClientSide) {
                this.explode(state, world, pos);
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        } else {
            if (!world.isClientSide) {
                ServerPlayer serverPlayerEntity = (ServerPlayer) player;
                if (serverPlayerEntity.getRespawnDimension() != world.dimension()
                        || !serverPlayerEntity.getRespawnPosition().equals(pos)) {
                    serverPlayerEntity.setRespawnPosition(world.dimension(), pos, 0.0f, false, true);
                    world.playSound((Player) null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
                            (double) pos.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS,
                            1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }

            return InteractionResult.CONSUME;
        }
    }

    public static boolean isNether(Level world) {
        return world.dimensionType().createDragonFight(); // world.getDimesnion().hasEnderDragonFight()
    }

    private static boolean canCharge(BlockState state) {
        return (Integer) state.getValue(CHARGE) < 4;
    }

    private void explode(BlockState state, Level world, final BlockPos explodedPos) {
        world.removeBlock(explodedPos, false);
        world.explode(null, explodedPos.getX(), explodedPos.getY(), explodedPos.getZ(), 6.9F, true,
                Explosion.BlockInteraction.DESTROY);
    }
}
