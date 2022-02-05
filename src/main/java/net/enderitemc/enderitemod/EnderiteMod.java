package net.enderitemc.enderitemod;

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
import net.enderitemc.enderitemod.tools.EnderiteElytraChestplate;
import net.enderitemc.enderitemod.tools.EnderiteElytraSeperated;
import net.enderitemc.enderitemod.tools.EnderiteShears;
import net.enderitemc.enderitemod.tools.EnderiteShield;
import net.enderitemc.enderitemod.tools.EnderiteSword;
import net.enderitemc.enderitemod.tools.HoeSubclass;
import net.enderitemc.enderitemod.tools.PickaxeSubclass;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolItem;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.BlockStateMatchRuleTest;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.CountPlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.heightprovider.*;

public class EnderiteMod implements ModInitializer {

	public static Config CONFIG = ConfigLoader.get();

	// Enderite Ingot
	public static final EnderiteIngot ENDERITE_INGOT = new EnderiteIngot(
			new Item.Settings().group(ItemGroup.MISC).fireproof());
	public static final EnderiteScrap ENDERITE_SCRAP = new EnderiteScrap(
			new Item.Settings().group(ItemGroup.MISC).fireproof());

	// Enderite Tools
	public static final ToolItem ENDERITE_PICKAXE = new PickaxeSubclass(EnderiteMaterial.ENDERITE, CONFIG.tools.enderitePickaxeAD, -2.8F,
			new Item.Settings().group(ItemGroup.TOOLS).fireproof());
	public static final ToolItem ENDERITE_AXE = new AxeSubclass(EnderiteMaterial.ENDERITE, CONFIG.tools.enderiteAxeAD, -3.0F,
			new Item.Settings().group(ItemGroup.TOOLS).fireproof());
	public static final ToolItem ENDERITE_HOE = new HoeSubclass(EnderiteMaterial.ENDERITE, CONFIG.tools.enderiteHoeAD, 0.0F,
			new Item.Settings().group(ItemGroup.TOOLS).fireproof());

	public static final ToolItem ENDERITE_SHOVEL = new ShovelItem(EnderiteMaterial.ENDERITE, CONFIG.tools.enderiteShovelAD, -3.0F,
			new Item.Settings().group(ItemGroup.TOOLS).fireproof());
	public static final EnderiteSword ENDERITE_SWORD = new EnderiteSword(EnderiteMaterial.ENDERITE, CONFIG.tools.enderiteSwordAD, -2.4F,
			new Item.Settings().group(ItemGroup.COMBAT).fireproof());

	// Enderite Block
	public static final EnderiteBlock ENDERITE_BLOCK = new EnderiteBlock(
			new FabricMaterialBuilder(MapColor.BLACK).build());
	public static final EnderiteOre ENDERITE_ORE = new EnderiteOre();
	public static final CrackedEnderiteOre CRACKED_ENDERITE_ORE = new CrackedEnderiteOre();

	public static final EnderiteRespawnAnchor ENDERITE_RESPAWN_ANCHOR = new EnderiteRespawnAnchor(AbstractBlock.Settings
			.of(Material.STONE, MapColor.BLACK).requiresTool().strength(50.0F, 1200.0F).luminance((state) -> {
				return EnderiteRespawnAnchor.getLightLevel(state, 15);
			}));

	// Enderite Armor
	public static final ArmorItem ENDERITE_HELMET = new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.HEAD,
			(new Item.Settings().group(ItemGroup.COMBAT).fireproof()));
	public static final ArmorItem ENDERITE_CHESTPLATE = new ArmorItem(EnderiteArmorMaterial.ENDERITE,
			EquipmentSlot.CHEST, (new Item.Settings().group(ItemGroup.COMBAT).fireproof()));
	public static final ArmorItem ENDERITE_LEGGINGS = new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.LEGS,
			(new Item.Settings().group(ItemGroup.COMBAT).fireproof()));
	public static final ArmorItem ENDERITE_BOOTS = new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.FEET,
			(new Item.Settings().group(ItemGroup.COMBAT).fireproof()));

	// Enderite Elytra
	public static final ArmorItem ENDERITE_ELYTRA = new EnderiteElytraChestplate(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.CHEST,
			(new Item.Settings().group(ItemGroup.COMBAT).fireproof().rarity(Rarity.EPIC)));
	public static SpecialRecipeSerializer<EnderiteElytraSpecialRecipe> ENDERITE_EYLTRA_SPECIAL_RECIPE = new SpecialRecipeSerializer<>(
			EnderiteElytraSpecialRecipe::new);
	public static final EnderiteElytraSeperated ENDERITE_ELYTRA_SEPERATED = new EnderiteElytraSeperated(
			(new Item.Settings().group(ItemGroup.TRANSPORTATION).fireproof().maxCount(1).maxDamage(1024)
					.rarity(Rarity.EPIC)));

	// Shulker Box
	public static BlockEntityType<EnderiteShulkerBoxBlockEntity> ENDERITE_SHULKER_BOX_BLOCK_ENTITY;
	public static final EnderiteShulkerBoxBlock ENDERITE_SHULKER_BOX = new EnderiteShulkerBoxBlock((DyeColor) null,
			FabricBlockSettings.of(Material.SHULKER_BOX).nonOpaque().strength(2.0f, 17.0f)
					.breakByTool(FabricToolTags.PICKAXES, 1));
	public static SpecialRecipeSerializer<EnderiteShulkerBoxRecipe> ENDERITE_SHULKER_BOX_RECIPE = new SpecialRecipeSerializer<>(
			EnderiteShulkerBoxRecipe::new);

	// Bows
	public static final EnderiteCrossbow ENDERITE_CROSSBOW = new EnderiteCrossbow(
			new Item.Settings().group(ItemGroup.COMBAT).fireproof().maxCount(1).maxDamage(768));
	public static final BowItem ENDERITE_BOW = new EnderiteBow(
			new Item.Settings().group(ItemGroup.COMBAT).fireproof().maxCount(1).maxDamage(768));

	// Shield
	public static final ShieldItem ENDERITE_SHIELD = new EnderiteShield(
			new Item.Settings().group(ItemGroup.COMBAT).fireproof().maxCount(1).maxDamage(768));

	public static SpecialRecipeSerializer<EnderiteShieldDecorationRecipe> ENDERITE_SHIELD_DECORATION_RECIPE = new SpecialRecipeSerializer<>(
			EnderiteShieldDecorationRecipe::new);

	// Enchantment
	public static Enchantment VOID_FLOATING_ENCHANTMENT;

	public static ScreenHandlerType<EnderiteShulkerBoxScreenHandler> ENDERITE_SHULKER_BOX_SCREEN_HANDLER;

	//public static ConfiguredFeature<?, ?> ENDERITE_ORE_FEATURE = OreFeature.ORE
	//		.configure(new OreFeatureConfig(new BlockStateMatchRuleTest(Blocks.END_STONE.getDefaultState()),
	//				ENDERITE_ORE.getDefaultState(), CONFIG.worldGeneration.enderiteOre.veinSize))
	//		.decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.fixed(12), YOffset.fixed(48)))).repeat(CONFIG.worldGeneration.enderiteOre.veinAmount));

	private static ConfiguredFeature<?, ?> ENDERITE_ORE_CONFIGURED_FEATURE = Feature.ORE
			.configure(new OreFeatureConfig(
				new BlockMatchRuleTest(Blocks.END_STONE), // we use new BlockMatchRuleTest(Blocks.END_STONE) here
				ENDERITE_ORE.getDefaultState(),
				CONFIG.worldGeneration.enderiteOre.veinSize));
	   
	public static PlacedFeature ENDERITE_ORE_PLACED_FEATURE = ENDERITE_ORE_CONFIGURED_FEATURE.withPlacement(
			CountPlacementModifier.of(CONFIG.worldGeneration.enderiteOre.veinAmount),
			HeightRangePlacementModifier.uniform(YOffset.fixed(12), YOffset.fixed(48)));

	// MOST IMPORTANT
	public static final ShearsItem ENDERITE_SHEAR = new EnderiteShears(
			new Item.Settings().group(ItemGroup.TOOLS).fireproof().maxCount(1).maxDamage(2048).rarity(Rarity.RARE));

	@Override
	public void onInitialize() {

		// Items

		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_ingot"), ENDERITE_INGOT);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_scrap"), ENDERITE_SCRAP);

		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_pickaxe"), ENDERITE_PICKAXE);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_axe"), ENDERITE_AXE);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_hoe"), ENDERITE_HOE);

		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_shovel"), ENDERITE_SHOVEL);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_sword"), ENDERITE_SWORD);

		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_crossbow"), ENDERITE_CROSSBOW);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_bow"), ENDERITE_BOW);

		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_shield"), ENDERITE_SHIELD);
		Registry.register(Registry.RECIPE_SERIALIZER, "enderitemod:crafting_special_enderiteshielddecoration",
				ENDERITE_SHIELD_DECORATION_RECIPE);

		// IMPORTANT
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_shears"), ENDERITE_SHEAR);

		// Blocks

		Registry.register(Registry.BLOCK, new Identifier("enderitemod", "enderite_block"), ENDERITE_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_block"),
				new BlockItem(ENDERITE_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS).fireproof()));

		Registry.register(Registry.BLOCK, new Identifier("enderitemod", "enderite_respawn_anchor"),
				ENDERITE_RESPAWN_ANCHOR);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_respawn_anchor"),
				new BlockItem(ENDERITE_RESPAWN_ANCHOR, new Item.Settings().group(ItemGroup.DECORATIONS).fireproof()));

		Registry.register(Registry.BLOCK, new Identifier("enderitemod", "enderite_ore"), ENDERITE_ORE);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_ore"),
				new BlockItem(ENDERITE_ORE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS).fireproof()));
		Registry.register(Registry.BLOCK, new Identifier("enderitemod", "cracked_enderite_ore"), CRACKED_ENDERITE_ORE);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "cracked_enderite_ore"),
				new BlockItem(CRACKED_ENDERITE_ORE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS).fireproof()));

		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_helmet"), ENDERITE_HELMET);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_chestplate"), ENDERITE_CHESTPLATE);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_leggings"), ENDERITE_LEGGINGS);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_boots"), ENDERITE_BOOTS);

		// Elytra
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_elytra"), ENDERITE_ELYTRA);
		Registry.register(Registry.RECIPE_SERIALIZER, "enderitemod:crafting_special_enderiteelytra",
				ENDERITE_EYLTRA_SPECIAL_RECIPE);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_elytra_seperated"),
				ENDERITE_ELYTRA_SEPERATED);

		// Shulker
		Registry.register(Registry.RECIPE_SERIALIZER, "enderitemod:crafting_special_enderiteshulkerbox",
				ENDERITE_SHULKER_BOX_RECIPE);
		Registry.register(Registry.BLOCK, new Identifier("enderitemod", "enderite_shulker_box"), ENDERITE_SHULKER_BOX);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_shulker_box"), new BlockItem(
				ENDERITE_SHULKER_BOX, new Item.Settings().group(ItemGroup.DECORATIONS).fireproof().maxCount(1)));
		ENDERITE_SHULKER_BOX_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
				"enderitemod:enderite_shulker_box_block_entity",
				FabricBlockEntityTypeBuilder.create(EnderiteShulkerBoxBlockEntity::new, ENDERITE_SHULKER_BOX).build(null));
		FabricBlockEntityTypeBuilder.create(ShulkerBoxBlockEntity::new, ENDERITE_SHULKER_BOX).build(null);

		DispenserBlock.registerBehavior(ENDERITE_SHULKER_BOX.asItem(), new BlockPlacementDispenserBehavior());

		// ENCHANTMENT
		VOID_FLOATING_ENCHANTMENT = Registry.register(Registry.ENCHANTMENT,
				new Identifier("enderitemod", "void_floating"), new VoidFloatingEnchantment());

		//RegistryKey<ConfiguredFeature<?, ?>> oreEnderiteEnd = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
		//		new Identifier("enderitemod", "ore_enderite_end"));
		//Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreEnderiteEnd.getValue(), ENDERITE_ORE_FEATURE);
		//BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES,
		//		oreEnderiteEnd);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
        				new Identifier("enderitemod", "ore_enderite_end"), ENDERITE_ORE_CONFIGURED_FEATURE);
    	Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("enderitemod", "ore_enderite_end"),
		ENDERITE_ORE_PLACED_FEATURE);
    	BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES,
        RegistryKey.of(Registry.PLACED_FEATURE_KEY,
            new Identifier("enderitemod", "ore_enderite_end")));


		System.out.println("-Initialized Enderitemod!-");
	}
}
