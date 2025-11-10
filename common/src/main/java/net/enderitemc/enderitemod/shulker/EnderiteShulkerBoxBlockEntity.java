package net.enderitemc.enderitemod.shulker;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ContainerUser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.stream.IntStream;

public class EnderiteShulkerBoxBlockEntity extends LootableContainerBlockEntity implements SidedInventory {
    private static final int[] AVAILABLE_SLOTS = IntStream.range(0, 45).toArray();
    private DefaultedList<ItemStack> inventory;
    private int viewerCount;
    private EnderiteShulkerBoxBlockEntity.AnimationStage animationStage;
    private float animationProgress;
    private float prevAnimationProgress;

    public EnderiteShulkerBoxBlockEntity(BlockPos pos, BlockState state) {
        super(EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.get(), pos, state);
        this.inventory = DefaultedList.ofSize(45, ItemStack.EMPTY);
        this.animationStage = EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSED;
    }

    public static void tick(World world, BlockPos pos, BlockState state, EnderiteShulkerBoxBlockEntity be) {
        be.updateAnimation(world, pos, state);
    }

    protected void updateAnimation(World world, BlockPos pos, BlockState state) {
        this.prevAnimationProgress = this.animationProgress;
        switch (this.animationStage) {
            case CLOSED:
                this.animationProgress = 0.0F;
                break;
            case OPENING:
                this.animationProgress += 0.1F;
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
                this.animationProgress -= 0.1F;
                if (this.prevAnimationProgress == 1.0F) {
                    updateNeighborStates(world, pos, state);
                }

                if (this.animationProgress <= 0.0F) {
                    this.animationStage = EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSED;
                    this.animationProgress = 0.0F;
                    this.updateNeighborStates(world, pos, state);
                }
                break;
            case OPENED:
                this.animationProgress = 1.0F;
        }

    }

    public EnderiteShulkerBoxBlockEntity.AnimationStage getAnimationStage() {
        return this.animationStage;
    }

    public Box getBoundingBox(BlockState state) {
        Vec3d vec3d = new Vec3d(0.5, 0.0, 0.5);
        return ShulkerEntity.calculateBoundingBox(1.0F, state.get(EnderiteShulkerBoxBlock.FACING), 0.5F * this.getAnimationProgress(1.0F), vec3d);
    }

    private void pushEntities(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof EnderiteShulkerBoxBlock) {
            Direction direction = state.get(EnderiteShulkerBoxBlock.FACING);
            Box box = ShulkerEntity.calculateBoundingBox(1.0F, direction, this.prevAnimationProgress, this.animationProgress, pos.toBottomCenterPos());
            List<Entity> list = world.getOtherEntities(null, box);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity.getPistonBehavior() != PistonBehavior.IGNORE) {
                        entity.move(
                            MovementType.SHULKER_BOX,
                            new Vec3d(
                                (box.getLengthX() + 0.01) * direction.getOffsetX(),
                                (box.getLengthY() + 0.01) * direction.getOffsetY(),
                                (box.getLengthZ() + 0.01) * direction.getOffsetZ()
                            )
                        );
                    }
                }
            }
        }
    }

    public int size() {
        return this.inventory.size();
    }

    public boolean onSyncedBlockEvent(int type, int data) {
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
            return super.onSyncedBlockEvent(type, data);
        }
    }

    private void updateNeighborStates(World world, BlockPos pos, BlockState state) {
        state.updateNeighbors(world, pos, 3);
        world.updateNeighbors(pos, state.getBlock());
    }

    @Override
    public void onBlockReplaced(BlockPos pos, BlockState oldState){
        // This function needs to be empty and not call super!!!
    }

    @Override
    public void onOpen(ContainerUser player) {
        if (!this.removed && !player.asLivingEntity().isSpectator()) {
            if (this.viewerCount < 0) {
                this.viewerCount = 0;
            }

            ++this.viewerCount;
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount == 1) {
                this.world.emitGameEvent(player.asLivingEntity(), GameEvent.CONTAINER_OPEN, this.pos);
                this.world.playSound((PlayerEntity) null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_OPEN,
                    SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.72F);
            }
        }

    }

    @Override
    public void onClose(ContainerUser player) {
        if (!this.removed && !player.asLivingEntity().isSpectator()) {
            --this.viewerCount;
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount <= 0) {
                this.world.emitGameEvent(player.asLivingEntity(), GameEvent.CONTAINER_CLOSE, this.pos);
                this.world.playSound((PlayerEntity) null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE,
                    SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.72F);
            }
        }

    }

    protected Text getContainerName() {
        return Text.translatable("container.enderitemod.enderiteShulkerBox");
    }

    @Override
    public void readData(ReadView nbt) {
        super.readData(nbt);
        this.readInventoryNbt(nbt);
    }

    public void writeData(WriteView nbt) {
        super.writeData(nbt);
        if (!this.writeLootTable(nbt)) {
            Inventories.writeData(nbt, this.inventory, false);
        }
    }

    public void readInventoryNbt(ReadView readView) {
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.readLootTable(readView)) {
            Inventories.readData(readView, this.inventory);
        }
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return AVAILABLE_SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return !((Block.getBlockFromItem(stack.getItem()) instanceof EnderiteShulkerBoxBlock)
            || (Block.getBlockFromItem(stack.getItem()) instanceof ShulkerBoxBlock));
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    public float getAnimationProgress(float f) {
        return MathHelper.lerp(f, this.prevAnimationProgress, this.animationProgress);
    }

    @Override
    public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new EnderiteShulkerBoxScreenHandler(syncId, playerInventory, this);
    }

    public boolean suffocates() {
        return this.animationStage == EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSED;
    }

    public static enum AnimationStage {
        CLOSED, OPENING, OPENED, CLOSING;
    }
}
