package net.chixozhmix.dnmmod.effect.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;

public class CompelledDuelEffect extends MobEffect {
    public static final TagKey<EntityType<?>> BOSSES =
            ForgeRegistries.ENTITY_TYPES.tags().createTagKey(new ResourceLocation("forge", "bosses"));

    private LivingEntity target;

    public CompelledDuelEffect() {
        super(MobEffectCategory.HARMFUL, 0xC9A00C);
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public LivingEntity getTarget() {
        return this.target;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide && pLivingEntity instanceof Mob mob && !(pLivingEntity.getType().is(BOSSES))) {
            LivingEntity newTarget = getTarget();

            if (newTarget != null && newTarget != mob.getTarget()) {
                mob.setTarget(newTarget);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 20 == 0;
    }
}
