package net.chixozhmix.dnmmod.effect.custom;

import net.chixozhmix.dnmmod.effect.ModEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AgathysArmor extends MobEffect {
    public AgathysArmor() {
        super(MobEffectCategory.BENEFICIAL, 0x32a852);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        pLivingEntity.setAbsorptionAmount(pLivingEntity.getAbsorptionAmount() - (float)(pAmplifier + 1));
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        pLivingEntity.setAbsorptionAmount(pLivingEntity.getAbsorptionAmount() + (float)(pAmplifier + 1));
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();

        // Проверяем, что у цели есть эффект
        if (target.hasEffect(ModEffects.AGATHYS_ARMOR.get()) &&
                event.getSource().getEntity() instanceof LivingEntity attacker) {

            // Проверяем, что это ближняя атака
            if (!isMeleeAttack(event.getSource())) {
                return;
            }

            // Получаем текущее количество сердец поглощения
            float absorptionHearts = target.getAbsorptionAmount();

            if (absorptionHearts > 0) {
                // Наносим ответный урон атакующему
                float retaliationDamage = absorptionHearts / 2;

                attacker.hurt(target.damageSources().thorns(target), retaliationDamage);

                // Визуальные эффекты
                if (target.level() instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, target.getX(), target.getY(), target.getZ(),
                            SoundEvents.THORNS_HIT, target.getSoundSource(), 1.0F, 1.0F);

                    // Спавним частицы
                    serverLevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR,
                            attacker.getX(), attacker.getY() + 1.0, attacker.getZ(),
                            8, 0.5, 0.5, 0.5, 0.1);
                }
            }
        }
    }

    private static boolean isMeleeAttack(DamageSource source) {
        // Проверяем основные типы ближних атак
        return source.is(DamageTypes.MOB_ATTACK) ||
                source.is(DamageTypes.PLAYER_ATTACK) ||
                source.is(DamageTypes.MOB_ATTACK_NO_AGGRO);
    }
}
