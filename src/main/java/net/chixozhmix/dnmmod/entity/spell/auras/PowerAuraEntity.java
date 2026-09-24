package net.chixozhmix.dnmmod.entity.spell.auras;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;



public class PowerAuraEntity extends AbstractAura {
    public PowerAuraEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void effectAura(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40));
    }

    @Override
    public void tick() {
        super.tick();

        var entities = level().getEntities(this, this.getBoundingBox(), this::canHitEntity);

        for (Entity entity : entities) {
            if(entity instanceof LivingEntity livingEntity && livingEntity.distanceToSqr(this) < this.getRadius() * this.getRadius()) {
                if(this.tickCount % 10 == 0)
                    applyEffect(livingEntity);
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return DamageSources.isFriendlyFireBetween(this.getOwner(), pTarget) && Utils.shouldHealEntity(this.getOwner(), pTarget);
    }
}
