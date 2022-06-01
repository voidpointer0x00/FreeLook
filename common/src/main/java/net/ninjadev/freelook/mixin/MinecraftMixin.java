package net.ninjadev.freelook.mixin;

import net.minecraft.client.Minecraft;
import net.ninjadev.freelook.event.CameraEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "runTick", at = @At(value = "TAIL"))
    public void onClientTick(boolean bl, CallbackInfo ci) {
        CameraEvents.onClientTick();
    }

}
