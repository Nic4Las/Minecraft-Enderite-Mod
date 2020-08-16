package net.enderitemc.enderitemod.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class EnderiteModModMenuIntegration implements ModMenuApi {
    @Override
    public String getModId() {
        return "enderitemod"; // Return your modid here
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            return new ClothConfig(parent).getScreen();// Return the screen here with the one you created from Cloth
                                                       // Config
            // Builder

        };
    }
}