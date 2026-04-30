package net.chixozhmix.dnmmod.entity.small_ice_spider;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.chixozhmix.dnmmod.goals.MeleeCasterAtackGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class SmallIceSpiderEntity extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker {

    private RawAnimation customAnimationToPlay;

    private final AnimatableInstanceCache cache;

    private final AnimationController<SmallIceSpiderEntity> movementController;
    private final AnimationController<SmallIceSpiderEntity> attackController;

    private static final AttributeSupplier.Builder ATTRIBUTES = LivingEntity.createLivingAttributes()
            .add(Attributes.ATTACK_DAMAGE, (double)2.0F)
            .add(Attributes.MAX_HEALTH, (double)10.0F)
            .add(Attributes.FOLLOW_RANGE, (double)35.0F)
            .add((Attribute) AttributeRegistry.SPELL_POWER.get(), (double)0.3F)
            .add(Attributes.MOVEMENT_SPEED, (double)0.27F)
            .add(Attributes.ATTACK_KNOCKBACK, 0.03F);

    public SmallIceSpiderEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.cache = GeckoLibUtil.createInstanceCache(this);

        this.movementController = new AnimationController<>(this, "movement", 0, this::movementPredicate);
        this.attackController = new AnimationController<>(this, "attack", 0, this::attackPredicate);

        this.xpReward = 5;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(movementController);
        controllerRegistrar.add(attackController);

        super.registerControllers(controllerRegistrar);
    }

    private PlayState movementPredicate(AnimationState<SmallIceSpiderEntity> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(
                    RawAnimation.begin().thenLoop("walk")
            );
        } else {
            state.getController().setAnimation(
                    RawAnimation.begin().thenLoop("idle")
            );
        }

        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationState<SmallIceSpiderEntity> state) {
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
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new MeleeCasterAtackGoal(this, 1.25F, 35, 80)
                .setSpells(
                        List.of(SpellRegistry.ICICLE_SPELL.get()),
                        List.of(),
                        List.of(),
                        List.of()));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return ATTRIBUTES;
    }

    @Override
    public void playAnimation(String animationName) {
        this.customAnimationToPlay = RawAnimation.begin().thenPlay(animationName);
        this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
    }

    @Override
    public void swing(InteractionHand pHand) {
        this.playAnimation("attack_fang_basic");
        super.swing(pHand);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }
}
