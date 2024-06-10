package net.enderitemc.enderitemod;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.google.common.base.Suppliers;

import com.mojang.serialization.Codec;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrarManager;
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
import net.enderitemc.enderitemod.misc.EnderiteDataComponents;
import net.enderitemc.enderitemod.misc.EnderiteElytraSpecialRecipe;
import net.enderitemc.enderitemod.misc.EnderiteUpgradeSmithingTemplate;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlock;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntity;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxRecipe;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.GenerationStep;

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
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(MOD_ID,
        RegistryKeys.ENCHANTMENT);
    public static final DeferredRegister<ItemGroup> TABS =
        DeferredRegister.create(MOD_ID, RegistryKeys.ITEM_GROUP);
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIAL =
            DeferredRegister.create(MOD_ID, RegistryKeys.ARMOR_MATERIAL);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(MOD_ID, RegistryKeys.DATA_COMPONENT_TYPE);


    public static Config CONFIG = ConfigLoader.get();

    // Tab
    public static final RegistrySupplier<ItemGroup> ENDERITE_TAB = TABS.register("enderite_group",
        () -> CreativeTabRegistry.create(
            Text.translatable("itemGroup.enderitemod.enderite_group"),
            () ->new ItemStack(EnderiteMod.ENDERITE_INGOT.get())
        )
    );

    // Enderite Ingot
    public static final RegistrySupplier<Item> ENDERITE_INGOT = ITEMS.register("enderite_ingot",
        () -> new EnderiteIngot(new Item.Settings().arch$tab(ENDERITE_TAB).fireproof()));
    public static final RegistrySupplier<Item> ENDERITE_SCRAP = ITEMS.register("enderite_scrap",
        () -> new EnderiteScrap(new Item.Settings().arch$tab(ENDERITE_TAB).fireproof()));

    public static final RegistrySupplier<ArmorMaterial> ENDERITE_ARMOR_MATERIAL = ARMOR_MATERIAL.register(EnderiteArmorMaterial.ID.getPath(),
            () -> EnderiteArmorMaterial.ENDERITE);


    // Enderite Block
    public static final Supplier<Item.Settings> BASE_ENDERITE_ITEM_SETTINGS = () -> new Item.Settings()
        .arch$tab(ENDERITE_TAB).fireproof();


    public static final RegistrySupplier<Block> ENDERITE_BLOCK = BLOCKS.register("enderite_block",
        () -> new EnderiteBlock());
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
        () -> new EnderiteRespawnAnchor());
    public static final RegistrySupplier<Item> ENDERITE_RESPAWN_ANCHOR_ITEM = ITEMS.register(
        "enderite_respawn_anchor",
        () -> new BlockItem(ENDERITE_RESPAWN_ANCHOR.get(), BASE_ENDERITE_ITEM_SETTINGS.get()));

    // Enderite Armor
    public static final RegistrySupplier<Item> ENDERITE_HELMET = ITEMS.register("enderite_helmet",
        () -> new ArmorItem(ENDERITE_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
            (new Item.Settings().arch$tab(ENDERITE_TAB).fireproof().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(CONFIG.armor.durabilityMultiplier)))));
    public static final RegistrySupplier<Item> ENDERITE_CHESTPLATE = ITEMS.register("enderite_chestplate",
        () -> new ArmorItem(ENDERITE_ARMOR_MATERIAL,
            ArmorItem.Type.CHESTPLATE,
            (new Item.Settings().arch$tab(ENDERITE_TAB).fireproof().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(CONFIG.armor.durabilityMultiplier)))));
    public static final RegistrySupplier<Item> ENDERITE_LEGGINGS = ITEMS.register("enderite_leggings",
        () -> new ArmorItem(ENDERITE_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
            (new Item.Settings().arch$tab(ENDERITE_TAB).fireproof().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(CONFIG.armor.durabilityMultiplier)))));
    public static final RegistrySupplier<Item> ENDERITE_BOOTS = ITEMS.register("enderite_boots",
        () -> new ArmorItem(ENDERITE_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
            (new Item.Settings().arch$tab(ENDERITE_TAB).fireproof().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(CONFIG.armor.durabilityMultiplier)))));

    // Enderite Elytra
    // Seperated to fabric-like and forge
    public static RegistrySupplier<? extends Item> ENDERITE_ELYTRA;
    public static RegistrySupplier<? extends Item> ENDERITE_ELYTRA_SEPERATED;

    public static final Settings ENDERITE_ELYTRA_SEPERATED_ITEM_SETTINGS = new Item.Settings()
        .arch$tab(ENDERITE_TAB)
        .fireproof()
        .maxCount(1)
        .maxDamage(1024)
        .rarity(Rarity.EPIC);
    public static final Settings ENDERITE_ELYTRA_ITEM_SETTINGS = new Item.Settings()
        .arch$tab(ENDERITE_TAB)
        .fireproof()
        .maxCount(1)
        .maxDamage(1024)
        .rarity(Rarity.EPIC);

    public static RegistrySupplier<RecipeSerializer<?>> ENDERITE_EYLTRA_SPECIAL_RECIPE = RECIPES
        .register("crafting_special_enderiteelytra",
            () -> new SpecialRecipeSerializer<>(
                EnderiteElytraSpecialRecipe::new));

    // Shulker Box
    public static final RegistrySupplier<Block> ENDERITE_SHULKER_BOX = BLOCKS.register("enderite_shulker_box",
        () -> new EnderiteShulkerBoxBlock());
    public static final RegistrySupplier<Item> ENDERITE_SHULKER_BOX_ITEM = ITEMS.register("enderite_shulker_box",
        () -> new BlockItem(ENDERITE_SHULKER_BOX.get(), BASE_ENDERITE_ITEM_SETTINGS.get().maxCount(1)));
    public static RegistrySupplier<RecipeSerializer<?>> ENDERITE_SHULKER_BOX_RECIPE = RECIPES
        .register("crafting_special_enderiteshulkerbox",
            () -> new SpecialRecipeSerializer<>(
                EnderiteShulkerBoxRecipe::new));
    public static RegistrySupplier<BlockEntityType<EnderiteShulkerBoxBlockEntity>> ENDERITE_SHULKER_BOX_BLOCK_ENTITY;

    // Enchantment
    public static RegistrySupplier<Enchantment> VOID_FLOATING_ENCHANTMENT = ENCHANTMENTS.register("void_floating",
        () -> new VoidFloatingEnchantment());

    public static ScreenHandlerType<EnderiteShulkerBoxScreenHandler> ENDERITE_SHULKER_BOX_SCREEN_HANDLER;

    // Trims
    public static final RegistrySupplier<Item> ENDERITE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("enderite_upgrade_smithing_template",
        () -> new SmithingTemplateItem(
            Text.translatable(Util.createTranslationKey("item", new Identifier(MOD_ID,"smithing_template.enderite_upgrade.applies_to"))).formatted(Formatting.BLUE),
            Text.translatable(Util.createTranslationKey("item", new Identifier(MOD_ID,"smithing_template.enderite_upgrade.ingredients"))).formatted(Formatting.BLUE),
            Text.translatable(Util.createTranslationKey("upgrade", new Identifier(MOD_ID,"enderite_upgrade"))).formatted(Formatting.GRAY),
            Text.translatable(Util.createTranslationKey("item", new Identifier(MOD_ID,"smithing_template.enderite_upgrade.base_slot_description"))),
            Text.translatable(Util.createTranslationKey("item", new Identifier(MOD_ID,"smithing_template.enderite_upgrade.additions_slot_description"))),
            SmithingTemplateItem.getNetheriteUpgradeEmptyBaseSlotTextures(),
            SmithingTemplateItem.getNetheriteUpgradeEmptyAdditionsSlotTextures()));

    public static void init() {
        BLOCKS.register();
        ITEMS.register();
        RECIPES.register();
        BLOCK_ENTITY_TYPES.register();
        ENCHANTMENTS.register();
        TABS.register();
        ARMOR_MATERIAL.register();
        EnderiteDataComponents.init();

        LifecycleEvent.SETUP.register(() -> {
            BiomeModifications.addProperties((ctx) -> ctx.hasTag(BiomeTags.IS_END), (ctx, mutable) -> {
                mutable.getGenerationProperties().addFeature(
                    GenerationStep.Feature.UNDERGROUND_ORES,
                    RegistryKey.of(RegistryKeys.PLACED_FEATURE,
                        new Identifier(EnderiteMod.MOD_ID,"ore_enderite_large")));
                mutable.getGenerationProperties().addFeature(
                    GenerationStep.Feature.UNDERGROUND_ORES,
                    RegistryKey.of(RegistryKeys.PLACED_FEATURE,
                        new Identifier(EnderiteMod.MOD_ID,"ore_enderite_small")));
            });
            CreativeTabRegistry.append(ENDERITE_TAB, ENDERITE_UPGRADE_SMITHING_TEMPLATE.get());
        });

        //EnderiteShears.registerLoottables_Fabric();
        EnderiteUpgradeSmithingTemplate.registerLoottables();
    }
}
