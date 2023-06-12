package org._9636dev.autovanilla.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AutoCommonConfig {
    private static AutoCommonConfig INSTANCE;

    public final ForgeConfigSpec.IntValue defaultSmithingEnergyPerTick;
    public final ForgeConfigSpec.IntValue defaultSmithingTimeInTicks;
    public final ForgeConfigSpec.IntValue maxEnergyStored;

    public AutoCommonConfig(ForgeConfigSpec.Builder builder) {
        this.defaultSmithingEnergyPerTick = builder.defineInRange("defaultSmithingEnergy", 20, 5, 2048);
        this.defaultSmithingTimeInTicks = builder.defineInRange("defaultSmithingTicks", 20, 1, 2048);
        this.maxEnergyStored = builder.defineInRange("maxEnergyStored", 100_000, 1, 1_000_000);
    }

    public static void setInstance(AutoCommonConfig INSTANCE) {
        AutoCommonConfig.INSTANCE = INSTANCE;
    }

    public static AutoCommonConfig getInstance() {
        return INSTANCE;
    }
}
