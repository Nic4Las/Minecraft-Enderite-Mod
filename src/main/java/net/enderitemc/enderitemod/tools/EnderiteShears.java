package net.enderitemc.enderitemod.tools;

import net.enderitemc.enderitemod.EnderiteMod;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
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
            beehiveBlock = ((BeehiveBlock)block);
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }
            beehiveBlock.dropHoneycomb(world, blockPos);
            beehiveBlock.takeHoney(world, blockstate, blockPos);
            world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            world.emitGameEvent((Entity)playerEntity, GameEvent.SHEAR, blockPos);
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, player -> player.sendToolBreakStatus(context.getHand()));
            }
            return ActionResult.success(world.isClient);
        }
        return super.useOnBlock(context);
    }

    public static void registerLoottables() {
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, table, setter) -> {
            
            tryBuildLootTable(id, table ,Blocks.ACACIA_LEAVES);
        
            tryBuildLootTable(id, table ,Blocks.AZALEA_LEAVES);
        
            tryBuildLootTable(id, table ,Blocks.BIRCH_LEAVES);
        
            tryBuildLootTable(id, table ,Blocks.CAVE_VINES);        
            tryBuildLootTable(id, table ,Blocks.CAVE_VINES_PLANT);
        
            tryBuildLootTable(id, table ,Blocks.COBWEB);
        
            tryBuildLootTable(id, table ,Blocks.DARK_OAK_LEAVES);
        
            tryBuildLootTable(id, table ,Blocks.DEAD_BUSH);
        
            tryBuildLootTable(id, table ,Blocks.FERN);
        
            tryBuildLootTable(id, table ,Blocks.FLOWERING_AZALEA_LEAVES);
        
            tryBuildLootTable(id, table ,Blocks.GLOW_LICHEN);
        
            tryBuildLootTable(id, table ,Blocks.GRASS);
        
            tryBuildLootTable(id, table ,Blocks.JUNGLE_LEAVES);
        
            tryBuildLootTable(id, table ,Blocks.LARGE_FERN);
        
            tryBuildLootTable(id, table ,Blocks.NETHER_SPROUTS);
        
            tryBuildLootTable(id, table ,Blocks.OAK_LEAVES);
        
            tryBuildLootTable(id, table ,Blocks.SEAGRASS);
        
            tryBuildLootTable(id, table ,Blocks.SPRUCE_LEAVES);

            tryBuildLootTable(id, table ,Blocks.TALL_GRASS);

            tryBuildLootTable(id, table ,Blocks.TALL_SEAGRASS);

            tryBuildLootTable(id, table ,Blocks.TWISTING_VINES);
            tryBuildLootTable(id, table ,Blocks.TWISTING_VINES_PLANT);

            tryBuildLootTable(id, table ,Blocks.VINE);

            tryBuildLootTable(id, table ,Blocks.WEEPING_VINES);
            tryBuildLootTable(id, table ,Blocks.WEEPING_VINES_PLANT);
        });
    }

    public static void tryBuildLootTable(Identifier id, FabricLootSupplierBuilder table, Block block) { 
        if(block.getLootTableId().equals(id)) {
            FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .withCondition(MatchToolLootCondition.builder(ItemPredicate.Builder.create().items(EnderiteMod.ENDERITE_SHEAR)).build())
                    .with(ItemEntry.builder(block.asItem()));
                    table.pool(poolBuilder);
        }
    }
    
}
