package net.chixozhmix.dnmmod.effect.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;

public class MindControlEffect extends MobEffect {
    private static final Random RANDOM = new Random();
    public static final TagKey<EntityType<?>> BOSSES =
            ForgeRegistries.ENTITY_TYPES.tags()
                    .createTagKey(new ResourceLocation("forge", "bosses"));

    public MindControlEffect() {
        super(MobEffectCategory.HARMFUL, 2744299);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide && pLivingEntity instanceof Mob mob && !(pLivingEntity.getType().is(BOSSES))) {
            LivingEntity newTarget = this.findNewTarget(pLivingEntity);

            if (newTarget != null && newTarget != mob.getTarget()) {
                mob.setTarget(newTarget);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 10 == 0;
    }

    private LivingEntity findNewTarget(LivingEntity affectedEntity) {
        Level level = affectedEntity.level();

        List<LivingEntity> possibleTargets = level.getEntitiesOfClass(
                LivingEntity.class,
                affectedEntity.getBoundingBox().inflate(12.0),
                entity -> entity != affectedEntity
                        && entity.isAlive()
                        && !(entity instanceof Player)
        );

        return possibleTargets.isEmpty()
                ? null
                : possibleTargets.get(RANDOM.nextInt(possibleTargets.size()));
    }
}
