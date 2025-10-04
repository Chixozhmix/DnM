package net.chixozhmix.dnmmod.api;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Random;
import java.util.function.Supplier;

public abstract class CustomMagicProjectile extends ThrowableItemProjectile {

    protected final float minDamage;
    protected final float maxDamage;
    protected final Supplier<? extends ParticleOptions> trailParticle;
    protected final int maxLifetime;
    protected final float gravity;
    protected final SoundEvent hitSound;
    protected final SoundEvent missSound;
    protected final MobEffect effect;
    protected final int effectDuration;
    protected final int effectAmplifier;
    protected final float effectProbability;

    // Конструктор без эффекта
    public CustomMagicProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level,
                                 float minDamage, float maxDamage,
                                 Supplier<? extends ParticleOptions> trailParticle,
                                 int maxLifetime, float gravity,
                                 SoundEvent hitSound, SoundEvent missSound) {
        this(entityType, level, minDamage, maxDamage, trailParticle, maxLifetime, gravity,
                hitSound, missSound, null, 0, 0, 0f);
    }

    // Конструктор с эффектом
    public CustomMagicProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level,
                                 float minDamage, float maxDamage,
                                 Supplier<? extends ParticleOptions> trailParticle,
                                 int maxLifetime, float gravity,
                                 SoundEvent hitSound, SoundEvent missSound,
                                 MobEffect effect, int effectDuration, int effectAmplifier, float effectProbability) {
        super(entityType, level);
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.trailParticle = trailParticle;
        this.maxLifetime = maxLifetime;
        this.gravity = gravity;
        this.hitSound = hitSound;
        this.missSound = missSound;
        this.effect = effect;
        this.effectDuration = effectDuration;
        this.effectAmplifier = effectAmplifier;
        this.effectProbability = effectProbability;
    }

    // Конструктор без эффекта
    public CustomMagicProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level, LivingEntity shooter,
                                 float minDamage, float maxDamage,
                                 Supplier<? extends ParticleOptions> trailParticle,
                                 int maxLifetime, float gravity,
                                 SoundEvent hitSound, SoundEvent missSound) {
        this(entityType, level, shooter, minDamage, maxDamage, trailParticle, maxLifetime, gravity,
                hitSound, missSound, null, 0, 0, 0f);
    }

    // Конструктор с эффектом
    public CustomMagicProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level, LivingEntity shooter,
                                 float minDamage, float maxDamage,
                                 Supplier<? extends ParticleOptions> trailParticle,
                                 int maxLifetime, float gravity,
                                 SoundEvent hitSound, SoundEvent missSound,
                                 MobEffect effect, int effectDuration, int effectAmplifier, float effectProbability) {
        super(entityType, shooter, level);
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.trailParticle = trailParticle;
        this.maxLifetime = maxLifetime;
        this.gravity = gravity;
        this.hitSound = hitSound;
        this.missSound = missSound;
        this.effect = effect;
        this.effectDuration = effectDuration;
        this.effectAmplifier = effectAmplifier;
        this.effectProbability = effectProbability;
    }

    // Конструктор без эффекта
    public CustomMagicProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level, double x, double y, double z,
                                 float minDamage, float maxDamage,
                                 Supplier<? extends ParticleOptions> trailParticle,
                                 int maxLifetime, float gravity,
                                 SoundEvent hitSound, SoundEvent missSound) {
        this(entityType, level, x, y, z, minDamage, maxDamage, trailParticle, maxLifetime, gravity,
                hitSound, missSound, null, 0, 0, 0f);
    }

    // Конструктор с эффектом
    public CustomMagicProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level, double x, double y, double z,
                                 float minDamage, float maxDamage,
                                 Supplier<? extends ParticleOptions> trailParticle,
                                 int maxLifetime, float gravity,
                                 SoundEvent hitSound, SoundEvent missSound,
                                 MobEffect effect, int effectDuration, int effectAmplifier, float effectProbability) {
        super(entityType, x, y, z, level);
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.trailParticle = trailParticle;
        this.maxLifetime = maxLifetime;
        this.gravity = gravity;
        this.hitSound = hitSound;
        this.missSound = missSound;
        this.effect = effect;
        this.effectDuration = effectDuration;
        this.effectAmplifier = effectAmplifier;
        this.effectProbability = effectProbability;
    }

    @Override
    public void tick() {
        super.tick();

        // Добавляем частицы за снарядом
        if (this.level().isClientSide && trailParticle != null) {
            for (int i = 0; i < 2; ++i) {
                this.level().addParticle(trailParticle.get(),
                        this.getX() + (this.random.nextDouble() - 0.5) * 0.2,
                        this.getY() + (this.random.nextDouble() - 0.5) * 0.2,
                        this.getZ() + (this.random.nextDouble() - 0.5) * 0.2,
                        0, 0, 0);
            }
        }

        if(this.tickCount >= maxLifetime) {
            if(!this.level().isClientSide)
                this.discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        Random random = new Random();
        float randomDamage = minDamage + random.nextFloat() * (maxDamage - minDamage);

        if(!this.level().isClientSide) {
            if(result.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHitResult = (EntityHitResult) result;
                if(entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                    // Наносим урон
                    livingEntity.hurt(this.damageSources().indirectMagic(this, this.getOwner()), randomDamage);

                    System.out.println("Projectile damage: " + randomDamage);

                    // Применяем эффект, если он есть и сработала вероятность
                    if (effect != null && random.nextFloat() <= effectProbability) {
                        livingEntity.addEffect(new MobEffectInstance(effect, effectDuration, effectAmplifier));
                        System.out.println("Applied effect: " + effect.getDisplayName().getString());
                    }

                    if (hitSound != null) {
                        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                                hitSound, SoundSource.NEUTRAL, 0.8F, 1.2F);
                    }
                } else {
                    if (missSound != null) {
                        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                                missSound, SoundSource.NEUTRAL, 0.5F, 1.5F);
                    }
                }

                this.level().broadcastEntityEvent(this, (byte) 3);
                this.discard();
            }
        }
    }

    @Override
    protected float getGravity() {
        return gravity;
    }

    // Абстрактный метод, который должен быть реализован в подклассах
    @Override
    protected abstract Item getDefaultItem();
}