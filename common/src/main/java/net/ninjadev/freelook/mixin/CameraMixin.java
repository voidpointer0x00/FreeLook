package net.ninjadev.freelook.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.ninjadev.freelook.event.CameraEvents;
import net.ninjadev.freelook.init.ModKeybinds;
import org.checkerframework.common.reflection.qual.Invoke;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {

    @Shadow
    protected void setRotation(float f, float g) {}

    @Redirect(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V"))
    public void setRotation(Camera instance, float f, float g) {
        if (Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON) return;
        if (ModKeybinds.keyFreeLook.isDown() || CameraEvents.toggle || CameraEvents.isFreelooking || CameraEvents.isInterpolating) {
            CameraEvents.onCameraUpdate(instance);
        } else {
            this.setRotation(f,g);
        }
    }
}
