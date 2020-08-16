package net.enderitemc.enderitemod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import net.enderitemc.enderitemod.EnderiteMod;
import net.fabricmc.loader.api.FabricLoader;

public class ConfigLoader {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILENAME = "enderitemod.json";
    private static File file;

    public static void load() {
        File file = getFile();
        if (!file.exists()) {
            EnderiteMod.CONFIG = new Config();
            save();
        } else {
            read();
            save();
        }
    }

    private static void read() {
        EnderiteMod.CONFIG = get();
    }

    public static void save() {
        File file = getFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(GSON.toJson(EnderiteMod.CONFIG));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void set(Config config) {
        EnderiteMod.CONFIG = config;
        save();
    }

    public static Config get() {
        File file = getFile();
        try (FileReader reader = new FileReader(file)) {
            return GSON.fromJson(reader, Config.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new Config();
        }
    }

    private static File getFile() {
        if (file == null) {
            file = new File(FabricLoader.getInstance().getConfigDir().toFile(), FILENAME);
        }
        return file;
    }

}