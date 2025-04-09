package ru.jetdev;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class KillAuraModule {
    private static final Minecraft mc = Minecraft.getInstance();
    public static volatile boolean isActive = false;
    private static volatile float range = 5.0f;
    private static volatile float fov = 90.0f;
    private static volatile String targetMode = "Nearest";
    private static volatile String targetType = "Monsters";
    private static volatile int attackSpeed = 10;
    private static volatile boolean throughWalls = false;
    private static volatile boolean autoBlock = false;
    private static volatile int attackCooldown = 0;
    public static volatile LivingEntity currentTarget = null; // Для синхронизации с ESP

    // Getters
    public static float getRange() { return range; }
    public static float getFov() { return fov; }
    public static String getTargetMode() { return targetMode; }
    public static String getTargetType() { return targetType; }
    public static int getAttackSpeed() { return attackSpeed; }
    public static boolean isThroughWalls() { return throughWalls; }
    public static boolean isAutoBlock() { return autoBlock; }
    public static LivingEntity getCurrentTarget() { return currentTarget; }

    public static synchronized void toggle() {
        isActive = !isActive;
        System.out.println("KillAura " + (isActive ? "Enabled" : "Disabled"));
    }

    public static synchronized void setRange(float newRange) {
        range = Math.max(1.0f, Math.min(newRange, 6.0f));
        System.out.println("KillAura Range set to: " + range + " blocks");
    }

    public static synchronized void setFov(float newFov) {
        fov = Math.max(30.0f, Math.min(newFov, 180.0f));
        System.out.println("KillAura FOV set to: " + fov + "°");
    }

    public static synchronized void setTargetMode(String newTargetMode) {
        targetMode = newTargetMode;
        System.out.println("KillAura Target Mode set to: " + targetMode);
    }

    public static synchronized void setTargetType(String newTargetType) {
        targetType = newTargetType;
        System.out.println("KillAura Target Type set to: " + targetType);
    }

    public static synchronized void setAttackSpeed(int newAttackSpeed) {
        attackSpeed = Math.max(1, Math.min(newAttackSpeed, 20));
        System.out.println("KillAura Attack Speed set to: " + attackSpeed + " ticks");
    }

    public static synchronized void setThroughWalls(boolean newThroughWalls) {
        throughWalls = newThroughWalls;
        System.out.println("KillAura Through Walls set to: " + throughWalls);
    }

    public static synchronized void setAutoBlock(boolean newAutoBlock) {
        autoBlock = newAutoBlock;
        System.out.println("KillAura Auto Block set to: " + autoBlock);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || mc.level == null || mc.player == null) return;
        onTick();
    }

    private static void onTick() {
        if (!isActive || mc.player == null || mc.level == null || mc.gameMode == null) return;

        if (attackCooldown > 0) {
            attackCooldown--;
            return;
        }

        List<LivingEntity> targets = getTargetsInRange();
        LivingEntity target = selectTarget(targets);
        currentTarget = target; // Синхронизация с ESP

        if (target != null) {
            attackTarget(target);
            attackCooldown = Math.max(1, attackSpeed);
        }

        if (autoBlock && mc.player.getMainHandItem().getItem() == Items.SHIELD) {
            mc.player.startUsingItem(Hand.MAIN_HAND);
        }
    }

    private static List<LivingEntity> getTargetsInRange() {
        if (mc.level == null || mc.player == null) return Collections.emptyList();

        return mc.level.getEntitiesOfClass(LivingEntity.class, mc.player.getBoundingBox().inflate(range),
                        entity -> entity != null && entity != mc.player && isValidTarget(entity))
                .stream()
                .filter(KillAuraModule::isInFov)
                .collect(Collectors.toList());
    }

    private static boolean isValidTarget(Entity entity) {
        if (!(entity instanceof LivingEntity)) return false;

        LivingEntity livingEntity = (LivingEntity) entity;
        boolean isMonster = entity instanceof MonsterEntity;
        boolean isPlayer = entity instanceof PlayerEntity && !isTeammate(livingEntity);
        boolean isAnimal = entity instanceof AnimalEntity;

        switch (targetType) {
            case "Monsters": return isMonster;
            case "Players": return isPlayer;
            case "Animals": return isAnimal;
            case "All": return isMonster || isPlayer || isAnimal;
            default: return false;
        }
    }

    private static boolean isTeammate(LivingEntity entity) {
        return entity instanceof PlayerEntity && mc.player.getTeam() != null &&
                entity.getTeam() != null && mc.player.getTeam().isAlliedTo(entity.getTeam());
    }

    private static boolean isInFov(Entity entity) {
        if (entity == null || mc.player == null) return false;

        Vector3d targetVec = entity.position().subtract(mc.player.position()).normalize();
        Vector3d lookVec = mc.player.getLookAngle().normalize();
        double dotProduct = lookVec.dot(targetVec);
        double angle = Math.toDegrees(Math.acos(Math.max(-1.0, Math.min(1.0, dotProduct))));
        return angle <= fov / 2.0 || throughWalls;
    }

    private static LivingEntity selectTarget(List<LivingEntity> targets) {
        if (targets == null || targets.isEmpty()) return null;

        switch (targetMode) {
            case "Nearest":
                return targets.stream()
                        .min(Comparator.comparingDouble(target -> target.distanceToSqr(mc.player)))
                        .orElse(null);
            case "Lowest HP":
                return targets.stream()
                        .min(Comparator.comparingDouble(LivingEntity::getHealth))
                        .orElse(null);
            case "Random":
                return targets.get(new Random().nextInt(targets.size()));
            default:
                return null;
        }
    }

    public static boolean attackTarget(LivingEntity target) {
        if (target == null || mc.gameMode == null || mc.player == null) return false;

        mc.gameMode.attack(mc.player, target);
        mc.player.swing(Hand.MAIN_HAND);
        return true;
    }
}