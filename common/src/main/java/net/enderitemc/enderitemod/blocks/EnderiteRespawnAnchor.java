package net.enderitemc.enderitemod.blocks;

import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.BlockHitResult;

public class EnderiteRespawnAnchor extends RespawnAnchorBlock implements EntityBlock {


    public EnderiteRespawnAnchor(BlockBehaviour.Properties settings) {
        super(settings.mapColor(MapColor.COLOR_BLACK)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(50.0f, 1200.0f)
            .lightLevel(state -> RespawnAnchorBlock.getScaledChargeLevel(state, 15)));
    }

    private static boolean isRespawnFuel(ItemStack stack) {
        return stack.getItem() == Items.ENDER_PEARL;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (EnderiteRespawnAnchor.isRespawnFuel(stack) && EnderiteRespawnAnchor.canBeCharged(state)) {
            EnderiteRespawnAnchor.charge(player, world, pos, state);
            stack.consume(1, player);
            if (world.isClientSide() && world.getBlockEntity(pos) instanceof EnderiteRespawnAnchorBlockEntity eRA) {
                eRA.charge = world.getBlockState(pos).getValue(CHARGE);
            }
            return InteractionResult.SUCCESS;
        }
        if (hand == InteractionHand.MAIN_HAND && EnderiteRespawnAnchor.isRespawnFuel(player.getItemInHand(InteractionHand.OFF_HAND)) && EnderiteRespawnAnchor.canBeCharged(state)) {
            return InteractionResult.PASS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(CHARGE) == 0) {
            return InteractionResult.PASS;
        } else if (!isEnd(world)) {
            if (!world.isClientSide()) {
                this.explode(state, world, pos);
            }

            return InteractionResult.SUCCESS;
        } else {
            if (player instanceof ServerPlayer serverPlayerEntity) {
                ServerPlayer.RespawnConfig respawn = serverPlayerEntity.getRespawnConfig();
                ServerPlayer.RespawnConfig respawn2 = new ServerPlayer.RespawnConfig(LevelData.RespawnData.of(world.dimension(), pos, 0.0F, 0.0F), false);
                if (respawn == null || !respawn.isSamePosition(respawn2)) {
                    serverPlayerEntity.setRespawnPosition(respawn2, true);
                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS_SERVER;
                }
            }

            return InteractionResult.CONSUME;
        }
    }

    public static boolean isEnd(Level world) {
        return world.dimensionTypeRegistration().is(BuiltinDimensionTypes.END);// getDimension().hasEnderDragonFight();
    }

    public static boolean shouldRespawnPlayer(Level world, boolean is_dead) {
        return isEnd(world) && is_dead;
    }

    private static boolean canBeCharged(BlockState state) {
        return (Integer) state.getValue(CHARGE) < 4;
    }

    private void explode(BlockState state, Level world, final BlockPos explodedPos) {
        world.removeBlock(explodedPos, false);
        world.explode(null, explodedPos.getX(), explodedPos.getY(), explodedPos.getZ(), 6.9F, true,
            Level.ExplosionInteraction.BLOCK);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnderiteRespawnAnchorBlockEntity(pos, state);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return false;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(CHARGE) == 0) {
            return;
        }
        if (world.getBlockState(pos.above()).isSolid()) {
            // Remove particles if top block is solid
            return;
        }
        double d = (double) pos.getX() + random.nextDouble();
        double e = (double) pos.getY() + 0.8;
        double f = (double) pos.getZ() + random.nextDouble();
        world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
    }
}
