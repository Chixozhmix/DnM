package net.chixozhmix.dnmmod.entity.ghost;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.chixozhmix.dnmmod.items.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GhostEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int invisibilityCooldown = 0;
    private boolean isInvisibleFromAbility = false;
    private static final double INVISIBILITY_CHANCE = 0.3; // 30% шанс
    private static final int INVISIBILITY_DURATION = 60; // 3 секунды (20 тиков = 1 секунда)
    private static final int INVISIBILITY_COOLDOWN = 100; // 5 секунд кулдауна

    private int attackAnimationTick = 0;

    public GhostEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        // В первую очередь проверяем анимацию атаки
        if (this.attackAnimationTick > 0) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("attack"));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("walk"));
            return  PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static AttributeSupplier createAttributes () {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.29D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.FLYING_SPEED, 0.29D).build();
    }

    @Override
    public void tick() {
        super.tick();

        // Обновляем счетчик анимации атаки
        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }

        // Обновляем кулдаун невидимости
        if (this.invisibilityCooldown > 0) {
            this.invisibilityCooldown--;
        }

        // Проверяем, нужно ли снять эффект невидимости
        if (this.isInvisibleFromAbility && this.invisibilityCooldown <= INVISIBILITY_COOLDOWN - INVISIBILITY_DURATION) {
            spawnInvisibilityParticles();
            this.isInvisibleFromAbility = false;
            this.setInvisible(false);
        }

        if (this.swinging) {
            this.getNavigation().stop();
            // Устанавливаем длительность анимации атаки (например, 10 тиков)
            this.attackAnimationTick = 20;
            this.swinging = false; // Сбрасываем флаг swinging
        }
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        this.swing(InteractionHand.MAIN_HAND);

        if (pEntity instanceof Player && this.invisibilityCooldown <= 0 && this.random.nextDouble() < INVISIBILITY_CHANCE) {
            activateInvisibility();
        }
        return super.doHurtTarget(pEntity);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.invisibilityCooldown <= 0 && this.random.nextDouble() < INVISIBILITY_CHANCE) {
            activateInvisibility();
        }
        return super.hurt(pSource, pAmount);
    }

    private void activateInvisibility() {
        spawnInvisibilityParticles();

        this.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE,
                2.0f,
                1.0f);

        this.isInvisibleFromAbility = true;
        this.setInvisible(true);
        this.invisibilityCooldown = INVISIBILITY_COOLDOWN;
    }

    private void spawnInvisibilityParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            // Используем серверный метод для спавна частиц
            serverLevel.sendParticles(
                    ParticleTypes.SMOKE,
                    this.getX(),
                    this.getY() + this.getEyeHeight() / 2,
                    this.getZ(),
                    40, // количество частиц
                    this.getBbWidth() / 2, // разброс по X
                    this.getBbHeight() / 2, // разброс по Y
                    this.getBbWidth() / 2, // разброс по Z
                    0.1 // скорость
            );
        } else {
            // Клиентская сторона (на всякий случай)
            for (int i = 0; i < 40; i++) {
                this.level().addParticle(ParticleTypes.SMOKE,
                        this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                        this.getY() + this.random.nextDouble() * this.getBbHeight(),
                        this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                        0, 0.1, 0);
            }
        }
    }

    @Override
    public boolean isInvisible() {
        return this.isInvisibleFromAbility || super.isInvisible();
    }

    public boolean isInvisibleFromAbility() {
        return this.isInvisibleFromAbility;
    }

    public int getInvisibilityCooldown() {
        return this.invisibilityCooldown;
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2F, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 30.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D, 20));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public void move(net.minecraft.world.entity.MoverType type, net.minecraft.world.phys.Vec3 movement) {
        super.move(type, movement);
    }
}
