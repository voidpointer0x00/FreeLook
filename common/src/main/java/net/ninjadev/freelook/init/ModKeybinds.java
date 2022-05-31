package net.ninjadev.freelook.init;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class ModKeybinds {

    public static KeyMapping keyFreeLook = new KeyMapping("key.freelook.desc", GLFW.GLFW_KEY_LEFT_ALT, "key.freelook.category");
    public static KeyMapping keyToggleMode = new KeyMapping("key.togglemode.desc", GLFW.GLFW_KEY_RIGHT_ALT, "key.freelook.category");

}
