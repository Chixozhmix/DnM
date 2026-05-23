package net.chixozhmix.dnmmod.goals;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class CapturingTargetAttackGoal extends Goal {
    private final Mob mob;
    private int attackTime;
    private final int attackDuration;
    private final int attackCooldown;
    private int cooldownTime = 0;
    private final boolean addEffect;

    private final double attackRange;
    private final double attackSqr;

    private final MobEffect mobEffect;
    private final float damage;
    private final float knockback;

    public CapturingTargetAttackGoal(Mob mob, int attackDuration, boolean addEffect, @Nullable MobEffect effect, float damage, float knockback) {
        this.mob = mob;
        this.addEffect = addEffect;
        this.attackDuration = attackDuration;
        this.mobEffect = effect;
        this.damage = damage;
        this.knockback = knockback;
        this.attackCooldown = 200;
        this.cooldownTime = 0;
        this.attackRange = 20.0D;
        this.attackSqr = this.attackRange * this.attackRange;

        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public void start() {
        this.attackTime = -10;
        this.mob.getNavigation().stop();
        LivingEntity target = this.mob.getTarget();

        if(target != null) {
            this.mob.getLookControl().setLookAt(target, 90.0F, 90.0F);
        }
    }

    @Override
    public void stop() {
        this.attackTime = 0;
        this.cooldownTime = this.attackCooldown;
        if(mob instanceof IBeamAttackMob beamAttackMob)
            beamAttackMob.setActiveAttackTarget(0);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        if (this.cooldownTime > 0) {
            this.cooldownTime--;
            return false;
        }

        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive())
            return false;

        double distanceToTarget = this.mob.distanceToSqr(target);
        return distanceToTarget <= attackSqr;

    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }

        double distanceToTarget = this.mob.distanceToSqr(target);
        return distanceToTarget <= (this.attackRange + 5.0D) * (this.attackRange + 5.0D);
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if(target != null) {
            this.mob.getNavigation().stop();
            this.mob.getLookControl().setLookAt(target, 90.0F, 90.0F);

            this.attackTime++;
            if(attackTime == 0) {
                if(this.mob instanceof IBeamAttackMob beamAttackMob)
                    beamAttackMob.setActiveAttackTarget(target.getId());
                if (!this.mob.isSilent()) {
                    this.mob.level().broadcastEntityEvent(this.mob, (byte) 21);
                }
            } else if (attackTime >= this.attackDuration) {
                if (this.addEffect)
                    effectAttack(target);
                else {
                    target.hurt(this.mob.damageSources().indirectMagic(this.mob, this.mob), this.damage);
                    knockbackEntity(this.mob, target, knockback);
                }
                this.stop();
            }

            super.tick();
        }
    }

    private void effectAttack(LivingEntity target) {
        target.addEffect(new MobEffectInstance(this.mobEffect, 200, 0));
    }

    private void knockbackEntity(LivingEntity mob, LivingEntity target, float strength) {
        Vec3 direction = target.position().subtract(mob.position()).normalize();

        if (direction.lengthSqr() < 0.0001) return;
        float verticalStrength = 0.4F;

        target.setDeltaMovement(
                direction.x * strength,
                verticalStrength,
                direction.z * strength
        );
        target.hurtMarked = true;
    }
}
