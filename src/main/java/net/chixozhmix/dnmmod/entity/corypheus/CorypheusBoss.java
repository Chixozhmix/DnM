package net.chixozhmix.dnmmod.entity.corypheus;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import net.chixozhmix.dnmmod.entity.darkspawn_larva.DarkspawnLarva;
import net.chixozhmix.dnmmod.entity.defiled_wizard.DefiledWizard;
import net.chixozhmix.dnmmod.goals.CorypheusFirstPhaseAttackGoal;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;


public class CorypheusBoss extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker, IEntityAdditionalSpawnData {
    private static final EntityDataAccessor<Boolean> DATA_IS_ANIMATING_RISE = SynchedEntityData.defineId(CorypheusBoss.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(CorypheusBoss.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_USING_KNOCKBACK =
            SynchedEntityData.defineId(CorypheusBoss.class, EntityDataSerializers.BOOLEAN);

    private boolean phaseTransitionTriggered = false;
    private boolean finalPhaseTransitionTriggered = false;

    private int spawnTimer;
    private final int timer = 200;
    private final float spawnDistanceSqr = 144.0F;

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
            .add(Attributes.ATTACK_KNOCKBACK, (double)1.0F)
            .add(Attributes.KNOCKBACK_RESISTANCE, (double)0.8F)
            .add(Attributes.MAX_HEALTH, (double)950.0F)
            .add(Attributes.FOLLOW_RANGE, (double)35.0F)
            .add((Attribute) AttributeRegistry.SPELL_POWER.get(), (double)1.5F)
            .add(Attributes.MOVEMENT_SPEED, (double)0.25F)
            .add(ForgeMod.ENTITY_REACH.get(), 5.0F)
            .add(Attributes.ARMOR, 10.0F)
            .add(AttributeRegistry.SPELL_RESIST.get(), 1.3F);


    public CorypheusBoss(Level pLevel) {
        this((EntityType) ModEntityType.CORYPHEUS.get(), pLevel);
        this.setPersistenceRequired();
    }

    public CorypheusBoss(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.riseAnimTime = 70;
        this.xpReward = 60;
        this.cache = GeckoLibUtil.createInstanceCache(this);

        this.movementController = new AnimationController<>(this, "movement", 0, this::movementPredicate);
        this.riseController = new AnimationController<>(this, "rise", 0, this::risePredicate);
        this.attackController = new AnimationController<>(this, "attack", 0, this::attackPredicate);

        setSpawnTimer(timer);

        if (this.isPhase(Phases.FirstPhase)) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0F);
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0F);
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
        this.entityData.define(DATA_IS_USING_KNOCKBACK, false);
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

        if (isUsingKnockback()) {
            if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                state.getController().setAnimation(RawAnimation.begin().thenPlay("cast_t_pose"));
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
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isPhase(Phases.FirstPhase) && !phaseTransitionTriggered) {
            float halfHealth = this.getMaxHealth() / 1.5F;
            float newHealth = this.getHealth() - pAmount;
            if (newHealth < halfHealth) {
                pAmount = this.getHealth() - halfHealth;
            }
        }
        else if(this.isPhase(Phases.SecondPhase) && !finalPhaseTransitionTriggered) {
            float finalHealth = this.getMaxHealth() / 3.0F;
            float newHealth = this.getHealth() - pAmount;
            if (newHealth < finalHealth) {
                pAmount = this.getHealth() - finalHealth;
            }
        }

        if(isAnimatingRise() || pSource.getEntity() instanceof DarkspawnLarva)
            return false;

        return super.hurt(pSource, pAmount);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        if(this.getTarget() != null && (isPhase(Phases.FirstPhase) || isPhase(Phases.FinalPhase))) {
            double distanceSquared = this.distanceToSqr(this.getTarget());
            if(distanceSquared <= this.spawnDistanceSqr && this.isSpawning())
            {
                --this.spawnTimer;
                if(spawnTimer == 0 && !this.level().isClientSide) {
                    this.spawnDarkspawn(true);
                    this.spawnDarkspawn(false);
                    setSpawnTimer(timer);
                    }
            }
        }

        if (this.isAnimatingRise()) {
            if (--this.riseAnimTime < 0) {
                this.entityData.set(DATA_IS_ANIMATING_RISE, false);
                this.setXRot(0.0F);
                this.setOldPosAndRot();
            }
            return;
        }

        float halfHealth = this.getMaxHealth() / 1.5F;
        float finalHealth = this.getMaxHealth() / 3.0F;

        if (this.isPhase(Phases.FirstPhase) && !phaseTransitionTriggered && this.getHealth() <= halfHealth) {
            switchToSecondPhase();
        }

        if (this.isPhase(Phases.SecondPhase) && !finalPhaseTransitionTriggered && this.getHealth() <= finalHealth) {
            switchToFinalPhase();
        }
    }

    @Override
    public void swing(InteractionHand pHand) {
        this.playAnimation("melee_attack");
        super.swing(pHand);
    }

    private void switchToSecondPhase() {
        this.phaseTransitionTriggered = true;
        this.setPhase(Phases.SecondPhase);

        this.setHasUsedSingleAttack(false);

        this.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 1.0F);

        this.setSecondPhaseGoal();

        this.updateMovementSpeed();
    }

    private void switchToFinalPhase() {
        this.finalPhaseTransitionTriggered = true;
        this.setPhase(Phases.FinalPhase);

        this.setHasUsedSingleAttack(false);

        this.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 1.0F);

        this.setFinalPhaseGoals();

        this.updateMovementSpeed();
    }

    private void updateMovementSpeed() {
        if (this.isPhase(Phases.FirstPhase)) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0F);
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0F);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25F);
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8F);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("BossPhase")) {
            this.setPhaseValue(pCompound.getInt("BossPhase"));
        }
        phaseTransitionTriggered = pCompound.getBoolean("PhaseTransitionTriggered");
        finalPhaseTransitionTriggered = pCompound.getBoolean("finalPhaseTransitionTriggered");

        if (this.getPhase() == 1) {
            setSecondPhaseGoal();
        } else if (this.getPhase() == 2) {
            setFinalPhaseGoals();
        }
        updateMovementSpeed();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("BossPhase", this.getPhase());
        pCompound.putBoolean("PhaseTransitionTriggered", phaseTransitionTriggered);
        pCompound.putBoolean("finalPhaseTransitionTriggered", finalPhaseTransitionTriggered);
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
                DefiledWizard.class,
                DarkspawnLarva.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Villager.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, AbstractIllager.class, true));
    }

    protected void setFirstPhaseGoals() {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CorypheusFirstPhaseAttackGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    protected void setSecondPhaseGoal(){
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, (new WarlockAttackGoal(this, (double)1.25F, 30, 55))
                .setSpells(
                        List.of(SpellRegistry.STARFALL_SPELL.get(), SpellRegistry.ELDRITCH_BLAST_SPELL.get()),
                        List.of(SpellRegistry.SHADOW_SLASH.get()),
                        List.of(),
                        List.of())
                .setSingleUseSpell(SpellRegistry.SCULK_TENTACLES_SPELL.get(), 60, 220, 3, 4));
        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, (double)0.9F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    protected void setFinalPhaseGoals() {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, (new WizardAttackGoal(this, (double)1.15F, 35, 55))
                .setSpells(
                        List.of(SpellRegistry.TELEKINESIS_SPELL.get(), SpellRegistry.SONIC_BOOM_SPELL.get(), SpellRegistry.ELDRITCH_BLAST_SPELL.get()),
                        List.of(SpellRegistry.SHADOW_SLASH.get()),
                        List.of(SpellRegistry.TELEPORT_SPELL.get()),
                        List.of())
                .setSingleUseSpell(RegistrySpells.HUNGER_OF_HADAR.get(), 70, 220, 2, 3));
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, (AbstractSpell)RegistrySpells.SUMMON_DARKSPAWN_LARVA.get(), 1, 1, 80, 140, 1));
        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, (double)0.9F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    public void setUsingKnockback(boolean value) {
        this.entityData.set(DATA_IS_USING_KNOCKBACK, value);
    }

    public boolean isUsingKnockback() {
        return this.entityData.get(DATA_IS_USING_KNOCKBACK);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.spawnTimer);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.spawnTimer = buffer.readInt();
    }

    private boolean isSpawning() {
        return this.spawnTimer > 0;
    }

    private void setSpawnTimer(int timer) {
        this.spawnTimer = timer;
    }

    public void spawnDarkspawn(boolean left) {
        Level level = this.level();
        if (level instanceof ServerLevel serverLevel) {
            DarkspawnLarva larva = new DarkspawnLarva(ModEntityType.DARKSPAWN_LARVA.get(), level);
            float angle = (float)(left ? -90 : 90) * ((float)Math.PI / 180F);
            Vec3 offset = this.getForward().multiply((double)3.0F, (double)0.0F, (double)3.0F).scale((double)this.getScale()).yRot(angle);
            Vec3 spawn = Utils.moveToRelativeGroundLevel(this.level(), Utils.raycastForBlock(this.level(), this.getEyePosition(), this.position().add(offset), ClipContext.Fluid.NONE).getLocation(), 4);
            larva.moveTo(spawn.add((double)0.0F, 0.1, (double)0.0F));
            larva.setYRot(this.getYRot());
            larva.finalizeSpawn(serverLevel, this.level().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
            this.level().addFreshEntity(larva);
            this.level().playSound((Player)null, spawn.x, spawn.y, spawn.z, SoundEvents.WARDEN_EMERGE, this.getSoundSource(), 2.0F, 0.9F);
        }
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

    private void setPhaseValue(int phaseValue) {
        this.entityData.set(PHASE, phaseValue);
    }

    public static enum Phases {
        FirstPhase(0),
        SecondPhase(1),
        FinalPhase(2);

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
