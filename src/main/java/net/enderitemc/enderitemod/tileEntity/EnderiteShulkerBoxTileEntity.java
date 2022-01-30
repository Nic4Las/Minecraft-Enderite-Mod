package net.enderitemc.enderitemod.tileEntity;

import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.enderitemc.enderitemod.block.EnderiteShulkerBox;
import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class EnderiteShulkerBoxTileEntity extends RandomizableContainerBlockEntity
        implements WorldlyContainer {

    private static final int[] SLOTS = IntStream.range(0, 27).toArray();
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private int openCount;
    private EnderiteShulkerBoxTileEntity.AnimationStatus animationStatus = EnderiteShulkerBoxTileEntity.AnimationStatus.CLOSED;
    private float progress;
    private float progressOld;

    public EnderiteShulkerBoxTileEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos blockPos, BlockState blockState) {
        super(tileEntityTypeIn, blockPos, blockState);
    }

    public EnderiteShulkerBoxTileEntity(BlockPos blockPos, BlockState blockState) {
        super(Registration.ENDERITE_SHULKER_BOX_TILE_ENTITY.get(), blockPos, blockState);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, EnderiteShulkerBoxTileEntity e) {
        e.updateAnimation();
        if (e.animationStatus == EnderiteShulkerBoxTileEntity.AnimationStatus.OPENING
                || e.animationStatus == EnderiteShulkerBoxTileEntity.AnimationStatus.CLOSING) {
            e.moveCollidedEntities();
        }
    }

    public void updateAnimation() {
        this.progressOld = this.progress;
        switch (this.animationStatus) {
            case CLOSED:
                this.progress = 0.0F;
                break;
            case OPENING:
                this.progress += 0.1F;
                if (this.progress >= 1.0F) {
                    this.moveCollidedEntities();
                    this.animationStatus = EnderiteShulkerBoxTileEntity.AnimationStatus.OPENED;
                    this.progress = 1.0F;
                    this.doNeighborUpdates();
                }
                break;
            case CLOSING:
                this.progress -= 0.1F;
                if (this.progress <= 0.0F) {
                    this.animationStatus = EnderiteShulkerBoxTileEntity.AnimationStatus.CLOSED;
                    this.progress = 0.0F;
                    this.doNeighborUpdates();
                }
                break;
            case OPENED:
                this.progress = 1.0F;
        }

    }

    public EnderiteShulkerBoxTileEntity.AnimationStatus getAnimationStatus() {
        return this.animationStatus;
    }

    public AABB getBoundingBox(BlockState p_190584_1_) {
        return this.getBoundingBox(p_190584_1_.getValue(EnderiteShulkerBox.FACING));
    }

    public AABB getBoundingBox(Direction p_190587_1_) {
        float f = this.getProgress(1.0F);
        return Shapes.block().bounds().expandTowards((double) (0.5F * f * (float) p_190587_1_.getStepX()),
                (double) (0.5F * f * (float) p_190587_1_.getStepY()),
                (double) (0.5F * f * (float) p_190587_1_.getStepZ()));
    }

    private AABB getTopBoundingBox(Direction p_190588_1_) {
        Direction direction = p_190588_1_.getOpposite();
        return this.getBoundingBox(p_190588_1_).contract((double) direction.getStepX(),
                (double) direction.getStepY(), (double) direction.getStepZ());
    }

    private void moveCollidedEntities() {
        BlockState blockstate = this.level.getBlockState(this.getBlockPos());
        if (blockstate.getBlock() instanceof EnderiteShulkerBox) {
            Direction direction = blockstate.getValue(EnderiteShulkerBox.FACING);
            AABB axisalignedbb = this.getTopBoundingBox(direction).move(this.worldPosition);
            List<Entity> list = this.level.getEntities((Entity) null, axisalignedbb);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = list.get(i);
                    if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                        double d0 = 0.0D;
                        double d1 = 0.0D;
                        double d2 = 0.0D;
                        AABB axisalignedbb1 = entity.getBoundingBox();
                        switch (direction.getAxis()) {
                            case X:
                                if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                                    d0 = axisalignedbb.maxX - axisalignedbb1.minX;
                                } else {
                                    d0 = axisalignedbb1.maxX - axisalignedbb.minX;
                                }

                                d0 = d0 + 0.01D;
                                break;
                            case Y:
                                if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                                    d1 = axisalignedbb.maxY - axisalignedbb1.minY;
                                } else {
                                    d1 = axisalignedbb1.maxY - axisalignedbb.minY;
                                }

                                d1 = d1 + 0.01D;
                                break;
                            case Z:
                                if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                                    d2 = axisalignedbb.maxZ - axisalignedbb1.minZ;
                                } else {
                                    d2 = axisalignedbb1.maxZ - axisalignedbb.minZ;
                                }

                                d2 = d2 + 0.01D;
                        }

                        entity.move(MoverType.SHULKER_BOX, new Vec3(d0 * (double) direction.getStepX(),
                                d1 * (double) direction.getStepY(), d2 * (double) direction.getStepZ()));
                    }
                }

            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getContainerSize() {
        return this.items.size();
    }

    /**
     * See {@link Block#eventReceived} for more information. This must return true
     * serverside before it is called clientside.
     */
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.openCount = type;
            if (type == 0) {
                this.animationStatus = EnderiteShulkerBoxTileEntity.AnimationStatus.CLOSING;
                this.doNeighborUpdates();
            }

            if (type == 1) {
                this.animationStatus = EnderiteShulkerBoxTileEntity.AnimationStatus.OPENING;
                this.doNeighborUpdates();
            }

            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    private void doNeighborUpdates() {
        this.getBlockState().updateNeighbourShapes(this.getLevel(), this.getBlockPos(), 3);
    }

    public void startOpen(Player player) {
        if (!player.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }

            ++this.openCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
            if (this.openCount == 1) {
                this.level.playSound((Player) null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN,
                        SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    public void stopOpen(Player player) {
        if (!player.isSpectator()) {
            --this.openCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
            if (this.openCount <= 0) {
                this.level.playSound((Player) null, this.worldPosition, SoundEvents.SHULKER_BOX_CLOSE,
                        SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    protected Component getDefaultName() {
        return new TranslatableComponent("container.enderitemod.enderiteShulkerBox");
    }

    public void load( CompoundTag p_230337_2_) {
        super.load(p_230337_2_);
        this.loadFromNbt(p_230337_2_);
    }

    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        return this.saveToNbt(compound);
    }

    public void loadFromNbt(CompoundTag compound) {
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compound) && compound.contains("Items", 9)) {
            ContainerHelper.loadAllItems(compound, this.items);
        }

    }

    public CompoundTag saveToNbt(CompoundTag compound) {
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.items, false);
        }

        return compound;
    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = itemsIn;
    }

    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from
     * the given side.
     */
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return !(Block.byItem(itemStackIn.getItem()) instanceof EnderiteShulkerBox);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from
     * the given side.
     */
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    public float getProgress(float p_190585_1_) {
        return Mth.lerp(p_190585_1_, this.progressOld, this.progress);
    }

    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new ShulkerBoxMenu(id, player, this);
    }

    public boolean isClosed() {
        return this.animationStatus == EnderiteShulkerBoxTileEntity.AnimationStatus.CLOSED;
    }

    @Override
    protected net.minecraftforge.items.IItemHandler createUnSidedHandler() {
        return new net.minecraftforge.items.wrapper.SidedInvWrapper(this, Direction.UP);
    }

    public static enum AnimationStatus {
        CLOSED, OPENING, OPENED, CLOSING;
    }
}