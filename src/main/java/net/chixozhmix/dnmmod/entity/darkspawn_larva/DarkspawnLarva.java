package net.chixozhmix.dnmmod.entity.darkspawn_larva;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import net.chixozhmix.dnmmod.entity.corypheus.CorypheusBoss;
import net.chixozhmix.dnmmod.entity.defiled_priest.DefiledPriest;
import net.chixozhmix.dnmmod.entity.defiled_wizard.DefiledWizard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
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
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class DarkspawnLarva extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker {
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");

    private RawAnimation customAnimationToPlay;

    private final AnimatableInstanceCache cache;

    private final AnimationController<DarkspawnLarva> movementController;
    private final AnimationController<DarkspawnLarva> attackController;

    private static final AttributeSupplier.Builder ATTRIBUTES = LivingEntity.createLivingAttributes()
            .add(Attributes.ATTACK_DAMAGE, (double)2.0F)
            .add(Attributes.ATTACK_KNOCKBACK, (double)0.00F)
            .add(Attributes.MAX_HEALTH, (double)15.0F)
            .add(Attributes.FOLLOW_RANGE, (double)25.0F)
            .add((Attribute) AttributeRegistry.SPELL_POWER.get(), (double)0.4F)
            .add(Attributes.MOVEMENT_SPEED, (double)0.27F)
            .add(ForgeMod.ENTITY_REACH.get(), 3.0F);

    public DarkspawnLarva(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.xpReward = 5;

        this.movementController = new AnimationController<>(this, "movement", 0, this::movementPredicate);
        this.attackController = new AnimationController<>(this, "attack", 0, this::attackPredicate);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return ATTRIBUTES;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
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

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, (new WarlockAttackGoal(this, (double)1.25F, 35, 60))
                .setSpells(
                        List.of(SpellRegistry.POISON_BREATH_SPELL.get()),
                        List.of(),
                        List.of(),
                        List.of()));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this,
                DefiledWizard.class,
                DefiledPriest.class,
                CorypheusBoss.class));
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
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }
}
