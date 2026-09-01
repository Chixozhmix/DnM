package net.chixozhmix.dnmmod.effect.custom;

import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;


@Mod.EventBusSubscriber
public class CorpsePoison extends MobEffect {
    private final float damage;

    public CorpsePoison(float damage) {
        super(MobEffectCategory.HARMFUL, 0x0c5017);
        this.damage = damage;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);

        if(pLivingEntity.level().getGameTime() % 20 == 0) {
            float applyDamage = damage * (pAmplifier + 1);
            pLivingEntity.hurt(pLivingEntity.damageSources().magic(), applyDamage);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @SubscribeEvent
    public static void poisonExploer(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effect = entity.getEffect(ModEffects.CORPSE_POISON.get());

        if(effect != null) {
            Level level = entity.level();
            if (!level.isClientSide()) {
                ((ServerLevel) level).sendParticles(
                        ParticleHelper.POISON_CLOUD,
                        entity.getX(),
                        entity.getY() + entity.getBbHeight() / 2,
                        entity.getZ(),
                        20,
                        entity.getBbWidth(),
                        entity.getBbHeight() / 2,
                        entity.getBbWidth(),
                        0.5
                );
            }

            List<LivingEntity> livingEntities = entity.level().getEntitiesOfClass(
                    LivingEntity.class,
                    entity.getBoundingBox().inflate(5),
                    e -> e != entity && e.isAlive()
            );

            for (LivingEntity living : livingEntities) {
                living.addEffect(new MobEffectInstance(
                        MobEffects.POISON,
                        100,
                        1,
                        false,
                        true,
                        true
                ));
            }
        }

    }
}
