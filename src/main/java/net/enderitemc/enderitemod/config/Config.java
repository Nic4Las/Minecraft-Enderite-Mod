package net.enderitemc.enderitemod.config;

public class Config {

    public General general = new General();
    public Tools tools = new Tools();
    public WorldGeneration worldGeneration = new WorldGeneration();

    public static class General {
        public boolean allowVoidFloatingEnchantment = true;
    }

    public static class Tools {
        public int enderitePickaxeAD = 4;
        public int enderiteAxeAD = 8;
        public int enderiteHoeAD = -2;
        public float enderiteShovelAD = 4.5f;
        public int enderiteSwordAD = 6;

        public float enderiteBowAD = 2.5f;
        public float enderiteBowArrowSpeed = 3.5f;
        public float enderiteCrossbowAD = 3.0f;
        public float enderiteCrossbowArrowSpeed = 3.65f;
    }

    public static class WorldGeneration {
        public EnderiteOre enderiteOre = new EnderiteOre();

        public static class EnderiteOre {
            public int veinSize = 3;
            public int veinAmount = 3;
        }
    }

}