package net.ninjadev.freelook.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Options.class)
public interface OptionsAccessor {

    @Accessor("keyMappings")
    void setKeyMappings(KeyMapping[] keyMappings);
}
