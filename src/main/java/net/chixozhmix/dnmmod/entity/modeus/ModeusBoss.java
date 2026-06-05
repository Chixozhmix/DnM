package net.chixozhmix.dnmmod.entity.modeus;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import net.chixozhmix.dnmmod.entity.darkspawn_larva.DarkspawnLarva;
import net.chixozhmix.dnmmod.entity.defiled_wizard.DefiledWizard;
import net.chixozhmix.dnmmod.entity.leshy.LeshyEntity;
import net.chixozhmix.dnmmod.entity.tainted_observer.DarkspawnObserver;
import net.chixozhmix.dnmmod.goals.*;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;


public class ModeusBoss extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker, IEntityAdditionalSpawnData, IBeamAttackMob {
    private static final EntityDataAccessor<Boolean> DATA_IS_ANIMATING_RISE = SynchedEntityData.defineId(ModeusBoss.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(ModeusBoss.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_USING_KNOCKBACK =
            SynchedEntityData.defineId(ModeusBoss.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_USING_AOE =
            SynchedEntityData.defineId(ModeusBoss.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_USING_TENTACLE =
            SynchedEntityData.defineId(ModeusBoss.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TARGET_ID =
            SynchedEntityData.defineId(ModeusBoss.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MELEE_ATTACK_TYPE =
            SynchedEntityData.defineId(ModeusBoss.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_USING_LOSS_MAGE =
            SynchedEntityData.defineId(ModeusBoss.class, EntityDataSerializers.BOOLEAN);

    private boolean phaseTransitionTriggered = false;
    private boolean finalPhaseTransitionTriggered = false;

    private AOEModeusAttackGoal aoeAttackGoal;
    private TentacleModeusAttackGoal tentackeAttackGoal;

    private Vec3 spawnPos = null;

    private int spawnTimer;
    private final int timer = 260;
    private final float spawnDistanceSqr = 1225.0F;
    private boolean isInvulnerable = false;

    private final int attackDuration = 40;
    public int clientSideAttackTime;

    private int lossMageAnimTimer = 0;
    private static final int LOSS_MAGE_ANIM_DURATION = 13;

    private int effectTimer = 0;

    private final float halfHealth;
    private final float finalHealth;

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation RISE_ANIM = RawAnimation.begin().thenPlay("raise");

    private static final String CAST_T_POSE = "cast_t_pose";
    private static final String CHARGE_SPLIT = "charge_spit";
    private static final String RADIAL_BEAM_CHARGE = "radial_beam_charge";
    private static final String CHARGE_TENTACLE_ATTACK = "charge_tentackle_attack";
    private static final String KATANA_SLASH = "katana_upslash";

    private int riseAnimTime;
    private final AnimatableInstanceCache cache;
    private RawAnimation customAnimationToPlay;

    private final AnimationController<ModeusBoss> movementController;
    private final AnimationController<ModeusBoss> riseController;
    private final AnimationController<ModeusBoss> attackController;

    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("entity.dnmmod.modeus"),
            BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_6);

    private static final AttributeSupplier.Builder ATTRIBUTES = LivingEntity.createLivingAttributes()
            .add(Attributes.ATTACK_DAMAGE, (double)10.0F)
            .add(Attributes.ATTACK_KNOCKBACK, (double)1.0F)
            .add(Attributes.KNOCKBACK_RESISTANCE, (double)0.8F)
            .add(Attributes.MAX_HEALTH, (double)950.0F)
            .add(Attributes.FOLLOW_RANGE, (double)58.0F)
            .add((Attribute) AttributeRegistry.SPELL_POWER.get(), (double)1.5F)
            .add((Attribute) AttributeRegistry.ELDRITCH_MAGIC_RESIST.get(), (double)1.3F)
            .add(Attributes.MOVEMENT_SPEED, (double)0.26F)
            .add(ForgeMod.ENTITY_REACH.get(), 5.0F)
            .add(Attributes.ARMOR, 10.0F)
            .add(AttributeRegistry.SPELL_RESIST.get(), 1.3F);


    public ModeusBoss(Level pLevel) {
        this((EntityType) ModEntityType.MODEUS.get(), pLevel);
        this.setPersistenceRequired();
    }

    public ModeusBoss(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.riseAnimTime = 70;
        this.xpReward = 60;
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.customAnimationToPlay = null;

        this.halfHealth = this.getMaxHealth() / 1.5F;
        this.finalHealth = this.getMaxHealth() / 3.0F;

        this.movementController = new AnimationController<>(this, "movement", 2, this::movementPredicate);
        this.riseController = new AnimationController<>(this, "rise", 2, this::risePredicate);
        this.attackController = new AnimationController<>(this, "attack", 2, this::attackPredicate);

        this.aoeAttackGoal = new AOEModeusAttackGoal(this, 40, 80, 200,
                8, 12.0F, 40.0F, 2.0, 0x0D7278);
        this.tentackeAttackGoal = new TentacleModeusAttackGoal(
                this,
                40,
                140,
                200,
                35,
                10.0F,
                25.0F
        );

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
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!level().isClientSide && spawnPos == null) {
            this.spawnPos = this.position();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_ANIMATING_RISE, false);
        this.entityData.define(PHASE, 0);
        this.entityData.define(DATA_IS_USING_KNOCKBACK, false);
        this.entityData.define(DATA_IS_USING_AOE, false);
        this.entityData.define(DATA_IS_USING_TENTACLE, false);
        this.entityData.define(DATA_ATTACK_TARGET_ID, 0);
        this.entityData.define(MELEE_ATTACK_TYPE, 0);
        this.entityData.define(IS_USING_LOSS_MAGE, false);
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

    private PlayState attackPredicate(AnimationState<ModeusBoss> state) {
        if (customAnimationToPlay != null) {
            state.getController().setAnimation(customAnimationToPlay);

            if (state.getController().hasAnimationFinished()) {
                customAnimationToPlay = null;
                state.getController().forceAnimationReset();
            }

            return PlayState.CONTINUE;
        }

        if (isLossMage()) {
            if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                state.getController().setAnimation(RawAnimation.begin().thenPlay(KATANA_SLASH));
            }
            return PlayState.CONTINUE;
        }

        if (isUsingKnockback()) {
            if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                state.getController().setAnimation(RawAnimation.begin().thenPlay(CAST_T_POSE));
            }
            return PlayState.CONTINUE;
        }

        if(hasActiveAttackTarget()) {
            if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                state.getController().setAnimation(RawAnimation.begin().thenPlay(CHARGE_SPLIT));
            }
            return PlayState.CONTINUE;
        }

        if(isUsingAOE()) {
            if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                state.getController().setAnimation(RawAnimation.begin().thenPlay(RADIAL_BEAM_CHARGE));
            }
            return PlayState.CONTINUE;
        }

        if(isUsingTentacle()) {
            if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                state.getController().setAnimation(RawAnimation.begin().thenPlay(CHARGE_TENTACLE_ATTACK));
            }
            return PlayState.CONTINUE;
        }

        state.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    private PlayState movementPredicate(AnimationState<ModeusBoss> state) {
        if (this.isAnimatingRise())
            return PlayState.STOP;

        if (state.isMoving()) {
            state.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    private PlayState risePredicate(AnimationState<ModeusBoss> state) {
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
            float newHealth = this.getHealth() - pAmount;
            if (newHealth < this.halfHealth) {
                pAmount = this.getHealth() - this.halfHealth;
            }
        }
        else if(this.isPhase(Phases.SecondPhase) && !finalPhaseTransitionTriggered) {
            float newHealth = this.getHealth() - pAmount;
            if (newHealth < this.finalHealth) {
                pAmount = this.getHealth() - this.finalHealth;
            }
        }

        if (isInvulnerable) {
            return false;
        }

        if(isAnimatingRise() || pSource.getEntity() instanceof DarkspawnLarva)
            return false;

        return super.hurt(pSource, pAmount);
    }

    public void checkDistanceToSpawn() {
        if(spawnPos == null || level().isClientSide) return;

        double maxDistance = 30;
        if(this.distanceToSqr(spawnPos) > maxDistance * maxDistance) {
            this.teleportTo(spawnPos.x, spawnPos.y, spawnPos.z);

            MagicManager.spawnParticles(level(), ParticleTypes.PORTAL,
                    spawnPos.x, spawnPos.y + 1, spawnPos.z, 50, 0.5, 0.5, 0.5, 0.1, true);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.effectTimer > 0) {
            this.effectTimer--;
        } else if (isPhase(Phases.FirstPhase)) {
            if(this.getTarget() != null)
                lossMagicEffect();
        }

        if (lossMageAnimTimer > 0) {
            lossMageAnimTimer--;
            if (lossMageAnimTimer <= 0) {
                setUsingLossMage(false);
            }
        }

        if (!this.level().isClientSide) {
            if (this.aoeAttackGoal != null) {
                this.aoeAttackGoal.updateCooldown();
            }
            if (this.tentackeAttackGoal != null) {
                this.tentackeAttackGoal.updateCooldown();
            }
        }

        if (this.level().isClientSide) {
            if (this.hasActiveAttackTarget()) {
                if (this.clientSideAttackTime < this.attackDuration) {
                    ++this.clientSideAttackTime;
                }
            } else {
                this.clientSideAttackTime = 0;
            }
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

        if (this.getTarget() != null && !this.isAnimatingRise()) {
            checkDistanceToSpawn();
        }

        if (this.isPhase(Phases.FirstPhase) && !phaseTransitionTriggered && this.getHealth() <= this.halfHealth) {
            switchToSecondPhase();
        }

        if (this.isPhase(Phases.SecondPhase) && !finalPhaseTransitionTriggered && this.getHealth() <= this.finalHealth) {
            switchToFinalPhase();
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
        if (this.isDeadOrDying() && !this.level().isClientSide) {
            this.castComplete();
            this.serverTriggerAnimation("death");
            Vec3 vec3 = this.getBoundingBox().getCenter();
            MagicManager.spawnParticles(this.level(), (ParticleOptions) ParticleTypes.SCULK_SOUL, vec3.x, vec3.y, vec3.z, 25, 0.2, 0.2, 0.2, 0.12, false);
        }
    }

    @Override
    public void calculateEntityAnimation(boolean pIncludeHeight) {
        super.calculateEntityAnimation(false);
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (!this.level().isClientSide) {
            float scale = this.getScale();
            Vec3 vec3 = this.position();
            if (this.deathTime >= 40 && !this.level().isClientSide() && !this.isRemoved()) {

                this.remove(RemovalReason.KILLED);
                MagicManager.spawnParticles(this.level(), (ParticleOptions)ParticleTypes.SOUL, vec3.x, vec3.y + (double)1.0F, vec3.z, 50, 0.3, 0.3, 0.3, 0.2 * (double)scale, true);
            }
        }
    }

    @Override
    public void swing(InteractionHand pHand) {
        MeleeAttackType attackType = selectMeleeAttack();
        this.entityData.set(MELEE_ATTACK_TYPE, attackType.id);
        this.playAnimation(attackType.getAnimationName());
        super.swing(pHand);
    }

    private MeleeAttackType selectMeleeAttack() {
        return this.random.nextFloat() <= 0.3F ?
                MeleeAttackType.DOWN_ATTACK :
                MeleeAttackType.SLASH_ATTACK;
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

        if (pCompound.contains("SpawnX")) {
            double x = pCompound.getDouble("SpawnX");
            double y = pCompound.getDouble("SpawnY");
            double z = pCompound.getDouble("SpawnZ");
            this.spawnPos = new Vec3(x, y, z);
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

        if (spawnPos != null) {
            pCompound.putDouble("SpawnX", spawnPos.x);
            pCompound.putDouble("SpawnY", spawnPos.y);
            pCompound.putDouble("SpawnZ", spawnPos.z);
        }
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
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Axolotl.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LeshyEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, FireBossEntity.class, true));
    }

    protected void setFirstPhaseGoals() {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ModeusFirstPhaseAttackGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new CapturingTargetAttackGoal(this, 40, false, null, 8.0F, -2.0F, 80));
    }

    protected void setSecondPhaseGoal(){
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, (new WarlockAttackGoal(this, (double)1.25F, 30, 40))
                .setSpells(
                        List.of(SpellRegistry.MAGIC_ARROW_SPELL.get(), SpellRegistry.MAGIC_MISSILE_SPELL.get()),
                        List.of(SpellRegistry.SHADOW_SLASH.get()),
                        List.of(),
                        List.of())
                .setSingleUseSpell(SpellRegistry.SUMMON_SWORDS.get(), 60, 220, 5, 5));
        this.goalSelector.addGoal(1, this.tentackeAttackGoal);
        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 30.0F, (double)0.9F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    protected void setFinalPhaseGoals() {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, (new WizardAttackGoal(this, (double)1.15F, 35, 55))
                .setSpells(
                        List.of(SpellRegistry.SONIC_BOOM_SPELL.get(), SpellRegistry.ELDRITCH_BLAST_SPELL.get()),
                        List.of(SpellRegistry.SHADOW_SLASH.get()),
                        List.of(),
                        List.of())
                .setSingleUseSpell(RegistrySpells.HUNGER_OF_HADAR.get(), 70, 220, 2, 3));
        this.goalSelector.addGoal(1, this.aoeAttackGoal);
        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 30.0F, (double)0.9F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    //Геттеры анимаций
    public void setUsingKnockback(boolean value) {
        this.entityData.set(DATA_IS_USING_KNOCKBACK, value);
    }
    public boolean isUsingKnockback() {
        return this.entityData.get(DATA_IS_USING_KNOCKBACK);
    }
    public void setUsingAOE(boolean value) {
        this.entityData.set(DATA_IS_USING_AOE, value);
    }
    public boolean isUsingAOE() {
        return this.entityData.get(DATA_IS_USING_AOE);
    }
    public void setUsingTentacle(boolean value) {
        this.entityData.set(DATA_IS_USING_TENTACLE, value);
    }
    public boolean isUsingTentacle() {
        return this.entityData.get(DATA_IS_USING_TENTACLE);
    }
    public void setUsingLossMage(boolean value) {
        this.entityData.set(IS_USING_LOSS_MAGE, value);
    }
    public boolean isLossMage() {
        return this.entityData.get(IS_USING_LOSS_MAGE);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public void push(Entity pEntity) {
    }

    @Override
    public void push(double pX, double pY, double pZ) {
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

    private void lossMagicEffect() {
        this.lossMageAnimTimer = LOSS_MAGE_ANIM_DURATION;

        AABB checkArea = this.getBoundingBox().inflate(25);

        if (!this.level().isClientSide) {
            this.setUsingLossMage(true);

            this.level().getEntitiesOfClass(LivingEntity.class, checkArea,
                    entity -> !(entity instanceof ModeusBoss) &&
                            !(entity instanceof DarkspawnLarva) &&
                            !entity.hasEffect(ModEffects.LOSS_MAGIC.get())
            ).forEach(entity -> {
                entity.addEffect(new MobEffectInstance(ModEffects.LOSS_MAGIC.get(), 200, 0));
            });

            MagicManager.spawnParticles(this.level(),
                    new BlastwaveParticleOptions(
                            ((SchoolType) SchoolRegistry.ELDRITCH.get()).getTargetingColor(),
                            20
                    ),
                    this.getX(),
                    this.getBoundingBox().getCenter().y,
                    this.getZ(),
                    1,
                    0.0F, 0.0F, 0.0F, 0.0F,
                    true
            );
        }

        this.effectTimer = 500;
    }

    public void spawnDarkspawn(boolean left) {
        Level level = this.level();
        if (level instanceof ServerLevel serverLevel) {
            DarkspawnLarva larva = new DarkspawnLarva(ModEntityType.DARKSPAWN_LARVA.get(), level);
            DarkspawnObserver observers = new DarkspawnObserver(ModEntityType.DARKSPAWN_OBSERVER.get(), level);

            float angle = (float)(left ? -90 : 90) * ((float)Math.PI / 180F);

            Vec3 offset = this.getForward()
                    .multiply(3.0F, 0.0F, 3.0F)
                    .scale(this.getScale())
                    .yRot(angle);
            Vec3 offset_y = this.getForward()
                    .multiply(0.0F, 0.0F, 0.0F)
                    .add(0.0D, 2.0D, 0.0D)
                    .scale(this.getScale())
                    .yRot(angle);

            Vec3 spawnGround = Utils.moveToRelativeGroundLevel(
                    this.level(),
                    Utils.raycastForBlock(this.level(), this.getEyePosition(),
                            this.position().add(offset), ClipContext.Fluid.NONE).getLocation(),
                    4
            );

            larva.moveTo(spawnGround.add(0.0F, 0.1, 0.0F));
            larva.setYRot(this.getYRot());
            larva.finalizeSpawn(serverLevel,
                    this.level().getCurrentDifficultyAt(this.blockPosition()),
                    MobSpawnType.MOB_SUMMONED, null, null);
            this.level().addFreshEntity(larva);

            Vec3 spawnAir = this.position().add(offset_y);
            observers.moveTo(spawnAir.x, spawnAir.y, spawnAir.z);
            observers.setYRot(this.getYRot());
            observers.finalizeSpawn(serverLevel,
                    this.level().getCurrentDifficultyAt(this.blockPosition()),
                    MobSpawnType.MOB_SUMMONED, null, null);
            this.level().addFreshEntity(observers);

            this.level().playSound(null, spawnGround.x, spawnGround.y, spawnGround.z,
                    SoundEvents.WARDEN_EMERGE, this.getSoundSource(), 2.0F, 0.9F);
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

    @Override
    public void setActiveAttackTarget(int entityId) {
        this.entityData.set(DATA_ATTACK_TARGET_ID, entityId);
    }

    @Override
    public boolean hasActiveAttackTarget() {
        return this.entityData.get(DATA_ATTACK_TARGET_ID) != 0;
    }

    @Override
    public @Nullable LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) return null;
        if (this.level().isClientSide) {
            Entity entity = this.level().getEntity(this.entityData.get(DATA_ATTACK_TARGET_ID));
            return entity instanceof LivingEntity ? (LivingEntity) entity : null;
        }
        return this.getTarget();
    }

    @Override
    public int getAttackDuration() {
        return this.attackDuration;
    }

    @Override
    public float getAttackAnimationScale(float partialTicks) {
        return ((float)this.clientSideAttackTime + partialTicks) / (float)this.attackDuration;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.isInvulnerable = invulnerable;
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

    public enum MeleeAttackType {
        DOWN_ATTACK(0, "melee_attack"),
        SLASH_ATTACK(1, "melee_attack_2");

        final int id;
        final String animationName;

        MeleeAttackType(int id, String animationName) {
            this.id = id;
            this.animationName = animationName;
        }

        public String getAnimationName() {
            return animationName;
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
