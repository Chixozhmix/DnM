package net.chixozhmix.dnmmod.entity.spell.auras;

import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.util.ParticleHelper;
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

    @Override
    public void ambientParticles() {
        if (!level().isClientSide) return;

        Vec3 center = position();
        float radius = getRadius();
        int count = (int) (8 * (radius * radius / 64f));
        for (int i = 0; i < count; i++) {
            double angle = this.random.nextDouble() * Math.PI * 2;
            double distance = Math.sqrt(this.random.nextDouble()) * radius;
            double x = Math.cos(angle) * distance;
            double z = Math.sin(angle) * distance;
            double y = this.random.nextDouble() * 2.0;

            level().addParticle(ParticleHelper.WISP, center.x + x, center.y + y, center.z + z, 0.0, 0.02, 0.0);
        }
    }
}
