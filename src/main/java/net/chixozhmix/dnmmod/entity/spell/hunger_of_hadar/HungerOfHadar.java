package net.chixozhmix.dnmmod.entity.spell.hunger_of_hadar;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.black_hole.BlackHole;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class HungerOfHadar extends Projectile implements AntiMagicSusceptible {
    private static final EntityDataAccessor<Float> DATA_RADIUS;
    private static final EntityDataAccessor<Float> DATA_DAMAGE;
    private static final EntityDataAccessor<Integer> DATA_DURATION;

    List<Entity> trackingEntities;
    private int damageInterval = 20;
    private int ticksSinceLastDamage = 0;
    private Vec3 center;

    private int soundInterval = 40;
    private int ticksSinceLastSound = 0;
    private boolean hasPlayedStartSound = false;

    public HungerOfHadar(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.trackingEntities = new ArrayList();
        this.noCulling = true; // Важно для больших объектов
    }

    public HungerOfHadar(Level pLevel, LivingEntity owner, Vec3 center) {
        this((EntityType) ModEntityType.HUNGER_OF_HADADR.get(), pLevel);
        this.setOwner(owner);
        this.center = center;
        this.setPos(center.x, center.y, center.z);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            ticksSinceLastDamage++;
            if (ticksSinceLastDamage >= damageInterval) {
                applyAreaDamage();
                ticksSinceLastDamage = 0;
            }

            handleSound();

            if (this.tickCount >= getDuration()) {
                playEndSound();
                this.discard();
            }
        }

        this.level().addParticle(ParticleHelper.VOID_TENTACLE_FOG, this.getX() + Utils.getRandomScaled((double)0.5F), this.getY() + Utils.getRandomScaled((double)0.5F) + (double)0.2F, this.getZ() + Utils.getRandomScaled((double)0.5F), Utils.getRandomScaled((double)2.0F), (double)(-this.random.nextFloat() * 0.5F), Utils.getRandomScaled((double)2.0F));
    }

    private void handleSound() {
        ticksSinceLastSound++;

        if (!hasPlayedStartSound && tickCount > 0) {
            playStartSound();
            hasPlayedStartSound = true;
        }

        if (ticksSinceLastSound >= soundInterval) {
            playLoopSound();
            ticksSinceLastSound = 0;
        }
    }

    private void playStartSound() {
        if (this.level().isClientSide) return;

        this.level().playSound(
                null,
                this.getX(), this.getY(), this.getZ(),
                SoundRegistry.BLACK_HOLE_CAST.get(),
                SoundSource.HOSTILE,
                1.5F,
                0.7F + random.nextFloat() * 0.3F
        );
    }

    private void playLoopSound() {
        if (this.level().isClientSide) return;

        this.level().playSound(
                null,
                this.getX(), this.getY(), this.getZ(),
                SoundEvents.SCULK_SHRIEKER_SHRIEK,
                SoundSource.HOSTILE,
                0.8F + (float) Math.sin(tickCount * 0.1) * 0.2F,
                0.8F + (float) Math.sin(tickCount * 0.15) * 0.2F
        );

        if (tickCount % 80 == 0) { // Каждые 4 секунды
            this.level().playSound(
                    null,
                    this.getX(), this.getY(), this.getZ(),
                    SoundRegistry.BLACK_HOLE_LOOP.get(),
                    SoundSource.HOSTILE,
                    1.0F,
                    0.5F + random.nextFloat() * 0.5F
            );
        }

        if (random.nextInt(3) == 0) { // Случайный звук
            this.level().playSound(
                    null,
                    this.getX() + (random.nextDouble() - 0.5) * getRadius(),
                    this.getY() + random.nextDouble() * getRadius(),
                    this.getZ() + (random.nextDouble() - 0.5) * getRadius(),
                    SoundEvents.SCULK_BLOCK_SPREAD,
                    SoundSource.HOSTILE,
                    1.2F,
                    0.6F + random.nextFloat() * 0.6F
            );
        }
    }

    private void playEndSound() {
        if (this.level().isClientSide) return;

        // Звук исчезновения
        this.level().playSound(
                null,
                this.getX(), this.getY(), this.getZ(),
                SoundEvents.SCULK_SHRIEKER_SHRIEK,
                SoundSource.HOSTILE,
                1.2F,
                1.2F
        );

        // Звук схлопывания
        this.level().playSound(
                null,
                this.getX(), this.getY(), this.getZ(),
                SoundRegistry.BLACK_HOLE_CAST.get(),
                SoundSource.HOSTILE,
                1.2F,
                0.4F
        );
    }
    public void setSoundInterval(int interval) {
        this.soundInterval = interval;
    }

    // Метод для принудительного проигрывания звука
    public void playCustomSound(SoundEvent sound, float volume, float pitch) {
        if (this.level().isClientSide) return;

        this.level().playSound(
                null,
                this.getX(), this.getY(), this.getZ(),
                sound,
                SoundSource.HOSTILE,
                volume,
                pitch
        );
    }

    private void applyAreaDamage() {
        DamageSource damageSource = ((AbstractSpell) RegistrySpells.HUNGER_OF_HADAR.get())
                .getDamageSource(this, this.getOwner());

        float radius = getRadius();

        // Используем позицию центра для определения области
        Vec3 centerPos = center != null ? center : this.position();
        AABB area = new AABB(
                centerPos.x - radius, centerPos.y - radius, centerPos.z - radius,
                centerPos.x + radius, centerPos.y + radius, centerPos.z + radius
        );

        List<LivingEntity> entitiesInRange = this.level().getEntitiesOfClass(
                LivingEntity.class,
                area,
                entity -> entity != this.getOwner() && entity.isAlive()
        );

        for (LivingEntity target : entitiesInRange) {
            if (this.getOwner() instanceof LivingEntity caster) {
                DamageSources.applyDamage(
                        target,
                        getDamage(),
                        damageSource
                );
                applyEffects(target);

                // Звук при попадании по цели (только на сервере)
                if (!this.level().isClientSide && random.nextInt(3) == 0) {
                    this.level().playSound(
                            null,
                            target.getX(), target.getY(), target.getZ(),
                            SoundRegistry.BLACK_HOLE_LOOP.get(),
                            SoundSource.HOSTILE,
                            1.0F,
                            0.8F + random.nextFloat() * 0.4F
                    );
                }
            }
        }
    }

    private void applyEffects(LivingEntity target) {
        target.addEffect(new MobEffectInstance(
                MobEffects.BLINDNESS,
                40,
                0,
                false,
                true,
                true
        ));

        target.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                40,
                1,
                false,
                true,
                true
        ));
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        if (!this.level().isClientSide) {
            playEndSound();
        }
        this.discard();
    }

    @Override
    public void refreshDimensions() {
        float radius = getRadius();
        AABB boundingBox = new AABB(
                -radius, -radius, -radius,
                radius, radius, radius
        );
        this.setBoundingBox(boundingBox.move(this.position()));
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        float radius = getRadius();
        return new AABB(
                this.getX() - radius, this.getY() - radius, this.getZ() - radius,
                this.getX() + radius, this.getY() + radius, this.getZ() + radius
        );
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    public int getDuration() {
        return this.entityData.get(DATA_DURATION);
    }

    public void setDuration(int duration) {
        this.entityData.set(DATA_DURATION, duration);
    }

    public void setDamage(float damage) {
        this.entityData.set(DATA_DAMAGE, damage);
    }

    public float getDamage() {
        return this.entityData.get(DATA_DAMAGE);
    }

    public void setDamageInterval(int interval) {
        this.damageInterval = interval;
    }

    public int getDamageInterval() {
        return damageInterval;
    }

    public Vec3 getCenter() {
        return center;
    }

    public void setCenter(Vec3 center) {
        this.center = center;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_RADIUS, 5.0F);
        this.entityData.define(DATA_DAMAGE, 1.0F);
        this.entityData.define(DATA_DURATION, 300);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(pKey);
    }

    public void setRadius(float pRadius) {
        if (!this.level().isClientSide) {
            this.getEntityData().set(DATA_RADIUS, Math.min(pRadius, 15.0F));
            this.refreshDimensions();
        }
    }

    public float getRadius() {
        return this.entityData.get(DATA_RADIUS);
    }

    public Vec3 getRenderCenter() {
        return center != null ? center : this.position();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putFloat("Radius", this.getRadius());
        pCompound.putInt("Age", this.tickCount);
        pCompound.putFloat("Damage", this.getDamage());
        pCompound.putInt("Duration", this.getDuration());
        pCompound.putInt("DamageInterval", this.damageInterval);
        pCompound.putInt("SoundInterval", this.soundInterval);
        pCompound.putBoolean("HasPlayedStartSound", this.hasPlayedStartSound);
        if (center != null) {
            pCompound.putDouble("CenterX", center.x);
            pCompound.putDouble("CenterY", center.y);
            pCompound.putDouble("CenterZ", center.z);
        }
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.tickCount = pCompound.getInt("Age");
        this.setDamage(pCompound.getFloat("Damage"));
        this.setDuration(pCompound.getInt("Duration"));
        this.damageInterval = pCompound.getInt("DamageInterval");
        this.soundInterval = pCompound.getInt("SoundInterval");
        this.hasPlayedStartSound = pCompound.getBoolean("HasPlayedStartSound");
        if (this.getDamage() == 0.0F) {
            this.setDamage(1.0F);
        }
        if (this.damageInterval == 0) {
            this.damageInterval = 20;
        }
        if (this.soundInterval == 0) {
            this.soundInterval = 40;
        }
        if (pCompound.contains("Radius")) {
            this.setRadius(pCompound.getFloat("Radius"));
        }
        if (pCompound.contains("CenterX")) {
            this.center = new Vec3(
                    pCompound.getDouble("CenterX"),
                    pCompound.getDouble("CenterY"),
                    pCompound.getDouble("CenterZ")
            );
        }
        super.readAdditionalSaveData(pCompound);
    }

    private void updateTrackingEntities() {
        this.trackingEntities = this.level().getEntities(this, this.getBoundingBox().inflate(1.0F));
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    static {
        DATA_RADIUS = SynchedEntityData.defineId(BlackHole.class, EntityDataSerializers.FLOAT);
        DATA_DAMAGE = SynchedEntityData.defineId(BlackHole.class, EntityDataSerializers.FLOAT);
        DATA_DURATION = SynchedEntityData.defineId(BlackHole.class, EntityDataSerializers.INT);
    }
}
