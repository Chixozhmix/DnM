package net.chixozhmix.dnmmod.entity.spell.meteor;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.network.particles.FieryExplosionParticlesPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ExplosionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class MeteorEntity extends AbstractMagicProjectile {
    public MeteorEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public MeteorEntity(Level pLevel, LivingEntity pShooter) {
        this((EntityType) ModEntityType.METEOR_ENTITY.get(), pLevel);
        this.setOwner(pShooter);
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() - vec3.x;
        double d1 = this.getY() - vec3.y;
        double d2 = this.getZ() - vec3.z;
        int count = Mth.clamp((int)(vec3.lengthSqr() * (double)2.0F), 1, 4);

        for(int i = 0; i < count; ++i) {
            Vec3 random = Utils.getRandomVec3((double)(this.getBbHeight() * 0.2F));
            float f = (float)i / (float)count;
            double x = Mth.lerp((double)f, d0, this.getX() + vec3.x);
            double y = Mth.lerp((double)f, d1, this.getY() + vec3.y);
            double z = Mth.lerp((double)f, d2, this.getZ() + vec3.z);
            this.level().addParticle(ParticleHelper.FIERY_SMOKE, true, x - random.x, y + (double)(this.getBbHeight() * 0.5F) - random.y, z - random.z, (double)0.0F, (double)0.0F, (double)0.0F);
        }
    }

    @Override
    public void impactParticles(double v, double v1, double v2) {

    }

    @Override
    public float getSpeed() {
        return 1.10f;
    }

    @Override
    public Optional<Supplier<SoundEvent>> getImpactSound() {
        return Optional.of((Supplier)() -> SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        if (!this.level().isClientSide) {
            this.impactParticles(this.xOld, this.yOld, this.zOld);
            float explosionRadius = this.getExplosionRadius();
            float explosionRadiusSqr = explosionRadius * explosionRadius;
            List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().inflate((double)explosionRadius));

            for(Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(hitResult.getLocation());
                if (distanceSqr < (double)explosionRadiusSqr && this.canHitEntity(entity)) {
                    double p = (double)1.0F - distanceSqr / (double)explosionRadiusSqr;
                    float damage = (float)((double)this.damage * p);
                    DamageSources.applyDamage(entity, damage, ((AbstractSpell) RegistrySpells.METEOR_SWARM.get()).getDamageSource(this, this.getOwner()));
                }
            }

            Explosion explosion = new Explosion(this.level(), (Entity)null, ((AbstractSpell)RegistrySpells.METEOR_SWARM.get()).getDamageSource(this, this.getOwner()), (ExplosionDamageCalculator)null, this.getX(), this.getY(), this.getZ(), this.getExplosionRadius(), true, Explosion.BlockInteraction.DESTROY);
            if (!MinecraftForge.EVENT_BUS.post(new ExplosionEvent.Start(this.level(), explosion))) {
                explosion.explode();
                explosion.finalizeExplosion(false);
            }

            PacketDistributor.sendToPlayersTrackingEntity(this, new FieryExplosionParticlesPacket(hitResult.getLocation().subtract(this.getDeltaMovement().scale((double)0.5F)), this.getExplosionRadius()));
            this.playSound(SoundEvents.GENERIC_EXPLODE, 4.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F);
            this.discardHelper(hitResult);
            CameraShakeManager.addCameraShake(new CameraShakeData(50, this.position(), 25.0F));
        }
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }
}
