package net.enderitemc.enderitemod.blocks;

import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;

public class EnderiteRespawnAnchor extends RespawnAnchorBlock implements BlockEntityProvider {


    public EnderiteRespawnAnchor() {
        super(AbstractBlock.Settings.create().mapColor(MapColor.BLACK)
                .instrument(Instrument.BASEDRUM).requiresTool().strength(50.0f, 1200.0f)
                .luminance(state -> RespawnAnchorBlock.getLightLevel(state, 15)));
    }

    private static boolean isChargeItem(ItemStack stack) {
        return stack.getItem() == Items.ENDER_PEARL;
    }



    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (EnderiteRespawnAnchor.isChargeItem(stack) && EnderiteRespawnAnchor.canCharge(state)) {
            EnderiteRespawnAnchor.charge(player, world, pos, state);
            stack.decrementUnlessCreative(1, player);
            if(world.isClient && world.getBlockEntity(pos) instanceof EnderiteRespawnAnchorBlockEntity eRA) {
                eRA.charge = world.getBlockState(pos).get(CHARGES);
            }
            return ItemActionResult.success(world.isClient);
        }
        if (hand == Hand.MAIN_HAND && EnderiteRespawnAnchor.isChargeItem(player.getStackInHand(Hand.OFF_HAND)) && EnderiteRespawnAnchor.canCharge(state)) {
            return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (state.get(CHARGES) == 0) {
            return ActionResult.PASS;
        }
        if (EnderiteRespawnAnchor.isNether(world)) {
            ServerPlayerEntity serverPlayerEntity;
            if (!(world.isClient || (serverPlayerEntity = (ServerPlayerEntity)player).getSpawnPointDimension() == world.getRegistryKey() && pos.equals(serverPlayerEntity.getSpawnPointPosition()))) {
                serverPlayerEntity.setSpawnPoint(world.getRegistryKey(), pos, 0.0f, false, true);
                world.playSound(null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.BLOCKS, 1.0f, 1.0f);
                return ActionResult.SUCCESS;
            }
            return ActionResult.CONSUME;
        }
        if (!world.isClient) {
            this.explode(state, world, pos);
        }
        return ActionResult.success(world.isClient);
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
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if(world.getBlockState(pos.up()).isSolid()) {
            // Remove particles if top block is solid
            return;
        }
        super.randomDisplayTick(state, world, pos, random);
    }
}