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
    public static final String SUBCATEGORY_ENDERITE_GENERATION = "enderite_generation";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.IntValue ENDERITE_COUNT;
    public static ForgeConfigSpec.IntValue ENDERITE_BOTTOM_OFFSET;
    public static ForgeConfigSpec.IntValue ENDERITE_TOP_OFFSET;
    public static ForgeConfigSpec.IntValue ENDERITE_MAXIMUM;

    private static final Logger LOGGER = LogManager.getLogger();

    static {
        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        //currently unused, but general settings config goes in here
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("World-generation settings").push(CATEGORY_WORLD_GENERATION);
        setupEnderiteGeneration();
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();

    }

    private static void setupEnderiteGeneration() {
        COMMON_BUILDER.comment("Enderite settings").push(SUBCATEGORY_ENDERITE_GENERATION);
        ENDERITE_COUNT = COMMON_BUILDER.comment("How many enderite ore should be generated per chunk (default: 3)").defineInRange("count",3,1,255);
        LOGGER.info("Enderite Count is set to "+ENDERITE_COUNT);
        ENDERITE_BOTTOM_OFFSET = COMMON_BUILDER.comment("Bottom offset for enderite ore generation, e.g. if you choose this value as '12' (default value), ore will only be generated above y = 12").defineInRange("bottom_offset",12,0,255);
        LOGGER.info("Enderite bottom offset is set to "+ENDERITE_BOTTOM_OFFSET);
        ENDERITE_TOP_OFFSET = COMMON_BUILDER.comment("Top offset for enderite ore generation, based on maximum height, e.g. if you choose this value as '12' (default value), ore will only be generated below y = maximum - 12").defineInRange("top_offset",12,0,255);
        LOGGER.info("Enderite top offset set to "+ENDERITE_TOP_OFFSET);
        ENDERITE_MAXIMUM = COMMON_BUILDER.comment("Maximum height for enderite ore generation, has to be greater than 'top_offset', default value is '48'").defineInRange("maximum", 48,1,255);
        LOGGER.info("Enderite maximum is set to "+ENDERITE_MAXIMUM);
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
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
