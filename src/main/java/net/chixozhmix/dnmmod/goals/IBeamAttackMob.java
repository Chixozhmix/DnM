package net.chixozhmix.dnmmod.goals;

import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public interface IBeamAttackMob {
    void setActiveAttackTarget(int entityId);
    boolean hasActiveAttackTarget();
    @Nullable
    LivingEntity getActiveAttackTarget();
    int getAttackDuration();
    float getAttackAnimationScale(float partialTicks);
}
