package net.chixozhmix.dnmmod.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.UUID;

public class ShrinkEffect extends MobEffect {
    private static final UUID SHRINK_MODIFIER_ID = UUID.fromString("12345678-1234-1234-1234-123456789abc");

    public ShrinkEffect() {
        super(MobEffectCategory.NEUTRAL, 0x88CCFF);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity.level().isClientSide()) return;

        ScaleType scaleType = ScaleTypes.BASE;
        ScaleData scaleData = scaleType.getScaleData(pLivingEntity);

        float targetScale = 1.0f / (pAmplifier + 2);

        scaleData.setBaseScale(targetScale);
        scaleData.setScale(targetScale);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.addAttributeModifiers(entity, attributes, amplifier);
        applyEffectTick(entity, amplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);

        // Восстанавливаем нормальный размер при окончании эффекта
        ScaleType scaleType = ScaleTypes.BASE;
        ScaleData scaleData = scaleType.getScaleData(entity);

        scaleData.setTargetScale(1.0F);
        scaleData.setScale(1.0F);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // Обновляем эффект каждые 10 тиков для плавности
        return duration % 10 == 0;
    }
}
