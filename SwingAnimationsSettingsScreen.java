package ru.jetdev;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import javax.annotation.Nonnull;

public class SwingAnimationsSettingsScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PADDING = 10;

    protected SwingAnimationsSettingsScreen() {
        super(new StringTextComponent("SwingAnimations Settings"));
    }

    @Override
    protected void init() {
        this.buttons.clear();
        int x = (width - BUTTON_WIDTH) / 2;
        int y = height / 2 - 40;

        addButton(new CustomButton(x, y, BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("Animation: " + SwingAnimations.animationType), button -> {
            String current = SwingAnimations.animationType;
            switch (current) {
                case "Default": SwingAnimations.setAnimationType("Spin"); break;
                case "Spin": SwingAnimations.setAnimationType("Flip"); break;
                case "Flip": SwingAnimations.setAnimationType("Wave"); break;
                default: SwingAnimations.setAnimationType("Default"); break;
            }
            button.setMessage(new StringTextComponent("Animation: " + SwingAnimations.animationType));
        }));
        y += BUTTON_HEIGHT + PADDING;

        addButton(new CustomButton(x, y, BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("Back"), button -> Minecraft.getInstance().setScreen(new CheatMenuScreen())));
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        fillGradient(matrixStack, 0, 0, width, height, 0xFF1A237E, 0xFF4A148C);
        RenderSystem.enableTexture();

        drawCenteredString(matrixStack, font, "SwingAnimations Settings", width / 2, 20, 0xFFFFFF);
        fill(matrixStack, 10, 40, width - 10, 41, 0xFF42A5F5);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}