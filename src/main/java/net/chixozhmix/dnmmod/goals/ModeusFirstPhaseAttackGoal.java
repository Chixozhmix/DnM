package net.chixozhmix.dnmmod.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import net.chixozhmix.dnmmod.entity.modeus.ModeusBoss;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

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
            this.attackRadius = 5.0F;
            this.attackRadiusSqr = this.attackRadius * this.attackRadius;
        } else {
            throw new IllegalStateException("Unable to add " + this.getClass().getSimpleName() + "to entity, must extend PathfinderMob.");
        }
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        if (this.target != null) {
            if (this.attackTime <= -5) {
                this.resetAttackTimer();
            }
            --this.attackTime;
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
                    knockbackEntity(this.mob, this.target);
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
        this.attackTime = 80;
    }

    private void knockbackEntity(LivingEntity mob, LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 160, 0));
        target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 160, 1));
        target.addEffect(new MobEffectInstance(ModEffects.DISORIENTATION.get(), 160, 0));

        Vec3 direction = target.position().subtract(mob.position()).normalize();

        if (direction.lengthSqr() < 0.0001) return;

        float horizontalStrength = 4.0F;
        float verticalStrength = 0.4F;

        target.setDeltaMovement(
                direction.x * horizontalStrength,
                verticalStrength,
                direction.z * horizontalStrength
        );
        target.hurtMarked = true;
    }
}
