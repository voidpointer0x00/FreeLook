package net.thedudemc.freelook.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.inventory.*;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.thedudemc.freelook.FreeLook;
import net.thedudemc.freelook.init.ModConfigs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A lot of this code is credited to PieKing1215 in their Mod InvMove. I suggest disabling this
 * feature and installing their mod for more compatibility and features!
 * <p>
 * https://www.curseforge.com/minecraft/mc-mods/invmove
 * https://github.com/PieKing1215/InvMove-Forge
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InventoryEvents {

    @SubscribeEvent
    public static void onInputUpdate(InputUpdateEvent event) {
        if (!ModConfigs.FREELOOK.shouldMoveInInventory()) return;

        Screen currentScreen = Minecraft.getInstance().currentScreen;
        if (canMoveIn(currentScreen) && !inTextField(currentScreen)) {
            KeyBinding.updateKeyBindState();
            movePlayer(event.getMovementInput(), Minecraft.getInstance().player.isSneaking(), Minecraft.getInstance().player.isSpectator());

        }
    }

    public static boolean canMoveIn(Screen screen) {
        if (screen == null) return false;

        IntegratedServer server = Minecraft.getInstance().getIntegratedServer();
        if (screen.isPauseScreen() && Minecraft.getInstance().isSingleplayer() && (server != null && server.getPublic()))
            return false;

        if (screen instanceof InventoryScreen) return true;
        if (screen instanceof CreativeScreen) return true;
        if (screen instanceof CraftingScreen) return true;
        if (screen instanceof ChestScreen) return true;
        if (screen instanceof ShulkerBoxScreen) return true;
        if (screen instanceof DispenserScreen) return true;
        if (screen instanceof HopperScreen) return true;
        if (screen instanceof EnchantmentScreen) return true;
        if (screen instanceof AnvilScreen) return true;
        if (screen instanceof BeaconScreen) return true;
        if (screen instanceof BrewingStandScreen) return true;
        if (screen instanceof FurnaceScreen) return true;
        if (screen instanceof BlastFurnaceScreen) return true;
        if (screen instanceof SmokerScreen) return true;
        if (screen instanceof LoomScreen) return true;
        if (screen instanceof CartographyTableScreen) return true;
        if (screen instanceof GrindstoneScreen) return true;
        if (screen instanceof StonecutterScreen) return true;
        if (screen instanceof MerchantScreen) return true;
        if (screen instanceof ReadBookScreen) return true;
        return screen instanceof EditBookScreen;
    }

    public static void movePlayer(MovementInput input, boolean slow, boolean isSpectator) {
        input.forwardKeyDown = isKeyDown(Minecraft.getInstance().gameSettings.keyBindForward);
        input.backKeyDown = isKeyDown(Minecraft.getInstance().gameSettings.keyBindBack);
        input.leftKeyDown = isKeyDown(Minecraft.getInstance().gameSettings.keyBindLeft);
        input.rightKeyDown = isKeyDown(Minecraft.getInstance().gameSettings.keyBindRight);
        input.moveForward = input.forwardKeyDown == input.backKeyDown ? 0.0F : (float) (input.forwardKeyDown ? 1 : -1);
        input.moveStrafe = input.leftKeyDown == input.rightKeyDown ? 0.0F : (float) (input.leftKeyDown ? 1 : -1);
        input.jump = isKeyDown(Minecraft.getInstance().gameSettings.keyBindJump);
        input.sneaking = isKeyDown(Minecraft.getInstance().gameSettings.keyBindSneak);
        if (!isSpectator && (input.sneaking || slow)) {
            input.moveStrafe = (float) ((double) input.moveStrafe * 0.3D);
            input.moveForward = (float) ((double) input.moveForward * 0.3D);
        }
        if (isKeyDown(Minecraft.getInstance().gameSettings.keyBindSprint)) {
            Minecraft.getInstance().player.setSprinting(true);
        }
    }

    private static boolean isKeyDown(KeyBinding key) {
        try {
            return ObfuscationReflectionHelper.getPrivateValue(KeyBinding.class, key, "field_74513_e"); // pressed
        } catch (Exception e) {
            FreeLook.LOGGER.warn("Access Error: KeyBinding.pressed - Key: \"" + key + "\": " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }


    private static boolean inTextField(Screen screen) {
        try {
            Field[] fs = getDeclaredFieldsSuper(screen.getClass());

            for (Field f : fs) {
                f.setAccessible(true);
                if (TextFieldWidget.class.isAssignableFrom(f.getType())) {
                    TextFieldWidget tfw = (TextFieldWidget) f.get(screen);
                    if (tfw != null && tfw.canWrite()) return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (screen instanceof IRecipeShownListener) {
            try {
                TextFieldWidget searchBar = ObfuscationReflectionHelper.getPrivateValue(RecipeBookGui.class, ((IRecipeShownListener) screen).getRecipeGui(), "field_193962_q"); //searchField
                if (searchBar.canWrite()) return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    public static Field[] getDeclaredFieldsSuper(Class aClass) {
        List<Field> fs = new ArrayList<>();

        do {
            fs.addAll(Arrays.asList(aClass.getDeclaredFields()));
        } while ((aClass = aClass.getSuperclass()) != null);

        return fs.toArray(new Field[0]);
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGUIDraw(GuiScreenEvent.DrawScreenEvent.Pre event) {
        Screen screen = event.getGui();

        if (canMoveIn(screen)) {
            RenderSystem.translatef(10000, 10000, 0);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onGUIBackgroundDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        Screen screen = event.getGui();

        if (canMoveIn(screen)) {
            RenderSystem.translatef(-10000, -10000, 0);
        }
    }
}
