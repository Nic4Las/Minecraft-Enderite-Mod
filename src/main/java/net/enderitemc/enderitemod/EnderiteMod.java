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
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolItem;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.gen.GenerationStep;

public class EnderiteMod implements ModInitializer {

	public static Config CONFIG = ConfigLoader.get();

	// Enderite Ingot
	public static final EnderiteIngot ENDERITE_INGOT = new EnderiteIngot(
			new Item.Settings().fireproof());
	public static final EnderiteScrap ENDERITE_SCRAP = new EnderiteScrap(
			new Item.Settings().fireproof());

	private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier("enderitemod", "enderite_group"))
			.icon(() -> new ItemStack(ENDERITE_INGOT))
			.build();

	// Enderite Tools
	public static final ToolItem ENDERITE_PICKAXE = new PickaxeSubclass(EnderiteMaterial.ENDERITE,
			CONFIG.tools.enderitePickaxeAD, -2.8F,
			new Item.Settings().fireproof());
	public static final ToolItem ENDERITE_AXE = new AxeSubclass(EnderiteMaterial.ENDERITE, CONFIG.tools.enderiteAxeAD,
			-3.0F,
			new Item.Settings().fireproof());
	public static final ToolItem ENDERITE_HOE = new HoeSubclass(EnderiteMaterial.ENDERITE, CONFIG.tools.enderiteHoeAD,
			0.0F,
			new Item.Settings().fireproof());

	public static final ToolItem ENDERITE_SHOVEL = new ShovelItem(EnderiteMaterial.ENDERITE,
			CONFIG.tools.enderiteShovelAD, -3.0F,
			new Item.Settings().fireproof());
	public static final EnderiteSword ENDERITE_SWORD = new EnderiteSword(EnderiteMaterial.ENDERITE,
			CONFIG.tools.enderiteSwordAD, -2.4F,
			new Item.Settings().fireproof());

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
	public static final ArmorItem ENDERITE_HELMET = new ArmorItem(EnderiteArmorMaterial.ENDERITE, ArmorItem.Type.HELMET,
			(new Item.Settings().fireproof()));
	public static final ArmorItem ENDERITE_CHESTPLATE = new ArmorItem(EnderiteArmorMaterial.ENDERITE,
			ArmorItem.Type.CHESTPLATE, (new Item.Settings().fireproof()));
	public static final ArmorItem ENDERITE_LEGGINGS = new ArmorItem(EnderiteArmorMaterial.ENDERITE, ArmorItem.Type.LEGGINGS,
			(new Item.Settings().fireproof()));
	public static final ArmorItem ENDERITE_BOOTS = new ArmorItem(EnderiteArmorMaterial.ENDERITE, ArmorItem.Type.BOOTS,
			(new Item.Settings().fireproof()));

	// Enderite Elytra
	public static final ArmorItem ENDERITE_ELYTRA = new EnderiteElytraChestplate(EnderiteArmorMaterial.ENDERITE,
			ArmorItem.Type.CHESTPLATE,
			(new Item.Settings().fireproof().rarity(Rarity.EPIC)));
	public static SpecialRecipeSerializer<EnderiteElytraSpecialRecipe> ENDERITE_EYLTRA_SPECIAL_RECIPE = new SpecialRecipeSerializer<>(
			EnderiteElytraSpecialRecipe::new);
	public static final EnderiteElytraSeperated ENDERITE_ELYTRA_SEPERATED = new EnderiteElytraSeperated(
			(new Item.Settings().fireproof().maxCount(1).maxDamage(1024)
					.rarity(Rarity.EPIC)));

	// Shulker Box
	public static BlockEntityType<EnderiteShulkerBoxBlockEntity> ENDERITE_SHULKER_BOX_BLOCK_ENTITY;
	public static final EnderiteShulkerBoxBlock ENDERITE_SHULKER_BOX = new EnderiteShulkerBoxBlock((DyeColor) null,
			FabricBlockSettings.of(Material.SHULKER_BOX).nonOpaque().strength(2.0f, 17.0f));
	public static SpecialRecipeSerializer<EnderiteShulkerBoxRecipe> ENDERITE_SHULKER_BOX_RECIPE = new SpecialRecipeSerializer<>(
			EnderiteShulkerBoxRecipe::new);

	// Bows
	public static final EnderiteCrossbow ENDERITE_CROSSBOW = new EnderiteCrossbow(
			new Item.Settings().fireproof().maxCount(1).maxDamage(768));
	public static final BowItem ENDERITE_BOW = new EnderiteBow(
			new Item.Settings().fireproof().maxCount(1).maxDamage(768));

	// Shield
	public static final ShieldItem ENDERITE_SHIELD = new EnderiteShield(
			new Item.Settings().fireproof().maxCount(1).maxDamage(768));

	public static SpecialRecipeSerializer<EnderiteShieldDecorationRecipe> ENDERITE_SHIELD_DECORATION_RECIPE = new SpecialRecipeSerializer<>(
			EnderiteShieldDecorationRecipe::new);

	// Enchantment
	public static Enchantment VOID_FLOATING_ENCHANTMENT;

	public static ScreenHandlerType<EnderiteShulkerBoxScreenHandler> ENDERITE_SHULKER_BOX_SCREEN_HANDLER;

	// MOST IMPORTANT
	public static final ShearsItem ENDERITE_SHEAR = new EnderiteShears(
			new Item.Settings().fireproof().maxCount(1).maxDamage(2048).rarity(Rarity.RARE));

	@Override
	public void onInitialize() {

		// Items

		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_ingot"), ENDERITE_INGOT);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_scrap"), ENDERITE_SCRAP);

		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_pickaxe"), ENDERITE_PICKAXE);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_axe"), ENDERITE_AXE);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_hoe"), ENDERITE_HOE);

		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_shovel"), ENDERITE_SHOVEL);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_sword"), ENDERITE_SWORD);

		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_crossbow"), ENDERITE_CROSSBOW);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_bow"), ENDERITE_BOW);

		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_shield"), ENDERITE_SHIELD);
		Registry.register(Registries.RECIPE_SERIALIZER, "enderitemod:crafting_special_enderiteshielddecoration",
				ENDERITE_SHIELD_DECORATION_RECIPE);

		// IMPORTANT
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_shears"), ENDERITE_SHEAR);

		// Blocks

		Registry.register(Registries.BLOCK, new Identifier("enderitemod", "enderite_block"), ENDERITE_BLOCK);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_block"),
				new BlockItem(ENDERITE_BLOCK, new Item.Settings().fireproof()));

		Registry.register(Registries.BLOCK, new Identifier("enderitemod", "enderite_respawn_anchor"),
				ENDERITE_RESPAWN_ANCHOR);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_respawn_anchor"),
				new BlockItem(ENDERITE_RESPAWN_ANCHOR, new Item.Settings().fireproof()));

		Registry.register(Registries.BLOCK, new Identifier("enderitemod", "enderite_ore"), ENDERITE_ORE);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_ore"),
				new BlockItem(ENDERITE_ORE, new Item.Settings().fireproof()));
		Registry.register(Registries.BLOCK, new Identifier("enderitemod", "cracked_enderite_ore"),
				CRACKED_ENDERITE_ORE);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "cracked_enderite_ore"),
				new BlockItem(CRACKED_ENDERITE_ORE, new Item.Settings().fireproof()));

		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_helmet"), ENDERITE_HELMET);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_chestplate"), ENDERITE_CHESTPLATE);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_leggings"), ENDERITE_LEGGINGS);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_boots"), ENDERITE_BOOTS);

		// Elytra
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_elytra"), ENDERITE_ELYTRA);
		Registry.register(Registries.RECIPE_SERIALIZER, "enderitemod:crafting_special_enderiteelytra",
				ENDERITE_EYLTRA_SPECIAL_RECIPE);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_elytra_seperated"),
				ENDERITE_ELYTRA_SEPERATED);

		// Shulker
		Registry.register(Registries.RECIPE_SERIALIZER, "enderitemod:crafting_special_enderiteshulkerbox",
				ENDERITE_SHULKER_BOX_RECIPE);
		Registry.register(Registries.BLOCK, new Identifier("enderitemod", "enderite_shulker_box"),
				ENDERITE_SHULKER_BOX);
		Registry.register(Registries.ITEM, new Identifier("enderitemod", "enderite_shulker_box"), new BlockItem(
				ENDERITE_SHULKER_BOX, new Item.Settings().fireproof().maxCount(1)));
		ENDERITE_SHULKER_BOX_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
				"enderitemod:enderite_shulker_box_block_entity",
				FabricBlockEntityTypeBuilder.create(EnderiteShulkerBoxBlockEntity::new, ENDERITE_SHULKER_BOX)
						.build(null));
		FabricBlockEntityTypeBuilder.create(ShulkerBoxBlockEntity::new, ENDERITE_SHULKER_BOX).build(null);

		DispenserBlock.registerBehavior(ENDERITE_SHULKER_BOX.asItem(), new BlockPlacementDispenserBehavior());

		// ENCHANTMENT
		VOID_FLOATING_ENCHANTMENT = Registry.register(Registries.ENCHANTMENT,
				new Identifier("enderitemod", "void_floating"), new VoidFloatingEnchantment());

		// ItemGroup
		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> {
			content.add(ENDERITE_ORE);
			content.add(CRACKED_ENDERITE_ORE);
			content.add(ENDERITE_SCRAP);
			content.add(ENDERITE_INGOT);
			content.add(ENDERITE_BLOCK);

			content.add(ENDERITE_PICKAXE);
			content.add(ENDERITE_AXE);
			content.add(ENDERITE_HOE);
			content.add(ENDERITE_SHOVEL);

			content.add(ENDERITE_SWORD);
			content.add(ENDERITE_SHIELD);
			content.add(ENDERITE_BOW);
			content.add(ENDERITE_CROSSBOW);

			content.add(ENDERITE_HELMET);
			content.add(ENDERITE_CHESTPLATE);
			content.add(ENDERITE_LEGGINGS);
			content.add(ENDERITE_BOOTS);

			content.add(ENDERITE_ELYTRA);
			content.add(ENDERITE_SHULKER_BOX);
			content.add(ENDERITE_RESPAWN_ANCHOR);
		});

		// Ore configuration
		BiomeModifications.addFeature(
				BiomeSelectors.foundInTheEnd(),
				GenerationStep.Feature.UNDERGROUND_ORES,
				RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("enderitemod", "ore_enderite_large")));
		BiomeModifications.addFeature(
				BiomeSelectors.foundInTheEnd(),
				GenerationStep.Feature.UNDERGROUND_ORES,
				RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("enderitemod", "ore_enderite_small")));

		// Loottables
		EnderiteShears.registerLoottables();

		System.out.println("-Initialized Enderitemod!-");
	}
}
