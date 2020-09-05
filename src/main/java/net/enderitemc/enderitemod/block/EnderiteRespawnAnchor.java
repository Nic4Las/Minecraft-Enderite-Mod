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
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockRayTraceResult hit) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (hand == Hand.MAIN_HAND && !isChargeItem(itemStack) && isChargeItem(player.getHeldItem(Hand.OFF_HAND))) {
            return ActionResultType.PASS;
        } else if (isChargeItem(itemStack) && canCharge(state)) {
            func_235564_a_(world, pos, state);
            if (!player.abilities.isCreativeMode) {
                itemStack.shrink(1);
            }

            return ActionResultType.func_233537_a_(world.isRemote);
        } else if ((Integer) state.get(field_235559_a_) == 0) {
            return ActionResultType.PASS;
        } else if (!isNether(world)) {
            if (!world.isRemote) {
                this.explode(state, world, pos);
            }

            return ActionResultType.func_233537_a_(world.isRemote);
        } else {
            if (!world.isRemote) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                if (serverPlayerEntity.func_241141_L_() != world.func_234923_W_()
                        || !serverPlayerEntity.func_241140_K_().equals(pos)) {
                    serverPlayerEntity.func_242111_a(world.func_234923_W_(), pos, 0.0f, false, true);
                    world.playSound((PlayerEntity) null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
                            (double) pos.getZ() + 0.5D, SoundEvents.field_232819_mt_, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return ActionResultType.SUCCESS;
                }
            }

            return ActionResultType.CONSUME;
        }
    }

    public static boolean isNether(World world) {
        return world.func_230315_m_().func_236046_h_(); // world.getDimesnion().hasEnderDragonFight()
    }

    private static boolean canCharge(BlockState state) {
        return (Integer) state.get(field_235559_a_) < 4;
    }

    private void explode(BlockState state, World world, final BlockPos explodedPos) {
        world.removeBlock(explodedPos, false);
        world.createExplosion(null, explodedPos.getX(), explodedPos.getY(), explodedPos.getZ(), 6.9F, true,
                Explosion.Mode.DESTROY);
    }
}
