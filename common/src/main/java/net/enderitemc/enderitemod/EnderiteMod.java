package net.enderitemc.enderitemod;

import com.google.common.base.Suppliers;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.blocks.EnderiteOre;
import net.enderitemc.enderitemod.blocks.EnderiteRespawnAnchor;
import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorBlockEntity;
import net.enderitemc.enderitemod.component.EnderiteDataComponents;
import net.enderitemc.enderitemod.config.Config;
import net.enderitemc.enderitemod.config.ConfigLoader;
import net.enderitemc.enderitemod.materials.EnderiteArmorMaterial;
import net.enderitemc.enderitemod.misc.EnderiteElytraSpecialRecipe;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.enderitemc.enderitemod.misc.EnderiteUpgradeSmithingTemplate;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlock;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntity;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxScreenHandler;
import net.enderitemc.enderitemod.tools.BlockEntityTypeBuilder;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.material.MapColor;
import java.util.ArrayList;
import java.util.function.Supplier;

public class EnderiteMod {
    public static final String MOD_ID = "enderitemod";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(MOD_ID,
        Registries.RECIPE_SERIALIZER);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(MOD_ID,
        Registries.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
        DeferredRegister.create(MOD_ID, Registries.DATA_COMPONENT_TYPE);


    public static Config CONFIG = ConfigLoader.get();

    // Tab
    public static final RegistrySupplier<CreativeModeTab> ENDERITE_TAB = TABS.register("enderite_group",
        () -> CreativeTabRegistry.create(
            Component.translatable("itemGroup.enderitemod.enderite_group"),
            () -> new ItemStack(EnderiteMod.ENDERITE_INGOT.get())
        )
    );
    public static final Supplier<Item.Properties> BASE_ENDERITE_ITEM_SETTINGS = () -> new Item.Properties()
        .arch$tab(ENDERITE_TAB).fireResistant();

    // Enderite Ingot
    public static final RegistrySupplier<Item> ENDERITE_INGOT = ITEMS.register("enderite_ingot",
        () -> new Item(getItemSettings("enderite_ingot", BASE_ENDERITE_ITEM_SETTINGS.get())
            .trimMaterial(ResourceKey.create(Registries.TRIM_MATERIAL, Identifier.fromNamespaceAndPath(MOD_ID, "enderite")))));
    public static final RegistrySupplier<Item> ENDERITE_SCRAP = ITEMS.register("enderite_scrap",
        () -> new Item(getItemSettings("enderite_scrap", BASE_ENDERITE_ITEM_SETTINGS.get())));

    // Enderite Block
    public static final RegistrySupplier<Block> ENDERITE_BLOCK = BLOCKS.register("enderite_block",
        () -> new Block(getBlockSettings("enderite_block").mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(66.0F, 1200.0F)
            .sound(SoundType.NETHERITE_BLOCK)));
    public static final RegistrySupplier<Item> ENDERITE_BLOCK_ITEM = ITEMS.register("enderite_block",
        () -> new BlockItem(ENDERITE_BLOCK.get(), getItemSettings("enderite_block", BASE_ENDERITE_ITEM_SETTINGS.get()).useBlockDescriptionPrefix()));

    public static final RegistrySupplier<Block> ENDERITE_ORE = BLOCKS.register("enderite_ore",
        () -> new EnderiteOre(getBlockSettings("enderite_ore")));
    public static final RegistrySupplier<Item> ENDERITE_ORE_ITEM = ITEMS.register("enderite_ore",
        () -> new BlockItem(ENDERITE_ORE.get(), getItemSettings("enderite_ore", BASE_ENDERITE_ITEM_SETTINGS.get()).useBlockDescriptionPrefix()));

    public static final RegistrySupplier<Block> CRACKED_ENDERITE_ORE = BLOCKS.register("cracked_enderite_ore",
        () -> new Block(getBlockSettings("cracked_enderite_ore").mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops()
            .sound(SoundType.ANCIENT_DEBRIS).strength(20.0f, 1200.0F)));
    public static final RegistrySupplier<Item> CRACKED_ENDERITE_ORE_ITEM = ITEMS.register("cracked_enderite_ore",
        () -> new BlockItem(CRACKED_ENDERITE_ORE.get(), getItemSettings("cracked_enderite_ore", BASE_ENDERITE_ITEM_SETTINGS.get()).useBlockDescriptionPrefix()));

    // Respawn Anchor
    public static final RegistrySupplier<Block> ENDERITE_RESPAWN_ANCHOR = BLOCKS.register("enderite_respawn_anchor",
        () -> new EnderiteRespawnAnchor(getBlockSettings("enderite_respawn_anchor")));
    public static final RegistrySupplier<Item> ENDERITE_RESPAWN_ANCHOR_ITEM = ITEMS.register(
        "enderite_respawn_anchor",
        () -> new BlockItem(ENDERITE_RESPAWN_ANCHOR.get(), getItemSettings("enderite_respawn_anchor", BASE_ENDERITE_ITEM_SETTINGS.get()).useBlockDescriptionPrefix()));
    public static final RegistrySupplier<BlockEntityType<EnderiteRespawnAnchorBlockEntity>> ENDERITE_RESPAWN_ANCHOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enderite_respawn_anchor",
        () -> BlockEntityTypeBuilder.create(EnderiteRespawnAnchorBlockEntity::new, ENDERITE_RESPAWN_ANCHOR.get()));

    // Enderite Armor
    public static final RegistrySupplier<Item> ENDERITE_HELMET = ITEMS.register("enderite_helmet",
        () -> new Item((getItemSettings("enderite_helmet", BASE_ENDERITE_ITEM_SETTINGS.get()).durability(ArmorType.HELMET.getDurability(CONFIG.armor.durabilityMultiplier)))
            .humanoidArmor(EnderiteArmorMaterial.ENDERITE, ArmorType.HELMET)));
    public static final RegistrySupplier<Item> ENDERITE_CHESTPLATE = ITEMS.register("enderite_chestplate",
        () -> new Item((getItemSettings("enderite_chestplate", BASE_ENDERITE_ITEM_SETTINGS.get()).durability(ArmorType.CHESTPLATE.getDurability(CONFIG.armor.durabilityMultiplier)))
            .humanoidArmor(EnderiteArmorMaterial.ENDERITE, ArmorType.CHESTPLATE)));
    public static final RegistrySupplier<Item> ENDERITE_LEGGINGS = ITEMS.register("enderite_leggings",
        () -> new Item((getItemSettings("enderite_leggings", BASE_ENDERITE_ITEM_SETTINGS.get()).durability(ArmorType.LEGGINGS.getDurability(CONFIG.armor.durabilityMultiplier)))
            .humanoidArmor(EnderiteArmorMaterial.ENDERITE, ArmorType.LEGGINGS)));
    public static final RegistrySupplier<Item> ENDERITE_BOOTS = ITEMS.register("enderite_boots",
        () -> new Item((getItemSettings("enderite_boots", BASE_ENDERITE_ITEM_SETTINGS.get()).durability(ArmorType.BOOTS.getDurability(CONFIG.armor.durabilityMultiplier)))
            .humanoidArmor(EnderiteArmorMaterial.ENDERITE, ArmorType.BOOTS)));


    public static final RegistrySupplier<Item> ENDERITE_HORSE_ARMOR = ITEMS.register("enderite_horse_armor",
        () -> new Item(getItemSettings("enderite_horse_armor", BASE_ENDERITE_ITEM_SETTINGS.get()).horseArmor(EnderiteArmorMaterial.ENDERITE)));
    public static final RegistrySupplier<Item> ENDERITE_NAUTILUS_ARMOR = ITEMS.register("enderite_nautilus_armor",
        () -> new Item(getItemSettings("enderite_nautilus_armor", BASE_ENDERITE_ITEM_SETTINGS.get()).nautilusArmor(EnderiteArmorMaterial.ENDERITE)));

    // Enderite Elytra
    public static final RegistrySupplier<Item> ENDERITE_ELYTRA = EnderiteMod.ITEMS.register("enderite_elytra",
        () -> new Item(getItemSettings("enderite_elytra", EnderiteMod.ENDERITE_ELYTRA_ITEM_SETTINGS.get())
            .humanoidArmor(EnderiteArmorMaterial.ENDERITE_ELYTRA, ArmorType.CHESTPLATE)
        ));
    public static final RegistrySupplier<Item> ENDERITE_ELYTRA_SEPERATED = EnderiteMod.ITEMS.register(
        "enderite_elytra_seperated",
        () -> new Item(
            getItemSettings("enderite_elytra_seperated", EnderiteMod.ENDERITE_ELYTRA_SEPERATED_ITEM_SETTINGS.get())
        ));

    public static final Supplier<Properties> ENDERITE_ELYTRA_SEPERATED_ITEM_SETTINGS = () -> new Item.Properties()
        .arch$tab(ENDERITE_TAB)
        .fireResistant()
        .stacksTo(1)
        .durability(1024)
        .rarity(Rarity.EPIC)
        .component(DataComponents.GLIDER, Unit.INSTANCE)
        .component(
            DataComponents.EQUIPPABLE,
            Equippable.builder(EquipmentSlot.CHEST).setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA).setAsset(EnderiteArmorMaterial.ENDERITE_ELYTRA_SEPERATED_ARMOR_MODEL_ID).setDamageOnHurt(false).build()
        )
        .repairable(EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT);
    public static final Supplier<Properties> ENDERITE_ELYTRA_ITEM_SETTINGS = () -> new Item.Properties()
        .arch$tab(ENDERITE_TAB)
        .fireResistant()
        .stacksTo(1)
        .durability(1024)
        .rarity(Rarity.EPIC)
        .component(DataComponents.GLIDER, Unit.INSTANCE);

    public static EnderiteElytraSpecialRecipe ENDERITE_ELYTRA_SPECIAL_RECIPE_INSTANCE = new EnderiteElytraSpecialRecipe();

    public static RegistrySupplier<RecipeSerializer<? extends CustomRecipe>> ENDERITE_EYLTRA_SPECIAL_RECIPE = RECIPES
        .register("crafting_special_enderiteelytra",
            () -> new RecipeSerializer<>(
                com.mojang.serialization.MapCodec.unit(ENDERITE_ELYTRA_SPECIAL_RECIPE_INSTANCE),
                StreamCodec.unit(ENDERITE_ELYTRA_SPECIAL_RECIPE_INSTANCE)));

    // Shulker Box
    public static final RegistrySupplier<Block> ENDERITE_SHULKER_BOX = BLOCKS.register("enderite_shulker_box",
        () -> new EnderiteShulkerBoxBlock("enderite_shulker_box"));
    public static final RegistrySupplier<Item> ENDERITE_SHULKER_BOX_ITEM = ITEMS.register("enderite_shulker_box",
        () -> new BlockItem(ENDERITE_SHULKER_BOX.get(), getItemSettings("enderite_shulker_box", BASE_ENDERITE_ITEM_SETTINGS.get()).stacksTo(1)
            .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY).useBlockDescriptionPrefix()));
    public static final RegistrySupplier<BlockEntityType<EnderiteShulkerBoxBlockEntity>> ENDERITE_SHULKER_BOX_BLOCK_ENTITY = EnderiteMod.BLOCK_ENTITY_TYPES
        .register("enderite_shulker_box_block_entity",
            () -> BlockEntityTypeBuilder
                .create(EnderiteShulkerBoxBlockEntity::new,
                    EnderiteMod.ENDERITE_SHULKER_BOX.get()));

    public static MenuType<EnderiteShulkerBoxScreenHandler> ENDERITE_SHULKER_BOX_SCREEN_HANDLER;

    // Enchantments
    public static Identifier VOID_FLOATING_ENCHANTMENT_ID = Identifier.fromNamespaceAndPath(MOD_ID, "void_floating");

    // Trims
    public static final RegistrySupplier<Item> ENDERITE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("enderite_upgrade_smithing_template",
        () -> new SmithingTemplateItem(
            Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(MOD_ID, "smithing_template.enderite_upgrade.applies_to"))).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(MOD_ID, "smithing_template.enderite_upgrade.ingredients"))).withStyle(ChatFormatting.BLUE),
            //Text.translatable(Util.createTranslationKey("upgrade", Identifier.of(MOD_ID,"enderite_upgrade"))).formatted(Formatting.GRAY),
            Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(MOD_ID, "smithing_template.enderite_upgrade.base_slot_description"))),
            Component.translatable(Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(MOD_ID, "smithing_template.enderite_upgrade.additions_slot_description"))),
            SmithingTemplateItem.createNetheriteUpgradeIconList(),
            SmithingTemplateItem.createNetheriteUpgradeMaterialList(),
            getItemSettings("enderite_upgrade_smithing_template").rarity(Rarity.UNCOMMON)));

    public static void init() {
        // Init static enderite tools
        EnderiteTools.init();

        EnderiteDataComponents.init();
        BLOCKS.register();
        ITEMS.register();
        RECIPES.register();
        BLOCK_ENTITY_TYPES.register();
        TABS.register();

        LifecycleEvent.SETUP.register(() -> {
            BiomeModifications.addProperties((ctx) -> ctx.hasTag(BiomeTags.IS_END), (ctx, mutable) -> {
                mutable.getGenerationProperties().addFeature(
                    GenerationStep.Decoration.UNDERGROUND_ORES,
                    ResourceKey.create(Registries.PLACED_FEATURE,
                        Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "ore_enderite_large")));
                mutable.getGenerationProperties().addFeature(
                    GenerationStep.Decoration.UNDERGROUND_ORES,
                    ResourceKey.create(Registries.PLACED_FEATURE,
                        Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "ore_enderite_small")));
            });
            CreativeTabRegistry.append(ENDERITE_TAB, ENDERITE_UPGRADE_SMITHING_TEMPLATE.get());
        });

        EnderiteUpgradeSmithingTemplate.registerLoottables();
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        ClientTooltipEvent.ITEM.register((stack, lines, tooltipContext, flag) -> {
            ArrayList<Component> component_tooltip = new ArrayList<>();
            if(stack.get(EnderiteDataComponents.TELEPORT_CHARGE.get()) instanceof TooltipProvider tta) {
                tta.addToTooltip(tooltipContext, component_tooltip::add, flag, stack.getComponents());
            }

            if(stack.get(EnderiteDataComponents.ENDERITE_TOOLTIP.get()) instanceof TooltipProvider tta) {
                tta.addToTooltip(tooltipContext, component_tooltip::add, flag, stack.getComponents());
            }
            lines.addAll(1, component_tooltip);
        });
    }

    public static BlockBehaviour.Properties getBlockSettings(String id) {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, id)));
    }

    public static Item.Properties getItemSettings(String id) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, id)));
    }

    public static Item.Properties getItemSettings(String id, Item.Properties settings) {
        return settings.setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, id)));
    }


}
