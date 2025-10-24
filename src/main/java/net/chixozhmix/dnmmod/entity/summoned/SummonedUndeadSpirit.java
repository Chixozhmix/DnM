package net.chixozhmix.dnmmod.entity.summoned;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.effect.SummonTimer;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.chixozhmix.dnmmod.effect.ModEffects;
import net.chixozhmix.dnmmod.entity.ModEntityType;
import net.chixozhmix.dnmmod.entity.custom.UndeadSpiritEntity;
import net.chixozhmix.dnmmod.spell.RegistrySpells;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.UUID;

public class SummonedUndeadSpirit extends UndeadSpiritEntity implements MagicSummon, GeoAnimatable {
    private static final EntityDataAccessor<Boolean> DATA_IS_ANIMATING_RISE = SynchedEntityData.defineId(SummonedUndeadSpirit.class, EntityDataSerializers.BOOLEAN);

    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    private int riseAnimTime;
    private final AnimatableInstanceCache cache;
    private int attackAnimationTick = 0;

    // Основной конструктор для регистрации EntityType
    public SummonedUndeadSpirit(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.riseAnimTime = 40;
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.xpReward = 0;
    }


    // Упрощенный конструктор для прямого создания
    public SummonedUndeadSpirit(Level level, LivingEntity owner, boolean playRiseAnimation) {
        this(ModEntityType.SUMMONED_UNDEAD_SPIRIT.get(), level); // Используем зарегистрированный EntityType
        this.setSummoner(owner);
        if (playRiseAnimation) {
            this.triggerRiseAnimation();
        }
    }

    @Override
    public boolean isPreventingPlayerRest(Player pPlayer) {
        return !this.isAlliedTo(pPlayer);
    }



    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_ANIMATING_RISE, false);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "rise", 0, this::predicateRise));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        // Анимация атаки
        if (this.attackAnimationTick > 0) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("attack"));
            return PlayState.CONTINUE;
        }

        // Анимация движения
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("walk"));
            return PlayState.CONTINUE;
        }

        // Анимация покоя
        state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
        return PlayState.CONTINUE;
    }

    private <T extends GeoAnimatable> PlayState predicateRise(AnimationState<T> state) {
        if (!this.isAnimatingRise()) {
            return PlayState.STOP;
        } else {
            if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                state.getController().setAnimation(RawAnimation.begin().thenPlay("rise"));
            }
            return PlayState.CONTINUE;
        }
    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isAnimatingRise()) {
            if (this.level().isClientSide) {
                this.clientDiggingParticles(this);
            }

            if (--this.riseAnimTime < 0) {
                this.entityData.set(DATA_IS_ANIMATING_RISE, false);
                this.setXRot(0.0F);
                this.setOldPosAndRot();
            }
            return; // Важно: во время анимации появления не обновляем атаку
        }

        // Обновляем счетчик анимации атаки
        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }

        // Исправленная логика атаки
        if (this.swinging) {
            if (this.attackAnimationTick <= 0) {
                this.attackAnimationTick = 20; // Уменьшите длительность для лучшей синхронизации
            }
            this.swinging = false; // Сбрасываем флаг сразу
        }
    }

    @Override
    public double getTick(Object entity) {
        return (double) this.tickCount;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, (double)1.2F, true));
        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, (double)0.9F, 15.0F, 5.0F, false, 25.0F));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == this.getSummoner())).setAlertOthers(new Class[0]));
    }

    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(this.level(), this.cachedSummoner, this.summonerUUID);
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    @Override
    public void onRemovedFromWorld() {
        this.onRemovedHelper(this, (SummonTimer) ModEffects.SUMMON_UNDEAD_SPIRIT.get());
        super.onRemovedFromWorld();
    }

    @Override
    public void remove(RemovalReason pReason) {
        super.remove(pReason);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        this.swing(InteractionHand.MAIN_HAND);
        // Замените на ваше заклинание если нужно
        return Utils.doMeleeAttack(this, pEntity, (RegistrySpells.SUMMON_UNDEAD_SPIRIT.get()).getDamageSource(this, this.getSummoner()));
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || !this.isAnimatingRise() && !this.shouldIgnoreDamage(pSource) ? super.hurt(pSource, pAmount) : false;
    }

    @Override
    public void onUnSummon() {
        if (!this.level().isClientSide) {
            MagicManager.spawnParticles(this.level(), ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(),
                    25, 0.4, 0.8, 0.4, 0.03, false);
            this.discard();
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.summonerUUID = OwnerHelper.deserializeOwner(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        OwnerHelper.serializeOwner(pCompound, this.summonerUUID);
    }

    protected void clientDiggingParticles(LivingEntity livingEntity) {
        RandomSource randomsource = livingEntity.getRandom();
        BlockState blockstate = livingEntity.getBlockStateOn();
        if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
            for (int i = 0; i < 15; ++i) {
                double d0 = livingEntity.getX() + (double) Mth.randomBetween(randomsource, -0.5F, 0.5F);
                double d1 = livingEntity.getY();
                double d2 = livingEntity.getZ() + (double) Mth.randomBetween(randomsource, -0.5F, 0.5F);
                livingEntity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0.0F, 0.0F, 0.0F);
            }
        }
    }

    public boolean isAnimatingRise() {
        return this.entityData.get(DATA_IS_ANIMATING_RISE);
    }

    public void triggerRiseAnimation() {
        this.entityData.set(DATA_IS_ANIMATING_RISE, true);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.isAnimatingRise();
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isAnimatingRise();
    }
}
