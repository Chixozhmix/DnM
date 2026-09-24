package net.chixozhmix.dnmmod.entity.spell.auras;

import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public abstract class AbstractAura extends AoeEntity {
    public AbstractAura(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCircular();
        this.reapplicationDelay = 10;
    }

    @Override
    public void applyEffect(LivingEntity livingEntity) {
        effectAura(livingEntity);
    }

    public abstract void effectAura(LivingEntity entity);

    @Override
    public float getParticleCount() {
        return 0;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public void tick() {
        Entity owner = this.getOwner();

        if(owner == null || !owner.isAlive()) {
            this.discard();
            return;
        }

        this.setPos(owner.getX(), owner.getY(), owner.getZ());
        this.setDeltaMovement(Vec3.ZERO);

        super.tick();
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, setHeight());
    }

    public float setHeight() {
        if(this.getOwner() != null)
            return this.getOwner().getBbHeight() + 2;

        return 3;
    }
}
