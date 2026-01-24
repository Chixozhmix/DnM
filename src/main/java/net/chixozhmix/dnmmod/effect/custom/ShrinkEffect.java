package net.chixozhmix.dnmmod.effect.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.puffish.attributesmod.api.PuffishAttributes;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.UUID;

public class ShrinkEffect extends MobEffect {

    private static final UUID SHRINK_MODIFIER_ID = UUID.fromString("12345678-1234-1234-1234-123456789abc");
    private static final UUID JUMP_MODIFIER_ID = UUID.fromString("87654321-4321-4321-4321-210987654321");
    private static final String ORIGINAL_SCALE_KEY = "OriginalScale";

    public ShrinkEffect() {
        super(MobEffectCategory.NEUTRAL, 0x88CCFF);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity.level().isClientSide()) return;

        ScaleType scaleType = ScaleTypes.BASE;
        ScaleData scaleData = scaleType.getScaleData(pLivingEntity);

        CompoundTag persistentData = pLivingEntity.getPersistentData();
        if (!persistentData.contains(ORIGINAL_SCALE_KEY)) {
            float currentScale = scaleData.getBaseScale();
            persistentData.putFloat(ORIGINAL_SCALE_KEY, currentScale);
        }

        float targetScale = 1.0f / (pAmplifier + 2);
        float jumpMultiplier = 1.0f + (pAmplifier + 1) * 0.5f;

        scaleData.setBaseScale(targetScale);
        scaleData.setScale(targetScale);

        updateJumpModifier(pLivingEntity, pAmplifier, jumpMultiplier);
    }

    private void updateJumpModifier(LivingEntity entity, int amplifier, float jumpMultiplier) {
        entity.getAttribute(PuffishAttributes.JUMP).removeModifier(JUMP_MODIFIER_ID);

        AttributeModifier jumpModifier = new AttributeModifier(
                JUMP_MODIFIER_ID,
                "Shrink Jump Boost",
                jumpMultiplier - 1.0f,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );

        entity.getAttribute(PuffishAttributes.JUMP).addTransientModifier(jumpModifier);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.addAttributeModifiers(entity, attributes, amplifier);

        ScaleType scaleType = ScaleTypes.BASE;
        ScaleData scaleData = scaleType.getScaleData(entity);
        CompoundTag persistentData = entity.getPersistentData();

        if (!persistentData.contains(ORIGINAL_SCALE_KEY)) {
            float currentScale = scaleData.getBaseScale();
            persistentData.putFloat(ORIGINAL_SCALE_KEY, currentScale);
        }

        applyEffectTick(entity, amplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);

        entity.getAttribute(PuffishAttributes.JUMP).removeModifier(JUMP_MODIFIER_ID);

        ScaleType scaleType = ScaleTypes.BASE;
        ScaleData scaleData = scaleType.getScaleData(entity);
        CompoundTag persistentData = entity.getPersistentData();

        if (persistentData.contains(ORIGINAL_SCALE_KEY)) {
            float originalScale = persistentData.getFloat(ORIGINAL_SCALE_KEY);
            scaleData.setBaseScale(originalScale);
            scaleData.setScale(originalScale);
            persistentData.remove(ORIGINAL_SCALE_KEY);
        } else {
            scaleData.setBaseScale(1.0f);
            scaleData.setScale(1.0f);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }
}
