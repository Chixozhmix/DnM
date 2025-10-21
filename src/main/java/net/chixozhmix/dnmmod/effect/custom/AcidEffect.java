package net.chixozhmix.dnmmod.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AcidEffect extends MobEffect {
    private final float damagePerTick;

    public AcidEffect(MobEffectCategory pCategory, int pColor, float damagePerTick) {
        super(pCategory, pColor);
        this.damagePerTick = damagePerTick;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);

        if (pLivingEntity.level().getGameTime() % 20 == 0) {
            // Урон увеличивается с уровнем эффекта
            float damage = damagePerTick * (pAmplifier + 1);
            pLivingEntity.hurt(pLivingEntity.damageSources().magic(), damage);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
