package net.enderitemc.enderitemod;

import com.google.common.base.Suppliers;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.blocks.EnderiteOre;
import net.enderitemc.enderitemod.blocks.EnderiteRespawnAnchor;
import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorBlockEntity;
import net.enderitemc.enderitemod.config.Config;
import net.enderitemc.enderitemod.config.ConfigLoader;
import net.enderitemc.enderitemod.materials.EnderiteArmorMaterial;
import net.enderitemc.enderitemod.misc.EnderiteDataComponents;
import net.enderitemc.enderitemod.misc.EnderiteElytraSpecialRecipe;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.enderitemc.enderitemod.misc.EnderiteUpgradeSmithingTemplate;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlock;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntity;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxScreenHandler;
import net.enderitemc.enderitemod.tools.BlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.equipment.EquipmentModels;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialCraftingRecipe.SpecialRecipeSerializer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.gen.GenerationStep;

import java.util.function.Supplier;

public class EnderiteMod {
    public static final String MOD_ID = "enderitemod";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, RegistryKeys.BLOCK);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(MOD_ID,
        RegistryKeys.RECIPE_SERIALIZER);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(MOD_ID,
        RegistryKeys.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<ItemGroup> TABS =
        DeferredRegister.create(MOD_ID, RegistryKeys.ITEM_GROUP);
    //    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIAL =
//            DeferredRegister.create(MOD_ID, RegistryKeys.ARMOR_MATERIAL);
    public static final DeferredRegister<ComponentType<?>> DATA_COMPONENT_TYPES =
        DeferredRegister.create(MOD_ID, RegistryKeys.DATA_COMPONENT_TYPE);


    public static Config CONFIG = ConfigLoader.get();

    // Tab
    public static final RegistrySupplier<ItemGroup> ENDERITE_TAB = TABS.register("enderite_group",
        () -> CreativeTabRegistry.create(
            Text.translatable("itemGroup.enderitemod.enderite_group"),
            () -> new ItemStack(EnderiteMod.ENDERITE_INGOT.get())
        )
    );
    public static final Supplier<Item.Settings> BASE_ENDERITE_ITEM_SETTINGS = () -> new Item.Settings()
        .arch$tab(ENDERITE_TAB).fireproof();

    // Enderite Ingot
    public static final RegistrySupplier<Item> ENDERITE_INGOT = ITEMS.register("enderite_ingot",
        () -> new Item(getItemSettings("enderite_ingot", BASE_ENDERITE_ITEM_SETTINGS.get())));
    public static final RegistrySupplier<Item> ENDERITE_SCRAP = ITEMS.register("enderite_scrap",
        () -> new Item(getItemSettings("enderite_scrap", BASE_ENDERITE_ITEM_SETTINGS.get())));

    // Enderite Armor
//    public static final RegistrySupplier<ArmorMaterial> ENDERITE_ARMOR_MATERIAL = ARMOR_MATERIAL.register(EnderiteArmorMaterial.ID.getPath(),
//            () -> EnderiteArmorMaterial.ENDERITE);

    // Enderite Block
    public static final RegistrySupplier<Block> ENDERITE_BLOCK = BLOCKS.register("enderite_block",
        () -> new Block(getBlockSettings("enderite_block").mapColor(MapColor.BLACK).requiresTool().strength(66.0F, 1200.0F)
            .sounds(BlockSoundGroup.NETHERITE)));
    public static final RegistrySupplier<Item> ENDERITE_BLOCK_ITEM = ITEMS.register("enderite_block",
        () -> new BlockItem(ENDERITE_BLOCK.get(), getItemSettings("enderite_block", BASE_ENDERITE_ITEM_SETTINGS.get()).useBlockPrefixedTranslationKey()));

    public static final RegistrySupplier<Block> ENDERITE_ORE = BLOCKS.register("enderite_ore",
        () -> new EnderiteOre(getBlockSettings("enderite_ore")));
    public static final RegistrySupplier<Item> ENDERITE_ORE_ITEM = ITEMS.register("enderite_ore",
        () -> new BlockItem(ENDERITE_ORE.get(), getItemSettings("enderite_ore", BASE_ENDERITE_ITEM_SETTINGS.get()).useBlockPrefixedTranslationKey()));

    public static final RegistrySupplier<Block> CRACKED_ENDERITE_ORE = BLOCKS.register("cracked_enderite_ore",
        () -> new Block(getBlockSettings("cracked_enderite_ore").mapColor(MapColor.BLACK).requiresTool()
            .sounds(BlockSoundGroup.ANCIENT_DEBRIS).strength(20.0f, 1200.0F)));
    public static final RegistrySupplier<Item> CRACKED_ENDERITE_ORE_ITEM = ITEMS.register("cracked_enderite_ore",
        () -> new BlockItem(CRACKED_ENDERITE_ORE.get(), getItemSettings("cracked_enderite_ore", BASE_ENDERITE_ITEM_SETTINGS.get()).useBlockPrefixedTranslationKey()));

    // Respawn Anchor
    public static final RegistrySupplier<Block> ENDERITE_RESPAWN_ANCHOR = BLOCKS.register("enderite_respawn_anchor",
        () -> new EnderiteRespawnAnchor(getBlockSettings("enderite_respawn_anchor")));
    public static final RegistrySupplier<Item> ENDERITE_RESPAWN_ANCHOR_ITEM = ITEMS.register(
        "enderite_respawn_anchor",
        () -> new BlockItem(ENDERITE_RESPAWN_ANCHOR.get(), getItemSettings("enderite_respawn_anchor", BASE_ENDERITE_ITEM_SETTINGS.get()).useBlockPrefixedTranslationKey()));
    public static final RegistrySupplier<BlockEntityType<EnderiteRespawnAnchorBlockEntity>> ENDERITE_RESPAWN_ANCHOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enderite_respawn_anchor",
        () -> BlockEntityTypeBuilder.create(EnderiteRespawnAnchorBlockEntity::new, ENDERITE_RESPAWN_ANCHOR.get()));

    // Enderite Armor
    public static final RegistrySupplier<Item> ENDERITE_HELMET = ITEMS.register("enderite_helmet",
        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentType.HELMET,
            (getItemSettings("enderite_helmet", BASE_ENDERITE_ITEM_SETTINGS.get()).maxDamage(EquipmentType.HELMET.getMaxDamage(CONFIG.armor.durabilityMultiplier)))));
    public static final RegistrySupplier<Item> ENDERITE_CHESTPLATE = ITEMS.register("enderite_chestplate",
        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentType.CHESTPLATE,
            (getItemSettings("enderite_chestplate", BASE_ENDERITE_ITEM_SETTINGS.get()).maxDamage(EquipmentType.CHESTPLATE.getMaxDamage(CONFIG.armor.durabilityMultiplier)))));
    public static final RegistrySupplier<Item> ENDERITE_LEGGINGS = ITEMS.register("enderite_leggings",
        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentType.LEGGINGS,
            (getItemSettings("enderite_leggings", BASE_ENDERITE_ITEM_SETTINGS.get()).maxDamage(EquipmentType.LEGGINGS.getMaxDamage(CONFIG.armor.durabilityMultiplier)))));
    public static final RegistrySupplier<Item> ENDERITE_BOOTS = ITEMS.register("enderite_boots",
        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, EquipmentType.BOOTS,
            (getItemSettings("enderite_boots", BASE_ENDERITE_ITEM_SETTINGS.get()).maxDamage(EquipmentType.BOOTS.getMaxDamage(CONFIG.armor.durabilityMultiplier)))));

    // Enderite Elytra
    public static final RegistrySupplier<Item> ENDERITE_ELYTRA = EnderiteMod.ITEMS.register("enderite_elytra",
        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE_ELYTRA,
            EquipmentType.CHESTPLATE,
            getItemSettings("enderite_elytra", EnderiteMod.ENDERITE_ELYTRA_ITEM_SETTINGS.get())
        ));
    public static final RegistrySupplier<Item> ENDERITE_ELYTRA_SEPERATED = EnderiteMod.ITEMS.register(
        "enderite_elytra_seperated",
        () -> new Item(
            getItemSettings("enderite_elytra_seperated", EnderiteMod.ENDERITE_ELYTRA_SEPERATED_ITEM_SETTINGS.get())
        ));

    public static final Supplier<Settings> ENDERITE_ELYTRA_SEPERATED_ITEM_SETTINGS = () -> new Item.Settings()
        .arch$tab(ENDERITE_TAB)
        .fireproof()
        .maxCount(1)
        .maxDamage(1024)
        .rarity(Rarity.EPIC)
        .component(DataComponentTypes.GLIDER, Unit.INSTANCE)
        .component(
            DataComponentTypes.EQUIPPABLE,
            EquippableComponent.builder(EquipmentSlot.CHEST).equipSound(SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA).model(EnderiteArmorMaterial.ENDERITE_ELYTRA_SEPERATED_ARMOR_MODEL_ID).damageOnHurt(false).build()
        )
        .repairable(EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT);
    public static final Supplier<Settings> ENDERITE_ELYTRA_ITEM_SETTINGS = () -> new Item.Settings()
        .arch$tab(ENDERITE_TAB)
        .fireproof()
        .maxCount(1)
        .maxDamage(1024)
        .rarity(Rarity.EPIC)
        .component(DataComponentTypes.GLIDER, Unit.INSTANCE);

    public static RegistrySupplier<RecipeSerializer<? extends SpecialCraftingRecipe>> ENDERITE_EYLTRA_SPECIAL_RECIPE = RECIPES
        .register("crafting_special_enderiteelytra",
            () -> new SpecialRecipeSerializer<>(
                EnderiteElytraSpecialRecipe::new));

    // Shulker Box
    public static final RegistrySupplier<Block> ENDERITE_SHULKER_BOX = BLOCKS.register("enderite_shulker_box",
        () -> new EnderiteShulkerBoxBlock("enderite_shulker_box"));
    public static final RegistrySupplier<Item> ENDERITE_SHULKER_BOX_ITEM = ITEMS.register("enderite_shulker_box",
        () -> new BlockItem(ENDERITE_SHULKER_BOX.get(), getItemSettings("enderite_shulker_box", BASE_ENDERITE_ITEM_SETTINGS.get()).maxCount(1)
            .component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT).useBlockPrefixedTranslationKey()));
    public static final RegistrySupplier<BlockEntityType<EnderiteShulkerBoxBlockEntity>> ENDERITE_SHULKER_BOX_BLOCK_ENTITY = EnderiteMod.BLOCK_ENTITY_TYPES
        .register("enderite_shulker_box_block_entity",
            () -> BlockEntityTypeBuilder
                .create(EnderiteShulkerBoxBlockEntity::new,
                    EnderiteMod.ENDERITE_SHULKER_BOX.get()));

    public static ScreenHandlerType<EnderiteShulkerBoxScreenHandler> ENDERITE_SHULKER_BOX_SCREEN_HANDLER;

    // Enchantments
    public static Identifier VOID_FLOATING_ENCHANTMENT_ID = Identifier.of(MOD_ID, "void_floating");

    // Trims
    public static final RegistrySupplier<Item> ENDERITE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("enderite_upgrade_smithing_template",
        () -> new SmithingTemplateItem(
            Text.translatable(Util.createTranslationKey("item", Identifier.of(MOD_ID, "smithing_template.enderite_upgrade.applies_to"))).formatted(Formatting.BLUE),
            Text.translatable(Util.createTranslationKey("item", Identifier.of(MOD_ID, "smithing_template.enderite_upgrade.ingredients"))).formatted(Formatting.BLUE),
            //Text.translatable(Util.createTranslationKey("upgrade", Identifier.of(MOD_ID,"enderite_upgrade"))).formatted(Formatting.GRAY),
            Text.translatable(Util.createTranslationKey("item", Identifier.of(MOD_ID, "smithing_template.enderite_upgrade.base_slot_description"))),
            Text.translatable(Util.createTranslationKey("item", Identifier.of(MOD_ID, "smithing_template.enderite_upgrade.additions_slot_description"))),
            SmithingTemplateItem.getNetheriteUpgradeEmptyBaseSlotTextures(),
            SmithingTemplateItem.getNetheriteUpgradeEmptyAdditionsSlotTextures(),
            getItemSettings("enderite_upgrade_smithing_template").rarity(Rarity.UNCOMMON)));

    public static void init() {
        BLOCKS.register();
        ITEMS.register();
        RECIPES.register();
        BLOCK_ENTITY_TYPES.register();
        TABS.register();
        //ARMOR_MATERIAL.register();
        EnderiteDataComponents.init();

        LifecycleEvent.SETUP.register(() -> {
            BiomeModifications.addProperties((ctx) -> ctx.hasTag(BiomeTags.IS_END), (ctx, mutable) -> {
                mutable.getGenerationProperties().addFeature(
                    GenerationStep.Feature.UNDERGROUND_ORES,
                    RegistryKey.of(RegistryKeys.PLACED_FEATURE,
                        Identifier.of(EnderiteMod.MOD_ID, "ore_enderite_large")));
                mutable.getGenerationProperties().addFeature(
                    GenerationStep.Feature.UNDERGROUND_ORES,
                    RegistryKey.of(RegistryKeys.PLACED_FEATURE,
                        Identifier.of(EnderiteMod.MOD_ID, "ore_enderite_small")));
            });
            CreativeTabRegistry.append(ENDERITE_TAB, ENDERITE_UPGRADE_SMITHING_TEMPLATE.get());
        });

        //EnderiteShears.registerLoottables_Fabric();
        EnderiteUpgradeSmithingTemplate.registerLoottables();
    }

    public static AbstractBlock.Settings getBlockSettings(String id) {
        return AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, id)));
    }

    public static Item.Settings getItemSettings(String id) {
        return new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, id)));
    }

    public static Item.Settings getItemSettings(String id, Item.Settings settings) {
        return settings.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, id)));
    }
}
