package net.enderitemc.enderitemod.tools;

import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.LootEvent.LootTableModificationContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class EnderiteShears extends ShearsItem {

    public EnderiteShears(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BeehiveBlock beehiveBlock;
        BlockPos blockPos;
        World world = context.getWorld();
        BlockState blockstate = world.getBlockState(blockPos = context.getBlockPos());
        Block block = blockstate.getBlock();
        if (block instanceof BeehiveBlock && blockstate.get(BeehiveBlock.HONEY_LEVEL) >= 5) {
            PlayerEntity playerEntity = context.getPlayer();
            ItemStack itemStack = context.getStack();
            beehiveBlock = ((BeehiveBlock) block);
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
            }
            BeehiveBlock.dropHoneycomb(world, blockPos);
            beehiveBlock.takeHoney(world, blockstate, blockPos);
            world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            world.emitGameEvent((Entity) playerEntity, GameEvent.SHEAR, blockPos);
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, LivingEntity.getSlotForHand(context.getHand()));
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }

    public static void registerLoottables_Fabric() {
        LootEvent.MODIFY_LOOT_TABLE.register((key, context, builtin) -> {
            tryBuildLootTable(key, context, Blocks.ACACIA_LEAVES);

            tryBuildLootTable(key, context, Blocks.AZALEA_LEAVES);

            tryBuildLootTable(key, context, Blocks.BIRCH_LEAVES);

            tryBuildLootTable(key, context, Blocks.CAVE_VINES);
            tryBuildLootTable(key, context, Blocks.CAVE_VINES_PLANT);

            tryBuildLootTable(key, context, Blocks.COBWEB);

            tryBuildLootTable(key, context, Blocks.DARK_OAK_LEAVES);

            tryBuildLootTable(key, context, Blocks.DEAD_BUSH);

            tryBuildLootTable(key, context, Blocks.FERN);

            tryBuildLootTable(key, context, Blocks.FLOWERING_AZALEA_LEAVES);

            tryBuildLootTable(key, context, Blocks.GLOW_LICHEN);

            tryBuildLootTable(key, context, Blocks.SHORT_GRASS);

            tryBuildLootTable(key, context, Blocks.JUNGLE_LEAVES);

            tryBuildLootTable(key, context, Blocks.LARGE_FERN);

            tryBuildLootTable(key, context, Blocks.NETHER_SPROUTS);

            tryBuildLootTable(key, context, Blocks.OAK_LEAVES);

            tryBuildLootTable(key, context, Blocks.SEAGRASS);

            tryBuildLootTable(key, context, Blocks.SPRUCE_LEAVES);

            tryBuildLootTable(key, context, Blocks.TALL_GRASS);

            tryBuildLootTable(key, context, Blocks.TALL_SEAGRASS);

            tryBuildLootTable(key, context, Blocks.TWISTING_VINES);
            tryBuildLootTable(key, context, Blocks.TWISTING_VINES_PLANT);

            tryBuildLootTable(key, context, Blocks.VINE);

            tryBuildLootTable(key, context, Blocks.WEEPING_VINES);
            tryBuildLootTable(key, context, Blocks.WEEPING_VINES_PLANT);

            tryBuildLootTable(key, context, Blocks.SMALL_DRIPLEAF);

            tryBuildLootTable(key, context, Blocks.MANGROVE_LEAVES);
            tryBuildLootTable(key, context, Blocks.HANGING_ROOTS);

            tryBuildLootTable(key, context, Blocks.CHERRY_LEAVES);
        });
    }

    public static void tryBuildLootTable(RegistryKey<LootTable> key, LootTableModificationContext context, Block block) {
        if (block.getLootTableKey().isPresent() && block.getLootTableKey().get().equals(key)) {
            LootPool.Builder pool = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(MatchToolLootCondition
                    .builder(ItemPredicate.Builder.create().items(Registries.ITEM, EnderiteTools.ENDERITE_SHEAR.get())))
                .with(ItemEntry.builder(block.asItem()));
            context.addPool(pool);
        }
    }

}
