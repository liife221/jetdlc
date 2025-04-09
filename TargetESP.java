package ru.jetdev;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

@Mod.EventBusSubscriber
public class TargetESP {
    private static final Minecraft mc = Minecraft.getInstance();
    public static boolean isActive = false;

    public static void toggle() {
        isActive = !isActive;
        System.out.println("TargetESP " + (isActive ? "Enabled" : "Disabled"));
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!isActive || mc.level == null || mc.player == null) return;

        LivingEntity target = KillAuraModule.getCurrentTarget();
        if (target != null && isValidTarget(target)) {
            renderESP(target, event.getPartialTicks());
        }
    }

    private static boolean isValidTarget(LivingEntity entity) {
        return !entity.isInvisible() && entity.isAlive();
    }

    private static void renderESP(LivingEntity entity, float partialTicks) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        RenderSystem.depthMask(false);
        RenderSystem.disableDepthTest(); // Ensure visibility through walls

        Vector3d pos = interpolateEntityPosition(entity, partialTicks);
        draw3DBox(pos, entity.getBbWidth(), entity.getBbHeight(), new Color(255, 0, 0, 120), new Color(255, 255, 255, 200));

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private static Vector3d interpolateEntityPosition(LivingEntity entity, float partialTicks) {
        double x = entity.xOld + (entity.getX() - entity.xOld) * partialTicks;
        double y = entity.yOld + (entity.getY() - entity.yOld) * partialTicks;
        double z = entity.zOld + (entity.getZ() - entity.zOld) * partialTicks;
        return new Vector3d(x, y, z).subtract(mc.getEntityRenderDispatcher().camera.getPosition());
    }

    private static void draw3DBox(Vector3d pos, double width, double height, Color fillColor, Color borderColor) {
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;
        float w = (float) width / 2;
        float h = (float) height;

        GL11.glPushMatrix();
        GL11.glLineWidth(2.0f);

        // Fill
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(fillColor.getRed() / 255f, fillColor.getGreen() / 255f, fillColor.getBlue() / 255f, fillColor.getAlpha() / 255f);
        GL11.glVertex3f((float)x - w, (float)y, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y, (float)z + w);
        GL11.glVertex3f((float)x - w, (float)y, (float)z + w);
        GL11.glVertex3f((float)x - w, (float)y + h, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y + h, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y + h, (float)z + w);
        GL11.glVertex3f((float)x - w, (float)y + h, (float)z + w);
        GL11.glEnd();

        // Border
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor4f(borderColor.getRed() / 255f, borderColor.getGreen() / 255f, borderColor.getBlue() / 255f, borderColor.getAlpha() / 255f);
        GL11.glVertex3f((float)x - w, (float)y, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y, (float)z + w);
        GL11.glVertex3f((float)x + w, (float)y, (float)z + w);
        GL11.glVertex3f((float)x - w, (float)y, (float)z + w);
        GL11.glVertex3f((float)x - w, (float)y, (float)z + w);
        GL11.glVertex3f((float)x - w, (float)y, (float)z - w);
        GL11.glVertex3f((float)x - w, (float)y + h, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y + h, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y + h, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y + h, (float)z + w);
        GL11.glVertex3f((float)x + w, (float)y + h, (float)z + w);
        GL11.glVertex3f((float)x - w, (float)y + h, (float)z + w);
        GL11.glVertex3f((float)x - w, (float)y + h, (float)z + w);
        GL11.glVertex3f((float)x - w, (float)y + h, (float)z - w);
        GL11.glVertex3f((float)x - w, (float)y, (float)z - w);
        GL11.glVertex3f((float)x - w, (float)y + h, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y + h, (float)z - w);
        GL11.glVertex3f((float)x + w, (float)y, (float)z + w);
        GL11.glVertex3f((float)x + w, (float)y + h, (float)z + w);
        GL11.glVertex3f((float)x - w, (float)y, (float)z + w);
        GL11.glVertex3f((float)x - w, (float)y + h, (float)z + w);
        GL11.glEnd();

        GL11.glPopMatrix();
    }
}