package net.enderitemc.enderitemod;

import net.enderitemc.enderitemod.blocks.CrackedEnderiteOre;
import net.enderitemc.enderitemod.blocks.EnderiteBlock;
import net.enderitemc.enderitemod.blocks.EnderiteOre;
import net.enderitemc.enderitemod.blocks.EnderiteRespawnAnchor;
import net.enderitemc.enderitemod.items.EnderiteIngot;
import net.enderitemc.enderitemod.items.EnderiteScrap;
import net.enderitemc.enderitemod.materials.EnderiteArmorMaterial;
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.enderitemc.enderitemod.oreGeneration.EndOreFeature;
import net.enderitemc.enderitemod.oreGeneration.EndOreFeatureConfig;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlock;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntity;
import net.enderitemc.enderitemod.tools.AxeSubclass;
import net.enderitemc.enderitemod.tools.EnderiteSword;
import net.enderitemc.enderitemod.tools.HoeSubclass;
import net.enderitemc.enderitemod.tools.PickaxeSubclass;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolItem;
import net.minecraft.predicate.block.BlockPredicate;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;

public class EnderiteMod implements ModInitializer {

	// Enderite Ingot
	public static final EnderiteIngot ENDERITE_INGOT = new EnderiteIngot(
			new Item.Settings().group(ItemGroup.MISC).fireproof());
	public static final EnderiteScrap ENDERITE_SCRAP = new EnderiteScrap(
			new Item.Settings().group(ItemGroup.MISC).fireproof());

	// Enderite Tools
	public static final ToolItem ENDERITE_PICKAXE = new PickaxeSubclass(EnderiteMaterial.ENDERITE, 4, -2.8F,
			new Item.Settings().group(ItemGroup.TOOLS).fireproof());
	public static final ToolItem ENDERITE_AXE = new AxeSubclass(EnderiteMaterial.ENDERITE, 8, -3.0F,
			new Item.Settings().group(ItemGroup.TOOLS).fireproof());
	public static final ToolItem ENDERITE_HOE = new HoeSubclass(EnderiteMaterial.ENDERITE, -2, 0.0F,
			new Item.Settings().group(ItemGroup.TOOLS).fireproof());

	public static final ToolItem ENDERITE_SHOVEL = new ShovelItem(EnderiteMaterial.ENDERITE, 4.5F, -3.0F,
			new Item.Settings().group(ItemGroup.TOOLS).fireproof());
	public static final EnderiteSword ENDERITE_SWORD = new EnderiteSword(EnderiteMaterial.ENDERITE, 6, -2.4F,
			new Item.Settings().group(ItemGroup.COMBAT).fireproof());

	// Enderite Block
	public static final EnderiteBlock ENDERITE_BLOCK = new EnderiteBlock(
			new FabricMaterialBuilder(MaterialColor.BLACK).build());
	public static final EnderiteOre ENDERITE_ORE = new EnderiteOre();
	public static final CrackedEnderiteOre CRACKED_ENDERITE_ORE = new CrackedEnderiteOre();

	public static final EnderiteRespawnAnchor ENDERITE_RESPAWN_ANCHOR = new EnderiteRespawnAnchor(AbstractBlock.Settings
			.of(Material.STONE, MaterialColor.BLACK).requiresTool().strength(50.0F, 1200.0F).lightLevel((state) -> {
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
	public static final ArmorItem ENDERITE_ELYTRA = new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.CHEST,
			(new Item.Settings().group(ItemGroup.COMBAT).fireproof().rarity(Rarity.EPIC)));

	// Shulker Box
	public static BlockEntityType<EnderiteShulkerBoxBlockEntity> ENDERITE_SHULKER_BOX_BLOCK_ENTITY;
	public static final EnderiteShulkerBoxBlock ENDERITE_SHULKER_BOX = new EnderiteShulkerBoxBlock((DyeColor) null,
			FabricBlockSettings.of(Material.SHULKER_BOX).nonOpaque().strength(2.0f, 2.0f)
					.breakByTool(FabricToolTags.PICKAXES, 1));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_ingot"), ENDERITE_INGOT);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_scrap"), ENDERITE_SCRAP);

		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_pickaxe"), ENDERITE_PICKAXE);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_axe"), ENDERITE_AXE);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_hoe"), ENDERITE_HOE);

		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_shovel"), ENDERITE_SHOVEL);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_sword"), ENDERITE_SWORD);

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

		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_elytra"), ENDERITE_ELYTRA);

		Registry.register(Registry.BLOCK, new Identifier("enderitemod", "enderite_shulker_box"), ENDERITE_SHULKER_BOX);
		Registry.register(Registry.ITEM, new Identifier("enderitemod", "enderite_shulker_box"), new BlockItem(
				ENDERITE_SHULKER_BOX, new Item.Settings().group(ItemGroup.DECORATIONS).fireproof().maxCount(1)));
		ENDERITE_SHULKER_BOX_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
				"enderitemod:enderite_shulker_box_block_entity",
				BlockEntityType.Builder.create(EnderiteShulkerBoxBlockEntity::new, ENDERITE_SHULKER_BOX).build(null));
		BlockEntityType.Builder.create(ShulkerBoxBlockEntity::new, ENDERITE_SHULKER_BOX).build(null);

		// Loop over existing biomes
		Registry.BIOME.forEach(this::handleBiome);

		// Listen for other biomes being registered
		RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> handleBiome(biome));

		System.out.println("-Initialized Enderitemod!-");
	}

	private void handleBiome(Biome biome) {
		if (biome.getCategory() == Biome.Category.THEEND) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, EndOreFeature.END_ORE_FEATURE.configure(
					new EndOreFeatureConfig(new BlockPredicate(Blocks.END_STONE), ENDERITE_ORE.getDefaultState(), 3 // Ore
																													// vein
																													// size
					)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(3, // Number of
																											// veins per
																											// chunk
							0, // Bottom Offset
							12, // Min y level
							48 // Max y level
			))));
		}
	}
}
