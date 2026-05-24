package net.chixozhmix.dnmmod.entity.darkspawn_larva;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import net.chixozhmix.dnmmod.entity.modeus.ModeusBoss;
import net.chixozhmix.dnmmod.entity.defiled_priest.DefiledPriest;
import net.chixozhmix.dnmmod.entity.defiled_wizard.DefiledWizard;
import net.chixozhmix.dnmmod.entity.tainted_observer.DarkspawnObserver;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class DarkspawnLarva extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker {
    private static final EntityDataAccessor<Boolean> DATA_IS_ANIMATING_RISE = SynchedEntityData.defineId(DarkspawnLarva.class, EntityDataSerializers.BOOLEAN);

    private static final RawAnimation RISE_ANIM = RawAnimation.begin().thenPlay("raise");

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");

    private RawAnimation customAnimationToPlay;
    private int riseAnimTime;

    private final AnimatableInstanceCache cache;

    private final AnimationController<DarkspawnLarva> movementController;
    private final AnimationController<DarkspawnLarva> attackController;
    private final AnimationController<DarkspawnLarva> riseController;

    private static final AttributeSupplier.Builder ATTRIBUTES = LivingEntity.createLivingAttributes()
            .add(Attributes.ATTACK_DAMAGE, (double)5.0F)
            .add(Attributes.ATTACK_KNOCKBACK, (double)0.1F)
            .add(Attributes.MAX_HEALTH, (double)20.0F)
            .add(Attributes.FOLLOW_RANGE, (double)25.0F)
            .add((Attribute) AttributeRegistry.SPELL_POWER.get(), (double)0.3F)
            .add(Attributes.MOVEMENT_SPEED, (double)0.27F)
            .add(ForgeMod.ENTITY_REACH.get(), 3.0F);

    public DarkspawnLarva(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.xpReward = 5;
        this.riseAnimTime = 40;
        this.customAnimationToPlay = null;

        this.movementController = new AnimationController<>(this, "movement", 0, this::movementPredicate);
        this.attackController = new AnimationController<>(this, "attack", 0, this::attackPredicate);
        this.riseController = new AnimationController<>(this, "rise", 0, this::risePredicate);

        this.triggerRiseAnimation();
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return ATTRIBUTES;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_ANIMATING_RISE, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(movementController);
        controllerRegistrar.add(attackController);
        controllerRegistrar.add(riseController);

        super.registerControllers(controllerRegistrar);
    }

    private PlayState movementPredicate(AnimationState<DarkspawnLarva> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationState<DarkspawnLarva> state) {
        if (customAnimationToPlay != null) {
            state.getController().setAnimation(customAnimationToPlay);

            if (state.getController().hasAnimationFinished()) {
                customAnimationToPlay = null;
                state.getController().forceAnimationReset();
            }

            return PlayState.CONTINUE;
        }

        state.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    private PlayState risePredicate(AnimationState<DarkspawnLarva> state) {
        if (!this.isAnimatingRise()) {
            return PlayState.STOP;
        }

        if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
            state.getController().setAnimation(RISE_ANIM);
        }

        return PlayState.CONTINUE;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, (new WarlockAttackGoal(this, (double)1.25F, 45, 55))
                .setSpells(
                        List.of(SpellRegistry.ELDRITCH_BLAST_SPELL.get()),
                        List.of(),
                        List.of(),
                        List.of()));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this,
                DefiledWizard.class,
                DefiledPriest.class,
                ModeusBoss.class,
                DarkspawnLarva.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
    }

    @Override
    public boolean shouldBeExtraAnimated() {
        return this.isCasting();
    }

    @Override
    public boolean shouldAlwaysAnimateHead() {
        return customAnimationToPlay == null;
    }

    @Override
    public boolean shouldPointArmsWhileCasting() {
        return customAnimationToPlay == null;
    }

    @Override
    public void playAnimation(String animationName) {
        this.customAnimationToPlay = RawAnimation.begin().thenPlay(animationName);
        this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
    }

    @Override
    public void swing(InteractionHand pHand) {
        this.playAnimation("attack");
        super.swing(pHand);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isAnimatingRise()) {

            if (--this.riseAnimTime < 0) {
                this.entityData.set(DATA_IS_ANIMATING_RISE, false);
                this.setXRot(0.0F);
                this.setOldPosAndRot();
            }
            return;
        }
    }

    public boolean isAnimatingRise() {
        return this.entityData.get(DATA_IS_ANIMATING_RISE);
    }

    public void triggerRiseAnimation() {
        this.entityData.set(DATA_IS_ANIMATING_RISE, true);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public boolean addEffect(MobEffectInstance pEffectInstance, @Nullable Entity pEntity) {
        if(pEffectInstance.getEffect() == MobEffects.POISON || pEffectInstance.getEffect() == ModEffects.CORPSE_POISON.get())
            return false;

        return super.addEffect(pEffectInstance, pEntity);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(pSource.getEntity() instanceof DarkspawnLarva || pSource.getEntity() instanceof ModeusBoss
                || pSource.getEntity() instanceof DarkspawnObserver)
            return false;

        return super.hurt(pSource, pAmount);
    }
}
