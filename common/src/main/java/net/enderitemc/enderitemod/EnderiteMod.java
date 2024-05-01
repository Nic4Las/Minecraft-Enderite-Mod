package net.enderitemc.enderitemod;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;

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
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.enderitemc.enderitemod.misc.EnderiteElytraSpecialRecipe;
import net.enderitemc.enderitemod.misc.EnderiteShieldDecorationRecipe;
import net.enderitemc.enderitemod.misc.EnderiteUpgradeSmithingTemplate;
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
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SmithingTemplateItem;
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



    public static final RegistrySupplier<ItemGroup> ENDERITE_TAB = TABS.register("enderite_group",
        () -> CreativeTabRegistry.create(
            Text.translatable("itemGroup.enderitemod.enderite_group"),
            () ->new ItemStack(EnderiteMod.ENDERITE_INGOT.get())
        )
    );




    public static Config CONFIG = ConfigLoader.get();

    // Enderite Ingot
    public static final RegistrySupplier<Item> ENDERITE_INGOT = ITEMS.register("enderite_ingot",
        () -> new EnderiteIngot(new Item.Settings().arch$tab(ENDERITE_TAB).fireproof()));
    public static final RegistrySupplier<Item> ENDERITE_SCRAP = ITEMS.register("enderite_scrap",
        () -> new EnderiteScrap(new Item.Settings().arch$tab(ENDERITE_TAB).fireproof()));



    // Enderite Tools
    public static final RegistrySupplier<Item> ENDERITE_PICKAXE = ITEMS.register("enderite_pickaxe",
        () -> new PickaxeSubclass(EnderiteMaterial.ENDERITE,
            CONFIG.tools.enderitePickaxeAD, -2.8F,
            new Item.Settings().arch$tab(ENDERITE_TAB).fireproof()));
    public static final RegistrySupplier<Item> ENDERITE_AXE = ITEMS.register("enderite_axe",
        () -> new AxeSubclass(EnderiteMaterial.ENDERITE, CONFIG.tools.enderiteAxeAD,
            -3.0F,
            new Item.Settings().arch$tab(ENDERITE_TAB).fireproof()));
    public static final RegistrySupplier<Item> ENDERITE_HOE = ITEMS.register("enderite_hoe",
        () -> new HoeSubclass(EnderiteMaterial.ENDERITE, CONFIG.tools.enderiteHoeAD,
            0.0F,
            new Item.Settings().arch$tab(ENDERITE_TAB).fireproof()));

    public static final RegistrySupplier<Item> ENDERITE_SHOVEL = ITEMS.register("enderite_shovel",
        () -> new ShovelItem(EnderiteMaterial.ENDERITE,
            CONFIG.tools.enderiteShovelAD, -3.0F,
            new Item.Settings().arch$tab(ENDERITE_TAB).fireproof()));
    public static final RegistrySupplier<Item> ENDERITE_SWORD = ITEMS.register("enderite_sword",
        () -> new EnderiteSword(EnderiteMaterial.ENDERITE,
            CONFIG.tools.enderiteSwordAD, -2.4F,
            new Item.Settings().arch$tab(ENDERITE_TAB).fireproof()));


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
        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, ArmorItem.Type.HELMET,
            (new Item.Settings().arch$tab(ENDERITE_TAB).fireproof())));
    public static final RegistrySupplier<Item> ENDERITE_CHESTPLATE = ITEMS.register("enderite_chestplate",
        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE,
            ArmorItem.Type.CHESTPLATE,
            (new Item.Settings().arch$tab(ENDERITE_TAB).fireproof())));
    public static final RegistrySupplier<Item> ENDERITE_LEGGINGS = ITEMS.register("enderite_leggings",
        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, ArmorItem.Type.LEGGINGS,
            (new Item.Settings().arch$tab(ENDERITE_TAB).fireproof())));
    public static final RegistrySupplier<Item> ENDERITE_BOOTS = ITEMS.register("enderite_boots",
        () -> new ArmorItem(EnderiteArmorMaterial.ENDERITE, ArmorItem.Type.BOOTS,
            (new Item.Settings().arch$tab(ENDERITE_TAB).fireproof())));

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

    // Bows
    public static final RegistrySupplier<Item> ENDERITE_CROSSBOW = ITEMS.register("enderite_crossbow",
        () -> new EnderiteCrossbow(
            new Item.Settings().arch$tab(ENDERITE_TAB).fireproof().maxCount(1)
                .maxDamage(768)));
    public static final RegistrySupplier<Item> ENDERITE_BOW = ITEMS.register("enderite_bow", () -> new EnderiteBow(
        new Item.Settings().arch$tab(ENDERITE_TAB).fireproof().maxCount(1).maxDamage(768)));

    // Shield
    public static final Settings ENDERITE_SHIELD_ITEM_SETTINGS = new Item.Settings().arch$tab(ENDERITE_TAB).fireproof().maxCount(1).maxDamage(768);
    public static RegistrySupplier<Item> ENDERITE_SHIELD = ITEMS.register("enderite_shield",
        () -> new EnderiteShield(
            ENDERITE_SHIELD_ITEM_SETTINGS));

    public static RegistrySupplier<RecipeSerializer<?>> ENDERITE_SHIELD_DECORATION_RECIPE = RECIPES
        .register("crafting_special_enderiteshielddecoration",
            () -> new SpecialRecipeSerializer<EnderiteShieldDecorationRecipe>(
                EnderiteShieldDecorationRecipe::new));

    // Enchantment
    public static RegistrySupplier<Enchantment> VOID_FLOATING_ENCHANTMENT = ENCHANTMENTS.register("void_floating",
        () -> new VoidFloatingEnchantment());

    public static ScreenHandlerType<EnderiteShulkerBoxScreenHandler> ENDERITE_SHULKER_BOX_SCREEN_HANDLER;

    // MOST IMPORTANT
    public static final RegistrySupplier<Item> ENDERITE_SHEAR = ITEMS.register("enderite_shears",
        () -> new EnderiteShears(
            new Item.Settings().arch$tab(ENDERITE_TAB).fireproof().maxCount(1)
                .maxDamage(2048)
                .rarity(Rarity.RARE)));

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

        EnderiteShears.registerLoottables();
        EnderiteUpgradeSmithingTemplate.registerLoottables();
    }
}
