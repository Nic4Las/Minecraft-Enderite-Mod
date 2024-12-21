package net.enderitemc.enderitemod.tools;

import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.enderitemc.enderitemod.misc.EnderiteShieldDecorationRecipe;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialCraftingRecipe.SpecialRecipeSerializer;
import net.minecraft.util.Rarity;

import java.util.function.Supplier;

import static net.enderitemc.enderitemod.EnderiteMod.BASE_ENDERITE_ITEM_SETTINGS;
import static net.enderitemc.enderitemod.EnderiteMod.getItemSettings;

public class EnderiteTools {

    // Enderite Tools
    public static final RegistrySupplier<Item> ENDERITE_PICKAXE = EnderiteMod.ITEMS.register("enderite_pickaxe",
        () -> new PickaxeItem(EnderiteMaterial.ENDERITE,
            EnderiteMod.CONFIG.tools.enderitePickaxeAD,
            -2.8F,
            getItemSettings("enderite_pickaxe", BASE_ENDERITE_ITEM_SETTINGS.get())));

    public static final RegistrySupplier<Item> ENDERITE_AXE = EnderiteMod.ITEMS.register("enderite_axe",
        () -> new AxeItem(EnderiteMaterial.ENDERITE,
            EnderiteMod.CONFIG.tools.enderiteAxeAD,
            -3.0F,
            getItemSettings("enderite_axe", BASE_ENDERITE_ITEM_SETTINGS.get()))
    );

    public static final RegistrySupplier<Item> ENDERITE_HOE = EnderiteMod.ITEMS.register("enderite_hoe",
        () -> new HoeItem(EnderiteMaterial.ENDERITE,
            EnderiteMod.CONFIG.tools.enderiteHoeAD,
            0.0F,
            getItemSettings("enderite_hoe", BASE_ENDERITE_ITEM_SETTINGS.get()))
    );

    public static final RegistrySupplier<Item> ENDERITE_SHOVEL = EnderiteMod.ITEMS.register("enderite_shovel",
        () -> new ShovelItem(EnderiteMaterial.ENDERITE,
            EnderiteMod.CONFIG.tools.enderiteShovelAD,
            -3.0F,
            getItemSettings("enderite_shovel", BASE_ENDERITE_ITEM_SETTINGS.get()))
    );

    public static final RegistrySupplier<Item> ENDERITE_SWORD = EnderiteMod.ITEMS.register("enderite_sword",
        () -> new EnderiteSword(EnderiteMaterial.ENDERITE,
            EnderiteMod.CONFIG.tools.enderiteSwordAD,
            -2.4F,
            getItemSettings("enderite_sword", BASE_ENDERITE_ITEM_SETTINGS.get())));

    // MOST IMPORTANT
    public static final RegistrySupplier<Item> ENDERITE_SHEAR = EnderiteMod.ITEMS.register("enderite_shears",
        () -> new EnderiteShears(
            getItemSettings("enderite_shears", BASE_ENDERITE_ITEM_SETTINGS.get())
                .maxCount(1)
                .maxDamage(2048)
                .rarity(Rarity.RARE)
                .enchantable(EnderiteMaterial.ENDERITE.enchantmentValue())
                .repairable(EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT)
                .component(DataComponentTypes.TOOL, ShearsItem.createToolComponent())));

    // Bows
    public static final RegistrySupplier<Item> ENDERITE_CROSSBOW = EnderiteMod.ITEMS.register("enderite_crossbow",
        () -> new EnderiteCrossbow(
            getItemSettings("enderite_crossbow", BASE_ENDERITE_ITEM_SETTINGS.get())
                .maxCount(1)
                .maxDamage(768)
                .enchantable(EnderiteMaterial.ENDERITE.enchantmentValue())
                .repairable(EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT)
                .component(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)));

    public static final RegistrySupplier<Item> ENDERITE_BOW = EnderiteMod.ITEMS.register("enderite_bow",
        () -> new EnderiteBow(
            getItemSettings("enderite_bow", BASE_ENDERITE_ITEM_SETTINGS.get())
                .maxCount(1)
                .maxDamage(768)
                .enchantable(EnderiteMaterial.ENDERITE.enchantmentValue())
                .repairable(EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT)));

    // Shield
    public static final Supplier<Item.Settings> ENDERITE_SHIELD_ITEM_SETTINGS = () -> BASE_ENDERITE_ITEM_SETTINGS.get()
        .maxCount(1)
        .maxDamage(768)
        .enchantable(EnderiteMaterial.ENDERITE.enchantmentValue())
        .repairable(EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT);

    public static RegistrySupplier<Item> ENDERITE_SHIELD;

    public static RegistrySupplier<RecipeSerializer<? extends SpecialCraftingRecipe>> ENDERITE_SHIELD_DECORATION_RECIPE = EnderiteMod.RECIPES
        .register("crafting_special_enderiteshielddecoration",
            () -> new SpecialRecipeSerializer<EnderiteShieldDecorationRecipe>(
                EnderiteShieldDecorationRecipe::new));
}
