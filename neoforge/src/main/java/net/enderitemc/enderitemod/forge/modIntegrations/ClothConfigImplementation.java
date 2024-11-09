package net.enderitemc.enderitemod.forge.modIntegrations;

import com.misterpemodder.shulkerboxtooltip.api.neoforge.ShulkerBoxTooltipPlugin;
import net.enderitemc.enderitemod.config.ClothConfig;
import net.enderitemc.enderitemod.modIntegrations.ShulkerBoxTooltipApiImplementation;
import net.minecraft.client.gui.screen.Screen;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ClothConfigImplementation {

    public static void registerEntryPoint(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class,
            () -> new IConfigScreenFactory() {
                @Override
                public Screen createScreen(ModContainer modContainer, Screen screen) {
                    return new ClothConfig(screen).getScreen();
                }
            });
    }
}
