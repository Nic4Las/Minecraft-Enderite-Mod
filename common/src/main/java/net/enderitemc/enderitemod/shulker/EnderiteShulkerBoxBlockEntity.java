package net.enderitemc.enderitemod.shulker;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.IntStream;

public class EnderiteShulkerBoxBlockEntity extends LootableContainerBlockEntity implements SidedInventory {
    private static final int[] AVAILABLE_SLOTS = IntStream.range(0, 27).toArray();
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
        be.tick2();
    }

    public void tick2() {
        this.updateAnimation();
        if (this.animationStage == EnderiteShulkerBoxBlockEntity.AnimationStage.OPENING
            || this.animationStage == EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSING) {
            this.pushEntities();
        }
    }

    protected void updateAnimation() {
        this.prevAnimationProgress = this.animationProgress;
        switch (this.animationStage) {
            case CLOSED:
                this.animationProgress = 0.0F;
                break;
            case OPENING:
                this.animationProgress += 0.1F;
                if (this.animationProgress >= 1.0F) {
                    this.pushEntities();
                    this.animationStage = EnderiteShulkerBoxBlockEntity.AnimationStage.OPENED;
                    this.animationProgress = 1.0F;
                    this.updateNeighborStates();
                }
                break;
            case CLOSING:
                this.animationProgress -= 0.1F;
                if (this.animationProgress <= 0.0F) {
                    this.animationStage = EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSED;
                    this.animationProgress = 0.0F;
                    this.updateNeighborStates();
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
        return this.getBoundingBox((Direction) state.get(EnderiteShulkerBoxBlock.FACING));
    }

    public Box getBoundingBox(Direction openDirection) {
        float f = this.getAnimationProgress(1.0F);
        return VoxelShapes.fullCube().getBoundingBox().stretch((double) (0.5F * f * (float) openDirection.getOffsetX()),
            (double) (0.5F * f * (float) openDirection.getOffsetY()),
            (double) (0.5F * f * (float) openDirection.getOffsetZ()));
    }

    private Box getCollisionBox(Direction facing) {
        Direction direction = facing.getOpposite();
        return this.getBoundingBox(facing).shrink((double) direction.getOffsetX(), (double) direction.getOffsetY(),
            (double) direction.getOffsetZ());
    }

    private void pushEntities() {
        BlockState blockState = this.world.getBlockState(this.getPos());
        if (blockState.getBlock() instanceof EnderiteShulkerBoxBlock) {
            Direction direction = (Direction) blockState.get(EnderiteShulkerBoxBlock.FACING);
            Box box = this.getCollisionBox(direction).offset(this.pos);
            List<Entity> list = this.world.getOtherEntities((Entity) null, box);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);
                    if (entity.getPistonBehavior() != PistonBehavior.IGNORE) {
                        double d = 0.0D;
                        double e = 0.0D;
                        double f = 0.0D;
                        Box box2 = entity.getBoundingBox();
                        switch (direction.getAxis()) {
                            case X:
                                if (direction.getDirection() == Direction.AxisDirection.POSITIVE) {
                                    d = box.maxX - box2.minX;
                                } else {
                                    d = box2.maxX - box.minX;
                                }

                                d += 0.01D;
                                break;
                            case Y:
                                if (direction.getDirection() == Direction.AxisDirection.POSITIVE) {
                                    e = box.maxY - box2.minY;
                                } else {
                                    e = box2.maxY - box.minY;
                                }

                                e += 0.01D;
                                break;
                            case Z:
                                if (direction.getDirection() == Direction.AxisDirection.POSITIVE) {
                                    f = box.maxZ - box2.minZ;
                                } else {
                                    f = box2.maxZ - box.minZ;
                                }

                                f += 0.01D;
                        }

                        entity.move(MovementType.SHULKER_BOX, new Vec3d(d * (double) direction.getOffsetX(),
                            e * (double) direction.getOffsetY(), f * (double) direction.getOffsetZ()));
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
                this.updateNeighborStates();
            }

            if (data == 1) {
                this.animationStage = EnderiteShulkerBoxBlockEntity.AnimationStage.OPENING;
                this.updateNeighborStates();
            }

            return true;
        } else {
            return super.onSyncedBlockEvent(type, data);
        }
    }

    private void updateNeighborStates() {
        this.getCachedState().updateNeighbors(this.getWorld(), this.getPos(), 3);
    }

    public void onOpen(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.viewerCount < 0) {
                this.viewerCount = 0;
            }

            ++this.viewerCount;
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount == 1) {
                this.world.playSound((PlayerEntity) null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_OPEN,
                    SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    public void onClose(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.viewerCount;
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount <= 0) {
                this.world.playSound((PlayerEntity) null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE,
                    SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    protected Text getContainerName() {
        return Text.translatable("container.enderitemod.enderiteShulkerBox");
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.readNbt(nbt, lookup);
        this.readInventoryNbt(nbt, lookup);
    }

    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.writeNbt(nbt, lookup);
        if (!this.writeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory, false, lookup);
        }
    }

    public void readInventoryNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.readLootTable(nbt) && nbt.contains("Items", NbtElement.LIST_TYPE)) {
            Inventories.readNbt(nbt, this.inventory, lookup);
        }
    }


    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }


    protected void setHeldStacks(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    public int[] getAvailableSlots(Direction side) {
        return AVAILABLE_SLOTS;
    }

    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return !(Block.getBlockFromItem(stack.getItem()) instanceof EnderiteShulkerBoxBlock
            || Block.getBlockFromItem(stack.getItem()) instanceof ShulkerBoxBlock);
    }

    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    public float getAnimationProgress(float f) {
        return MathHelper.lerp(f, this.prevAnimationProgress, this.animationProgress);
    }

    public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        // return new ShulkerBoxScreenHandler(syncId, playerInventory, this);
        return new EnderiteShulkerBoxScreenHandler(syncId, playerInventory, this);
    }

    public boolean suffocates() {
        return this.animationStage == EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSED;
    }

    public static enum AnimationStage {
        CLOSED, OPENING, OPENED, CLOSING;
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }
}
