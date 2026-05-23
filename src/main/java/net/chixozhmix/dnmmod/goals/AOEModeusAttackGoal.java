package net.chixozhmix.dnmmod.goals;

import net.chixozhmix.dnmmod.entity.modeus.ModeusBoss;
import net.chixozhmix.dnmmod.entity.spell.trident_strike_area.TridentStrikeAreaEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class AOEModeusAttackGoal extends Goal {
    private final ModeusBoss modeus;
    private final int warmupTime;
    private final int activeTime;
    private final int cooldownTime;
    private final int beamCount;
    private final float damage;
    private final float range;
    private final double beamWidth;
    private final int areaColor;

    private int tickCounter;
    private int cooldownCounter;
    private State currentState = State.COOLDOWN;

    // Список созданных индикаторов лучей
    private List<TridentStrikeAreaEntity> beamIndicators = new ArrayList<>();

    public AOEModeusAttackGoal(ModeusBoss modeus, int warmupTime, int activeTime, int cooldownTime,
                               int beamCount, float damage, float range, double beamWidth) {
        this(modeus, warmupTime, activeTime, cooldownTime, beamCount, damage, range, beamWidth, 0x0D7278);
    }

    public AOEModeusAttackGoal(ModeusBoss modeus, int warmupTime, int activeTime, int cooldownTime,
                               int beamCount, float damage, float range, double beamWidth, int areaColor) {
        this.modeus = modeus;
        this.warmupTime = warmupTime;
        this.activeTime = activeTime;
        this.cooldownTime = cooldownTime;
        this.beamCount = beamCount;
        this.damage = damage;
        this.range = range;
        this.beamWidth = beamWidth;
        this.areaColor = areaColor;

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.cooldownCounter = this.cooldownTime / 2;
    }

    private enum State {
        COOLDOWN,
        WARMUP,
        ACTIVE
    }

    @Override
    public boolean canUse() {
        if (this.cooldownCounter > 0) {
            return false;
        }

        LivingEntity target = this.modeus.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }

        if (this.modeus.isAnimatingRise() || this.modeus.isCasting()) {
            return false;
        }

        double distanceSqr = this.modeus.distanceToSqr(target);
        return distanceSqr <= (range * range);
    }

    @Override
    public boolean canContinueToUse() {
        return (currentState == State.WARMUP || currentState == State.ACTIVE) &&
                this.modeus.getTarget() != null &&
                this.modeus.getTarget().isAlive() &&
                !this.modeus.isAnimatingRise();
    }

    @Override
    public void start() {
        super.start();
        this.tickCounter = 0;
        this.currentState = State.WARMUP;
        this.modeus.getNavigation().stop();
        this.modeus.setUsingAOE(true);
        this.modeus.setInvulnerable(true);
        this.modeus.playAnimation("radial_beam_charge");

        createBeamIndicators();
    }

    @Override
    public void stop() {
        super.stop();
        this.cooldownCounter = this.cooldownTime;
        this.currentState = State.COOLDOWN;
        this.tickCounter = 0;
        this.modeus.setUsingAOE(false);
        this.modeus.setInvulnerable(false);

        removeBeamIndicators();
    }

    @Override
    public void tick() {
        switch (currentState) {
            case COOLDOWN:
                break;
            case WARMUP:
                this.tickCounter++;
                handleWarmup();
                break;
            case ACTIVE:
                this.tickCounter++;
                handleActive();
                break;
        }
    }

    public void updateCooldown() {
        if (this.cooldownCounter > 0 && currentState == State.COOLDOWN) {
            this.cooldownCounter--;
        }
    }

    /**
     * Создает визуальные индикаторы для каждого луча
     */
    private void createBeamIndicators() {
        if (!(modeus.level() instanceof ServerLevel serverLevel)) return;

        float angleStep = 360f / beamCount;
        Vec3 startPos = modeus.getEyePosition().subtract(0.0F, 1.0F, 0.0F);

        for (int i = 0; i < beamCount; i++) {
            double angle = Math.toRadians(angleStep * i);
            Vec3 beamDir = new Vec3(Math.cos(angle), 0, Math.sin(angle)).normalize();

            TridentStrikeAreaEntity indicator = new TridentStrikeAreaEntity(
                    serverLevel,
                    startPos,
                    beamDir,
                    range,
                    (float) beamWidth,
                    areaColor
            );

            indicator.setDuration(warmupTime + 40);
            indicator.setShouldFade(true);
            serverLevel.addFreshEntity(indicator);

            beamIndicators.add(indicator);
        }
    }

    /**
     * Удаляет все индикаторы
     */
    private void removeBeamIndicators() {
        for (TridentStrikeAreaEntity indicator : beamIndicators) {
            if (indicator != null && indicator.isAlive()) {
                indicator.discard();
            }
        }
        beamIndicators.clear();
    }

    private void handleWarmup() {

        if (modeus.level() instanceof ServerLevel serverLevel) {
            float angleStep = 360f / beamCount;
            for (int i = 0; i < beamCount; i++) {
                double angle = Math.toRadians(angleStep * i + (tickCounter * 5));
                double x = Math.cos(angle) * 2.0;
                double z = Math.sin(angle) * 2.0;
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                        modeus.getX() + x,
                        modeus.getY() + 1.5,
                        modeus.getZ() + z,
                        2, 0.1, 0.2, 0.1, 0.02);
            }
        }

        if (this.tickCounter >= this.warmupTime) {
            this.tickCounter = 0;
            this.currentState = State.ACTIVE;
            this.modeus.playAnimation("radial_beam_attack");
            this.modeus.level().playSound(null, this.modeus.blockPosition(),
                    SoundEvents.WARDEN_SONIC_BOOM, this.modeus.getSoundSource(), 2.0F, 0.8F);

            //removeBeamIndicators();
        }
    }

    private void handleActive() {
        if (this.modeus.level().isClientSide()) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) this.modeus.level();

        if (this.tickCounter % 2 == 0) {
            applyBeamDamage(serverLevel);
        }

        spawnBeamParticles();

        if (this.tickCounter >= this.activeTime) {
            this.stop();
        }
    }

    private void applyBeamDamage(ServerLevel serverLevel) {
        float angleStep = 360f / beamCount;
        Vec3 beamStart = this.modeus.position().add(0, this.modeus.getBbHeight() / 2, 0);

        for (int i = 0; i < beamCount; i++) {
            double angle = Math.toRadians(angleStep * i);
            Vec3 beamDir = new Vec3(Math.cos(angle), 0, Math.sin(angle)).normalize();

            AABB checkArea = this.modeus.getBoundingBox().inflate(range);
            for (LivingEntity entity : this.modeus.level().getEntitiesOfClass(LivingEntity.class, checkArea)) {
                if (entity == this.modeus || !entity.isAlive()) continue;
                if (entity.isAlliedTo(this.modeus) && !(entity instanceof net.minecraft.world.entity.player.Player))
                    continue;

                Vec3 entityPos = entity.position().add(0, entity.getBbHeight() / 2, 0);
                Vec3 toEntity = entityPos.subtract(beamStart);
                double distanceAlongBeam = toEntity.dot(beamDir);

                if (distanceAlongBeam > 0 && distanceAlongBeam < range) {
                    Vec3 closestPointOnBeam = beamStart.add(beamDir.scale(distanceAlongBeam));
                    double distToBeam = entityPos.distanceTo(closestPointOnBeam);

                    if (distToBeam <= beamWidth + entity.getBbWidth() / 2.0) {
                        entity.hurt(this.modeus.damageSources().magic(), damage);

                        Vec3 knockback = new Vec3(
                                entity.getX() - this.modeus.getX(),
                                0.2,
                                entity.getZ() - this.modeus.getZ()
                        ).normalize().scale(0.8);
                        entity.setDeltaMovement(entity.getDeltaMovement().add(knockback));

                        serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT,
                                entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(),
                                8, 0.0, 0.0, 0.0, 0.0);
                    }
                }
            }
        }
    }

    private void spawnBeamParticles() {
        if (!(this.modeus.level() instanceof ServerLevel serverLevel)) return;

        float angleStep = 360f / beamCount;
        Vec3 start = this.modeus.position().add(0, this.modeus.getBbHeight() / 2, 0);

        for (int i = 0; i < beamCount; i++) {
            double angle = Math.toRadians(angleStep * i);
            Vec3 beamDir = new Vec3(Math.cos(angle), 0, Math.sin(angle));

            for (double d = 0.5; d < range; d += 0.6) {
                Vec3 pos = start.add(beamDir.scale(d));

                // Чередующиеся партиклы для эффекта
                if (this.tickCounter % 4 < 2) {
                    serverLevel.sendParticles(ParticleTypes.SONIC_BOOM,
                            pos.x, pos.y + 0.1, pos.z,
                            1, 0.0, 0.0, 0.0, 0.0);
                } else {
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                            pos.x, pos.y + 0.1, pos.z,
                            2, 0.05, 0.05, 0.05, 0.01);
                }
            }
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
