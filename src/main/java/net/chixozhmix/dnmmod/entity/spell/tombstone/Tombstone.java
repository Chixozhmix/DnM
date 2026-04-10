package net.chixozhmix.dnmmod.entity.spell.tombstone;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.chixozhmix.dnmmod.registers.SoundsRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
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
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.UUID;

public class Tombstone extends LivingEntity implements GeoEntity, AntiMagicSusceptible {

    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private int age;
    private float damage;
    private final AnimatableInstanceCache cache  = GeckoLibUtil.createInstanceCache(this);


    public Tombstone(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public Tombstone(Level level, LivingEntity owner, float damage) {
        this((EntityType) ModEntityType.TOMBSTONE.get(), level);
        this.setOwner(owner);
        this.setDamage(damage);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            if (this.age > 300) {
                this.discard();
            } else if (this.age < 280 && this.age % 20 == 0) {
                this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate((double)0.5F)).forEach(this::dealDamage);
                if (Utils.random.nextFloat() < 0.15F) {
                    this.playSound((SoundEvent) SoundsRegistry.GHOST_AMBIENT.get(), 1.5F, 0.5F + Utils.random.nextFloat() * 0.65F);
                }
            }

            if (this.age == 260 && Utils.random.nextFloat() < 0.3F) {
                this.playSound((SoundEvent)SoundsRegistry.NECRO_MAGIC.get(), 2.0F, 1.0F);
            }
        } else if (this.age < 280 && Utils.random.nextFloat() < 0.15F) {
            this.level().addParticle(ParticleHelper.VOID_TENTACLE_FOG, this.getX() + (random.nextDouble() - 0.5) * 0.5,
                    this.getY() + 0.1,  // у ног
                    this.getZ() + (random.nextDouble() - 0.5) * 0.5,
                    0, 0.05, 0);
        }

        ++this.age;
    }

    public boolean dealDamage(LivingEntity target) {
        if (target != this.getOwner() && DamageSources.applyDamage(target, this.damage, ((AbstractSpell) RegistrySpells.BURIAL_GROUND.get()).getDamageSource(this, this.getOwner()))) {
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return !pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) ? false : super.hurt(pSource, pAmount);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
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
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        MagicManager.spawnParticles(this.level(), ParticleTypes.SMOKE, this.getX(), this.getY() + (double)1.0F, this.getZ(), 50, 0.2, (double)1.25F, 0.2, 0.08, false);
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
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Age", this.age);
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.age = pCompound.getInt("Age");
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }
}
