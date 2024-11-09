package net.enderitemc.enderitemod.fabriclike.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.enderitemc.enderitemod.config.ClothConfig;

public class EnderiteModModMenuIntegration implements ModMenuApi {
    // @Override
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