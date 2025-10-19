package net.chixozhmix.dnmmod.entity.spell.ice_dagger;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.magic_missile.MagicMissileProjectile;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.entity.ModEntityType;
import net.chixozhmix.dnmmod.entity.spell.cloud_dagger.CloudDagger;
import net.chixozhmix.dnmmod.spell.RegistrySpells;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class IceDagger extends AbstractMagicProjectile implements GeoAnimatable {

    AnimatableInstanceCache cache  = GeckoLibUtil.createInstanceCache(this);

    public IceDagger(EntityType<? extends IceDagger> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public IceDagger(EntityType<? extends IceDagger> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        this.setOwner(shooter);
    }

    public IceDagger(Level levelIn, LivingEntity shooter) {
        this(ModEntityType.ICE_DAGGER.get(), levelIn, shooter);
    }

    @Override
    public void trailParticles() {
        for(int i = 0; i < 2; ++i) {
            double speed = 0.02;
            double dx = Utils.random.nextDouble() * (double)2.0F * speed - speed;
            double dy = Utils.random.nextDouble() * (double)2.0F * speed - speed;
            double dz = Utils.random.nextDouble() * (double)2.0F * speed - speed;
            this.level().addParticle(ParticleTypes.SNOWFLAKE, this.getX() + dx, this.getY() + dy, this.getZ() + dz, dx, dy, dz);
            if (this.tickCount > 1) {
                this.level().addParticle(ParticleTypes.SNOWFLAKE, this.getX() + dx - this.getDeltaMovement().x / (double)2.0F,
                        this.getY() + dy - this.getDeltaMovement().y / (double)2.0F,
                        this.getZ() + dz - this.getDeltaMovement().z / (double)2.0F, dx, dy, dz);
            }
        }
    }

    @Override
    public void impactParticles(double v, double v1, double v2) {
        MagicManager.spawnParticles(this.level(), ParticleTypes.SNOWFLAKE, v, v1, v2, 25, (double)0.0F, (double)0.0F, (double)0.0F, 0.18, true);

    }

    @Override
    public float getSpeed() {
        return 2.5f;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        DamageSources.applyDamage(pResult.getEntity(), this.damage, ((AbstractSpell) RegistrySpells.ICE_DAGGER.get()).getDamageSource(this, this.getOwner()));
        this.discard();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate)
                .setAnimationSpeed(1.5f));
    }

    private PlayState predicate(AnimationState<IceDagger> cloudDaggerAnimationState) {

        cloudDaggerAnimationState.setAnimation(RawAnimation.begin().thenLoop("idle"));

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }
}
