package net.enderitemc.enderitemod.tools;

import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.component.EnderiteChargeComponent;
import net.enderitemc.enderitemod.component.EnderiteDataComponents;
import net.enderitemc.enderitemod.component.EnderiteTooltipComponent;
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.enderitemc.enderitemod.misc.EnderiteElytraSpecialRecipe;
import net.enderitemc.enderitemod.misc.EnderiteShieldDecorationRecipe;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.network.codec.StreamCodec;
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
                .stacksTo(1)
                .durability(2048)
                .rarity(Rarity.RARE)
                .enchantable(EnderiteMaterial.ENDERITE.enchantmentValue())
                .repairable(EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT)
                .component(DataComponents.TOOL, ShearsItem.createToolProperties())));

    // Bows
    public static final RegistrySupplier<Item> ENDERITE_CROSSBOW = EnderiteMod.ITEMS.register("enderite_crossbow",
        () -> new EnderiteCrossbow(
            getItemSettings("enderite_crossbow", BASE_ENDERITE_ITEM_SETTINGS.get())
                .stacksTo(1)
                .durability(768)
                .enchantable(EnderiteMaterial.ENDERITE.enchantmentValue())
                .repairable(EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT)
                .component(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY)));

    public static final RegistrySupplier<Item> ENDERITE_BOW = EnderiteMod.ITEMS.register("enderite_bow",
        () -> new EnderiteBow(
            getItemSettings("enderite_bow", BASE_ENDERITE_ITEM_SETTINGS.get())
                .stacksTo(1)
                .durability(768)
                .enchantable(EnderiteMaterial.ENDERITE.enchantmentValue())
                .repairable(EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT)));

    // Shield
    public static final Supplier<Item.Properties> ENDERITE_SHIELD_ITEM_SETTINGS = () -> BASE_ENDERITE_ITEM_SETTINGS.get()
        .stacksTo(1)
        .durability(768)
        .enchantable(EnderiteMaterial.ENDERITE.enchantmentValue())
        .repairable(EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT)
        .component(DataComponents.BLOCKS_ATTACKS,
            new BlocksAttacks(
                0.25F,
                1.0F,
                List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                Optional.empty(),
                Optional.of(SoundEvents.SHIELD_BLOCK),
                Optional.of(SoundEvents.SHIELD_BREAK)
            )
        )
        .component(EnderiteDataComponents.TELEPORT_CHARGE.get(), EnderiteChargeComponent.of(0))
        .component(EnderiteDataComponents.ENDERITE_TOOLTIP.get(), EnderiteTooltipComponent.ofShield())
        .component(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(EnderiteDataComponents.ENDERITE_TOOLTIP.get(), false));

    public static final RegistrySupplier<Item> ENDERITE_SHIELD = EnderiteMod.ITEMS.register("enderite_shield",
        () -> new EnderiteShield(
            EnderiteMod.getItemSettings("enderite_shield", ENDERITE_SHIELD_ITEM_SETTINGS.get())));

    public static EnderiteShieldDecorationRecipe ENDERITE_SHIELD_DECORATION_RECIPE_INSTANCE = new EnderiteShieldDecorationRecipe();

    public static RegistrySupplier<RecipeSerializer<? extends CustomRecipe>> ENDERITE_SHIELD_DECORATION_RECIPE = EnderiteMod.RECIPES
        .register("crafting_special_enderiteshielddecoration",
            () -> new RecipeSerializer<>(
                com.mojang.serialization.MapCodec.unit(ENDERITE_SHIELD_DECORATION_RECIPE_INSTANCE),
                StreamCodec.unit(ENDERITE_SHIELD_DECORATION_RECIPE_INSTANCE)));

    // Spear
    public static final RegistrySupplier<Item> ENDERITE_SPEAR = EnderiteMod.ITEMS.register("enderite_spear",
        () -> new Item(
            getItemSettings("enderite_spear", BASE_ENDERITE_ITEM_SETTINGS.get())
                .stacksTo(1)
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
