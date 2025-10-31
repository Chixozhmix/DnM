package net.chixozhmix.dnmmod.entity.leshy;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import net.chixozhmix.dnmmod.spell.RegistrySpells;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LeshyEntity extends AbstractSpellCastingMob implements Enemy {

    private int attackAnimationTick = 0;

    public LeshyEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 60;
    }

    public final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("ui.dnmmod.leshy_bossbar"),
            BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_12);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.fixed(1.2f, 4.7f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, (new WizardAttackGoal(this, (double)1.25F, 25, 50))
                .setSpells(List.of((AbstractSpell) SpellRegistry.ACID_ORB_SPELL.get(),
                                (AbstractSpell)RegistrySpells.CAUSTIC_BREW.get(),
                                (AbstractSpell) SpellRegistry.STOMP_SPELL.get(),
                                (AbstractSpell)SpellRegistry.BLOOD_STEP_SPELL.get()),
                        List.of((AbstractSpell)SpellRegistry.FIREFLY_SWARM_SPELL.get()),
                        List.of(),
                        List.of((AbstractSpell)SpellRegistry.BLIGHT_SPELL.get(),
                                (AbstractSpell)SpellRegistry.ROOT_SPELL.get()))
                .setSingleUseSpell((AbstractSpell) RegistrySpells.SUMMON_RAVEN.get(), 80, 350, 10, 10));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, (double)10.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)0.2F)
                .add(Attributes.MAX_HEALTH, (double)300.0F)
                .add(Attributes.FOLLOW_RANGE, (double)30.0F)
                .add(AttributeRegistry.SPELL_POWER.get(), (double)1.25F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.22F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double)0.1F)
                .add(Attributes.ARMOR, (double)15.0F)
                .add(AttributeRegistry.SPELL_RESIST.get(), 0.5f)
                .add(AttributeRegistry.SUMMON_DAMAGE.get(), 0.4f);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        this.swing(InteractionHand.MAIN_HAND);

        if(pEntity instanceof LivingEntity) {
            ((LivingEntity) pEntity).addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,
                    100,
                    0,
                    false,
                    true,
                    true
            ));
        }

        return super.doHurtTarget(pEntity);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }

        if (this.swinging) {
            this.getNavigation().stop();
            this.attackAnimationTick = 20;
            this.swinging = false;
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
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
