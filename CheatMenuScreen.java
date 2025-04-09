package ru.jetdev;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import javax.annotation.Nonnull;

public class CheatMenuScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PADDING = 10;

    protected CheatMenuScreen() {
        super(new StringTextComponent("Celestial Cheats"));
    }

    @Override
    protected void init() {
        this.buttons.clear();
        int y = PADDING + 50;

        addButton(createButton(y, "KillAura: " + (KillAuraModule.isActive ? "ON" : "OFF"), button -> {
            KillAuraModule.toggle();
            button.setMessage(new StringTextComponent("KillAura: " + (KillAuraModule.isActive ? "ON" : "OFF")));
        }));
        y += BUTTON_HEIGHT + PADDING;

        addButton(createButton(y, "Aimbot: " + (AimbotModule.isActive ? "ON" : "OFF"), button -> {
            AimbotModule.toggle();
            button.setMessage(new StringTextComponent("Aimbot: " + (AimbotModule.isActive ? "ON" : "OFF")));
        }));
        y += BUTTON_HEIGHT + PADDING;
    }

    private Button createButton(int y, String title, Button.IPressable pressable) {
        int x = (this.width - BUTTON_WIDTH) / 2;
        return new CustomButton(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, new StringTextComponent(title), pressable);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Widget widget : this.buttons) {
            if (widget instanceof CustomButton) {
                CustomButton btn = (CustomButton) widget;
                if (btn.isMouseOver(mouseX, mouseY)) {
                    if (button == 1 && btn.getMessage().getString().startsWith("KillAura")) {
                        Minecraft.getInstance().setScreen(new KillAuraSettingsScreen());
                    }
                    btn.onPress();
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        fillGradient(matrixStack, 0, 0, width, height, 0xFF1A237E, 0xFF4A148C);
        RenderSystem.enableTexture();

        drawCenteredString(matrixStack, font, "Celestial Cheats", width / 2, 20, 0xFFFFFF);
        fill(matrixStack, 10, 40, width - 10, 41, 0xFF42A5F5);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}