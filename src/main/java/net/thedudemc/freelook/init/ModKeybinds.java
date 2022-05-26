package net.thedudemc.freelook.init;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {

    public static KeyMapping keyFreeLook = new KeyMapping("key.freelook.desc", GLFW.GLFW_KEY_LEFT_ALT, "key.freelook.category");
    public static KeyMapping keyToggleMode = new KeyMapping("key.togglemode.desc", GLFW.GLFW_KEY_RIGHT_ALT, "key.freelook.category");

    public static void register() {
        ClientRegistry.registerKeyBinding(keyFreeLook);
        ClientRegistry.registerKeyBinding(keyToggleMode);
    }

}
