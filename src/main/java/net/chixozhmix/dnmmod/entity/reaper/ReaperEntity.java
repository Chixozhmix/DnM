package net.chixozhmix.dnmmod.entity.reaper;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.chixozhmix.dnmmod.registers.SoundsRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
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
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class ReaperEntity extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker {
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("fly_idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("fly_walk");

    private RawAnimation customAnimationToPlay;

    private final AnimatableInstanceCache cache;

    private final AnimationController<ReaperEntity> movementController;

    private static final AttributeSupplier.Builder ATTRIBUTES = LivingEntity.createLivingAttributes()
            .add(Attributes.ATTACK_DAMAGE, (double)5.0F)
            .add(Attributes.ATTACK_KNOCKBACK, (double)0.1F)
            .add(Attributes.MAX_HEALTH, (double)40.0F)
            .add(Attributes.FOLLOW_RANGE, (double)35.0F)
            .add((Attribute) AttributeRegistry.SPELL_POWER.get(), (double)0.5F)
            .add(Attributes.MOVEMENT_SPEED, (double)0.27F)
            .add(Attributes.ARMOR, (double) 4.0F);

    public ReaperEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.cache = GeckoLibUtil.createInstanceCache(this);

        this.movementController = new AnimationController<>(this, "movement", 0, this::movementPredicate);

        this.xpReward = 35;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(movementController);

        super.registerControllers(controllerRegistrar);
    }

    private PlayState movementPredicate(AnimationState<ReaperEntity> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(IDLE_ANIM);
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
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, (new WizardAttackGoal(this, (double)1.25F, 35, 80))
                .setSpells(List.of((AbstractSpell) SpellRegistry.ACUPUNCTURE_SPELL.get(),
                        (AbstractSpell)SpellRegistry.BLOOD_SLASH_SPELL.get(),
                        (AbstractSpell) RegistrySpells.TALL_THE_DEAD.get()),
                        List.of((AbstractSpell)SpellRegistry.FANG_WARD_SPELL.get()),
                        List.of(),
                        List.of())
                .setSingleUseSpell((AbstractSpell)RegistrySpells.BURIAL_GROUND.get(), 80, 350, 4, 5));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return ATTRIBUTES;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundsRegistry.GHOST_AMBIENT.get();
    }

    @Override
    public void playAnimation(String animationName) {
        this.customAnimationToPlay = RawAnimation.begin().thenPlay(animationName);
        this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
    }

    @Override
    public boolean addEffect(MobEffectInstance pEffectInstance, @Nullable Entity pEntity) {
        if(pEffectInstance.getEffect() == MobEffects.POISON || pEffectInstance.getEffect() == ModEffects.CORPSE_POISON.get())
            return false;

        return super.addEffect(pEffectInstance, pEntity);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }
}
