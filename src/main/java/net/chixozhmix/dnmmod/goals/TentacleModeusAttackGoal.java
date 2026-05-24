package net.chixozhmix.dnmmod.goals;

import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.chixozhmix.dnmmod.entity.modeus.ModeusBoss;
import net.chixozhmix.dnmmod.entity.spell.trident_strike_area.TridentStrikeAreaEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class TentacleModeusAttackGoal extends Goal {
    private final ModeusBoss modeus;
    private final int warmupTime;
    private final int activeTime;
    private final int cooldownTime;
    private final int tentacleCount;
    private final float tentacleDamage;
    private final float spawnRadius;
    private final float minDistanceBetweenTentacles;

    private int tickCounter;
    private int cooldownCounter;
    private State currentState = State.COOLDOWN;

    private final List<Vec3> spawnPositions = new ArrayList<>();

    public TentacleModeusAttackGoal(ModeusBoss modeus, int warmupTime, int activeTime,
                                    int cooldownTime, int tentacleCount, float tentacleDamage,
                                    float spawnRadius) {
        this.modeus = modeus;
        this.warmupTime = warmupTime;
        this.activeTime = activeTime;
        this.cooldownTime = cooldownTime;
        this.tentacleCount = tentacleCount;
        this.tentacleDamage = tentacleDamage;
        this.spawnRadius = spawnRadius;
        this.minDistanceBetweenTentacles = 3.0F;

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
        return distanceSqr <= (spawnRadius * spawnRadius);
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
        this.tickCounter = 0;
        this.currentState = State.WARMUP;
        this.modeus.getNavigation().stop();
        this.modeus.setUsingTentacle(true);
        this.modeus.setInvulnerable(true);

        generateSpawnPositions();
    }

    @Override
    public void stop() {
        this.cooldownCounter = this.cooldownTime;
        this.currentState = State.COOLDOWN;
        this.tickCounter = 0;
        this.modeus.setUsingTentacle(false);
        this.modeus.setInvulnerable(false);

        this.spawnPositions.clear();
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

    /**
     * Генерирует случайные позиции для спавна тентаклей вокруг босса
     */
    private void generateSpawnPositions() {
        spawnPositions.clear();
        Vec3 bossPos = this.modeus.position();
        Random random = new Random();
        List<Vec3> candidates = new ArrayList<>();

        int maxAttempts = this.tentacleCount * 10;
        int attempts = 0;

        while (candidates.size() < this.tentacleCount && attempts < maxAttempts) {
            double angle = random.nextDouble() * Math.PI * 2;
            double distance = Math.sqrt(random.nextDouble()) * this.spawnRadius;

            double x = bossPos.x + Math.cos(angle) * distance;
            double z = bossPos.z + Math.sin(angle) * distance;
            double y = bossPos.y;

            Vec3 candidatePos = new Vec3(x, y, z);

            boolean tooClose = false;
            for (Vec3 existingPos : candidates) {
                if (candidatePos.distanceTo(existingPos) < this.minDistanceBetweenTentacles) {
                    tooClose = true;
                    break;
                }
            }
            if (!tooClose && candidatePos.distanceTo(bossPos) > 1.5) {
                candidates.add(candidatePos);
            }

            attempts++;
        }

        this.spawnPositions.addAll(candidates);
    }

    private void handleWarmup() {
        if (modeus.level() instanceof ServerLevel serverLevel) {
            for (Vec3 pos : spawnPositions) {
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        pos.x, pos.y + 1.0, pos.z,
                        3, 0.3, 0.3, 0.3, 0.05);
            }

            for (int i = 0; i < 3; i++) {
                double angle = Math.toRadians((tickCounter * 10 + i * 120) % 360);
                double x = Math.cos(angle) * 2.5;
                double z = Math.sin(angle) * 2.5;
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        modeus.getX() + x,
                        modeus.getY() + 1.0,
                        modeus.getZ() + z,
                        1, 0.1, 0.2, 0.1, 0.02);
            }
        }

        if (this.tickCounter >= this.warmupTime) {
            this.tickCounter = 0;
            this.currentState = State.ACTIVE;

            this.modeus.level().playSound(null, this.modeus.blockPosition(),
                    SoundRegistry.VOID_TENTACLES_FINISH.get(), this.modeus.getSoundSource(), 2.0F, 0.8F);

            spawnTentacles();
        }
    }

    /**
     * Спавнит VoidTentacle в заданных позициях
     */
    private void spawnTentacles() {
        if (!(modeus.level() instanceof ServerLevel serverLevel)) return;

        for (Vec3 pos : spawnPositions) {
            VoidTentacle tentacle = new VoidTentacle(serverLevel, this.modeus, this.tentacleDamage);
            tentacle.setPos(pos.x, pos.y, pos.z);
            tentacle.setYRot(this.modeus.getRandom().nextFloat() * 360.0F);

            serverLevel.addFreshEntity(tentacle);

            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE,
                    pos.x, pos.y + 0.5, pos.z,
                    10, 0.5, 0.5, 0.5, 0.1);
        }
    }

    private void handleActive() {
        if (this.tickCounter >= this.activeTime) {
            this.stop();
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void updateCooldown() {
        if (this.cooldownCounter > 0 && currentState == State.COOLDOWN) {
            this.cooldownCounter--;
        }
    }
}
