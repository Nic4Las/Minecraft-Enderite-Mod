package net.enderitemc.enderitemod.block;

import java.util.List;

import javax.annotation.Nullable;

import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.tileEntity.EnderiteShulkerBoxTileEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class EnderiteShulkerBox extends ShulkerBoxBlock {

    public EnderiteShulkerBox(DyeColor color, Properties properties) {
        super(color, properties);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnderiteShulkerBoxTileEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == Registration.ENDERITE_SHULKER_BOX_TILE_ENTITY.get() ? (world1, pos, state1, be) -> EnderiteShulkerBoxTileEntity.tick(world1, pos, state1,(EnderiteShulkerBoxTileEntity) be) : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
            BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (player.isSpectator()) {
            return InteractionResult.CONSUME;
        } else {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof EnderiteShulkerBoxTileEntity) {
                EnderiteShulkerBoxTileEntity shulkerboxtileentity = (EnderiteShulkerBoxTileEntity) tileentity;
                boolean flag;
                /*if (shulkerboxtileentity.getAnimationStatus() == EnderiteShulkerBoxTileEntity.AnimationStatus.CLOSED) {
                    Direction direction = state.getValue(FACING);
                    flag = worldIn.noCollision(ShulkerSharedHelper.openBoundingBox(pos, direction));
                } else {
                    flag = true;
                }*/

                if (this.canOpen(state, worldIn, pos, (EnderiteShulkerBoxTileEntity)tileentity)) {
                    player.openMenu(shulkerboxtileentity);
                    player.awardStat(Stats.OPEN_SHULKER_BOX);
                    PiglinAi.angerNearbyPiglins(player, true);
                }

                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    private static boolean canOpen(BlockState p_154547_, Level p_154548_, BlockPos p_154549_, EnderiteShulkerBoxTileEntity p_154550_) {
        if (p_154550_.getAnimationStatus() != EnderiteShulkerBoxTileEntity.AnimationStatus.CLOSED) {
           return true;
        } else {
           AABB aabb = Shulker.getProgressDeltaAabb(p_154547_.getValue(FACING), 0.0F, 0.5F).move(p_154549_).deflate(1.0E-6D);
           return p_154548_.noCollision(aabb);
        }
     }

    /**
     * Called before the Block is set to air in the world. Called regardless of if
     * the player's tool can actually collect this block
     */
    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof EnderiteShulkerBoxTileEntity) {
            EnderiteShulkerBoxTileEntity shulkerboxtileentity = (EnderiteShulkerBoxTileEntity) tileentity;
            if (!worldIn.isClientSide && player.isCreative() && !shulkerboxtileentity.isEmpty()) {
                ItemStack itemstack = getColoredItemStack(this.getColor());
                CompoundTag compoundnbt = shulkerboxtileentity.saveToNbt(new CompoundTag());
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
        BlockEntity tileentity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
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
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof EnderiteShulkerBoxTileEntity) {
                ((EnderiteShulkerBoxTileEntity) tileentity).setCustomName(stack.getDisplayName());
            }
        }

    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof EnderiteShulkerBoxTileEntity) {
                worldIn.updateNeighbourForOutputSignal(pos, state.getBlock());
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        return tileentity instanceof EnderiteShulkerBoxTileEntity
                ? Shapes.create(((EnderiteShulkerBoxTileEntity) tileentity).getBoundingBox(state))
                : Shapes.block();
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter worldIn, BlockPos pos, BlockState state) {
        ItemStack itemstack = new ItemStack(this);
        EnderiteShulkerBoxTileEntity shulkerboxtileentity = (EnderiteShulkerBoxTileEntity) worldIn.getBlockEntity(pos);
        CompoundTag compoundnbt = shulkerboxtileentity.saveToNbt(new CompoundTag());
        if (!compoundnbt.isEmpty()) {
            itemstack.addTagElement("BlockEntityTag", compoundnbt);
        }

        return itemstack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos,
            Player player) {
        // ItemStack itemStack = super.getPickStack(world, pos, state);
        ItemStack itemStack = getCloneItemStack(world, pos, state);
        EnderiteShulkerBoxTileEntity shulkerBoxBlockEntity = (EnderiteShulkerBoxTileEntity) world.getBlockEntity(pos);
        CompoundTag compoundTag = shulkerBoxBlockEntity.saveToNbt(new CompoundTag());
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