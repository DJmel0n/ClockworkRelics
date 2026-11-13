package com.djmelon.clockworkrelics;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = ClockworkRelics.MODID, dist = Dist.CLIENT)
public class ClockworkRelicsClient {

    public ClockworkRelicsClient() {
        ClockworkRelics.LOGGER.info("Clockwork Relics client initialized!");
    }
}
