package net.enderitemc.enderitemod.init;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.block.EnderiteOre;
import net.enderitemc.enderitemod.block.EnderiteRespawnAnchor;
import net.enderitemc.enderitemod.item.EnderiteElytra;
import net.enderitemc.enderitemod.item.EnderiteHelmet;
import net.enderitemc.enderitemod.item.EnderiteIngot;
import net.enderitemc.enderitemod.item.EnderiteScrap;
import net.enderitemc.enderitemod.materials.EnderiteArmorMaterial;
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = EnderiteMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            EnderiteMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            EnderiteMod.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister
            .create(ForgeRegistries.TILE_ENTITIES, EnderiteMod.MOD_ID);

    private static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        LOGGER.info("Registering blocks from Enderite Mod");
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registering items from Enderite Mod");
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registering tiles from Enderite Mod");
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        // CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        // ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        // DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Finished registering for all objects from Enderite Mod");
    }

    // Items
    public static final RegistryObject<Item> ENDERITE_INGOT = ITEMS.register("enderite_ingot",
            () -> new EnderiteIngot(new Item.Properties().group(ItemGroup.MISC).func_234689_a_())); // obfuscated method
                                                                                                    // is "fireproof"
    public static final RegistryObject<Item> ENDERITE_SCRAP = ITEMS.register("enderite_scrap",
            () -> new EnderiteScrap(new Item.Properties().group(ItemGroup.MISC).func_234689_a_())); // obfuscated method
                                                                                                    // is "fireproof"

    // Tools
    public static final RegistryObject<Item> ENDERITE_PICKAXE = ITEMS.register("enderite_pickaxe",
            () -> new PickaxeItem(EnderiteMaterial.ENDERITE, 4, -2.8F,
                    (new Item.Properties()).group(ItemGroup.TOOLS).func_234689_a_())); // obfuscated method is
                                                                                       // "fireproof"
    public static final RegistryObject<Item> ENDERITE_AXE = ITEMS.register("enderite_axe",
            () -> new AxeItem(EnderiteMaterial.ENDERITE, 8, -3.0F,
                    (new Item.Properties()).group(ItemGroup.TOOLS).func_234689_a_())); // obfuscated method is
                                                                                       // "fireproof"
    public static final RegistryObject<Item> ENDERITE_SHOVEL = ITEMS.register("enderite_shovel",
            () -> new ShovelItem(EnderiteMaterial.ENDERITE, 4.5F, -3.0F,
                    (new Item.Properties()).group(ItemGroup.TOOLS).func_234689_a_())); // obfuscated method is
                                                                                       // "fireproof"
    public static final RegistryObject<Item> ENDERITE_SWORD = ITEMS.register("enderite_sword",
            () -> new SwordItem(EnderiteMaterial.ENDERITE, 6, -2.4F,
                    (new Item.Properties()).group(ItemGroup.COMBAT).func_234689_a_())); // obfuscated method is
                                                                                        // "fireproof"
    public static final RegistryObject<Item> ENDERITE_HOE = ITEMS.register("enderite_hoe",
            () -> new HoeItem(EnderiteMaterial.ENDERITE, -2, 0.0F,
                    (new Item.Properties()).group(ItemGroup.TOOLS).func_234689_a_())); // obfuscated method is
                                                                                       // "fireproof"

    // Blocks
    public static final RegistryObject<Block> ENDERITE_BLOCK = BLOCKS.register("enderite_block",
            () -> new Block(Block.Properties.create(Material.IRON, MaterialColor.BLACK).sound(SoundType.field_235594_P_)
                    .func_235861_h_().hardnessAndResistance(66.0F, 1200.0F).harvestTool(ToolType.PICKAXE)
                    .harvestLevel(4)));
    public static final RegistryObject<Item> ENDERITE_BLOCK_ITEM = ITEMS.register("enderite_block",
            () -> new BlockItem(ENDERITE_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

    public static final RegistryObject<Block> ENDERITE_ORE = BLOCKS.register("enderite_ore", EnderiteOre::new);
    public static final RegistryObject<Item> ENDERITE_ORE_ITEM = ITEMS.register("enderite_ore",
            () -> new BlockItem(ENDERITE_ORE.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

    public static final RegistryObject<Block> CRACKED_ENDERITE_ORE = BLOCKS.register("cracked_enderite_ore",
            () -> new Block(AbstractBlock.Properties.from(ENDERITE_BLOCK.get()).sound(SoundType.field_235595_Q_)));
    public static final RegistryObject<Item> CRACKED_ENDERITE_ORE_ITEM = ITEMS.register("cracked_enderite_ore",
            () -> new BlockItem(CRACKED_ENDERITE_ORE.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

    public static final RegistryObject<Block> ENDERITE_RESPAWN_ANCHOR = BLOCKS.register("enderite_respawn_anchor",
            () -> new EnderiteRespawnAnchor(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.BLACK)
                    .hardnessAndResistance(50.0F, 1200.0F)
                    .func_235838_a_((state) -> EnderiteRespawnAnchor.func_235565_a_(state, 15))));
    public static final RegistryObject<Item> ENDERITE_RESPAWN_ANCHOR_ITEM = ITEMS.register("enderite_respawn_anchor",
            () -> new BlockItem(ENDERITE_RESPAWN_ANCHOR.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));

    // Armor
    public static final RegistryObject<Item> ENDERITE_HELMET = ITEMS.register("enderite_helmet",
            () -> new EnderiteHelmet(EnderiteArmorMaterial.ENDERITE, EquipmentSlotType.HEAD,
                    (new Item.Properties().group(ItemGroup.COMBAT).func_234689_a_()))); // obfuscated method is
                                                                                        // "fireproof"
    public static final RegistryObject<Item> ENDERITE_CHESTPLATE = ITEMS.register("enderite_chestplate",
            () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlotType.CHEST,
                    (new Item.Properties().group(ItemGroup.COMBAT).func_234689_a_()))); // obfuscated method is
                                                                                        // "fireproof"
    public static final RegistryObject<Item> ENDERITE_LEGGINGS = ITEMS.register("enderite_leggings",
            () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlotType.LEGS,
                    (new Item.Properties().group(ItemGroup.COMBAT).func_234689_a_()))); // obfuscated method is
                                                                                        // "fireproof"
    public static final RegistryObject<Item> ENDERITE_BOOTS = ITEMS.register("enderite_boots",
            () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentSlotType.FEET,
                    (new Item.Properties().group(ItemGroup.COMBAT).func_234689_a_()))); // obfuscated method is
                                                                                        // "fireproof"

    // Elytra
    public static final RegistryObject<Item> ENDERITE_ELYTRA = ITEMS.register("enderite_elytra",
            () -> new EnderiteElytra(EnderiteArmorMaterial.ENDERITE, EquipmentSlotType.CHEST,
                    (new Item.Properties().group(ItemGroup.COMBAT).func_234689_a_().rarity(Rarity.EPIC)))); // obfuscated
                                                                                                            // method is
                                                                                                            // "fireproof"

}
