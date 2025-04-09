package ru.jetdev;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class KillAuraSettingsScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PADDING = 10;
    private static final int SHADOW_OFFSET = 2;

    private final List<Button> killAuraSettingsButtons = new ArrayList<>();

    protected KillAuraSettingsScreen() {
        super(new StringTextComponent("§bKillAura Settings"));
    }

    @Override
    protected void init() {
        this.buttons.clear();
        killAuraSettingsButtons.clear();

        int menuX = this.width / 2 - BUTTON_WIDTH / 2;
        int currentY = this.height / 2 - 80;

        // Range
        this.addButton(createButton(menuX, currentY, new StringTextComponent("§bRange: " + KillAuraModule.getRange() + " blocks"),
                button -> {
                    float currentRange = KillAuraModule.getRange();
                    float newRange = currentRange >= 6.0f ? 1.0f : currentRange + 1.0f;
                    KillAuraModule.setRange(newRange);
                    button.setMessage(new StringTextComponent("§bRange: " + newRange + " blocks"));
                }));
        currentY += BUTTON_HEIGHT + PADDING;

        // FOV
        this.addButton(createButton(menuX, currentY, new StringTextComponent("§bFOV: " + KillAuraModule.getFov() + "°"),
                button -> {
                    float currentFov = KillAuraModule.getFov();
                    float newFov = currentFov >= 180.0f ? 30.0f : currentFov + 10.0f;
                    KillAuraModule.setFov(newFov);
                    button.setMessage(new StringTextComponent("§bFOV: " + newFov + "°"));
                }));
        currentY += BUTTON_HEIGHT + PADDING;

        // Target Mode
        this.addButton(createButton(menuX, currentY, new StringTextComponent("§bTarget Mode: " + KillAuraModule.getTargetMode()),
                button -> {
                    String currentTargetMode = KillAuraModule.getTargetMode();
                    switch (currentTargetMode) {
                        case "Nearest": KillAuraModule.setTargetMode("Lowest HP"); break;
                        case "Lowest HP": KillAuraModule.setTargetMode("Random"); break;
                        default: KillAuraModule.setTargetMode("Nearest"); break;
                    }
                    button.setMessage(new StringTextComponent("§bTarget Mode: " + KillAuraModule.getTargetMode()));
                }));
        currentY += BUTTON_HEIGHT + PADDING;

        // Target Type
        this.addButton(createButton(menuX, currentY, new StringTextComponent("§bTarget Type: " + KillAuraModule.getTargetType()),
                button -> {
                    String currentTargetType = KillAuraModule.getTargetType();
                    switch (currentTargetType) {
                        case "Monsters": KillAuraModule.setTargetType("Players"); break;
                        case "Players": KillAuraModule.setTargetType("Animals"); break;
                        case "Animals": KillAuraModule.setTargetType("All"); break;
                        default: KillAuraModule.setTargetType("Monsters"); break;
                    }
                    button.setMessage(new StringTextComponent("§bTarget Type: " + KillAuraModule.getTargetType()));
                }));
        currentY += BUTTON_HEIGHT + PADDING;

        // Attack Speed
        this.addButton(createButton(menuX, currentY, new StringTextComponent("§bAttack Speed: " + KillAuraModule.getAttackSpeed() + " ticks"),
                button -> {
                    int currentSpeed = KillAuraModule.getAttackSpeed();
                    int newSpeed = currentSpeed >= 20 ? 1 : currentSpeed + 1;
                    KillAuraModule.setAttackSpeed(newSpeed);
                    button.setMessage(new StringTextComponent("§bAttack Speed: " + newSpeed + " ticks"));
                }));
        currentY += BUTTON_HEIGHT + PADDING;

        // Through Walls
        this.addButton(createButton(menuX, currentY, new StringTextComponent("§bThrough Walls: " + (KillAuraModule.isThroughWalls() ? "§aON" : "§cOFF")),
                button -> {
                    boolean currentThroughWalls = KillAuraModule.isThroughWalls();
                    KillAuraModule.setThroughWalls(!currentThroughWalls);
                    button.setMessage(new StringTextComponent("§bThrough Walls: " + (KillAuraModule.isThroughWalls() ? "§aON" : "§cOFF")));
                }));
        currentY += BUTTON_HEIGHT + PADDING;

        // Auto Block
        this.addButton(createButton(menuX, currentY, new StringTextComponent("§bAuto Block: " + (KillAuraModule.isAutoBlock() ? "§aON" : "§cOFF")),
                button -> {
                    boolean currentAutoBlock = KillAuraModule.isAutoBlock();
                    KillAuraModule.setAutoBlock(!currentAutoBlock);
                    button.setMessage(new StringTextComponent("§bAuto Block: " + (KillAuraModule.isAutoBlock() ? "§aON" : "§cOFF")));
                }));
        currentY += BUTTON_HEIGHT + PADDING;

        // Back button
        this.addButton(createButton(menuX, currentY, new StringTextComponent("§bBack"),
                button -> Minecraft.getInstance().setScreen(new CheatMenuScreen())));
    }

    private Button createButton(int x, int y, StringTextComponent title, Button.IPressable pressable) {
        return new CustomButton(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, title, pressable);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        fillGradient(matrixStack, 0, 0, this.width, this.height, 0xFF1A237E, 0xFF4A148C);
        RenderSystem.enableTexture();

        drawCenteredString(matrixStack, this.font, "§l§bKillAura Settings", this.width / 2, 20, 0xFFFFFF);
        drawHorizontalLine(matrixStack, 10, this.width - 10, 40, 0xFF42A5F5);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void drawHorizontalLine(MatrixStack matrixStack, int startX, int endX, int y, int color) {
        fill(matrixStack, startX, y, endX, y + 1, color);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}