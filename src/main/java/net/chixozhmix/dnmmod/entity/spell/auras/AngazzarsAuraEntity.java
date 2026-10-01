package net.chixozhmix.dnmmod.entity.spell.auras;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.Util.ParticleSpawnHelper;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class AngazzarsAuraEntity extends AbstractAura{
    public AngazzarsAuraEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void effectAura(LivingEntity entity) {

    }

    @Override
    public void tick() {
        super.tick();

        var entities = level().getEntities(this, this.getBoundingBox(), this::canHitEntity);

        for (int i = 0; i < 2; i++) {
            ParticleSpawnHelper.swirlingParticle(level(), this.getRadius(), position(), random.nextFloat() < 0.3 ? ParticleHelper.FIRE : ParticleTypes.LAVA);
        }

        for (Entity entity : entities) {
            if(entity instanceof LivingEntity) {
                if(!entity.isOnFire())
                    DamageSources.applyDamage(entity, this.damage, (RegistrySpells.ANGAZZARS_CLOAK.get()).getDamageSource(this, this.getOwner()).setFireTicks(60));
            } else if(entity instanceof AbstractArrow arrow && arrow.getOwner() != this.getOwner()) {
                arrow.discard();
                if(level() instanceof ServerLevel serverLevel)
                    serverLevel.sendParticles(ParticleHelper.FIRE, arrow.getX(), arrow.getY(), arrow.getZ(), 10, 0.4, 0.4, 0.4, 0.6);
            }
        }
    }

    @Override
    public void ambientParticles() {

    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return super.canHitEntity(pTarget) && !DamageSources.isFriendlyFireBetween(this.getOwner(), pTarget) || pTarget instanceof AbstractArrow;
    }
}
