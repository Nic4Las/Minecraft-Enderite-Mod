package net.enderitemc.enderitemod.init;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class EnderiteModConfig {
        public static final String CATEGORY_GENERAL = "general";
        public static final String CATEGORY_WORLD_GENERATION = "world_generation";
        public static final String CATEGORY_TOOL_SETTINGS = "tool_settings";
        public static final String SUBCATEGORY_ENDERITE_BOW = "enderite_bow";
        public static final String SUBCATEGORY_ENDERITE_GENERATION = "enderite_generation";

        private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        public static ForgeConfigSpec COMMON_CONFIG;
        public static ForgeConfigSpec CLIENT_CONFIG;

        public static ForgeConfigSpec.IntValue ENDERITE_COUNT;
        public static ForgeConfigSpec.IntValue ENDERITE_BOTTOM_OFFSET;
        public static ForgeConfigSpec.IntValue ENDERITE_TOP_OFFSET;
        public static ForgeConfigSpec.IntValue ENDERITE_MAXIMUM;

        public static ForgeConfigSpec.DoubleValue ENDERITE_BOW_BASE_DAMAGE;
        public static ForgeConfigSpec.DoubleValue ENDERITE_BOW_CHARGE_TIME;

        static {
                COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
                // currently unused, but general settings config goes in here
                COMMON_BUILDER.pop();

                COMMON_BUILDER.comment("World-generation settings").push(CATEGORY_WORLD_GENERATION);
                setupEnderiteGeneration();
                COMMON_BUILDER.pop();

                COMMON_BUILDER.comment("Settings for tools, affect the way these tools work")
                                .push(CATEGORY_TOOL_SETTINGS);
                setupToolSettings();
                COMMON_BUILDER.pop();

                COMMON_CONFIG = COMMON_BUILDER.build();
                CLIENT_CONFIG = CLIENT_BUILDER.build();

        }

        private static void setupEnderiteGeneration() {
                COMMON_BUILDER.comment("Enderite settings").push(SUBCATEGORY_ENDERITE_GENERATION);
                ENDERITE_COUNT = COMMON_BUILDER
                                .comment("\nHow many enderite ore should be generated per chunk (default: 3)")
                                .defineInRange("count", 3, 1, 255);
                ENDERITE_BOTTOM_OFFSET = COMMON_BUILDER.comment(
                                "\nBottom offset for enderite ore generation, e.g. if you choose this value as '12' (default value), ore will only be generated above y = 12")
                                .defineInRange("bottom_offset", 12, 0, 255);
                ENDERITE_TOP_OFFSET = COMMON_BUILDER.comment(
                                "\nTop offset for enderite ore generation, based on maximum height, e.g. if you choose this value as '12' (default value), ore will only be generated below y = maximum - 12")
                                .defineInRange("top_offset", 12, 0, 255);
                ENDERITE_MAXIMUM = COMMON_BUILDER.comment(
                                "\nMaximum height for enderite ore generation, has to be greater than 'top_offset', default value is '48'")
                                .defineInRange("maximum", 48, 1, 255);
        }

        private static void setupToolSettings() {
                COMMON_BUILDER.comment("Settings for the Enderite Bow, do only affect the bow and no other tools")
                                .push(SUBCATEGORY_ENDERITE_BOW);
                ENDERITE_BOW_BASE_DAMAGE = COMMON_BUILDER.comment(
                                "\nDetermines the base damage of the bow, i.e. how many damage you will cause with one single arrow (default: 1.5)")
                                .defineInRange("bow_base_damage", 1.5d, 0d, 255d); // I can't think of a useful
                                                                                   // maxValue...
                ENDERITE_BOW_CHARGE_TIME = COMMON_BUILDER.comment(
                                "\nDetermines the time you need to fully charge the Enderite Bow (charge time or draw speed), default value is '30.0', which is nearly the same as the vanilla bow")
                                .defineInRange("charge_time", 30.0d, 1.0d, 255d);
        }

        public static void loadConfig(ForgeConfigSpec spec, Path path) {
                final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave()
                                .writingMode(WritingMode.REPLACE).build();
                configData.load();
                spec.setConfig(configData);
        }

        @SubscribeEvent
        public static void onLoad(final ModConfig.Loading configEvent) {

        }

        @SubscribeEvent
        public static void onReload(final ModConfig.Reloading configEvent) {
        }
}
