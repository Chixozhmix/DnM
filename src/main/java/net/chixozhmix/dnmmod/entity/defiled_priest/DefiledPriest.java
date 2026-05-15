package net.chixozhmix.dnmmod.entity.defiled_priest;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.nbt.CompoundTag;
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
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class DefiledPriest extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker {
    private static RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");

    private RawAnimation customAnimationToPlay;

    private final AnimatableInstanceCache cache;
    private final AnimationController<DefiledPriest> movementController;

    private static final AttributeSupplier.Builder ATTRIBUTES = LivingEntity.createLivingAttributes()
            .add(Attributes.ATTACK_DAMAGE, (double)4.0F)
            .add(Attributes.ATTACK_KNOCKBACK, (double)0.05F)
            .add(Attributes.MAX_HEALTH, (double)120.0F)
            .add(Attributes.ARMOR, 5.0F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.2F)
            .add(Attributes.FOLLOW_RANGE, (double)30.0F)
            .add((Attribute) AttributeRegistry.SPELL_POWER.get(), (double)0.7F)
            .add(Attributes.MOVEMENT_SPEED, (double)0.26F);

    public DefiledPriest(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.xpReward = 15;

        this.setPersistenceRequired();

        this.movementController = new AnimationController<>(this, "movement", 0, this::movementPredicate);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return ATTRIBUTES;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(movementController);

        super.registerControllers(controllerRegistrar);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private PlayState movementPredicate(AnimationState<DefiledPriest> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, (new WizardAttackGoal(this, (double)1.25F, 30, 50))
                .setSpells(
                        List.of(SpellRegistry.BLOOD_SLASH_SPELL.get(), SpellRegistry.WISP_SPELL.get(), SpellRegistry.ELDRITCH_BLAST_SPELL.get()),
                        List.of(SpellRegistry.SHADOW_SLASH.get()),
                        List.of(SpellRegistry.TELEPORT_SPELL.get()),
                        List.of(SpellRegistry.HEAL_SPELL.get(), RegistrySpells.AGATHYS_ARMOR_SPELL.get()))
                .setSingleUseSpell(RegistrySpells.HUNGER_OF_HADAR.get(), 100, 350, 3, 4));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));;
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
}
