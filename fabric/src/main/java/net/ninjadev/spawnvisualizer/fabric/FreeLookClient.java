package net.ninjadev.spawnvisualizer.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.ninjadev.freelook.FreeLook;
import net.ninjadev.freelook.init.ModKeybinds;

public class FreeLookClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FreeLook.init();
        KeyBindingHelper.registerKeyBinding(ModKeybinds.keyFreeLook);
        KeyBindingHelper.registerKeyBinding(ModKeybinds.keyToggleMode);
    }
}
