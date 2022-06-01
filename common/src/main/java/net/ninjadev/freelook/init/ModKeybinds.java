package net.ninjadev.freelook.init;

import net.minecraft.client.KeyMapping;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {

    public static KeyMapping keyFreeLook = new KeyMapping("key.freelook.desc", GLFW.GLFW_KEY_LEFT_ALT, "key.freelook.category");
    public static KeyMapping keyToggleMode = new KeyMapping("key.togglemode.desc", GLFW.GLFW_KEY_RIGHT_ALT, "key.freelook.category");

    public static KeyMapping[] register(KeyMapping[] keyMappings) {
        return ArrayUtils.addAll(keyMappings, keyFreeLook, keyToggleMode);
    }
}
