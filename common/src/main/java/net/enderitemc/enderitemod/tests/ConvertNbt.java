package net.enderitemc.enderitemod.tests;

import net.minecraft.data.DataOutput;
import net.minecraft.data.DataWriter;
import net.minecraft.data.SnbtProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ConvertNbt {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) throw new IllegalArgumentException();
        ConvertNbt.convertSnbt2Nbt(List.of(args[0]), args[1]);
    }

    public static void convertSnbt2Nbt(List<String> input, String output) throws IOException {
        // Instantiate SnbtProvider
        List<Path> in_paths = input.stream().map(Path::of).toList();
        Path out_path = Path.of(output);
        SnbtProvider provider = new SnbtProvider(
            new DataOutput(out_path), in_paths);

        // Rune conversion
        provider.run(DataWriter.UNCACHED);
    }
}
