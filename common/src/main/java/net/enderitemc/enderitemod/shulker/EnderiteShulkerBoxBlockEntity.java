package net.enderitemc.enderitemod.shulker;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.List;
import java.util.stream.IntStream;

public class EnderiteShulkerBoxBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
    public static float OPEN_SPEED = 1.25f;
    public static float CLOSE_SPEED = 0.67f;

    private static final int[] AVAILABLE_SLOTS = IntStream.range(0, 45).toArray();
    private NonNullList<ItemStack> inventory;
    private int viewerCount;
    private EnderiteShulkerBoxBlockEntity.AnimationStage animationStage;
    private float animationProgress;
    private float prevAnimationProgress;

    public EnderiteShulkerBoxBlockEntity(BlockPos pos, BlockState state) {
        super(EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.get(), pos, state);
        this.inventory = NonNullList.withSize(45, ItemStack.EMPTY);
        this.animationStage = EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSED;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, EnderiteShulkerBoxBlockEntity be) {
        be.updateAnimation(world, pos, state);
    }

    protected float getOpenSpeed(BlockState state) {
        if (state.getBlock() instanceof EnderiteShulkerBoxBlock) {
            Direction direction = state.getValue(EnderiteShulkerBoxBlock.FACING);
            return switch (direction) {
                case UP -> OPEN_SPEED;
                case DOWN -> CLOSE_SPEED;
                default -> 1.0f;
            };
        }
        return 1.0f;
    }
    protected float getCloseSpeed(BlockState state) {
        if (state.getBlock() instanceof EnderiteShulkerBoxBlock) {
            Direction direction = state.getValue(EnderiteShulkerBoxBlock.FACING);
            return switch (direction) {
                case UP -> CLOSE_SPEED;
                case DOWN -> OPEN_SPEED;
                default -> 1.0f;
            };
        }
        return 1.0f;
    }

    protected void updateAnimation(Level world, BlockPos pos, BlockState state) {
        this.prevAnimationProgress = this.animationProgress;
        switch (this.animationStage) {
            case CLOSED:
                this.animationProgress = 0.0F;
                break;
            case OPENING:
                this.animationProgress += 0.1F * getOpenSpeed(state);
                if (this.prevAnimationProgress == 0.0F) {
                    updateNeighborStates(world, pos, state);
                }

                if (this.animationProgress >= 1.0F) {
                    this.animationStage = EnderiteShulkerBoxBlockEntity.AnimationStage.OPENED;
                    this.animationProgress = 1.0F;
                    this.updateNeighborStates(world, pos, state);
                }
                this.pushEntities(world, pos, state);
                break;
            case CLOSING:
                this.animationProgress -= 0.1F * getCloseSpeed(state);
                if (this.prevAnimationProgress == 1.0F) {
                    updateNeighborStates(world, pos, state);
                }

                if (this.animationProgress <= 0.0F) {
                    this.animationStage = EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSED;
                    this.animationProgress = 0.0F;
                    this.updateNeighborStates(world, pos, state);
                    if (level != null) {
                        this.level.playSound((Player) null, this.worldPosition, SoundEvents.NETHERITE_BLOCK_BREAK,
                            SoundSource.BLOCKS, 0.5F, 0.1f);
                    }
                }
                break;
            case OPENED:
                this.animationProgress = 1.0F;
        }

    }

    public EnderiteShulkerBoxBlockEntity.AnimationStage getAnimationStage() {
        return this.animationStage;
    }

    public AABB getBoundingBox(BlockState state) {
        Vec3 vec3d = new Vec3(0.5, 0.0, 0.5);
        return Shulker.getProgressAabb(1.0F, state.getValue(EnderiteShulkerBoxBlock.FACING), 0.5F * this.getAnimationProgress(1.0F), vec3d);
    }

    private void pushEntities(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof EnderiteShulkerBoxBlock) {
            Direction direction = state.getValue(EnderiteShulkerBoxBlock.FACING);
            AABB box = Shulker.getProgressDeltaAabb(1.0F, direction, this.prevAnimationProgress, this.animationProgress, Vec3.atBottomCenterOf(pos));
            List<Entity> list = world.getEntities(null, box);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                        entity.move(
                            MoverType.SHULKER_BOX,
                            new Vec3(
                                (box.getXsize() + 0.01) * direction.getStepX(),
                                (box.getYsize() + 0.01) * direction.getStepY(),
                                (box.getZsize() + 0.01) * direction.getStepZ()
                            )
                        );
                    }
                }
            }
        }
    }

    public int getContainerSize() {
        return this.inventory.size();
    }

    public boolean triggerEvent(int type, int data) {
        if (type == 1) {
            this.viewerCount = data;
            if (data == 0) {
                this.animationStage = EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSING;
            }

            if (data == 1) {
                this.animationStage = EnderiteShulkerBoxBlockEntity.AnimationStage.OPENING;
            }

            return true;
        } else {
            return super.triggerEvent(type, data);
        }
    }

    private void updateNeighborStates(Level world, BlockPos pos, BlockState state) {
        state.updateNeighbourShapes(world, pos, 3);
        world.updateNeighborsAt(pos, state.getBlock());
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState oldState){
        // This function needs to be empty and not call super!!!
    }

    @Override
    public void startOpen(ContainerUser player) {
        if (!this.remove && !player.getLivingEntity().isSpectator()) {
            if (this.viewerCount < 0) {
                this.viewerCount = 0;
            }

            ++this.viewerCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount == 1) {
                this.level.gameEvent(player.getLivingEntity(), GameEvent.CONTAINER_OPEN, this.worldPosition);
                this.level.playSound((Player) null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN,
                    SoundSource.BLOCKS, 0.5F, this.level.getRandom().nextFloat() * 0.1F + 0.72F * getOpenSpeed(this.level.getBlockState(this.worldPosition)));
            }
        }

    }

    @Override
    public void stopOpen(ContainerUser player) {
        if (!this.remove && !player.getLivingEntity().isSpectator()) {
            --this.viewerCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount <= 0) {
                this.level.gameEvent(player.getLivingEntity(), GameEvent.CONTAINER_CLOSE, this.worldPosition);
                this.level.playSound((Player) null, this.worldPosition, SoundEvents.SHULKER_BOX_CLOSE,
                    SoundSource.BLOCKS, 0.5F, this.level.getRandom().nextFloat() * 0.1F + 0.72F * getCloseSpeed(this.level.getBlockState(this.worldPosition)));
            }
        }

    }

    protected Component getDefaultName() {
        return Component.translatable("container.enderitemod.enderiteShulkerBox");
    }

    @Override
    public void loadAdditional(ValueInput nbt) {
        super.loadAdditional(nbt);
        this.readInventoryNbt(nbt);
    }

    public void saveAdditional(ValueOutput nbt) {
        super.saveAdditional(nbt);
        if (!this.trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, this.inventory, false);
        }
    }

    public void readInventoryNbt(ValueInput readView) {
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(readView)) {
            ContainerHelper.loadAllItems(readView, this.inventory);
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return AVAILABLE_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return !((Block.byItem(stack.getItem()) instanceof EnderiteShulkerBoxBlock)
            || (Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock));
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    public float getAnimationProgress(float f) {
        return Mth.lerp(f, this.prevAnimationProgress, this.animationProgress);
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new EnderiteShulkerBoxScreenHandler(syncId, playerInventory, this);
    }

    public boolean suffocates() {
        return this.animationStage == EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSED;
    }

    public static enum AnimationStage {
        CLOSED, OPENING, OPENED, CLOSING;
    }
}
