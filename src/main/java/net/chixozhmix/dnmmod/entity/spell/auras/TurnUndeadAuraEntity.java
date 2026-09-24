package net.chixozhmix.dnmmod.entity.spell.auras;

import io.redspace.ironsspellbooks.damage.DamageSources;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class TurnUndeadAuraEntity extends AbstractAura{
    public TurnUndeadAuraEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void effectAura(LivingEntity entity) {

    }

    @Override
    public void tick() {
        super.tick();

        var entities = level().getEntities(this, this.getBoundingBox(), this::canHitEntity);

        for (Entity entity : entities) {
            if(entity instanceof LivingEntity livingEntity && livingEntity.distanceToSqr(this) < this.getRadius() * this.getRadius()) {
                if(this.tickCount % 35 == 0)
                    DamageSources.applyDamage(entity, this.damage, (RegistrySpells.TURN_UNDEAD_AURA.get()).getDamageSource(this, this.getOwner()));
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        if(pTarget instanceof LivingEntity livingEntity)
            return livingEntity.getMobType() == MobType.UNDEAD && !DamageSources.isFriendlyFireBetween(this.getOwner(), pTarget);

        return false;
    }
}
