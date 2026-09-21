package net.chixozhmix.dnmmod.goals.dragons;

import net.chixozhmix.dnmmod.entity.dragons.AbstractDragonEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class DragonFlyingGoal extends Goal {
    private final AbstractDragonEntity dragon;

    private double flightHeight;

    private double targetX;
    private double targetZ;

    private int flightTime;

    private Vec3 flyingOrigin;
    private double flyingRadius;

    public DragonFlyingGoal(AbstractDragonEntity dragon, double flyingRadius) {
        this.dragon = dragon;
        this.flyingRadius = flyingRadius;

        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Уже летит
        if (dragon.isFlying()) {
            return false;
        }

        // Пока приземляется — не взлетаем
        if (dragon.isLanding()) {
            return false;
        }

        // Небольшая задержка между решениями
        if (dragon.getFlightDecisionCooldown() > 0) {
            dragon.decreaseFlightDecisionCooldown();
            return false;
        }

        dragon.resetFlightDecisionCooldown();

        // Если есть цель — чаще летаем
        if (dragon.getTarget() != null && dragon.getTarget().isAlive()) {
            return dragon.getRandom().nextFloat() < 0.65F;
        }

        return dragon.getRandom().nextFloat() < 0.35F;
    }

    @Override
    public boolean canContinueToUse() {
        return dragon.isFlying() && flightTime > 0 && !dragon.isDeadOrDying();
    }

    @Override
    public void start() {
        dragon.setFlying(true);

        flyingOrigin = dragon.getSpawnPos();
        flightHeight = 20.0D + dragon.getRandom().nextDouble() * 20.0D;

        chooseNewFlightTarget();

        // 10-20 секунд
        flightTime = 200 + dragon.getRandom().nextInt(200);
        dragon.setNoGravity(true);

        dragon.setDeltaMovement(dragon.getDeltaMovement().x, 0.35D, dragon.getDeltaMovement().z);
    }

    @Override
    public void tick() {
        if (flightTime > 0)
            flightTime--;

        tickFlight();
    }

    @Override
    public void stop() {
        dragon.setFlying(false);
        dragon.setLanding(true);
        dragon.setNoGravity(false);
        Vec3 motion = dragon.getDeltaMovement();
        dragon.setDeltaMovement(motion.x, Math.min(motion.y, 0.0D), motion.z);
    }

    private void tickFlight() {
        double groundY = dragon.getGroundHeight();
        double targetY = groundY + flightHeight;

        double distanceFromOrigin = dragon.position().subtract(flyingOrigin).horizontalDistance();

        if (distanceFromOrigin > flyingRadius) {
            targetX = flyingOrigin.x;
            targetZ = flyingOrigin.z;
        }

        double dx = targetX - dragon.getX();
        double dz = targetZ - dragon.getZ();

        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        // Долетели до точки
        if (horizontalDistance < 5.0D) {
            chooseNewFlightTarget();

            dx = targetX - dragon.getX();
            dz = targetZ - dragon.getZ();

            horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        }

        if (horizontalDistance > 0.001D) {
            dx /= horizontalDistance;
            dz /= horizontalDistance;
        }

        double dy = targetY - dragon.getY();
        double verticalSpeed = Mth.clamp(dy * 0.08D, -0.25D, 0.25D);
        double speed = 0.45D;

        dragon.setDeltaMovement(dx * speed, verticalSpeed, dz * speed);

        float targetYaw = (float) (Mth.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;

        dragon.setYRot(Mth.rotLerp(0.15F, dragon.getYRot(), targetYaw));
        dragon.yRotO = dragon.getYRot();
    }

    private void chooseNewFlightTarget() {
        double angle = dragon.getRandom().nextDouble() * Math.PI * 2.0D;
        double distance = 10.0D + dragon.getRandom().nextDouble() * 25.0D;

        targetX = flyingOrigin.x + Math.cos(angle) * distance;
        targetZ = flyingOrigin.z + Math.sin(angle) * distance;
    }
}
