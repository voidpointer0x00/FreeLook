package net.thedudemc.freelook.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.thedudemc.freelook.init.ModKeybinds;
import net.thedudemc.freelook.init.ModConfigs;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CameraEvents {

    private static Minecraft mc = Minecraft.getInstance();
    private static ClientPlayerEntity player;

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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onCameraUpdate(CameraSetup event) {
        if (player == null) player = mc.player;
        if (mc.options.getCameraType() != PointOfView.FIRST_PERSON) return;

        if (isInterpolating) {
            lockPlayerRotation();
            interpolate(event);
        } else if (ModKeybinds.keyFreeLook.isDown()) {
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
        originalYaw = player.yRot;
        originalPitch = player.xRot;
        originalHeadYaw = player.yHeadRot;
        prevMouseX = mc.mouseHandler.xpos();
        prevMouseY = mc.mouseHandler.ypos();
    }

    private static void updateCameraRotation(CameraSetup event) {
        double dx = mouseDX * getSensitivity() * 0.15D;
        double dy = mouseDY * getSensitivity() * 0.15D;
        yaw = (float) dx - prevYaw + originalYaw;
        if (mc.options.invertYMouse)
            pitch = (float) dy + prevPitch + originalPitch;
        else
            pitch = (float) dy - prevPitch + originalPitch;
        if (ModConfigs.FREELOOK.shouldClamp())
            yaw = MathHelper.clamp(yaw, (originalYaw + -100.0F), (originalYaw + 100.0F));
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);

        event.setYaw(yaw);
        event.setPitch(pitch);
        prevYaw = (float) dx + prevYaw;
        prevPitch = (float) dy + prevPitch;
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
        player.yRot = originalYaw;
        player.xRot = originalPitch;
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
