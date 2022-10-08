package net.ninjadev.freelook.init;

import net.minecraft.client.KeyMapping;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {

    public static KeyMapping keyFreeLook = new KeyMapping("Use", GLFW.GLFW_KEY_LEFT_ALT, "FreeLook");
    public static KeyMapping keyToggleMode = new KeyMapping("Toggle", GLFW.GLFW_KEY_RIGHT_ALT, "FreeLook");

    public static KeyMapping[] register(KeyMapping[] keyMappings) {
        return ArrayUtils.addAll(keyMappings, keyFreeLook, keyToggleMode);
    }
}
