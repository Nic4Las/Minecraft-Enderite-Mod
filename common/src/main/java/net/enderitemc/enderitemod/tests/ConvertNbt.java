package net.enderitemc.enderitemod.tests;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.structures.SnbtToNbt;

public class ConvertNbt {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) throw new IllegalArgumentException();
        ConvertNbt.convertSnbt2Nbt(List.of(args[0]), args[1]);
    }

    public static void convertSnbt2Nbt(List<String> input, String output) throws IOException {
        // Instantiate SnbtProvider
        List<Path> in_paths = input.stream().map(Path::of).toList();
        Path out_path = Path.of(output);
        SnbtToNbt provider = new SnbtToNbt(
            new PackOutput(out_path), in_paths);

        // Rune conversion
        provider.run(CachedOutput.NO_CACHE);
    }
}
