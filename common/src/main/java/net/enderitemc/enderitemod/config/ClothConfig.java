package net.enderitemc.enderitemod.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class ClothConfig {
    public static final int WARN_COLOR = 0xFFFF1100;

    public final Screen screen;
    Config DEFAULT = new Config();

    public ClothConfig(Screen parent) {

        //REMOVE screen=null; //Update to 1.19.4 level (remove ore config)
        // screen = null;


        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent)
            .setTitle(Text.translatable("title.enderitemod.config"));

        Config currentConfig = ConfigLoader.get();

        builder.setSavingRunnable(() -> {
            ConfigLoader.set(currentConfig);
            // Serialise the config into the config file. This will be called last after all
            // variables are updated.
        });

        ConfigCategory general = builder
            .getOrCreateCategory(Text.translatable("category.enderitemod.general"));
        ConfigCategory restart = builder
            .getOrCreateCategory(Text.translatable("category.enderitemod.restart"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Void floating enchantment
        general.addEntry(entryBuilder
            .startTextDescription(Text.translatable(
                "option.enderitemod.general.allow_void_floating_enchantment.moved_to_json"))
            .setColor(WARN_COLOR)
            .setTooltip(Text.translatable(
                    "option.enderitemod.general.allow_void_floating_enchantment.moved_to_json.hover")
                .append("\n- enderitemod/enchantment/void_floating.json")
                .append("\n- minecraft/tags/enchantment/non_treasure.json"))
            .build());

        // BOW
        ArrayList<AbstractConfigListEntry> list_bc = new ArrayList<>();

        list_bc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.general.enderite_bow.attack_damage"),
                currentConfig.tools.enderiteBowAD)
            .setDefaultValue(DEFAULT.tools.enderiteBowAD).setMin(1.0f).setMax(16.0f)
            .setTooltip(Text.translatable(
                "option.enderitemod.general.enderite_bow.attack_damage.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderiteBowAD = newValue).build());
        list_bc.add(entryBuilder
            .startFloatField(
                Text.translatable(
                    "option.enderitemod.general.enderite_bow.arrow_speed"),
                currentConfig.tools.enderiteBowArrowSpeed)
            .setDefaultValue(DEFAULT.tools.enderiteBowArrowSpeed).setMin(1.0f).setMax(8.0f)
            .setTooltip(Text.translatable(
                "option.enderitemod.general.enderite_bow.arrow_speed.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderiteBowArrowSpeed = newValue)
            .build());
        list_bc.add(entryBuilder
            .startFloatField(
                Text.translatable(
                    "option.enderitemod.general.enderite_bow.charge_time"),
                currentConfig.tools.enderiteBowChargeTime)
            .setDefaultValue(DEFAULT.tools.enderiteBowChargeTime).setMin(1.0f).setMax(100.0f)
            .setTooltip(Text.translatable(
                "option.enderitemod.general.enderite_bow.charge_time.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderiteBowChargeTime = newValue)
            .build());
        list_bc.add(entryBuilder.startBooleanToggle(
                Text.translatable(
                    "option.enderitemod.general.enderite_bow.needs_arrow"),
                currentConfig.tools.enderiteBowNeedsArrow)
            .setDefaultValue(DEFAULT.tools.enderiteBowNeedsArrow)
            .setTooltip(Text.translatable(
                "option.enderitemod.general.enderite_bow.needs_arrow.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderiteBowNeedsArrow = newValue)
            .build());
        list_bc.add(entryBuilder.startBooleanToggle(
                Text.translatable("option.enderitemod.general.enderite_bow.infinity_needs_arrow"),
                currentConfig.tools.enderiteBowWithInfinityNeedsArrow)
            .setDefaultValue(DEFAULT.tools.enderiteBowWithInfinityNeedsArrow)
            .setTooltip(Text.translatable(
                "option.enderitemod.general.enderite_bow.infinity_needs_arrow.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderiteBowWithInfinityNeedsArrow = newValue)
            .build());

        general.addEntry(entryBuilder
            .startSubCategory(
                Text.translatable("option.enderitemod.general.enderite_bow.config"),
                list_bc)
            .build());

        // Crossbow
        ArrayList<AbstractConfigListEntry> list_cbc = new ArrayList<>();

        list_cbc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.general.enderite_crossbow.attack_damage"),
                currentConfig.tools.enderiteCrossbowAD)
            .setDefaultValue(DEFAULT.tools.enderiteCrossbowAD).setMin(1.0f).setMax(16.0f)
            .setTooltip(Text.translatable(
                "option.enderitemod.general.enderite_crossbow.attack_damage.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderiteCrossbowAD = newValue)
            .build());
        list_cbc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.general.enderite_crossbow.arrow_speed"),
                currentConfig.tools.enderiteCrossbowArrowSpeed)
            .setDefaultValue(DEFAULT.tools.enderiteCrossbowArrowSpeed).setMin(1.0f).setMax(8.0f)
            .setTooltip(Text.translatable(
                "option.enderitemod.general.enderite_crossbow.arrow_speed.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderiteCrossbowArrowSpeed = newValue)
            .build());
        list_cbc.add(entryBuilder
            .startFloatField(
                Text.translatable(
                    "option.enderitemod.general.enderite_crossbow.charge_time"),
                currentConfig.tools.enderiteCrossBowChargeTime)
            .setDefaultValue(DEFAULT.tools.enderiteCrossBowChargeTime).setMin(1.0f).setMax(100.0f)
            .setTooltip(Text.translatable(
                "option.enderitemod.general.enderite_crossbow.charge_time.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderiteCrossBowChargeTime = newValue)
            .build());

        general.addEntry(entryBuilder
            .startSubCategory(
                Text.translatable("option.enderitemod.general.enderite_crossbow.config"),
                list_cbc)
            .build());

        general.addEntry(entryBuilder.startIntField(
            Text.translatable("option.enderitemod.general.enderite_sword_teleport_distance"),
            currentConfig.tools.enderiteSwordTeleportDistance)
            .setDefaultValue(DEFAULT.tools.enderiteSwordTeleportDistance)
            .setTooltip(Text.translatable("option.enderitemod.general.enderite_sword_teleport_distance.hover"))
                .setSaveConsumer(newValue -> currentConfig.tools.enderiteSwordTeleportDistance = newValue)
            .setMin(0).setMax(256)
            .build()
        );

        // RESTART
        // Upgrade Template
        restart.addEntry(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_upgrade_template_frequency"),
                currentConfig.general.enderiteUpgradeTemplateChance)
            .setDefaultValue(DEFAULT.general.enderiteUpgradeTemplateChance).setMin(0.0f).setMax(1.0f)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_upgrade_template_frequency.hover"))
            .setSaveConsumer(newValue -> currentConfig.general.enderiteUpgradeTemplateChance = newValue)
            .requireRestart()
            .build());

        // ORE
        restart.addEntry(entryBuilder
            .startTextDescription(Text.translatable(
                "option.enderitemod.restart.enderite_ore.moved_to_json"))
            .setColor(WARN_COLOR)
            .setTooltip(Text.translatable(
                    "option.enderitemod.restart.enderite_ore.moved_to_json.hover")
                .append("\n- enderitemod/worldgen/configured_feature/ore_enderite_large.json")
                .append("\n- enderitemod/worldgen/configured_feature/ore_enderite_small.json")
                .append("\n- enderitemod/worldgen/placed_feature/ore_enderite_large.json")
                .append("\n- enderitemod/worldgen/placed_feature/ore_enderite_small.json"))
            .build());

        // restart.addEntry(entryBuilder
        // .startIntField(Text.translatable(
        // "option.enderitemod.restart.enderite_ore.vein_size"),
        // currentConfig.worldGeneration.enderiteOre.veinSize)
        // .setDefaultValue(DEFAULT.worldGeneration.enderiteOre.veinSize).setMin(2).setMax(16)
        // .setTooltip(Text.translatable(
        // "option.enderitemod.restart.enderite_ore.vein_size.hover"))
        // .setSaveConsumer(
        // newValue -> currentConfig.worldGeneration.enderiteOre.veinSize = newValue)
        //
        //.build());

        // restart.addEntry(entryBuilder
        // .startIntField(Text.translatable(
        // "option.enderitemod.restart.enderite_ore.vein_amount"),
        // currentConfig.worldGeneration.enderiteOre.veinAmount)
        // .setDefaultValue(DEFAULT.worldGeneration.enderiteOre.veinAmount).setMin(2).setMax(16)
        // .setTooltip(Text.translatable(
        // "option.enderitemod.restart.enderite_ore.vein_amount.hover"))
        // .setSaveConsumer(
        // newValue -> currentConfig.worldGeneration.enderiteOre.veinAmount = newValue)
        //
        //.build());

        // TOOLS
        ArrayList<AbstractConfigListEntry> list_tdc = new ArrayList<>();

        list_tdc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_tools.attack_damage_bonus"),
                currentConfig.tools.attackDamageBonus)
            .setDefaultValue(DEFAULT.tools.attackDamageBonus).setMin(1)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_tools.attack_damage_bonus.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.attackDamageBonus = newValue)
            .requireRestart()
            .build());

        list_tdc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_sword.attack_damage"),
                currentConfig.tools.enderiteSwordAD + (currentConfig.tools.attackDamageBonus - 2)) // TODO: change -2 in next version, see EnderiteTools
            .setDefaultValue(DEFAULT.tools.enderiteSwordAD + (DEFAULT.tools.attackDamageBonus - 2)).setMin(1).setMax(16)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_sword.attack_damage.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderiteSwordAD = newValue - (currentConfig.tools.attackDamageBonus - 2))
            .requireRestart()
            .build());

        list_tdc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_pickaxe.attack_damage"),
                currentConfig.tools.enderitePickaxeAD + (currentConfig.tools.attackDamageBonus - 2))
            .setDefaultValue(DEFAULT.tools.enderitePickaxeAD + (DEFAULT.tools.attackDamageBonus - 2)).setMin(1).setMax(16)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_pickaxe.attack_damage.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderitePickaxeAD = newValue - (currentConfig.tools.attackDamageBonus - 2))
            .requireRestart()
            .build());

        list_tdc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_axe.attack_damage"),
                currentConfig.tools.enderiteAxeAD + (currentConfig.tools.attackDamageBonus - 2))
            .setDefaultValue(DEFAULT.tools.enderiteAxeAD + (DEFAULT.tools.attackDamageBonus - 2)).setMin(1).setMax(16)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_axe.attack_damage.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderiteAxeAD = newValue - (currentConfig.tools.attackDamageBonus - 2))
            .requireRestart()
            .build());

        list_tdc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_shovel.attack_damage"),
                currentConfig.tools.enderiteShovelAD + (currentConfig.tools.attackDamageBonus - 2))
            .setDefaultValue(DEFAULT.tools.enderiteShovelAD + (DEFAULT.tools.attackDamageBonus - 2)).setMin(1).setMax(16)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_shovel.attack_damage.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderiteShovelAD = newValue - (currentConfig.tools.attackDamageBonus - 2))

            .requireRestart()
            .build());

        list_tdc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_hoe.attack_damage"),
                currentConfig.tools.enderiteHoeAD + (currentConfig.tools.attackDamageBonus - 2))
            .setDefaultValue(DEFAULT.tools.enderiteHoeAD + (DEFAULT.tools.attackDamageBonus - 2)).setMin(1).setMax(16)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_hoe.attack_damage.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.enderiteHoeAD = newValue - (currentConfig.tools.attackDamageBonus - 2))
            .requireRestart()
            .build());

        restart.addEntry(entryBuilder
            .startSubCategory(
                Text.translatable("option.enderitemod.restart.tools.damage_config"),
                list_tdc)
            .build());

        // Spear
        ArrayList<AbstractConfigListEntry> list_tsc = new ArrayList<>();

        list_tsc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_spear.swing_animation_seconds"),
                currentConfig.tools.spear.swingAnimationSeconds)
            .setDefaultValue(DEFAULT.tools.spear.swingAnimationSeconds).setMin(0.1f).setMax(4)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_spear.swing_animation_seconds.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.spear.swingAnimationSeconds = newValue)
            .requireRestart()
            .build());

        list_tsc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_spear.charge_damage_multiplier"),
                currentConfig.tools.spear.chargeDamageMultiplier)
            .setDefaultValue(DEFAULT.tools.spear.chargeDamageMultiplier).setMin(0.1f).setMax(4)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_spear.charge_damage_multiplier.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.spear.chargeDamageMultiplier = newValue)
            .requireRestart()
            .build());

        list_tsc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_spear.charge_delay_seconds"),
                currentConfig.tools.spear.chargeDelaySeconds)
            .setDefaultValue(DEFAULT.tools.spear.chargeDelaySeconds).setMin(0.1f).setMax(1)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_spear.charge_delay_seconds.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.spear.chargeDelaySeconds = newValue)
            .requireRestart()
            .build());

        list_tsc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_spear.max_duration_for_dismount"),
                currentConfig.tools.spear.maxDurationForDismountSeconds)
            .setDefaultValue(DEFAULT.tools.spear.maxDurationForDismountSeconds).setMin(0.1f).setMax(16)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_spear.max_duration_for_dismount.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.spear.maxDurationForDismountSeconds = newValue)
            .requireRestart()
            .build());

        list_tsc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_spear.min_speed_for_dismount"),
                currentConfig.tools.spear.minSpeedForDismount)
            .setDefaultValue(DEFAULT.tools.spear.minSpeedForDismount).setMin(0.1f).setMax(16)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_spear.min_speed_for_dismount.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.spear.minSpeedForDismount = newValue)
            .requireRestart()
            .build());

        restart.addEntry(entryBuilder
            .startSubCategory(
                Text.translatable("option.enderitemod.restart.tools.spear_config"),
                list_tsc)
            .build());

        // Tools general
        ArrayList<AbstractConfigListEntry> list_tgc = new ArrayList<>();

        list_tgc.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.enderite_tools.mining_speed"),
                currentConfig.tools.miningSpeed)
            .setDefaultValue(DEFAULT.tools.miningSpeed).setMin(1)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_tools.mining_speed.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.miningSpeed = newValue)
            .requireRestart()
            .build());

        list_tgc.add(entryBuilder
            .startIntField(Text.translatable(
                    "option.enderitemod.restart.enderite_tools.durability"),
                currentConfig.tools.durability)
            .setDefaultValue(DEFAULT.tools.durability).setMin(1)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_tools.durability.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.durability = newValue)
            .requireRestart()
            .build());

        list_tgc.add(entryBuilder
            .startIntField(Text.translatable(
                    "option.enderitemod.restart.enderite_tools.max_teleport_charge"),
                currentConfig.tools.maxTeleportCharge)
            .setDefaultValue(DEFAULT.tools.maxTeleportCharge).setMin(0)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_tools.max_teleport_charge.hover"))
            .setSaveConsumer(newValue -> currentConfig.tools.maxTeleportCharge = newValue)
            .requireRestart()
            .build());

        restart.addEntry(entryBuilder
            .startSubCategory(
                Text.translatable("option.enderitemod.restart.tools.general_config"),
                list_tgc)
            .build());


        // ARMOR
        ArrayList<AbstractConfigListEntry> list_ac = new ArrayList<>();

        list_ac.add(entryBuilder
            .startIntField(Text.translatable(
                    "option.enderitemod.restart.enderite_helmet.protection"),
                currentConfig.armor.helmetProtection)
            .setDefaultValue(DEFAULT.armor.helmetProtection).setMin(1).setMax(16)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_helmet.protection.hover"))
            .setSaveConsumer(newValue -> currentConfig.armor.helmetProtection = newValue)
            .requireRestart()
            .build());

        list_ac.add(entryBuilder
            .startIntField(Text.translatable(
                    "option.enderitemod.restart.enderite_chestplate.protection"),
                currentConfig.armor.chestplateProtection)
            .setDefaultValue(DEFAULT.armor.chestplateProtection).setMin(1).setMax(16)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_chestplate.protection.hover"))
            .setSaveConsumer(newValue -> currentConfig.armor.chestplateProtection = newValue)
            .requireRestart()
            .build());

        list_ac.add(entryBuilder
            .startIntField(Text.translatable(
                    "option.enderitemod.restart.enderite_leggings.protection"),
                currentConfig.armor.leggingsProtection)
            .setDefaultValue(DEFAULT.armor.leggingsProtection).setMin(1).setMax(16)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_leggings.protection.hover"))
            .setSaveConsumer(newValue -> currentConfig.armor.leggingsProtection = newValue)
            .requireRestart()
            .build());

        list_ac.add(entryBuilder
            .startIntField(Text.translatable(
                    "option.enderitemod.restart.enderite_boots.protection"),
                currentConfig.armor.bootsProtection)
            .setDefaultValue(DEFAULT.armor.bootsProtection).setMin(1).setMax(16)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_boots.protection.hover"))
            .setSaveConsumer(newValue -> currentConfig.armor.bootsProtection = newValue)
            .requireRestart()
            .build());

        list_ac.add(entryBuilder
            .startIntField(Text.translatable(
                    "option.enderitemod.restart.enderite_body.protection"),
                currentConfig.armor.bodyProtection)
            .setDefaultValue(DEFAULT.armor.bodyProtection).setMin(1).setMax(99)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.enderite_body.protection.hover"))
            .setSaveConsumer(newValue -> currentConfig.armor.bodyProtection = newValue)
            .requireRestart()
            .build());

        list_ac.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.armor.toughness"),
                currentConfig.armor.toughness)
            .setDefaultValue(DEFAULT.armor.toughness).setMin(0).setMax(16)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.armor.toughness.hover"))
            .setSaveConsumer(newValue -> currentConfig.armor.toughness = newValue)
            .requireRestart()
            .build());

        list_ac.add(entryBuilder
            .startFloatField(Text.translatable(
                    "option.enderitemod.restart.armor.knockbackResistance"),
                currentConfig.armor.knockbackResistance)
            .setDefaultValue(DEFAULT.armor.knockbackResistance).setMin(0).setMax(1)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.armor.knockbackResistance.hover"))
            .setSaveConsumer(newValue -> currentConfig.armor.knockbackResistance = newValue)
            .requireRestart()
            .build());

        list_ac.add(entryBuilder
            .startIntField(Text.translatable(
                    "option.enderitemod.restart.armor.durabilityMultiplier"),
                currentConfig.armor.durabilityMultiplier)
            .setDefaultValue(DEFAULT.armor.durabilityMultiplier).setMin(1).setMax(99)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.armor.durabilityMultiplier.hover"))
            .setSaveConsumer(newValue -> currentConfig.armor.durabilityMultiplier = newValue)
            .requireRestart()
            .build());

        list_ac.add(entryBuilder
            .startIntField(Text.translatable(
                    "option.enderitemod.restart.armor.enchantability"),
                currentConfig.armor.enchantability)
            .setDefaultValue(DEFAULT.armor.enchantability).setMin(1).setMax(99)
            .setTooltip(Text.translatable(
                "option.enderitemod.restart.armor.enchantability.hover"))
            .setSaveConsumer(newValue -> currentConfig.armor.enchantability = newValue)
            .requireRestart()
            .build());

        restart.addEntry(entryBuilder
            .startSubCategory(
                Text.translatable("option.enderitemod.restart.tools.armor_config"),
                list_ac)
            .build());

        this.screen = builder.build();

    }

    public Screen getScreen() {
        return screen;
    }
}