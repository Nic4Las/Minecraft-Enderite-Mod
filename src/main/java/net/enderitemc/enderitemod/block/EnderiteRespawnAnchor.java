package net.enderitemc.enderitemod.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EnderiteRespawnAnchor extends RespawnAnchorBlock {

    public EnderiteRespawnAnchor(Properties properties) {
        super(properties);
    }

    private static boolean isChargeItem(ItemStack stack) {
        return stack.getItem() == Items.ENDER_PEARL;
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockRayTraceResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (hand == Hand.MAIN_HAND && !isChargeItem(itemStack) && isChargeItem(player.getItemInHand(Hand.OFF_HAND))) {
            return ActionResultType.PASS;
        } else if (isChargeItem(itemStack) && canCharge(state)) {
            charge(world, pos, state);
            if (!player.abilities.instabuild) {
                itemStack.shrink(1);
            }

            return ActionResultType.sidedSuccess(world.isClientSide);
        } else if ((Integer) state.getValue(CHARGE) == 0) {
            return ActionResultType.PASS;
        } else if (!isNether(world)) {
            if (!world.isClientSide) {
                this.explode(state, world, pos);
            }

            return ActionResultType.sidedSuccess(world.isClientSide);
        } else {
            if (!world.isClientSide) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                if (serverPlayerEntity.getRespawnDimension() != world.dimension()
                        || !serverPlayerEntity.getRespawnPosition().equals(pos)) {
                    serverPlayerEntity.setRespawnPosition(world.dimension(), pos, 0.0f, false, true);
                    world.playSound((PlayerEntity) null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
                            (double) pos.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.BLOCKS,
                            1.0F, 1.0F);
                    return ActionResultType.SUCCESS;
                }
            }

            return ActionResultType.CONSUME;
        }
    }

    public static boolean isNether(World world) {
        return world.dimensionType().createDragonFight(); // world.getDimesnion().hasEnderDragonFight()
    }

    private static boolean canCharge(BlockState state) {
        return (Integer) state.getValue(CHARGE) < 4;
    }

    private void explode(BlockState state, World world, final BlockPos explodedPos) {
        world.removeBlock(explodedPos, false);
        world.explode(null, explodedPos.getX(), explodedPos.getY(), explodedPos.getZ(), 6.9F, true,
                Explosion.Mode.DESTROY);
    }
}
