package net.enderitemc.enderitemod.forge.modIntegrations;

import com.misterpemodder.shulkerboxtooltip.api.neoforge.ShulkerBoxTooltipPlugin;
import net.enderitemc.enderitemod.modIntegrations.ShulkerBoxTooltipApiImplementation;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class ShulkerBoxTooltipImplementation {

    public static void registerEntryPoint(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(ShulkerBoxTooltipPlugin.class,
            () -> new ShulkerBoxTooltipPlugin(
                ShulkerBoxTooltipApiImplementation::new));
    }
}
