package ru.jetdev;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SwingAnimations {
    public static boolean isActive = true; // Флаг активности анимации
    public static String animationType = "spin"; // Тип анимации по умолчанию

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> event) {
        if (!isActive || !(event.getEntity() instanceof AbstractClientPlayerEntity)) return;

        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) event.getEntity();
        PlayerRenderer renderer = (PlayerRenderer) event.getRenderer();
        PlayerModel<AbstractClientPlayerEntity> model = renderer.getModel();
        float partialTicks = Minecraft.getInstance().getDeltaFrameTime();

        if (Minecraft.getInstance().level != null) {
            long gameTime = Minecraft.getInstance().level.getGameTime();
            switch (animationType.toLowerCase()) {
                case "spin": // Вращение руки
                    model.rightArm.xRot += (float) (Math.PI * 2 * partialTicks);
                    break;
                case "flip": // Переворот руки с использованием синусоиды
                    model.rightArm.xRot = -model.rightArm.xRot + (float) (Math.sin(gameTime * 0.1) * 0.5);
                    break;
                case "wave": // Волнообразное движение руки
                    model.rightArm.zRot += (float) (Math.cos(gameTime * 0.2) * 0.3);
                    break;
                default:
                    break;
            }
        }
    }

    // Переменная для отслеживания состояния анимации (должна быть определена        // Метод для переключения состояния анимации
    public static void toggle() {
        isActive = !isActive; // Инвертируем значение (true -> false, false -> true)
        System.out.println("SwingAnimations " + (isActive ? "Enabled" : "Disabled")); // Логирование для отладки
    }

    // Метод для управления анимацией (пример)
    public static void setAnimationType(String type) {
        animationType = type;
    }
}
