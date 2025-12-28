package net.enderitemc.enderitemod.tools;

import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.component.EnderiteChargeComponent;
import net.enderitemc.enderitemod.component.EnderiteDataComponents;
import net.enderitemc.enderitemod.component.EnderiteTooltipComponent;
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.enderitemc.enderitemod.misc.EnderiteShieldDecorationRecipe;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialCraftingRecipe.SpecialRecipeSerializer;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static net.enderitemc.enderitemod.EnderiteMod.BASE_ENDERITE_ITEM_SETTINGS;
import static net.enderitemc.enderitemod.EnderiteMod.getItemSettings;

public class EnderiteTools {

    // Enderite Tools
    public static final RegistrySupplier<Item> ENDERITE_PICKAXE = EnderiteMod.ITEMS.register("enderite_pickaxe",
        () -> new Item(
            getItemSettings("enderite_pickaxe", BASE_ENDERITE_ITEM_SETTINGS.get())
                .pickaxe(EnderiteMaterial.ENDERITE,
                    EnderiteMod.CONFIG.tools.enderitePickaxeAD - 3, // TODO: change in next version
                    -2.8F)));

    public static final RegistrySupplier<Item> ENDERITE_AXE = EnderiteMod.ITEMS.register("enderite_axe",
        () -> new AxeItem(EnderiteMaterial.ENDERITE,
            EnderiteMod.CONFIG.tools.enderiteAxeAD - 3, // TODO: change in next version
            -3.0F,
            getItemSettings("enderite_axe", BASE_ENDERITE_ITEM_SETTINGS.get()))
    );

    public static final RegistrySupplier<Item> ENDERITE_HOE = EnderiteMod.ITEMS.register("enderite_hoe",
        () -> new HoeItem(EnderiteMaterial.ENDERITE,
            EnderiteMod.CONFIG.tools.enderiteHoeAD - 3, // TODO: change in next version
            0.0F,
            getItemSettings("enderite_hoe", BASE_ENDERITE_ITEM_SETTINGS.get()))
    );

    public static final RegistrySupplier<Item> ENDERITE_SHOVEL = EnderiteMod.ITEMS.register("enderite_shovel",
        () -> new ShovelItem(EnderiteMaterial.ENDERITE,
            EnderiteMod.CONFIG.tools.enderiteShovelAD - 3, // TODO: change in next version
            -3.0F,
            getItemSettings("enderite_shovel", BASE_ENDERITE_ITEM_SETTINGS.get()))
    );

    public static final RegistrySupplier<Item> ENDERITE_SWORD = EnderiteMod.ITEMS.register("enderite_sword",
        () -> new EnderiteSword(EnderiteMaterial.ENDERITE,
            EnderiteMod.CONFIG.tools.enderiteSwordAD - 3, // TODO: change in next version
            -2.4F,
            getItemSettings("enderite_sword", BASE_ENDERITE_ITEM_SETTINGS.get())
                .component(EnderiteDataComponents.TELEPORT_CHARGE.get(), EnderiteChargeComponent.of(0))
                .component(EnderiteDataComponents.ENDERITE_TOOLTIP.get(), EnderiteTooltipComponent.ofSword())));

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
        .repairable(EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT)
        .component(DataComponentTypes.BLOCKS_ATTACKS,
            new BlocksAttacksComponent(
                0.25F,
                1.0F,
                List.of(new BlocksAttacksComponent.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                new BlocksAttacksComponent.ItemDamage(3.0F, 1.0F, 1.0F),
                Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                Optional.of(SoundEvents.ITEM_SHIELD_BLOCK),
                Optional.of(SoundEvents.ITEM_SHIELD_BREAK)
            )
        )
        .component(EnderiteDataComponents.TELEPORT_CHARGE.get(), EnderiteChargeComponent.of(0))
        .component(EnderiteDataComponents.ENDERITE_TOOLTIP.get(), EnderiteTooltipComponent.ofShield())
        .component(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT.with(EnderiteDataComponents.ENDERITE_TOOLTIP.get(), false));

    public static final RegistrySupplier<Item> ENDERITE_SHIELD = EnderiteMod.ITEMS.register("enderite_shield",
        () -> new EnderiteShield(
            EnderiteMod.getItemSettings("enderite_shield", ENDERITE_SHIELD_ITEM_SETTINGS.get())));

    public static RegistrySupplier<RecipeSerializer<? extends SpecialCraftingRecipe>> ENDERITE_SHIELD_DECORATION_RECIPE = EnderiteMod.RECIPES
        .register("crafting_special_enderiteshielddecoration",
            () -> new SpecialRecipeSerializer<EnderiteShieldDecorationRecipe>(
                EnderiteShieldDecorationRecipe::new));

    // Spear
    public static final RegistrySupplier<Item> ENDERITE_SPEAR = EnderiteMod.ITEMS.register("enderite_spear",
        () -> new Item(
            getItemSettings("enderite_spear", BASE_ENDERITE_ITEM_SETTINGS.get())
                .maxCount(1)
                .spear(EnderiteMaterial.ENDERITE,
                    EnderiteMod.CONFIG.tools.spear.swingAnimationSeconds, // 1.25F,
                    EnderiteMod.CONFIG.tools.spear.chargeDamageMultiplier, // 1.375F,
                    EnderiteMod.CONFIG.tools.spear.chargeDelaySeconds, // 0.3F,
                    EnderiteMod.CONFIG.tools.spear.maxDurationForDismountSeconds, // 2.0F,
                    EnderiteMod.CONFIG.tools.spear.minSpeedForDismount, // 6.5F,
                    EnderiteMod.CONFIG.tools.spear.maxDurationForDismountSeconds * 2.25F, // 4.5F
                    5.1F,
                    EnderiteMod.CONFIG.tools.spear.maxDurationForDismountSeconds * 3.75F, // 7.5F
                    4.6F)));

    public static void init() {
        if (ENDERITE_SHIELD == null) {
            System.out.println("EnderiteMod: Tools not registered!");
        }
    }
}
