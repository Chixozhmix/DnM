package net.chixozhmix.dnmmod.effect.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class CompelledDuelEffect extends MobEffect {
    public static final String TARGET_UUID = "CompelledDuelTarget";

    public static final TagKey<EntityType<?>> BOSSES =
            ForgeRegistries.ENTITY_TYPES.tags().createTagKey(new ResourceLocation("forge", "bosses"));

    public CompelledDuelEffect() {
        super(MobEffectCategory.HARMFUL, 0xC9A00C);
    }

    public static void setTarget(Mob mob, LivingEntity target) {
        if(target.isAlive()) {
            mob.getPersistentData().putUUID(TARGET_UUID, target.getUUID());
            mob.setTarget(target);
        }
    }

    public static UUID getTargetUUID(Mob mob) {
        if (mob.getPersistentData().hasUUID(TARGET_UUID)) {
            return mob.getPersistentData().getUUID(TARGET_UUID);
        }

        return null;
    }

    public static void clearTarget(Mob mob) {
        mob.getPersistentData().remove(TARGET_UUID);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!(livingEntity instanceof Mob mob) || livingEntity.level().isClientSide) return;

        if (livingEntity.getType().is(BOSSES)) {
            clearTarget(mob);
            return;
        }

        UUID targetUUID = getTargetUUID(mob);

        if (targetUUID == null) return;

        Level level = mob.level();

        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;

        Entity targetEntity = serverLevel.getEntity(targetUUID);
        if (targetEntity instanceof LivingEntity target) {
            if (target.isAlive()) {
                if (mob.getTarget() != target) {
                    mob.setTarget(target);
                }
            } else {
                clearTarget(mob);
            }
        } else {
            clearTarget(mob);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }
}
