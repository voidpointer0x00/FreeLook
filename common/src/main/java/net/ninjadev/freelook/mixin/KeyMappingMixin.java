package net.ninjadev.freelook.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {

    @Shadow @Final private String category;

    @Shadow @Final private static Map<String, Integer> CATEGORY_SORT_ORDER;

    @Shadow @Final private String name;

    // Basically overwriting this method on KeyMapping.class so that MC takes into consideration our
    // manually added keymappings. This should not affect other mods as this is basically a clone
    // of Forge's handling of the same. Without this, the keybind screen will crash the game.
    @Inject(method = "compareTo(Lnet/minecraft/client/KeyMapping;)I", at = @At(value = "HEAD"), cancellable = true)
    public void compareTo(KeyMapping keyMapping, CallbackInfoReturnable<Integer> cir) {
        if (category.equals(keyMapping.getCategory())) {
            cir.setReturnValue(I18n.get(name).compareTo(I18n.get(keyMapping.getName())));
            cir.cancel();
            return;
        }
        Integer tCat = CATEGORY_SORT_ORDER.get(category);
        Integer oCat = CATEGORY_SORT_ORDER.get(keyMapping.getCategory());
        if (tCat == null && oCat != null) {
            cir.setReturnValue(1);
            cir.cancel();
            return;
        }
        if (tCat != null && oCat == null) {
            cir.setReturnValue(-1);
            cir.cancel();
            return;
        }
        if (tCat == null) {
            cir.setReturnValue(I18n.get(category).compareTo(I18n.get(keyMapping.getCategory())));
            cir.cancel();
            return;
        }
        cir.setReturnValue(tCat.compareTo(oCat));
    }

}
