package net.enderitemc.enderitemod.shulker;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class EnderiteShulkerBoxBlock extends ShulkerBoxBlock {

    public EnderiteShulkerBoxBlock(String id) {
        super((DyeColor) null, Settings.copy(Blocks.SHULKER_BOX)
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(EnderiteMod.MOD_ID, id)))
            .nonOpaque().strength(2.0f, 17.0f));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EnderiteShulkerBoxBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
                                                                  BlockEntityType<T> type) {
        return validateTicker(type, EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.get(),
            (world1, pos, state1, be) -> EnderiteShulkerBoxBlockEntity.tick(world1, pos, state1, be));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world instanceof ServerWorld serverWorld) {
            if (player.isSpectator()) {
                return ActionResult.CONSUME;
            } else {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity) {
                    if (canOpen(state, world, pos, shulkerBoxBlockEntity)) {
                        player.openHandledScreen(shulkerBoxBlockEntity);
                        player.incrementStat(Stats.OPEN_SHULKER_BOX);
                        PiglinBrain.onGuardedBlockInteracted(serverWorld, player, true);
                    }

                    return ActionResult.CONSUME;
                } else {
                    return ActionResult.PASS;
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    private static boolean canOpen(BlockState state, World world, BlockPos pos, EnderiteShulkerBoxBlockEntity entity) {
        if (entity.getAnimationStage() != EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSED) {
            return true;
        } else {
            Box box = ShulkerEntity.calculateBoundingBox(1.0f, state.get(FACING), 0.0f, 0.5f, pos.toCenterPos()).contract(1.0E-6);
            return world.isSpaceEmpty(box);
        }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity) {
            if (!world.isClient && player.isCreative() && !shulkerBoxBlockEntity.isEmpty()) {
                ItemStack itemStack = getItemStack();
                itemStack.applyComponentsFrom(blockEntity.createComponentMap());
                ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
                    (double) pos.getZ() + 0.5D, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            } else {
                shulkerBoxBlockEntity.generateLoot(player);
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof EnderiteShulkerBoxBlockEntity) {
            EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity = (EnderiteShulkerBoxBlockEntity) blockEntity;
            builder = builder.addDynamicDrop(CONTENTS_DYNAMIC_DROP_ID, lootConsumer -> {
                for (int i = 0; i < shulkerBoxBlockEntity.size(); ++i) {
                    lootConsumer.accept(shulkerBoxBlockEntity.getStack(i));
                }
            });
        }
        return super.getDroppedStacks(state, builder);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof EnderiteShulkerBoxBlockEntity
            ? VoxelShapes.cuboid(((EnderiteShulkerBoxBlockEntity) blockEntity).getBoundingBox(state))
            : VoxelShapes.fullCube();
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public static ItemStack getItemStack() {
        return new ItemStack(EnderiteMod.ENDERITE_SHULKER_BOX.get());
    }

}