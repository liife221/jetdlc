package ru.jetdev;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import javax.annotation.Nonnull;

public class CustomButton extends Button {
    private float hoverProgress = 0.0f;

    public CustomButton(int x, int y, int width, int height, @Nonnull StringTextComponent title, @Nonnull IPressable pressable) {
        super(x, y, width, height, title, pressable);
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        hoverProgress = isHovered() ? Math.min(1.0f, hoverProgress + partialTicks * 0.1f) : Math.max(0.0f, hoverProgress - partialTicks * 0.1f);

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();

        int alpha = (int) (100 + hoverProgress * 155);
        int fillColor = (alpha << 24) | 0x42A5F5;
        int borderColor = 0xFFFFFFFF;

        fillRoundedRect(matrixStack, x, y, x + width, y + height, 5, fillColor);
        drawRoundedRectBorder(matrixStack, x, y, x + width, y + height, 5, borderColor);

        RenderSystem.enableTexture();
        drawCenteredString(matrixStack, Minecraft.getInstance().font, getMessage(), x + width / 2, y + (height - 8) / 2, 0xFFFFFF);

        RenderSystem.disableBlend();
    }

    private void fillRoundedRect(MatrixStack matrixStack, int left, int top, int right, int bottom, int radius, int color) {
        fill(matrixStack, left + radius, top, right - radius, bottom, color);
        fill(matrixStack, left, top + radius, right, bottom - radius, color);
    }

    private void drawRoundedRectBorder(MatrixStack matrixStack, int left, int top, int right, int bottom, int radius, int color) {
        fill(matrixStack, left, top, right, top + 1, color);
        fill(matrixStack, left, bottom - 1, right, bottom, color);
        fill(matrixStack, left, top, left + 1, bottom, color);
        fill(matrixStack, right - 1, top, right, bottom, color);
    }
}