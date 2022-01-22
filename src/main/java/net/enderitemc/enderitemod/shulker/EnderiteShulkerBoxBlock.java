package net.enderitemc.enderitemod.shulker;

import java.util.List;

import net.enderitemc.enderitemod.EnderiteMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.loot.context.LootContext.Builder;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class EnderiteShulkerBoxBlock extends ShulkerBoxBlock {

    public EnderiteShulkerBoxBlock(DyeColor color, Settings settings) {
        super(color, settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EnderiteShulkerBoxBlockEntity(pos,state);
    }

    // @Override
    // public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
    //     return EnderiteShulkerBoxBlockEntity::tick;
    // }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else if (player.isSpectator()) {
            return ActionResult.CONSUME;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof EnderiteShulkerBoxBlockEntity) {
                EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity = (EnderiteShulkerBoxBlockEntity) blockEntity;

                if (canOpen(state, world, pos, shulkerBoxBlockEntity)) {
                    player.openHandledScreen(shulkerBoxBlockEntity);
                    player.incrementStat(Stats.OPEN_SHULKER_BOX);
                    PiglinBrain.onGuardedBlockInteracted(player, true);
                }

                return ActionResult.CONSUME;
            } else {
                return ActionResult.PASS;
            }
        }
    }

    private static boolean canOpen(BlockState state, World world, BlockPos pos, EnderiteShulkerBoxBlockEntity entity) {
        if (entity.getAnimationStage() != EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSED) {
           return true;
        } else {
           Box box = ShulkerEntity.method_33347((Direction)state.get(FACING), 0.0F, 0.5F).offset(pos).contract(1.0E-6D);
           return world.isSpaceEmpty(box);
        }
     }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EnderiteShulkerBoxBlockEntity) {
            EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity = (EnderiteShulkerBoxBlockEntity) blockEntity;
            if (!world.isClient && player.isCreative() && !shulkerBoxBlockEntity.isEmpty()) {
                ItemStack itemStack = getItemStack();
                NbtCompound compoundTag = shulkerBoxBlockEntity.serializeInventory(new NbtCompound());
                if (!compoundTag.isEmpty()) {
                    itemStack.putSubTag("BlockEntityTag", compoundTag);
                }

                if (shulkerBoxBlockEntity.hasCustomName()) {
                    itemStack.setCustomName(shulkerBoxBlockEntity.getCustomName());
                }

                ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
                        (double) pos.getZ() + 0.5D, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            } else {
                shulkerBoxBlockEntity.checkLootInteraction(player);
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, Builder builder) {
        BlockEntity blockEntity = (BlockEntity) builder.getNullable(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof EnderiteShulkerBoxBlockEntity) {
            EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity = (EnderiteShulkerBoxBlockEntity) blockEntity;
            builder = builder.putDrop(CONTENTS, (lootContext, consumer) -> {
                for (int i = 0; i < shulkerBoxBlockEntity.size(); ++i) {
                    consumer.accept(shulkerBoxBlockEntity.getStack(i));
                }

            });
        }

        return super.getDroppedStacks(state, builder);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof EnderiteShulkerBoxBlockEntity) {
                ((EnderiteShulkerBoxBlockEntity) blockEntity).setCustomName(itemStack.getName());
            }
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof EnderiteShulkerBoxBlockEntity) {
                world.updateComparators(pos, state.getBlock());
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof EnderiteShulkerBoxBlockEntity
                ? VoxelShapes.cuboid(((EnderiteShulkerBoxBlockEntity) blockEntity).getBoundingBox(state))
                : VoxelShapes.fullCube();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        // ItemStack itemStack = super.getPickStack(world, pos, state);
        ItemStack itemStack = getItemStack();
        EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity = (EnderiteShulkerBoxBlockEntity) world.getBlockEntity(pos);
        NbtCompound compoundTag = shulkerBoxBlockEntity.serializeInventory(new NbtCompound());
        if (!compoundTag.isEmpty()) {
            itemStack.putSubTag("BlockEntityTag", compoundTag);
        }

        return itemStack;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public static ItemStack getItemStack() {
        return new ItemStack(EnderiteMod.ENDERITE_SHULKER_BOX);
    }

}