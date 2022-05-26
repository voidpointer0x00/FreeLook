package net.thedudemc.freelook.event;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.thedudemc.freelook.init.ModConfigs;
import net.thedudemc.freelook.init.ModKeybinds;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CameraEvents {

    private static final Minecraft mc = Minecraft.getInstance();
    private static LocalPlayer player;

    private static float yaw;
    private static float pitch;
    private static float prevYaw;
    private static float prevPitch;
    private static float originalYaw;
    private static float originalPitch;
    private static float originalHeadYaw;

    private static double mouseDX;
    private static double mouseDY;
    private static double prevMouseX;
    private static double prevMouseY;

    private static long lerpStart = 0;
    private static long lerpTimeElapsed = 0;
    private static boolean initialPress = true;
    private static boolean isInterpolating = false;

    private static boolean toggle = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;

        if (ModKeybinds.keyToggleMode.consumeClick()) {
            toggle = !toggle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onCameraUpdate(CameraSetup event) {
        if (player == null) player = mc.player;
        if (mc.options.getCameraType() != CameraType.FIRST_PERSON) return;

        if (isInterpolating) {
            lockPlayerRotation();
            interpolate(event);
        } else if (ModKeybinds.keyFreeLook.isDown() || toggle) {
            if (initialPress) setup();

            lockPlayerRotation();
            updateMouseInput();
            updateCameraRotation(event);

            initialPress = false;
        } else {
            if (!initialPress) {
                if (ModConfigs.FREELOOK.shouldInterpolate()) {
                    event.setYaw(yaw);
                    event.setPitch(pitch);
                    lerpStart = System.currentTimeMillis();
                    isInterpolating = true;
                } else {
                    event.setYaw(originalYaw);
                    event.setPitch(originalPitch);
                }
                initialPress = true;
            }
        }
    }

    private static void setup() {
        originalYaw = player.getYRot();
        originalPitch = player.getXRot();
        originalHeadYaw = player.yHeadRot;
        prevMouseX = mc.mouseHandler.xpos();
        prevMouseY = mc.mouseHandler.ypos();
    }

    private static void updateCameraRotation(CameraSetup event) {
        double dx = mouseDX * getSensitivity() * 0.15D;
        double dy = mouseDY * getSensitivity() * 0.15D;
        yaw = (float) dx - prevYaw + originalYaw;
        if (mc.options.invertYMouse) {
            pitch = (float) dy + prevPitch + originalPitch;
        } else {
            pitch = (float) dy - prevPitch + originalPitch;
        }
        if (ModConfigs.FREELOOK.shouldClamp()) {
            yaw = Mth.clamp(yaw, (originalYaw + -100.0F), (originalYaw + 100.0F));
        }
        pitch = Mth.clamp(pitch, -90.0F, 90.0F);

        prevYaw = Mth.clamp((float) dx + prevYaw, -99f, 99f);
        prevPitch = (float) dy + prevPitch;

        event.setYaw(yaw);
        event.setPitch(pitch);
    }

    private static void interpolate(CameraSetup event) {
        double duration = ModConfigs.FREELOOK.getInterpolateSpeed() * 1000f;
        float delta = (System.currentTimeMillis() - lerpStart) - lerpTimeElapsed;
        delta /= duration;

        float percentCompleted = (float) lerpTimeElapsed / (float) duration;
        float interpolatedYaw = lerp(yaw, originalYaw, percentCompleted * 10f * delta);
        float interpolatedPitch = lerp(pitch, originalPitch, percentCompleted * 10f * delta);

        event.setYaw(interpolatedYaw);
        event.setPitch(interpolatedPitch);
        yaw = interpolatedYaw;
        pitch = interpolatedPitch;

        lerpTimeElapsed = (System.currentTimeMillis() - lerpStart);
        if (lerpTimeElapsed >= duration) reset(event);
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static void reset(CameraSetup event) {
        event.setYaw(originalYaw);
        event.setPitch(originalPitch);
        isInterpolating = false;
        lerpTimeElapsed = 0;
        yaw = 0;
        pitch = 0;
        prevYaw = 0;
        prevPitch = 0;
        originalYaw = 0;
        originalPitch = 0;
        mouseDX = 0;
        mouseDY = 0;
        prevMouseX = 0;
        prevMouseY = 0;
    }

    private static void lockPlayerRotation() {
        player.setYRot(originalYaw);
        player.setXRot(originalPitch);
        player.yHeadRot = originalHeadYaw;
    }

    private static void updateMouseInput() {
        mouseDX = prevMouseX - mc.mouseHandler.xpos();
        mouseDY = prevMouseY - mc.mouseHandler.ypos();

        prevMouseX = mc.mouseHandler.xpos();
        prevMouseY = mc.mouseHandler.ypos();
    }

    private static double getSensitivity() {
        return (mc.options.sensitivity * 0.6D * 0.2D) * 8.0D; // some magic number based on MC code
    }

}
