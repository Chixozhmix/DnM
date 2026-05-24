package net.chixozhmix.dnmmod.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.chixozhmix.dnmmod.entity.darkspawn_larva.DarkspawnLarva;
import net.chixozhmix.dnmmod.entity.modeus.ModeusBoss;
import net.chixozhmix.dnmmod.entity.spell.red_cristall.RedCristallEntity;
import net.chixozhmix.dnmmod.entity.tainted_observer.DarkspawnObserver;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class ModeusFirstPhaseAttackGoal extends Goal {
    protected PathfinderMob mob;
    protected LivingEntity target;
    protected int attackTime;
    protected final float attackRadius;
    protected final float attackRadiusSqr;
    private static final int ANIMATION_DURATION = 5;
    private boolean hasUsedAbility = false;
    private int animationTick = 0;

    public ModeusFirstPhaseAttackGoal(IMagicEntity abstractSpellCastingMob) {
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.TARGET));
        if (abstractSpellCastingMob instanceof PathfinderMob m) {
            this.mob = m;
            this.attackRadius = 6.0F;
            this.attackRadiusSqr = this.attackRadius * this.attackRadius;
        } else {
            throw new IllegalStateException("Unable to add " + this.getClass().getSimpleName() + "to entity, must extend PathfinderMob.");
        }
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        if (this.target != null) {
            if (this.attackTime > 0) {
                --this.attackTime;
            }
            return this.attackTime <= 0;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return animationTick > 0 && this.target != null && this.target.isAlive();
    }

    @Override
    public void start() {
        this.animationTick = ANIMATION_DURATION;
        this.hasUsedAbility = false;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            this.stop();
            return;
        }

        this.mob.getLookControl().setLookAt(this.target, 45.0F, 45.0F);

        if (this.animationTick > 0) {
            --this.animationTick;

            double distanceSquared = this.mob.distanceToSqr(this.target);
            if (distanceSquared < (double) this.attackRadiusSqr) {
                if (this.mob instanceof ModeusBoss boss) {
                    boss.setUsingKnockback(true);
                }
                if (!this.hasUsedAbility) {
                    knockbackEntity(this.mob);
                    spawnCrystal(this.mob, this.target);
                    this.hasUsedAbility = true;
                }
            }
        }
    }

    @Override
    public void stop() {
        this.animationTick = 0;
        this.hasUsedAbility = false;

        if (this.mob instanceof ModeusBoss boss) {
            boss.setUsingKnockback(false);
        }

        this.resetAttackTimer();
    }

    protected void resetAttackTimer() {
        this.attackTime = 60;
    }

    private void knockbackEntity(LivingEntity mob) {
        AABB area = new AABB(
                mob.getX() - 5.0F, mob.getY() - 5.0F, mob.getZ() - 5.0F,
                mob.getX() + 5.0F, mob.getY() + 5.0F, mob.getZ() + 5.0F);

        List<LivingEntity> entities = mob.level().getEntitiesOfClass(
                LivingEntity.class,
                area,
                entity ->
                        !(entity instanceof DarkspawnObserver) &&
                                !(entity instanceof DarkspawnLarva)
        );

        for (LivingEntity entity : entities) {
            if(entity != mob)
            {
                if(entity instanceof Player player) {
                    player.addEffect(new MobEffectInstance(ModEffects.DISORIENTATION.get(), 160, 0));
                    if(player.isBlocking()) {
                        player.getUseItem().hurtAndBreak(2, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
                    }
                }

                entity.hurt(mob.damageSources().indirectMagic(mob, mob), 6);

                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 160, 0));
                entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 160, 1));

                Vec3 direction = entity.position().subtract(mob.position());
                if (direction.lengthSqr() < 0.0001) continue;
                direction = direction.normalize();

                float horizontalStrength = 4.0F;
                float verticalStrength = 0.4F;

                entity.setDeltaMovement(
                        direction.x * horizontalStrength,
                        direction.y * verticalStrength,
                        direction.z * horizontalStrength
                );
                entity.hurtMarked = true;
            }
        }
    }

    private void spawnCrystal(LivingEntity mob, LivingEntity target) {
        if (!mob.level().isClientSide) {
            float minScale = 2.0F;
            float maxScale = 4.0F;
            int countPerSide = 3;

            Vec3 forwardBase = mob.getForward().multiply(1.0F, 0.0F, 1.0F).normalize();
            Vec3 rightBase = forwardBase.yRot((float) Math.toRadians(90));
            Vec3 leftBase = forwardBase.yRot((float) Math.toRadians(-90));

            Vec3[] directions = {forwardBase, leftBase, rightBase};

            for (Vec3 direction : directions) {
                Vec3 start = mob.getEyePosition()
                        .add(direction.scale(1.5F));
                start = Utils.moveToRelativeGroundLevel(mob.level(), start, 1, 3)
                        .add(0.0F, 0.1F, 0.0F);

                double distance = (double) countPerSide;
                if (target != null) {
                    Vec3 targetPos = target.position()
                            .add(target.getDeltaMovement().multiply(distance, 0.0F, distance));
                    distance = targetPos.subtract(start).horizontalDistance();
                }

                if (distance < 0.01) {
                    distance = 1.0;
                }

                for (int i = 0; i < countPerSide; i++) {
                    float progress = (float) ((float) (i + 1) / distance);
                    if (progress > 1.0F) progress = 1.0F;
                    float scale = Mth.lerp(progress, minScale, maxScale);

                    Vec3 spawn = start.add(direction.scale((double) i));
                    Vec3 ground = Utils.moveToRelativeGroundLevel(mob.level(), spawn, 8);
                    spawn = ground.subtract(spawn)
                            .scale((double) Mth.clamp((float) i / 3.0F, 0.0F, 1.0F))
                            .add(spawn);
                    BlockPos belowPos = BlockPos.containing(spawn).below();

                    if (mob.level().getBlockState(belowPos)
                            .isFaceSturdy(mob.level(), belowPos, Direction.UP)) {
                        RedCristallEntity crystal = new RedCristallEntity(mob.level(), mob);
                        if (i % 2 == 0) {
                            crystal.setSilent(true);
                        }
                        crystal.setCristallSize(scale);
                        crystal.moveTo(spawn);
                        crystal.setWaitTime(i);
                        crystal.setDamage(0);
                        crystal.setYRot(mob.getYRot() - 45.0F +
                                (float) Utils.random.nextIntBetweenInclusive(-20, 20));
                        crystal.setXRot((float) Utils.random.nextIntBetweenInclusive(-15, 15));

                        mob.level().addFreshEntity(crystal);
                    }
                }
            }
        }
    }
}
