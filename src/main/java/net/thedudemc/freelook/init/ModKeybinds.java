package net.thedudemc.freelook.init;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModKeybinds {

    public static KeyBinding keyFreeLook = new KeyBinding("key.freelook.desc", GLFW.GLFW_KEY_LEFT_ALT, "key.freelook.category");
    public static KeyBinding keyToggleMode = new KeyBinding("key.togglemode.desc", GLFW.GLFW_KEY_RIGHT_ALT, "key.freelook.category");

    public static void register() {
        ClientRegistry.registerKeyBinding(keyFreeLook);
        ClientRegistry.registerKeyBinding(keyToggleMode);
    }

}
