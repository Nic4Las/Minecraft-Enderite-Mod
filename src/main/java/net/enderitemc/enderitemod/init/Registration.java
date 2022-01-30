package net.enderitemc.enderitemod.init;

import net.enderitemc.enderitemod.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.block.EnderiteOre;
import net.enderitemc.enderitemod.block.EnderiteRespawnAnchor;
import net.enderitemc.enderitemod.block.EnderiteShulkerBox;
import net.enderitemc.enderitemod.enchantments.VoidFloatingEnchantment;
import net.enderitemc.enderitemod.materials.EnderiteArmorMaterial;
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.enderitemc.enderitemod.recipe.EnderiteElytraSpecialRecipe;
import net.enderitemc.enderitemod.recipe.EnderiteShieldDecorationRecipe;
import net.enderitemc.enderitemod.recipe.EnderiteShulkerBoxRecipe;
import net.enderitemc.enderitemod.renderer.EnderiteShieldRenderer;
import net.enderitemc.enderitemod.tileEntity.EnderiteShulkerBoxTileEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = EnderiteMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {
        private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
                        EnderiteMod.MOD_ID);
        private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                        EnderiteMod.MOD_ID);
        private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister
                        .create(ForgeRegistries.BLOCK_ENTITIES, EnderiteMod.MOD_ID);
        private static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES,
                        EnderiteMod.MOD_ID);

        private static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister
                        .create(ForgeRegistries.RECIPE_SERIALIZERS, EnderiteMod.MOD_ID);
        private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister
                        .create(ForgeRegistries.ENCHANTMENTS, EnderiteMod.MOD_ID);

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
                        () -> new EnderiteIngot(new Item.Properties().tab(CreativeModeTab.TAB_MISC).fireResistant())); // obfuscated
                                                                                                                 // method
                                                                                                                 // is
                                                                                                                 // "fireproof"
        public static final RegistryObject<Item> ENDERITE_SCRAP = ITEMS.register("enderite_scrap",
                        () -> new EnderiteScrap(new Item.Properties().tab(CreativeModeTab.TAB_MISC).fireResistant())); // obfuscated
                                                                                                                 // method
                                                                                                                 // is
                                                                                                                 // "fireproof"

        // Tools
        public static final RegistryObject<Item> ENDERITE_PICKAXE = ITEMS.register("enderite_pickaxe",
                        () -> new PickaxeItem(EnderiteMaterial.ENDERITE, 4, -2.8F,
                                        (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS).fireResistant())); // obfuscated
                                                                                                            // method is
                                                                                                            // "fireproof"
        public static final RegistryObject<Item> ENDERITE_AXE = ITEMS.register("enderite_axe",
                        () -> new AxeItem(EnderiteMaterial.ENDERITE, 8, -3.0F,
                                        (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS).fireResistant())); // obfuscated
                                                                                                            // method is
                                                                                                            // "fireproof"
        public static final RegistryObject<Item> ENDERITE_SHOVEL = ITEMS.register("enderite_shovel",
                        () -> new ShovelItem(EnderiteMaterial.ENDERITE, 4.5F, -3.0F,
                                        (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS).fireResistant())); // obfuscated
                                                                                                            // method is
                                                                                                            // "fireproof"
        public static final RegistryObject<Item> ENDERITE_SWORD = ITEMS.register("enderite_sword",
                        () -> new EnderiteSword(EnderiteMaterial.ENDERITE,
                                        EnderiteModConfig.ENDERITE_SWORD_BASE_DAMAGE.get() - 3, -2.4F,
                                        (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT).fireResistant())); // obfuscated
                                                                                                             // method
                                                                                                             // is
                                                                                                             // "fireproof"
        public static final RegistryObject<Item> ENDERITE_HOE = ITEMS.register("enderite_hoe",
                        () -> new HoeItem(EnderiteMaterial.ENDERITE, -2, 0.0F,
                                        (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS).fireResistant())); // obfuscated
                                                                                                            // method is
                                                                                                            // "fireproof"

        // Blocks
        public static final RegistryObject<Block> ENDERITE_BLOCK = BLOCKS.register("enderite_block",
                        () -> new Block(Block.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK)
                                        .sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops()
                                        .strength(66.0F, 1200.0F)));
        public static final RegistryObject<Item> ENDERITE_BLOCK_ITEM = ITEMS.register("enderite_block",
                        () -> new BlockItem(ENDERITE_BLOCK.get(),
                                        new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS).fireResistant()));

        public static final RegistryObject<Block> ENDERITE_ORE = BLOCKS.register("enderite_ore", EnderiteOre::new);
        public static final RegistryObject<Item> ENDERITE_ORE_ITEM = ITEMS.register("enderite_ore",
                        () -> new BlockItem(ENDERITE_ORE.get(),
                                        new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS).fireResistant()));

        public static final RegistryObject<Block> CRACKED_ENDERITE_ORE = BLOCKS.register("cracked_enderite_ore",
                        () -> new Block(BlockBehaviour.Properties.copy(ENDERITE_BLOCK.get())
                                        .sound(SoundType.ANCIENT_DEBRIS)));
        public static final RegistryObject<Item> CRACKED_ENDERITE_ORE_ITEM = ITEMS.register("cracked_enderite_ore",
                        () -> new BlockItem(CRACKED_ENDERITE_ORE.get(),
                                        new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS).fireResistant()));

        public static final RegistryObject<Block> ENDERITE_RESPAWN_ANCHOR = BLOCKS.register("enderite_respawn_anchor",
                        () -> new EnderiteRespawnAnchor(BlockBehaviour.Properties
                                        .of(Material.STONE, MaterialColor.COLOR_BLACK).strength(50.0F, 1200.0F)
                                        .lightLevel((state) -> EnderiteRespawnAnchor.getScaledChargeLevel(state, 15))));
        public static final RegistryObject<Item> ENDERITE_RESPAWN_ANCHOR_ITEM = ITEMS
                        .register("enderite_respawn_anchor", () -> new BlockItem(ENDERITE_RESPAWN_ANCHOR.get(),
                                        new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS).fireResistant()));

        // Armor
        public static final RegistryObject<Item> ENDERITE_HELMET = ITEMS.register("enderite_helmet",
                        () -> new EnderiteHelmet(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.HEAD,
                                        (new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).fireResistant()))); // obfuscated
                                                                                                             // method
                                                                                                             // is
                                                                                                             // "fireproof"
        public static final RegistryObject<Item> ENDERITE_CHESTPLATE = ITEMS.register("enderite_chestplate",
                        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.CHEST,
                                        (new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).fireResistant()))); // obfuscated
                                                                                                             // method
                                                                                                             // is
                                                                                                             // "fireproof"
        public static final RegistryObject<Item> ENDERITE_LEGGINGS = ITEMS.register("enderite_leggings",
                        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.LEGS,
                                        (new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).fireResistant()))); // obfuscated
                                                                                                             // method
                                                                                                             // is
                                                                                                             // "fireproof"
        public static final RegistryObject<Item> ENDERITE_BOOTS = ITEMS.register("enderite_boots",
                        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.FEET,
                                        (new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).fireResistant()))); // obfuscated
                                                                                                             // method
                                                                                                             // is
                                                                                                             // "fireproof"

        // Elytra
        public static final RegistryObject<Item> ENDERITE_ELYTRA = ITEMS.register("enderite_elytra",
                        () -> new EnderiteElytra(EnderiteArmorMaterial.ENDERITE, EquipmentSlot.CHEST,
                                        (new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).fireResistant()
                                                        .rarity(Rarity.EPIC)))); // obfuscated
                                                                                 // method is
                                                                                 // "fireproof"
        public static final RegistryObject<RecipeSerializer<EnderiteElytraSpecialRecipe>> ENDERITE_EYLTRA_SPECIAL_RECIPE = RECIPES
                        .register("crafting_special_enderiteelytra",
                                        () -> new SimpleRecipeSerializer<>(EnderiteElytraSpecialRecipe::new));

        // Shulker
        public static final RegistryObject<Block> ENDERITE_SHULKER_BOX = BLOCKS
                        .register("enderite_shulker_box",
                                        () -> new EnderiteShulkerBox(null, BlockBehaviour.Properties
                                                        .of(Material.SHULKER_SHELL, MaterialColor.COLOR_BLACK)
                                                        .noOcclusion().strength(2.0F, 17.0F)));
        public static final RegistryObject<Item> ENDERITE_SHULKER_BOX_ITEM = ITEMS.register("enderite_shulker_box",
                        () -> new BlockItem(ENDERITE_SHULKER_BOX.get(), new Item.Properties()
                                        .tab(CreativeModeTab.TAB_DECORATIONS).stacksTo(1).fireResistant()));
        public static final RegistryObject<BlockEntityType<EnderiteShulkerBoxTileEntity>> ENDERITE_SHULKER_BOX_TILE_ENTITY = TILES
                        .register("enderite_shulker_box_tile_entity", () -> BlockEntityType.Builder
                                        .of(EnderiteShulkerBoxTileEntity::new, ENDERITE_SHULKER_BOX.get()).build(null));
        public static final RegistryObject<RecipeSerializer<EnderiteShulkerBoxRecipe>> ENDERITE_SHULKER_BOX_RECIPE = RECIPES
                        .register("crafting_special_enderiteshulkerbox",
                                        () -> new SimpleRecipeSerializer<>(EnderiteShulkerBoxRecipe::new));

        // Bow/Crossbow/shield
        public static final RegistryObject<Item> ENDERITE_SHIELD = ITEMS.register("enderite_shield",
                        () -> new EnderiteShield(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1)
                                        .durability(768).fireResistant()/*.setISTER(() -> EnderiteShieldRenderer::new)*/));
        public static final RegistryObject<RecipeSerializer<EnderiteShieldDecorationRecipe>> ENDERITE_SHIELD_DECORATION_RECIPE = RECIPES
                        .register("crafting_special_enderiteshielddecoration",
                                        () -> new SimpleRecipeSerializer<>(EnderiteShieldDecorationRecipe::new));

        public static final RegistryObject<Item> ENDERITE_BOW = ITEMS.register("enderite_bow", () -> new EnderiteBow(
                        new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1).durability(768).fireResistant()));

        public static final RegistryObject<Item> ENDERITE_CROSSBOW = ITEMS.register("enderite_crossbow",
                        () -> new EnderiteCrossbow(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1)
                                        .durability(768).fireResistant()));

        // ENCHANTMENT Void floating
        public static final RegistryObject<Enchantment> VOID_FLOATING = ENCHANTMENTS.register("void_floating",
                        () -> new VoidFloatingEnchantment());

}
