package net.chixozhmix.dnmmod.entity.corypheus;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import net.chixozhmix.dnmmod.entity.defiled_wizard.DefiledWizard;
import net.chixozhmix.dnmmod.entity.flame_atronach.FlameAtronachEntity;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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


public class CorypheusBoss extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker {
    private static final EntityDataAccessor<Boolean> DATA_IS_ANIMATING_RISE = SynchedEntityData.defineId(FlameAtronachEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(CorypheusBoss.class, EntityDataSerializers.INT);
    private boolean phaseTransitionTriggered = false;

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation RISE_ANIM = RawAnimation.begin().thenPlay("raise");
    private static final RawAnimation DEATH = RawAnimation.begin().thenPlay("death");

    private int riseAnimTime;
    private final AnimatableInstanceCache cache;
    private RawAnimation customAnimationToPlay;

    private final AnimationController<CorypheusBoss> movementController;
    private final AnimationController<CorypheusBoss> riseController;
    private final AnimationController<CorypheusBoss> attackController;

    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("entity.dnmmod.corypheus"),
            BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_6);

    private static final AttributeSupplier.Builder ATTRIBUTES = LivingEntity.createLivingAttributes()
            .add(Attributes.ATTACK_DAMAGE, (double)10.0F)
            .add(Attributes.ATTACK_KNOCKBACK, (double)0.4F)
            .add(Attributes.KNOCKBACK_RESISTANCE, (double)0.8F)
            .add(Attributes.MAX_HEALTH, (double)950.0F)
            .add(Attributes.FOLLOW_RANGE, (double)35.0F)
            .add((Attribute) AttributeRegistry.SPELL_POWER.get(), (double)1.5F)
            .add(Attributes.MOVEMENT_SPEED, (double)0.26F)
            .add(ForgeMod.ENTITY_REACH.get(), 7.0F)
            .add(Attributes.ARMOR, 10.0F)
            .add(AttributeRegistry.SPELL_RESIST.get(), 1.2F);


    public CorypheusBoss(Level pLevel) {
        this((EntityType) ModEntityType.CORYPHEUS.get(), pLevel);
        this.setPersistenceRequired();
    }

    public CorypheusBoss(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.riseAnimTime = 15;
        this.xpReward = 60;
        this.cache = GeckoLibUtil.createInstanceCache(this);

        this.movementController = new AnimationController<>(this, "movement", 0, this::movementPredicate);
        this.riseController = new AnimationController<>(this, "rise", 0, this::risePredicate);
        this.attackController = new AnimationController<>(this, "attack", 0, this::attackPredicate);

        if (this.isPhase(Phases.FirstPhase)) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0F);
        }
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return ATTRIBUTES;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_ANIMATING_RISE, false);
        this.entityData.define(PHASE, 0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (!pLevel.isClientSide()) {
            this.triggerRiseAnimation();
            this.updateMovementSpeed();
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(movementController);
        controllerRegistrar.add(riseController);
        controllerRegistrar.add(attackController);

        super.registerControllers(controllerRegistrar);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private PlayState attackPredicate(AnimationState<CorypheusBoss> state) {
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

    private PlayState movementPredicate(AnimationState<CorypheusBoss> state) {
        if (this.isAnimatingRise())
            return PlayState.STOP;


        if (state.isMoving()) {
            state.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    private PlayState risePredicate(AnimationState<CorypheusBoss> state) {
        if (!this.isAnimatingRise()) {
            return PlayState.STOP;
        }

        if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
            state.getController().setAnimation(RISE_ANIM);
        }

        return PlayState.CONTINUE;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        // Не даем упасть ниже половины HP в первой фазе
        if (this.isPhase(Phases.FirstPhase) && !phaseTransitionTriggered) {
            float halfHealth = this.getMaxHealth() / 2.0F;
            float newHealth = this.getHealth() - pAmount;
            if (newHealth < halfHealth) {
                pAmount = this.getHealth() - halfHealth;
            }
        }

        return super.hurt(pSource, pAmount);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        if (this.isAnimatingRise()) {

            if (--this.riseAnimTime < 0) {
                this.entityData.set(DATA_IS_ANIMATING_RISE, false);
                this.setXRot(0.0F);
                this.setOldPosAndRot();
            }
            return;
        }

        float halfHealth = this.getMaxHealth() / 2.0F;

        if (this.isPhase(Phases.FirstPhase) && !phaseTransitionTriggered && this.getHealth() <= halfHealth) {
            switchToFinalPhase();
        }
    }

    @Override
    public void swing(InteractionHand pHand) {
        this.playAnimation("melee_attack");
        super.swing(pHand);
    }

    private void switchToFinalPhase() {
        this.phaseTransitionTriggered = true;
        this.setPhase(Phases.FinalPhase);

        this.setHasUsedSingleAttack(false);

        this.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 1.0F);

        this.setFinalPhaseGoals();

        this.updateMovementSpeed();
    }

    private void updateMovementSpeed() {
        if (this.isPhase(Phases.FirstPhase)) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0F);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.26F);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
    }

    public boolean isAnimatingRise() {
        return this.entityData.get(DATA_IS_ANIMATING_RISE);
    }

    public void triggerRiseAnimation() {
        this.entityData.set(DATA_IS_ANIMATING_RISE, true);
    }

    @Override
    protected void registerGoals() {
        this.setFirstPhaseGoals();
        this.targetSelector.addGoal(1, new MomentHurtByTargetGoal(this,
                DefiledWizard.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Villager.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, AbstractIllager.class, true));
    }

    protected void setFirstPhaseGoals() {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, (AbstractSpell)SpellRegistry.RAISE_DEAD_SPELL.get(), 2, 2, 60, 160, 1));
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, (AbstractSpell)RegistrySpells.THUNDERWAVE.get(), 10, 10, 20, 40, 1)
        {
            @Override
            public void tick() {
                if (this.target != null) {
                    double distanceSquared = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
                    if (distanceSquared < 5) {
                        this.mob.getLookControl().setLookAt(this.target, 45.0F, 45.0F);
                        this.spellCastingMob.initiateCastSpell(this.spell, this.mob.getRandom().nextIntBetweenInclusive(this.minSpellLevel, this.maxSpellLevel));
                        this.stop();
                    }

                }
            }
        });
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    protected void setFinalPhaseGoals() {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, (new WarlockAttackGoal(this, (double)1.25F, 35, 55))
                .setSpells(
                        List.of(SpellRegistry.ACUPUNCTURE_SPELL.get(), SpellRegistry.SONIC_BOOM_SPELL.get(), SpellRegistry.ELDRITCH_BLAST_SPELL.get()),
                        List.of(SpellRegistry.SHADOW_SLASH.get()),
                        List.of(SpellRegistry.TELEPORT_SPELL.get()),
                        List.of(SpellRegistry.HEAL_SPELL.get(), RegistrySpells.AGATHYS_ARMOR_SPELL.get()))
                .setSingleUseSpell(SpellRegistry.BLACK_HOLE_SPELL.get(), 60, 220, 2, 3));
        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, (double)0.9F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }


    @Override
    public boolean isPushable() {
        return false;
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
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    public boolean isPhase(Phases phase) {
        return phase.value == this.getPhase();
    }

    private void setPhase(Phases phase) {
        this.entityData.set(PHASE, phase.value);
    }

    public int getPhase() {
        return this.entityData.get(PHASE);
    }

    public static enum Phases {
        FirstPhase(0),
        FinalPhase(1);

        final int value;

        private Phases(int value) {
            this.value = value;
        }
    }

    /* BOSS BAR */

    @Override
    public void startSeenByPlayer(ServerPlayer pServerPlayer) {
        super.startSeenByPlayer(pServerPlayer);

        this.bossEvent.addPlayer(pServerPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer pServerPlayer) {
        super.stopSeenByPlayer(pServerPlayer);

        this.bossEvent.removePlayer(pServerPlayer);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }
}
