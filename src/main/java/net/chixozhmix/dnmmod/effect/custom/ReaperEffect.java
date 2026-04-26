package net.chixozhmix.dnmmod.effect.custom;

import net.chixozhmix.dnmmod.entity.reaper.ReaperEntity;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class ReaperEffect extends MobEffect {
    public ReaperEffect() {
        super(MobEffectCategory.HARMFUL, 0x29072B);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);

        if(pLivingEntity.level() instanceof ServerLevel serverLevel) {
            ReaperEntity reaper = ModEntityType.REAPER.get().create(serverLevel);
            if(reaper != null) {
                reaper.moveTo(pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), pLivingEntity.getYRot(), pLivingEntity.getXRot());
                serverLevel.addFreshEntity(reaper);
            }
        }
    }
}
