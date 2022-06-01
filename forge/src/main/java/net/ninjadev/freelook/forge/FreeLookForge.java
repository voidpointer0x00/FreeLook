package net.ninjadev.freelook.forge;

import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.ninjadev.freelook.FreeLook;
import net.ninjadev.freelook.init.ModKeybinds;

@Mod("freelook")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FreeLookForge {

    public FreeLookForge() {
        FreeLook.init();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
//        ClientRegistry.registerKeyBinding(ModKeybinds.keyFreeLook);
//        ClientRegistry.registerKeyBinding(ModKeybinds.keyToggleMode);
    }
}
