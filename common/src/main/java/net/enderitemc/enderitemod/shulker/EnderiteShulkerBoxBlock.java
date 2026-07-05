package net.enderitemc.enderitemod.shulker;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.List;

public class EnderiteShulkerBoxBlock extends ShulkerBoxBlock {

    public static final Identifier CONTENTS_DYNAMIC_DROP_ID = Identifier.withDefaultNamespace("contents");

    private static final BlockBehaviour.StatePredicate SUFFOCATES_PREDICATE = (state, world, pos) -> !(world.getBlockEntity(pos) instanceof EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity) || shulkerBoxBlockEntity.suffocates();

    public EnderiteShulkerBoxBlock(String id) {
        super((DyeColor) null, Properties.ofFullCopy(Blocks.SHULKER_BOX)
            .setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, id)))
            .noOcclusion().strength(2.0f, 17.0f)
            .isSuffocating(SUFFOCATES_PREDICATE)
            .isViewBlocking(SUFFOCATES_PREDICATE)
            .sound(SoundType.NETHERITE_BLOCK));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnderiteShulkerBoxBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state,
                                                                  BlockEntityType<T> type) {
        return createTickerHelper(type, EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.get(),
                EnderiteShulkerBoxBlockEntity::tick);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world instanceof ServerLevel serverWorld) {
            if (player.isSpectator()) {
                return InteractionResult.CONSUME;
            } else {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity) {
                    if (canOpen(state, world, pos, shulkerBoxBlockEntity)) {
                        player.openMenu(shulkerBoxBlockEntity);
                        player.awardStat(Stats.OPEN_SHULKER_BOX);
                        PiglinAi.angerNearbyPiglins(serverWorld, player, true);
                    }

                    return InteractionResult.CONSUME;
                } else {
                    return InteractionResult.PASS;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    private static boolean canOpen(BlockState state, Level world, BlockPos pos, EnderiteShulkerBoxBlockEntity entity) {
        if (entity.getAnimationStage() != EnderiteShulkerBoxBlockEntity.AnimationStage.CLOSED) {
            return true;
        } else {
            AABB box = Shulker.getProgressDeltaAabb(1.0f, state.getValue(FACING), 0.0f, 0.5f, Vec3.atBottomCenterOf(pos)).deflate(1.0E-6);
            return world.noCollision(box);
        }
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity) {
            if (!world.isClientSide() && player.preventsBlockDrops() && !shulkerBoxBlockEntity.isEmpty()) {
                ItemStack itemStack = getItemStack();
                itemStack.applyComponents(blockEntity.collectComponents());
                ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
                    (double) pos.getZ() + 0.5D, itemStack);
                itemEntity.setDefaultPickUpDelay();
                world.addFreshEntity(itemEntity);
            } else {
                shulkerBoxBlockEntity.unpackLootTable(player);
            }
        }

        return super.playerWillDestroy(world, pos, state, player);
    }


    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity) {
            builder = builder.withDynamicDrop(CONTENTS, lootConsumer -> {
                for (int i = 0; i < shulkerBoxBlockEntity.getContainerSize(); ++i) {
                    lootConsumer.accept(shulkerBoxBlockEntity.getItem(i));
                }
            });
        }
        return super.getDrops(state, builder);
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity && !shulkerBoxBlockEntity.suffocates()
            ? (VoxelShape)SHAPES_OPEN_SUPPORT.get(((Direction)state.getValue(FACING)).getOpposite())
            : Shapes.block();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof EnderiteShulkerBoxBlockEntity shulkerBlockEntity
            ? Shapes.create(shulkerBlockEntity.getBoundingBox(state))
            : Shapes.block();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public static ItemStack getItemStack() {
        return new ItemStack(EnderiteMod.ENDERITE_SHULKER_BOX.get());
    }

}
