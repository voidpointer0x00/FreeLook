package net.ninjadev.spawnvisualizer.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.ninjadev.freelook.FreeLook;

public class FreeLookClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FreeLook.init();
    }
}
