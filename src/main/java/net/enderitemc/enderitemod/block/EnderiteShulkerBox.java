package net.enderitemc.enderitemod.block;

import java.util.List;

import javax.annotation.Nullable;

import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.tileEntity.EnderiteShulkerBoxTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ShulkerAABBHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderiteShulkerBox extends ShulkerBoxBlock {

    public EnderiteShulkerBox(DyeColor color, Properties properties) {
        super(color, properties);
    }

    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new EnderiteShulkerBoxTileEntity();
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
            BlockRayTraceResult hit) {
        if (worldIn.isClientSide) {
            return ActionResultType.SUCCESS;
        } else if (player.isSpectator()) {
            return ActionResultType.CONSUME;
        } else {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof EnderiteShulkerBoxTileEntity) {
                EnderiteShulkerBoxTileEntity shulkerboxtileentity = (EnderiteShulkerBoxTileEntity) tileentity;
                boolean flag;
                if (shulkerboxtileentity.getAnimationStatus() == EnderiteShulkerBoxTileEntity.AnimationStatus.CLOSED) {
                    Direction direction = state.getValue(FACING);
                    flag = worldIn.noCollision(ShulkerAABBHelper.openBoundingBox(pos, direction));
                } else {
                    flag = true;
                }

                if (flag) {
                    player.openMenu(shulkerboxtileentity);
                    player.awardStat(Stats.OPEN_SHULKER_BOX);
                    PiglinTasks.angerNearbyPiglins(player, true);
                }

                return ActionResultType.CONSUME;
            } else {
                return ActionResultType.PASS;
            }
        }
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if
     * the player's tool can actually collect this block
     */
    @Override
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof EnderiteShulkerBoxTileEntity) {
            EnderiteShulkerBoxTileEntity shulkerboxtileentity = (EnderiteShulkerBoxTileEntity) tileentity;
            if (!worldIn.isClientSide && player.isCreative() && !shulkerboxtileentity.isEmpty()) {
                ItemStack itemstack = getColoredItemStack(this.getColor());
                CompoundNBT compoundnbt = shulkerboxtileentity.saveToNbt(new CompoundNBT());
                if (!compoundnbt.isEmpty()) {
                    itemstack.addTagElement("BlockEntityTag", compoundnbt);
                }

                if (shulkerboxtileentity.hasCustomName()) {
                    itemstack.setHoverName(shulkerboxtileentity.getCustomName());
                }

                ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
                        (double) pos.getZ() + 0.5D, itemstack);
                itementity.setDefaultPickUpDelay();
                worldIn.addFreshEntity(itementity);
            } else {
                shulkerboxtileentity.unpackLootTable(player);
            }
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        TileEntity tileentity = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
        if (tileentity instanceof EnderiteShulkerBoxTileEntity) {
            EnderiteShulkerBoxTileEntity shulkerboxtileentity = (EnderiteShulkerBoxTileEntity) tileentity;
            builder = builder.withDynamicDrop(CONTENTS, (p_220168_1_, p_220168_2_) -> {
                for (int i = 0; i < shulkerboxtileentity.getContainerSize(); ++i) {
                    p_220168_2_.accept(shulkerboxtileentity.getItem(i));
                }

            });
        }

        return super.getDrops(state, builder);
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place
     * logic
     */
    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof EnderiteShulkerBoxTileEntity) {
                ((EnderiteShulkerBoxTileEntity) tileentity).setCustomName(stack.getDisplayName());
            }
        }

    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof EnderiteShulkerBoxTileEntity) {
                worldIn.updateNeighbourForOutputSignal(pos, state.getBlock());
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        return tileentity instanceof EnderiteShulkerBoxTileEntity
                ? VoxelShapes.create(((EnderiteShulkerBoxTileEntity) tileentity).getBoundingBox(state))
                : VoxelShapes.block();
    }

    @Override
    public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
        ItemStack itemstack = new ItemStack(this);
        EnderiteShulkerBoxTileEntity shulkerboxtileentity = (EnderiteShulkerBoxTileEntity) worldIn.getBlockEntity(pos);
        CompoundNBT compoundnbt = shulkerboxtileentity.saveToNbt(new CompoundNBT());
        if (!compoundnbt.isEmpty()) {
            itemstack.addTagElement("BlockEntityTag", compoundnbt);
        }

        return itemstack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos,
            PlayerEntity player) {
        // ItemStack itemStack = super.getPickStack(world, pos, state);
        ItemStack itemStack = getCloneItemStack(world, pos, state);
        EnderiteShulkerBoxTileEntity shulkerBoxBlockEntity = (EnderiteShulkerBoxTileEntity) world.getBlockEntity(pos);
        CompoundNBT compoundTag = shulkerBoxBlockEntity.saveToNbt(new CompoundNBT());
        if (!compoundTag.isEmpty()) {
            itemStack.addTagElement("BlockEntityTag", compoundTag);
        }

        return itemStack;
    }

    public static ItemStack getStack() {
        return new ItemStack(Registration.ENDERITE_SHULKER_BOX.get());
    }

    public static Block getBlockByColor(@Nullable DyeColor colorIn) {
        return Registration.ENDERITE_SHULKER_BOX.get();
    }

    public static ItemStack getColoredItemStack(@Nullable DyeColor colorIn) {
        return new ItemStack(getBlockByColor(colorIn));
    }

}