package net.enderitemc.enderitemod.fabric;

import org.quiltmc.loader.api.QuiltLoader;

import net.enderitemc.enderitemod.ExampleExpectPlatform;

import java.nio.file.Path;

public class ExampleExpectPlatformImpl {
    /**
     * This is our actual method to
     * {@link ExampleExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return QuiltLoader.getConfigDir();
    }
}
