package net.thedudemc.freelook.event;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.thedudemc.freelook.init.ModConfigs;
import net.thedudemc.freelook.init.ModKeybinds;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void setupClient(final FMLClientSetupEvent event) {
        ModConfigs.register();
        ModKeybinds.register();
    }
}
