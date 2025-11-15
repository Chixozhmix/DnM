package net.chixozhmix.dnmmod.entity.spell.cloud_dagger;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.entity.ModEntityType;
import net.chixozhmix.dnmmod.spell.RegistrySpells;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.UUID;

public class CloudDagger extends LivingEntity implements GeoEntity, AntiMagicSusceptible {

    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private float damage;
    private int age;
    private int lifetime = 100;
    private final AnimatableInstanceCache cache  = GeckoLibUtil.createInstanceCache(this);

    public CloudDagger(EntityType<? extends CloudDagger> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    public CloudDagger(Level level, LivingEntity owner, float damage) {
        this(ModEntityType.MAGIC_DAGGER.get(), level);
        this.setOwner(owner);
        this.setDamage(damage);
        this.lifetime = 100 + level.random.nextInt(40); // 5-7 seconds lifetime
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            // Check for expiration
            if (this.age > this.lifetime) {
                this.discard();
                return;
            }

            // Damage nearby entities every 10 ticks
            if (this.age % 10 == 0) {
                this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.5))
                        .forEach(this::dealDamage);
            }

            // Occasional sound effects
            if (this.age % 40 == 0 && Utils.random.nextFloat() < 0.2F) {
                this.playSound(SoundRegistry.VOID_TENTACLES_AMBIENT.get(), 1.0F, 1.2F + Utils.random.nextFloat() * 0.3F);
            }

        } else {
            // Client-side particle effects
            if (Utils.random.nextFloat() < 0.3F) {
                // Sparkle particles
                this.level().addParticle(ParticleHelper.UNSTABLE_ENDER,
                        this.getX() + Utils.getRandomScaled(0.3),
                        this.getY() + Utils.getRandomScaled(0.3),
                        this.getZ() + Utils.getRandomScaled(0.3),
                        Utils.getRandomScaled(0.02),
                        Utils.getRandomScaled(0.02),
                        Utils.getRandomScaled(0.02));
            }
        }

        ++this.age;
    }

    public boolean dealDamage(LivingEntity target) {
        if (target != this.getOwner() && target.isAlive() && !target.isInvulnerable()) {
            if (DamageSources.applyDamage(target, this.damage,
                    ((AbstractSpell) RegistrySpells.CLOUD_DAGGER.get()).getDamageSource(this, this.getOwner()))) {

                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldShowName() {
        return false;
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Override
    public Component getName() {
        return Component.empty();
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return !pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) ? false : super.hurt(pSource, pAmount);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }
        return this.owner;
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        MagicManager.spawnParticles(this.level(), ParticleTypes.SMOKE,
                this.getX(), this.getY(), this.getZ(), 20, 0.1, 0.1, 0.1, 0.05, false);
        this.discard();
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Age", this.age);
        pCompound.putInt("Lifetime", this.lifetime);
        pCompound.putFloat("Damage", this.damage);
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.age = pCompound.getInt("Age");
        this.lifetime = pCompound.getInt("Lifetime");
        this.damage = pCompound.getFloat("Damage");
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
            return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::animationPredicate)
                .setAnimationSpeed(2.0f));
    }

    private PlayState animationPredicate(AnimationState<CloudDagger> cloudDaggerAnimationState) {


        cloudDaggerAnimationState.setAnimation(RawAnimation.begin().thenLoop("idle"));

        return PlayState.CONTINUE;
    }


    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
