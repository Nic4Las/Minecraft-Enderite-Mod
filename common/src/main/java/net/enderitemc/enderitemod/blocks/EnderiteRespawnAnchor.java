package net.enderitemc.enderitemod.blocks;

import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

public class EnderiteRespawnAnchor extends RespawnAnchorBlock implements BlockEntityProvider {


    public EnderiteRespawnAnchor(AbstractBlock.Settings settings) {
        super(settings.mapColor(MapColor.BLACK)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(50.0f, 1200.0f)
            .luminance(state -> RespawnAnchorBlock.getLightLevel(state, 15)));
    }

    private static boolean isChargeItem(ItemStack stack) {
        return stack.getItem() == Items.ENDER_PEARL;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (EnderiteRespawnAnchor.isChargeItem(stack) && EnderiteRespawnAnchor.canCharge(state)) {
            EnderiteRespawnAnchor.charge(player, world, pos, state);
            stack.decrementUnlessCreative(1, player);
            if (world.isClient && world.getBlockEntity(pos) instanceof EnderiteRespawnAnchorBlockEntity eRA) {
                eRA.charge = world.getBlockState(pos).get(CHARGES);
            }
            return ActionResult.SUCCESS;
        }
        if (hand == Hand.MAIN_HAND && EnderiteRespawnAnchor.isChargeItem(player.getStackInHand(Hand.OFF_HAND)) && EnderiteRespawnAnchor.canCharge(state)) {
            return ActionResult.PASS;
        }
        return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (state.get(CHARGES) == 0) {
            return ActionResult.PASS;
        } else if (!isNether(world)) {
            if (!world.isClient) {
                this.explode(state, world, pos);
            }

            return ActionResult.SUCCESS;
        } else {
            if (player instanceof ServerPlayerEntity serverPlayerEntity) {
                ServerPlayerEntity.Respawn respawn = serverPlayerEntity.getRespawn();
                ServerPlayerEntity.Respawn respawn2 = new ServerPlayerEntity.Respawn(world.getRegistryKey(), pos, 0.0F, false);
                if (respawn == null || !respawn.posEquals(respawn2)) {
                    serverPlayerEntity.setSpawnPoint(respawn2, true);
                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return ActionResult.SUCCESS_SERVER;
                }
            }

            return ActionResult.CONSUME;
        }
    }

    public static boolean isNether(World world) {
        return world.getDimensionEntry().matchesKey(DimensionTypes.THE_END);// getDimension().hasEnderDragonFight();
    }

    private static boolean canCharge(BlockState state) {
        return (Integer) state.get(CHARGES) < 4;
    }

    private void explode(BlockState state, World world, final BlockPos explodedPos) {
        world.removeBlock(explodedPos, false);
        world.createExplosion(null, explodedPos.getX(), explodedPos.getY(), explodedPos.getZ(), 6.9F, true,
            World.ExplosionSourceType.BLOCK);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EnderiteRespawnAnchorBlockEntity(pos, state);
    }

    @Override
    protected boolean isTransparent(BlockState state) {
        return false;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(CHARGES) == 0) {
            return;
        }
        if (world.getBlockState(pos.up()).isSolid()) {
            // Remove particles if top block is solid
            return;
        }
        double d = (double) pos.getX() + random.nextDouble();
        double e = (double) pos.getY() + 0.8;
        double f = (double) pos.getZ() + random.nextDouble();
        world.addParticleClient(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
    }
}