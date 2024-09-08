package net.enderitemc.enderitemod.tools;

import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.enderitemc.enderitemod.misc.EnderiteShieldDecorationRecipe;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Rarity;

import java.util.function.Supplier;

public class EnderiteTools {

    public static Supplier<Item.Settings> ENDERITE_TOOL_SETTINGS = () -> new Item.Settings().arch$tab(EnderiteMod.ENDERITE_TAB).fireproof();

    // Enderite Tools
    public static final RegistrySupplier<Item> ENDERITE_PICKAXE = EnderiteMod.ITEMS.register("enderite_pickaxe",
        () -> new PickaxeItem(EnderiteMaterial.ENDERITE,
            ENDERITE_TOOL_SETTINGS.get().attributeModifiers(
                    PickaxeItem.createAttributeModifiers(EnderiteMaterial.ENDERITE,
                            EnderiteMod.CONFIG.tools.enderitePickaxeAD, -2.8F)
            )));

    public static final RegistrySupplier<Item> ENDERITE_AXE = EnderiteMod.ITEMS.register("enderite_axe",
        () -> new AxeItem(EnderiteMaterial.ENDERITE,
            ENDERITE_TOOL_SETTINGS.get().attributeModifiers(
                    AxeItem.createAttributeModifiers(EnderiteMaterial.ENDERITE, EnderiteMod.CONFIG.tools.enderiteAxeAD,
                            -3.0F)
            )));

    public static final RegistrySupplier<Item> ENDERITE_HOE = EnderiteMod.ITEMS.register("enderite_hoe",
        () -> new HoeItem(EnderiteMaterial.ENDERITE,
            ENDERITE_TOOL_SETTINGS.get().attributeModifiers(
                    HoeItem.createAttributeModifiers(EnderiteMaterial.ENDERITE, EnderiteMod.CONFIG.tools.enderiteHoeAD,
                            0.0F)
            )));

    public static final RegistrySupplier<Item> ENDERITE_SHOVEL = EnderiteMod.ITEMS.register("enderite_shovel",
        () -> new ShovelItem(EnderiteMaterial.ENDERITE,
            ENDERITE_TOOL_SETTINGS.get().attributeModifiers(
                    ShovelItem.createAttributeModifiers(EnderiteMaterial.ENDERITE, EnderiteMod.CONFIG.tools.enderiteShovelAD, -3.0F)
            )));

    public static final RegistrySupplier<Item> ENDERITE_SWORD = EnderiteMod.ITEMS.register("enderite_sword",
        () -> new EnderiteSword(EnderiteMaterial.ENDERITE,
            EnderiteMod.CONFIG.tools.enderiteSwordAD, -2.4F,
            ENDERITE_TOOL_SETTINGS.get()));

    // MOST IMPORTANT
    public static final RegistrySupplier<Item> ENDERITE_SHEAR = EnderiteMod.ITEMS.register("enderite_shears",
            () -> new EnderiteShears(
                    ENDERITE_TOOL_SETTINGS.get()
                            .maxCount(1)
                            .maxDamage(2048)
                            .rarity(Rarity.RARE)
                            .component(DataComponentTypes.TOOL, ShearsItem.createToolComponent())));

    // Bows
    public static final RegistrySupplier<Item> ENDERITE_CROSSBOW = EnderiteMod.ITEMS.register("enderite_crossbow",
        () -> new EnderiteCrossbow(
            ENDERITE_TOOL_SETTINGS.get().maxCount(1).maxDamage(768).component(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)));

    public static final RegistrySupplier<Item> ENDERITE_BOW = EnderiteMod.ITEMS.register("enderite_bow", () -> new EnderiteBow(
        ENDERITE_TOOL_SETTINGS.get().maxCount(1).maxDamage(768)));

    // Shield
    public static final Item.Settings ENDERITE_SHIELD_ITEM_SETTINGS = ENDERITE_TOOL_SETTINGS.get().maxCount(1).maxDamage(768);

    public static RegistrySupplier<Item> ENDERITE_SHIELD;

    public static RegistrySupplier<RecipeSerializer<?>> ENDERITE_SHIELD_DECORATION_RECIPE = EnderiteMod.RECIPES
        .register("crafting_special_enderiteshielddecoration",
            () -> new SpecialRecipeSerializer<EnderiteShieldDecorationRecipe>(
                EnderiteShieldDecorationRecipe::new));
}
