package net.enderitemc.enderitemod;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.blocks.CrackedEnderiteOre;
import net.enderitemc.enderitemod.blocks.EnderiteBlock;
import net.enderitemc.enderitemod.blocks.EnderiteOre;
import net.enderitemc.enderitemod.blocks.EnderiteRespawnAnchor;
import net.enderitemc.enderitemod.config.Config;
import net.enderitemc.enderitemod.config.ConfigLoader;
import net.enderitemc.enderitemod.enchantments.VoidFloatingEnchantment;
import net.enderitemc.enderitemod.items.EnderiteIngot;
import net.enderitemc.enderitemod.items.EnderiteScrap;
import net.enderitemc.enderitemod.materials.EnderiteArmorMaterial;
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.enderitemc.enderitemod.misc.EnderiteElytraSpecialRecipe;
import net.enderitemc.enderitemod.misc.EnderiteShieldDecorationRecipe;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlock;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntity;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxRecipe;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxScreenHandler;
import net.enderitemc.enderitemod.tools.AxeSubclass;
import net.enderitemc.enderitemod.tools.EnderiteBow;
import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.enderitemc.enderitemod.tools.EnderiteShears;
import net.enderitemc.enderitemod.tools.EnderiteShield;
import net.enderitemc.enderitemod.tools.EnderiteSword;
import net.enderitemc.enderitemod.tools.HoeSubclass;
import net.enderitemc.enderitemod.tools.PickaxeSubclass;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

public class EnderiteMod {
        public static final String MOD_ID = "enderitemod";
        // We can use this if we don't want to use DeferredRegister
        public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
        // Registering a new creative tab
        public static final ItemGroup ENDERITE_TAB = CreativeTabRegistry.create(new Identifier(MOD_ID, "enderite_tab"),
                        () -> new ItemStack(EnderiteMod.ENDERITE_INGOT.get()));

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_KEY);
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registry.BLOCK_KEY);
        public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(MOD_ID,
                        Registry.RECIPE_SERIALIZER_KEY);
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(MOD_ID,
                        Registry.BLOCK_ENTITY_TYPE_KEY);
        public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(MOD_ID,
                        Registry.ENCHANTMENT_KEY);
        public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(
                        MOD_ID,
                        Registry.CONFIGURED_FEATURE_KEY);
        public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(MOD_ID,
                        Registry.PLACED_FEATURE_KEY);

        public static Config CONFIG = ConfigLoader.get();

        // Enderite Ingot
        public static final RegistrySupplier<Item> ENDERITE_INGOT = ITEMS.register("enderite_ingot",
                        () -> new EnderiteIngot(new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof()));
        public static final RegistrySupplier<Item> ENDERITE_SCRAP = ITEMS.register("enderite_scrap",
                        () -> new EnderiteScrap(new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof()));

        // Enderite Tools
        public static final RegistrySupplier<Item> ENDERITE_PICKAXE = ITEMS.register("enderite_pickaxe",
                        () -> new PickaxeSubclass(EnderiteMaterial.ENDERITE,
                                        CONFIG.tools.enderitePickaxeAD, -2.8F,
                                        new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof()));
        public static final RegistrySupplier<Item> ENDERITE_AXE = ITEMS.register("enderite_axe",
                        () -> new AxeSubclass(EnderiteMaterial.ENDERITE, CONFIG.tools.enderiteAxeAD,
                                        -3.0F,
                                        new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof()));
        public static final RegistrySupplier<Item> ENDERITE_HOE = ITEMS.register("enderite_hoe",
                        () -> new HoeSubclass(EnderiteMaterial.ENDERITE, CONFIG.tools.enderiteHoeAD,
                                        0.0F,
                                        new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof()));

        public static final RegistrySupplier<Item> ENDERITE_SHOVEL = ITEMS.register("enderite_shovel",
                        () -> new ShovelItem(EnderiteMaterial.ENDERITE,
                                        CONFIG.tools.enderiteShovelAD, -3.0F,
                                        new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof()));
        public static final RegistrySupplier<Item> ENDERITE_SWORD = ITEMS.register("enderite_sword",
                        () -> new EnderiteSword(EnderiteMaterial.ENDERITE,
                                        CONFIG.tools.enderiteSwordAD, -2.4F,
                                        new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof()));

        // Enderite Block
        public static final Supplier<Item.Settings> BASE_ENDERITE_ITEM_SETTINGS = () -> new Item.Settings()
                        .group(EnderiteMod.ENDERITE_TAB).fireproof();
        public static final RegistrySupplier<Block> ENDERITE_BLOCK = BLOCKS.register("enderite_block",
                        () -> new EnderiteBlock(
                                        new Material.Builder(MapColor.BLACK).build()));
        public static final RegistrySupplier<Item> ENDERITE_BLOCK_ITEM = ITEMS.register("enderite_block",
                        () -> new BlockItem(ENDERITE_BLOCK.get(), BASE_ENDERITE_ITEM_SETTINGS.get()));
        public static final RegistrySupplier<Block> ENDERITE_ORE = BLOCKS.register("enderite_ore",
                        () -> new EnderiteOre());
        public static final RegistrySupplier<Item> ENDERITE_ORE_ITEM = ITEMS.register("enderite_ore",
                        () -> new BlockItem(ENDERITE_ORE.get(), BASE_ENDERITE_ITEM_SETTINGS.get()));
        public static final RegistrySupplier<Block> CRACKED_ENDERITE_ORE = BLOCKS.register("cracked_enderite_ore",
                        () -> new CrackedEnderiteOre());
        public static final RegistrySupplier<Item> CRACKED_ENDERITE_ORE_ITEM = ITEMS.register("cracked_enderite_ore",
                        () -> new BlockItem(CRACKED_ENDERITE_ORE.get(), BASE_ENDERITE_ITEM_SETTINGS.get()));

        public static final RegistrySupplier<Block> ENDERITE_RESPAWN_ANCHOR = BLOCKS.register("enderite_respawn_anchor",
                        () -> new EnderiteRespawnAnchor(AbstractBlock.Settings
                                        .of(Material.STONE, MapColor.BLACK).requiresTool().strength(50.0F, 1200.0F)
                                        .luminance((state) -> {
                                                return EnderiteRespawnAnchor.getLightLevel(state, 15);
                                        })));
        public static final RegistrySupplier<Item> ENDERITE_RESPAWN_ANCHOR_ITEM = ITEMS.register(
                        "enderite_respawn_anchor",
                        () -> new BlockItem(ENDERITE_RESPAWN_ANCHOR.get(), BASE_ENDERITE_ITEM_SETTINGS.get()));

        // Enderite Armor
        public static final RegistrySupplier<Item> ENDERITE_HELMET = ITEMS.register("enderite_helmet",
                        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.HEAD,
                                        (new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof())));
        public static final RegistrySupplier<Item> ENDERITE_CHESTPLATE = ITEMS.register("enderite_chestplate",
                        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE,
                                        EquipmentSlot.CHEST,
                                        (new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof())));
        public static final RegistrySupplier<Item> ENDERITE_LEGGINGS = ITEMS.register("enderite_leggings",
                        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.LEGS,
                                        (new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof())));
        public static final RegistrySupplier<Item> ENDERITE_BOOTS = ITEMS.register("enderite_boots",
                        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.FEET,
                                        (new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof())));

        // Enderite Elytra
        // Seperated to fabric-like and forge
        public static RegistrySupplier<? extends Item> ENDERITE_ELYTRA;
        public static RegistrySupplier<? extends Item> ENDERITE_ELYTRA_SEPERATED;
        public static RegistrySupplier<RecipeSerializer<?>> ENDERITE_EYLTRA_SPECIAL_RECIPE = RECIPES
                        .register("crafting_special_enderiteelytra",
                                        () -> new SpecialRecipeSerializer<>(
                                                        EnderiteElytraSpecialRecipe::new));

        // Shulker Box
        public static final RegistrySupplier<Block> ENDERITE_SHULKER_BOX = BLOCKS.register("enderite_shulker_box",
                        () -> new EnderiteShulkerBoxBlock((DyeColor) null,
                                        AbstractBlock.Settings.of(Material.SHULKER_BOX).nonOpaque().strength(2.0f,
                                                        17.0f)));
        public static final RegistrySupplier<Item> ENDERITE_SHULKER_BOX_ITEM = ITEMS.register("enderite_shulker_box",
                        () -> new BlockItem(ENDERITE_SHULKER_BOX.get(), BASE_ENDERITE_ITEM_SETTINGS.get().maxCount(1)));
        public static RegistrySupplier<RecipeSerializer<?>> ENDERITE_SHULKER_BOX_RECIPE = RECIPES
                        .register("crafting_special_enderiteshulkerbox",
                                        () -> new SpecialRecipeSerializer<>(
                                                        EnderiteShulkerBoxRecipe::new));
        public static RegistrySupplier<BlockEntityType<EnderiteShulkerBoxBlockEntity>> ENDERITE_SHULKER_BOX_BLOCK_ENTITY;

        // Bows
        public static final RegistrySupplier<Item> ENDERITE_CROSSBOW = ITEMS.register("enderite_crossbow",
                        () -> new EnderiteCrossbow(
                                        new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof().maxCount(1)
                                                        .maxDamage(768)));
        public static final RegistrySupplier<Item> ENDERITE_BOW = ITEMS.register("enderite_bow", () -> new EnderiteBow(
                        new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof().maxCount(1).maxDamage(768)));

        // Shield
        public static RegistrySupplier<Item> ENDERITE_SHIELD = ITEMS.register("enderite_shield",
                        () -> new EnderiteShield(
                                        new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof().maxCount(1)
                                                        .maxDamage(768)));

        public static RegistrySupplier<RecipeSerializer<?>> ENDERITE_SHIELD_DECORATION_RECIPE = RECIPES
                        .register("crafting_special_enderiteshielddecoration",
                                        () -> new SpecialRecipeSerializer<>(
                                                        EnderiteShieldDecorationRecipe::new));

        // Enchantment
        public static RegistrySupplier<Enchantment> VOID_FLOATING_ENCHANTMENT = ENCHANTMENTS.register("void_floating",
                        () -> new VoidFloatingEnchantment());

        public static ScreenHandlerType<EnderiteShulkerBoxScreenHandler> ENDERITE_SHULKER_BOX_SCREEN_HANDLER;

        // MOST IMPORTANT
        public static final RegistrySupplier<Item> ENDERITE_SHEAR = ITEMS.register("enderite_shears",
                        () -> new EnderiteShears(
                                        new Item.Settings().group(ItemGroup.TOOLS).fireproof().maxCount(1)
                                                        .maxDamage(2048)
                                                        .rarity(Rarity.RARE)));

        // World Generation
        public static RegistrySupplier<ConfiguredFeature<OreFeatureConfig, ?>> ENDERITE_ORE_CONFIGURED_FEATURE = CONFIGURED_FEATURES
                        .register("ore_enderite_end",
                                        () -> new ConfiguredFeature<>(
                                                        Feature.ORE,
                                                        new OreFeatureConfig(
                                                                        new BlockMatchRuleTest(Blocks.END_STONE),
                                                                        EnderiteMod.ENDERITE_ORE.get()
                                                                                        .getDefaultState(),
                                                                        EnderiteMod.CONFIG.worldGeneration.enderiteOre.veinSize)));
        public static RegistrySupplier<PlacedFeature> ENDERITE_ORE_PLACED_FEATURE = PLACED_FEATURES.register(
                        "ore_enderite_end",
                        () -> new PlacedFeature(
                                        BuiltinRegistries.CONFIGURED_FEATURE.getEntry(
                                                        BuiltinRegistries.CONFIGURED_FEATURE
                                                                        .getKey(ENDERITE_ORE_CONFIGURED_FEATURE.get())
                                                                        .orElseThrow())
                                                        .orElseThrow(),
                                        List.of(CountPlacementModifier
                                                        .of(EnderiteMod.CONFIG.worldGeneration.enderiteOre.veinAmount),
                                                        HeightRangePlacementModifier.uniform(YOffset.fixed(12),
                                                                        YOffset.fixed(48)),
                                                        SquarePlacementModifier.of())));

        public static void init() {
                BLOCKS.register();
                ITEMS.register();
                RECIPES.register();
                BLOCK_ENTITY_TYPES.register();
                ENCHANTMENTS.register();
                CONFIGURED_FEATURES.register();
                PLACED_FEATURES.register();

                // 1.19.3 Ore Gen
                // BiomeModifications.addFeature(
                // BiomeSelectors.foundInTheEnd(),
                // GenerationStep.Feature.UNDERGROUND_ORES,
                // RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("enderitemod",
                // "ore_enderite_large")));
                // BiomeModifications.addFeature(
                // BiomeSelectors.foundInTheEnd(),
                // GenerationStep.Feature.UNDERGROUND_ORES,
                // RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("enderitemod",
                // "ore_enderite_small")));
                LifecycleEvent.SETUP.register(() -> {
                        BiomeModifications.addProperties((ctx, mutable) -> {
                                if (ctx.hasTag(BiomeTags.IS_END)) {
                                        mutable.getGenerationProperties().addFeature(
                                                        GenerationStep.Feature.UNDERGROUND_ORES,
                                                        BuiltinRegistries.PLACED_FEATURE.getEntry(RegistryKey.of(
                                                                        Registry.PLACED_FEATURE_KEY,
                                                                        ENDERITE_ORE_PLACED_FEATURE.getId()))
                                                                        .orElseThrow());

                                        // mutable.getGenerationProperties().addFeature(
                                        // GenerationStep.Feature.UNDERGROUND_ORES,
                                        // ENDERITE_ORE_PLACED_FEATURE.get());
                                }
                        });
                });

                EnderiteShears.registerLoottables();

                System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
        }
}
