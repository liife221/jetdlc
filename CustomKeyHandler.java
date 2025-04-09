package ru.jetdev;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomKeyHandler {

    private static final Minecraft mc = Minecraft.getInstance();
    private static final int KILL_AURA_KEY = GLFW.GLFW_KEY_R; // Клавиша K
    private static final int MENU_KEY = GLFW.GLFW_KEY_RIGHT_SHIFT; // Клавиша M для открытия меню

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && mc.level != null && mc.player != null) {
            long window = mc.getWindow().getWindow();

            // Обработка клавиши KillAura
            if (GLFW.glfwGetKey(window, KILL_AURA_KEY) == GLFW.GLFW_PRESS) {
                KillAuraModule.toggle();
                System.out.println("Нажата клавиша K, KillAura переключён на: " + KillAuraModule.isActive);
            }

            // Обработка клавиши для открытия меню
            if (GLFW.glfwGetKey(window, MENU_KEY) == GLFW.GLFW_PRESS) {
                if (mc.screen == null) {
                    mc.setScreen(new CheatMenuScreen()); // Открываем меню
                    System.out.println("Нажата клавиша M, меню открыто.");
                }
            }
        }
    }
}