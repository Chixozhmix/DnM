package net.chixozhmix.dnmmod.entity.hidden_tentacle;

import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HiddenTentacleEntity extends Monster implements GeoEntity, Enemy, IAnimatedAttacker {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Integer> DATA_STATE = SynchedEntityData.defineId(HiddenTentacleEntity.class, EntityDataSerializers.INT);

    private static final String IDLE_ANIM = "idle";
    private static final String TRIGGER_IDLE_ANIM = "trigger_idle";
    private static final String TRIGGER_ANIM = "trigger";
    private static final String HIDDEN_ANIM = "hidden";
    private static final String ATTACK_ANIM = "attack";

    private int stateTimer = 0;

    private static final AttributeSupplier.Builder ATTRIBUTES = LivingEntity.createLivingAttributes()
            .add(Attributes.ATTACK_DAMAGE, 4.0D)
            .add(Attributes.MAX_HEALTH, 15.0D)
            .add(Attributes.FOLLOW_RANGE, 10.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.0D)
            .add(Attributes.ATTACK_KNOCKBACK, 0.1F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0F);

    public HiddenTentacleEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoAi(false);
        this.getNavigation().stop();
        this.xpReward = 10;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_STATE, TentacleState.HIDDEN.ordinal());
    }

    public TentacleState getTentacleState() {
        int ordinal = this.entityData.get(DATA_STATE);
        TentacleState[] values = TentacleState.values();
        if (ordinal >= 0 && ordinal < values.length) {
            return values[ordinal];
        }
        return TentacleState.HIDDEN;
    }

    public void setTentacleState(TentacleState newState) {
        if (this.getTentacleState() == newState) return;
        this.entityData.set(DATA_STATE, newState.ordinal());
        this.stateTimer = 0;

        if(!this.level().isClientSide && newState == TentacleState.EMERGING)
            this.playSound(SoundRegistry.VOID_TENTACLES_START.get(), 1.5f, 1.0f);
        else if(!this.level().isClientSide && newState == TentacleState.HIDING)
            this.playSound(SoundRegistry.VOID_TENTACLES_LEAVE.get(), 1.5f, 1.0f);
    }

    @Override
    public void travel(Vec3 pTravelVector) {
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return ATTRIBUTES;
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide)
            return;

        LivingEntity target = getTarget();
        TentacleState currentState = getTentacleState();

        stateTimer++;

        // таймеры для одноразовых анимаций (на случай если hasAnimationFinished капризничает)
        if (currentState == TentacleState.EMERGING && stateTimer >= 11) {
            setTentacleState(TentacleState.IDLE);
        } else if (currentState == TentacleState.ATTACK && stateTimer >= 12) {
            setTentacleState(TentacleState.IDLE);
        } else if (currentState == TentacleState.HIDING && stateTimer >= 11) {
            setTentacleState(TentacleState.HIDDEN);
        }

        if (target == null || !target.isAlive() || distanceTo(target) > 6.0D) {
            if (currentState != TentacleState.HIDDEN && currentState != TentacleState.HIDING) {
                setTentacleState(TentacleState.HIDING);
            }
            return;
        }

        double distance = distanceTo(target);

        if (currentState == TentacleState.HIDDEN && distance <= 6.0D) {
            setTentacleState(TentacleState.EMERGING);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TentacleAttackGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<HiddenTentacleEntity> state) {
        TentacleState currentTentacleState = getTentacleState();

        switch (currentTentacleState) {
            case HIDDEN -> state.getController().setAnimation(RawAnimation.begin().thenLoop(IDLE_ANIM));
            case EMERGING -> state.getController().setAnimation(RawAnimation.begin().thenPlay(TRIGGER_ANIM));
            case IDLE -> state.getController().setAnimation(RawAnimation.begin().thenLoop(TRIGGER_IDLE_ANIM));
            case ATTACK -> state.getController().setAnimation(RawAnimation.begin().thenPlay(ATTACK_ANIM));
            case HIDING -> state.getController().setAnimation(RawAnimation.begin().thenPlay(HIDDEN_ANIM));
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void playAnimation(String animationName) {
        if (animationName.equals(ATTACK_ANIM)) {
            setTentacleState(TentacleState.ATTACK);
        }
        this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
    }

    @Override
    public void swing(InteractionHand pHand) {
        setTentacleState(TentacleState.ATTACK);
        super.swing(pHand);
    }

    public enum TentacleState {
        HIDDEN,
        EMERGING,
        IDLE,
        HIDING,
        ATTACK
    }

    static class TentacleAttackGoal extends net.minecraft.world.entity.ai.goal.Goal {
        private final HiddenTentacleEntity tentacle;
        private int attackCooldown = 0;

        public TentacleAttackGoal(HiddenTentacleEntity tentacle) {
            this.tentacle = tentacle;
            this.setFlags(java.util.EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = tentacle.getTarget();
            if (target == null || !target.isAlive()) {
                return false;
            }
            return tentacle.distanceToSqr(target) <= 6.0 * 6.0;
        }

        @Override
        public void start() {
            tentacle.getNavigation().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = tentacle.getTarget();
            if (target == null) return;

            tentacle.getLookControl().setLookAt(target, 30.0F, 30.0F);

            if (attackCooldown > 0) {
                attackCooldown--;
            }

            TentacleState state = tentacle.getTentacleState();

            if (state == TentacleState.IDLE) {
                if (tentacle.distanceToSqr(target) <= 4.0 * 4.0 && attackCooldown <= 0) {
                    tentacle.setTentacleState(TentacleState.ATTACK);
                    tentacle.swing(InteractionHand.MAIN_HAND);

                    target.hurt(tentacle.damageSources().mobAttack(tentacle), (float) tentacle.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    if(!tentacle.level().isClientSide)
                        tentacle.playSound(SoundEvents.WARDEN_HURT, 1.0f, 1.0f);

                    attackCooldown = 30;
                }
            }
        }
    }
}