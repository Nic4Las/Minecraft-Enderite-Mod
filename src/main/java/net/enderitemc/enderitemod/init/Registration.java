package net.enderitemc.enderitemod.init;

import net.enderitemc.enderitemod.item.*;
import net.minecraft.enchantment.Enchantment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.block.EnderiteOre;
import net.enderitemc.enderitemod.block.EnderiteRespawnAnchor;
import net.enderitemc.enderitemod.block.EnderiteShulkerBox;
import net.enderitemc.enderitemod.materials.EnderiteArmorMaterial;
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.enderitemc.enderitemod.recipe.EnderiteElytraSpecialRecipe;
import net.enderitemc.enderitemod.recipe.EnderiteShieldDecorationRecipe;
import net.enderitemc.enderitemod.recipe.EnderiteShulkerBoxRecipe;
import net.enderitemc.enderitemod.renderer.EnderiteShieldRenderer;
import net.enderitemc.enderitemod.tileEntity.EnderiteShulkerBoxTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.Rarity;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = EnderiteMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {
        private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
                        EnderiteMod.MOD_ID);
        private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                        EnderiteMod.MOD_ID);
        private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister
                        .create(ForgeRegistries.TILE_ENTITIES, EnderiteMod.MOD_ID);
        private static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES,
                        EnderiteMod.MOD_ID);

        private static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister
                        .create(ForgeRegistries.RECIPE_SERIALIZERS, EnderiteMod.MOD_ID);

        private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, EnderiteMod.MOD_ID);

        private static final Logger LOGGER = LogManager.getLogger();

        public static void init() {
                LOGGER.info("Registering blocks from Enderite Mod");
                BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
                LOGGER.info("Registering items from Enderite Mod");
                ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
                LOGGER.info("Registering tiles from Enderite Mod");
                TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
                LOGGER.info("Registering recipes from Enderite Mod");
                RECIPES.register(FMLJavaModLoadingContext.get().getModEventBus());
                LOGGER.info("Registering enchantments from Enderite Mod");
                ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
                // CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
                // ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
                // DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
                LOGGER.info("Finished registering for all objects from Enderite Mod");
        }

        // Items
        public static final RegistryObject<Item> ENDERITE_INGOT = ITEMS.register("enderite_ingot",
                        () -> new EnderiteIngot(new Item.Properties().group(ItemGroup.MISC).func_234689_a_())); // obfuscated
                                                                                                                // method
                                                                                                                // is
                                                                                                                // "fireproof"
        public static final RegistryObject<Item> ENDERITE_SCRAP = ITEMS.register("enderite_scrap",
                        () -> new EnderiteScrap(new Item.Properties().group(ItemGroup.MISC).func_234689_a_())); // obfuscated
                                                                                                                // method
                                                                                                                // is
                                                                                                                // "fireproof"

        // Tools
        public static final RegistryObject<Item> ENDERITE_PICKAXE = ITEMS.register("enderite_pickaxe",
                        () -> new PickaxeItem(EnderiteMaterial.ENDERITE, 4, -2.8F,
                                        (new Item.Properties()).group(ItemGroup.TOOLS).func_234689_a_())); // obfuscated
                                                                                                           // method is
                                                                                                           // "fireproof"
        public static final RegistryObject<Item> ENDERITE_AXE = ITEMS.register("enderite_axe",
                        () -> new AxeItem(EnderiteMaterial.ENDERITE, 8, -3.0F,
                                        (new Item.Properties()).group(ItemGroup.TOOLS).func_234689_a_())); // obfuscated
                                                                                                           // method is
                                                                                                           // "fireproof"
        public static final RegistryObject<Item> ENDERITE_SHOVEL = ITEMS.register("enderite_shovel",
                        () -> new ShovelItem(EnderiteMaterial.ENDERITE, 4.5F, -3.0F,
                                        (new Item.Properties()).group(ItemGroup.TOOLS).func_234689_a_())); // obfuscated
                                                                                                           // method is
                                                                                                           // "fireproof"
        public static final RegistryObject<Item> ENDERITE_SWORD = ITEMS.register("enderite_sword",
                        () -> new EnderiteSword(EnderiteMaterial.ENDERITE, 6, -2.4F,
                                        (new Item.Properties()).group(ItemGroup.COMBAT).func_234689_a_())); // obfuscated
                                                                                                            // method is
                                                                                                            // "fireproof"
        public static final RegistryObject<Item> ENDERITE_HOE = ITEMS.register("enderite_hoe",
                        () -> new HoeItem(EnderiteMaterial.ENDERITE, -2, 0.0F,
                                        (new Item.Properties()).group(ItemGroup.TOOLS).func_234689_a_())); // obfuscated
                                                                                                           // method is
                                                                                                           // "fireproof"

        // Blocks
        public static final RegistryObject<Block> ENDERITE_BLOCK = BLOCKS.register("enderite_block",
                        () -> new Block(Block.Properties.create(Material.IRON, MaterialColor.BLACK)
                                        .sound(SoundType.field_235594_P_).func_235861_h_()
                                        .hardnessAndResistance(66.0F, 1200.0F).harvestTool(ToolType.PICKAXE)
                                        .harvestLevel(4)));
        public static final RegistryObject<Item> ENDERITE_BLOCK_ITEM = ITEMS.register("enderite_block",
                        () -> new BlockItem(ENDERITE_BLOCK.get(),
                                        new Item.Properties().group(ItemGroup.BUILDING_BLOCKS).func_234689_a_()));

        public static final RegistryObject<Block> ENDERITE_ORE = BLOCKS.register("enderite_ore", EnderiteOre::new);
        public static final RegistryObject<Item> ENDERITE_ORE_ITEM = ITEMS.register("enderite_ore",
                        () -> new BlockItem(ENDERITE_ORE.get(),
                                        new Item.Properties().group(ItemGroup.BUILDING_BLOCKS).func_234689_a_()));

        public static final RegistryObject<Block> CRACKED_ENDERITE_ORE = BLOCKS.register("cracked_enderite_ore",
                        () -> new Block(AbstractBlock.Properties.from(ENDERITE_BLOCK.get())
                                        .sound(SoundType.field_235595_Q_)));
        public static final RegistryObject<Item> CRACKED_ENDERITE_ORE_ITEM = ITEMS.register("cracked_enderite_ore",
                        () -> new BlockItem(CRACKED_ENDERITE_ORE.get(),
                                        new Item.Properties().group(ItemGroup.BUILDING_BLOCKS).func_234689_a_()));

        public static final RegistryObject<Block> ENDERITE_RESPAWN_ANCHOR = BLOCKS.register("enderite_respawn_anchor",
                        () -> new EnderiteRespawnAnchor(AbstractBlock.Properties
                                        .create(Material.ROCK, MaterialColor.BLACK)
                                        .hardnessAndResistance(50.0F, 1200.0F)
                                        .func_235838_a_((state) -> EnderiteRespawnAnchor.func_235565_a_(state, 15))));
        public static final RegistryObject<Item> ENDERITE_RESPAWN_ANCHOR_ITEM = ITEMS
                        .register("enderite_respawn_anchor", () -> new BlockItem(ENDERITE_RESPAWN_ANCHOR.get(),
                                        new Item.Properties().group(ItemGroup.DECORATIONS).func_234689_a_()));

        // Armor
        public static final RegistryObject<Item> ENDERITE_HELMET = ITEMS.register("enderite_helmet",
                        () -> new EnderiteHelmet(EnderiteArmorMaterial.ENDERITE, EquipmentSlotType.HEAD,
                                        (new Item.Properties().group(ItemGroup.COMBAT).func_234689_a_()))); // obfuscated
                                                                                                            // method is
                                                                                                            // "fireproof"
        public static final RegistryObject<Item> ENDERITE_CHESTPLATE = ITEMS.register("enderite_chestplate",
                        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlotType.CHEST,
                                        (new Item.Properties().group(ItemGroup.COMBAT).func_234689_a_()))); // obfuscated
                                                                                                            // method is
                                                                                                            // "fireproof"
        public static final RegistryObject<Item> ENDERITE_LEGGINGS = ITEMS.register("enderite_leggings",
                        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlotType.LEGS,
                                        (new Item.Properties().group(ItemGroup.COMBAT).func_234689_a_()))); // obfuscated
                                                                                                            // method is
                                                                                                            // "fireproof"
        public static final RegistryObject<Item> ENDERITE_BOOTS = ITEMS.register("enderite_boots",
                        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlotType.FEET,
                                        (new Item.Properties().group(ItemGroup.COMBAT).func_234689_a_()))); // obfuscated
                                                                                                            // method is
                                                                                                            // "fireproof"

        // Elytra
        public static final RegistryObject<Item> ENDERITE_ELYTRA = ITEMS.register("enderite_elytra",
                        () -> new EnderiteElytra(EnderiteArmorMaterial.ENDERITE, EquipmentSlotType.CHEST,
                                        (new Item.Properties().group(ItemGroup.COMBAT).func_234689_a_()
                                                        .rarity(Rarity.EPIC)))); // obfuscated
                                                                                 // method is
                                                                                 // "fireproof"
        public static final RegistryObject<IRecipeSerializer<EnderiteElytraSpecialRecipe>> ENDERITE_EYLTRA_SPECIAL_RECIPE = RECIPES
                        .register("crafting_special_enderiteelytra",
                                        () -> new SpecialRecipeSerializer<>(EnderiteElytraSpecialRecipe::new));

        // Shulker
        public static final RegistryObject<Block> ENDERITE_SHULKER_BOX = BLOCKS
                        .register("enderite_shulker_box",
                                        () -> new EnderiteShulkerBox(null, AbstractBlock.Properties
                                                        .create(Material.SHULKER, MaterialColor.BLACK).notSolid()
                                                        .hardnessAndResistance(2.0F, 17.0F)));
        public static final RegistryObject<Item> ENDERITE_SHULKER_BOX_ITEM = ITEMS.register("enderite_shulker_box",
                        () -> new BlockItem(ENDERITE_SHULKER_BOX.get(),
                                        new Item.Properties().group(ItemGroup.DECORATIONS).maxStackSize(1).func_234689_a_()));
        public static final RegistryObject<TileEntityType<EnderiteShulkerBoxTileEntity>> ENDERITE_SHULKER_BOX_TILE_ENTITY = TILES
                        .register("enderite_shulker_box_tile_entity", () -> TileEntityType.Builder
                                        .create(EnderiteShulkerBoxTileEntity::new, ENDERITE_SHULKER_BOX.get())
                                        .build(null));
        public static final RegistryObject<IRecipeSerializer<EnderiteShulkerBoxRecipe>> ENDERITE_SHULKER_BOX_RECIPE = RECIPES
                        .register("crafting_special_enderiteshulkerbox",
                                        () -> new SpecialRecipeSerializer<>(EnderiteShulkerBoxRecipe::new));

        // Bow/Crossbow/shield
        public static final RegistryObject<Item> ENDERITE_SHIELD = ITEMS.register("enderite_shield",
                        () -> new EnderiteShield(new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1)
                                        .func_234689_a_().setISTER(() -> EnderiteShieldRenderer::new)));
        public static final RegistryObject<IRecipeSerializer<EnderiteShieldDecorationRecipe>> ENDERITE_SHIELD_DECORATION_RECIPE = RECIPES
                        .register("crafting_special_enderiteshielddecoration",
                                        () -> new SpecialRecipeSerializer<>(EnderiteShieldDecorationRecipe::new));

        public static final RegistryObject<Item> ENDERITE_BOW = ITEMS.register("enderite_bow", () -> new EnderiteBow(
                        new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1).func_234689_a_()));

        public static final RegistryObject<Item> ENDERITE_CROSSBOW = ITEMS.register("enderite_crossbow",
                        () -> new EnderiteCrossbow(new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1)
                                        .func_234689_a_()));

}
