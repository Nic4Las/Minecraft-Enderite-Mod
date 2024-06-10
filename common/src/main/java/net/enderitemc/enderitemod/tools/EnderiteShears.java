package net.enderitemc.enderitemod.tools;

import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.LootEvent.LootTableModificationContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
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
            return ActionResult.success(world.isClient);
        }
        return super.useOnBlock(context);
    }

    public static void registerLoottables_Fabric() {
        LootEvent.MODIFY_LOOT_TABLE.register((baseTable, table, builtin) -> {
            Identifier id = baseTable.getValue();

            tryBuildLootTable(id, table, Blocks.ACACIA_LEAVES);

            tryBuildLootTable(id, table, Blocks.AZALEA_LEAVES);

            tryBuildLootTable(id, table, Blocks.BIRCH_LEAVES);

            tryBuildLootTable(id, table, Blocks.CAVE_VINES);
            tryBuildLootTable(id, table, Blocks.CAVE_VINES_PLANT);

            tryBuildLootTable(id, table, Blocks.COBWEB);

            tryBuildLootTable(id, table, Blocks.DARK_OAK_LEAVES);

            tryBuildLootTable(id, table, Blocks.DEAD_BUSH);

            tryBuildLootTable(id, table, Blocks.FERN);

            tryBuildLootTable(id, table, Blocks.FLOWERING_AZALEA_LEAVES);

            tryBuildLootTable(id, table, Blocks.GLOW_LICHEN);

            tryBuildLootTable(id, table, Blocks.SHORT_GRASS);

            tryBuildLootTable(id, table, Blocks.JUNGLE_LEAVES);

            tryBuildLootTable(id, table, Blocks.LARGE_FERN);

            tryBuildLootTable(id, table, Blocks.NETHER_SPROUTS);

            tryBuildLootTable(id, table, Blocks.OAK_LEAVES);

            tryBuildLootTable(id, table, Blocks.SEAGRASS);

            tryBuildLootTable(id, table, Blocks.SPRUCE_LEAVES);

            tryBuildLootTable(id, table, Blocks.TALL_GRASS);

            tryBuildLootTable(id, table, Blocks.TALL_SEAGRASS);

            tryBuildLootTable(id, table, Blocks.TWISTING_VINES);
            tryBuildLootTable(id, table, Blocks.TWISTING_VINES_PLANT);

            tryBuildLootTable(id, table, Blocks.VINE);

            tryBuildLootTable(id, table, Blocks.WEEPING_VINES);
            tryBuildLootTable(id, table, Blocks.WEEPING_VINES_PLANT);

            tryBuildLootTable(id, table, Blocks.SMALL_DRIPLEAF);

            tryBuildLootTable(id, table, Blocks.MANGROVE_LEAVES);
            tryBuildLootTable(id, table, Blocks.HANGING_ROOTS);
        });
    }

    public static void tryBuildLootTable(Identifier id, LootTableModificationContext table, Block block) {
        if (block.getLootTableKey().equals(id)) {
            LootPool.Builder pool = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .conditionally(MatchToolLootCondition
                            .builder(ItemPredicate.Builder.create().items(EnderiteTools.ENDERITE_SHEAR.get())))
                    .with(ItemEntry.builder(block.asItem()));
            table.addPool(pool);
        }
    }

}
