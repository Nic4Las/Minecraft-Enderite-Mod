package net.enderitemc.enderitemod.config;

public class Config {

    public General general = new General();
    public Tools tools = new Tools();
    public WorldGeneration worldGeneration = new WorldGeneration();
    public Armor armor = new Armor();

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
        public boolean enderiteBowNeedsArrow = true;
        public boolean enderiteBowWithInfinityNeedsArrow = false;
        public float enderiteCrossbowAD = 3.0f;
        public float enderiteCrossbowArrowSpeed = 3.65f;

        public int durability = 4096;
    }

    public static class WorldGeneration {
        public EnderiteOre enderiteOre = new EnderiteOre();

        public static class EnderiteOre {
            public int veinSize = 3;
            public int veinAmount = 3;
        }
    }

    public static class Armor {
        public int bootsProtection = 4;
        public int leggingsProtection = 7;
        public int chestplateProtection = 9;
        public int helmetProtection = 4;

        public int durabilityMultiplier = 8;
        public int enchantability = 17;
        public float toughness = 4.0f;
        public float knockbackResistance = 0.1f;
    }

}