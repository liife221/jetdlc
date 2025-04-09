package ru.jetdev;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class AimbotModule {
    private static final Minecraft mc = Minecraft.getInstance();
    public static boolean isActive = false;
    private static final float MAX_DISTANCE = 6.0f;

    public static void toggle() {
        isActive = !isActive;
        System.out.println("Aimbot " + (isActive ? "Enabled" : "Disabled"));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (!isActive || mc.player == null || mc.level == null) {
            return;
        }

        PlayerEntity player = mc.player;
        List<LivingEntity> entities = mc.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(MAX_DISTANCE));

        LivingEntity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        for (LivingEntity entity : entities) {
            if (entity == player) continue; // Игнорируем самого игрока

            if (entity.getTeam() != null && entity.getTeam().equals(player.getTeam())) {
                continue; // Игнорируем союзников
            }

            double distanceToPlayer = player.distanceToSqr(entity);
            if (distanceToPlayer < closestDistance) {
                closestDistance = distanceToPlayer;
                closestEntity = entity;
            }
        }

        if (closestEntity != null) {
            Vector3d targetPosition = closestEntity.position();
            if (targetPosition != null) {
                // Используем EntityAnchorArgument.Type для корректного вызова lookAt
                player.lookAt(EntityAnchorArgument.Type.EYES, targetPosition);
            }
        }
    }
}