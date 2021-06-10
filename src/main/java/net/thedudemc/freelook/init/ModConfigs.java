package net.thedudemc.freelook.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.thedudemc.freelook.FreeLook;
import net.thedudemc.freelook.config.FreeLookConfiguration;

public class ModConfigs {

    public static FreeLookConfiguration FREELOOK;

    @OnlyIn(Dist.CLIENT)
    public static void register() {
        FreeLook.LOGGER.info("registerConfigs()");
        FREELOOK = (FreeLookConfiguration) new FreeLookConfiguration().readConfig();

    }
}
