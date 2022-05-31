package net.ninjadev.freelook;

import net.ninjadev.freelook.init.ModConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class FreeLook {

    public static final String MODID = "freelook";
    public static final Logger LOGGER = LogManager.getLogger("FreeLook");

    public static void init() {
        ModConfigs.register();
    }

}
