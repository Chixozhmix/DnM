package net.chixozhmix.dnmmod.entity.goblin_shaman;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GoblinShamanEntity extends AbstractSpellCastingMob implements Enemy {

    public GoblinShamanEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 20;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.fixed(0.6f, 0.7f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, (new WizardAttackGoal(this, (double)1.25F, 35, 80)).setSpells(List.of((AbstractSpell) SpellRegistry.ACID_ORB_SPELL.get(), (AbstractSpell)SpellRegistry.POISON_ARROW_SPELL.get(), (AbstractSpell)SpellRegistry.MAGIC_MISSILE_SPELL.get()), List.of((AbstractSpell)SpellRegistry.FANG_WARD_SPELL.get()), List.of(), List.of((AbstractSpell)SpellRegistry.BLIGHT_SPELL.get(), (AbstractSpell)SpellRegistry.ROOT_SPELL.get())).setSingleUseSpell((AbstractSpell)SpellRegistry.STOMP_SPELL.get(), 80, 350, 4, 5).setDrinksPotions());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    @Override
    public MobType getMobType() {
        return  MobType.ILLAGER;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, (double)3.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)0.0F)
                .add(Attributes.MAX_HEALTH, (double)35.0F)
                .add(Attributes.FOLLOW_RANGE, (double)25.0F)
                .add((Attribute) AttributeRegistry.SPELL_POWER.get(), (double)0.5F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.27F);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        if (pRandom.nextFloat() < 0.3F) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.GRAYBEARD_STAFF.get()));
        }
        if (pRandom.nextFloat() < 0.2F) {
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.TARNISHED_CROWN.get()));
        }
        this.setDropChance(EquipmentSlot.MAINHAND, 0.1F);
        this.setDropChance(EquipmentSlot.HEAD, 0.05F);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);

        RandomSource randomSource = this.random;

        this.spawnAtLocation(new ItemStack(ItemRegistry.ARCANE_ESSENCE.get(), randomSource.nextInt(8)));
    }
}
