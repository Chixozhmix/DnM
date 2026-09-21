package net.chixozhmix.dnmmod.entity.dragons;

import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import net.chixozhmix.dnmmod.Util.ModTags;
import net.chixozhmix.dnmmod.api.entity.dragons.HitboxController;
import net.chixozhmix.dnmmod.entity.dragons.client.AnimationsEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

public class AbstractDragonEntity extends PathfinderMob implements Enemy, GeoEntity, IAnimatedAttacker {
    //Для движения хитбоксов
    private static final EntityDataAccessor<String> ANIM_STATE = SynchedEntityData.defineId(AbstractDragonEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(AbstractDragonEntity.class, EntityDataSerializers.BOOLEAN);

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("fly");
    private static final RawAnimation FLY_IDLE_ANIM = RawAnimation.begin().thenLoop("fly_idle");

    private static final AttributeSupplier.Builder ATTRIBUTES = LivingEntity.createLivingAttributes()
            .add(Attributes.ATTACK_DAMAGE, (double)12.0F)
            //.add(Attributes.FLYING_SPEED, 0.9F)
            .add(Attributes.ATTACK_KNOCKBACK, (double)0.15F)
            .add(Attributes.MAX_HEALTH, (double)400.0F)
            .add(Attributes.FOLLOW_RANGE, (double)32.0F)
            .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
            .add(ForgeMod.ENTITY_REACH.get(), 10.0F);

    //Parts
    public final  DragonPartEntity head;
    public final  DragonPartEntity neck;
    public final  DragonPartEntity neck2;

    public final DragonPartEntity tail1;
    public final DragonPartEntity tail2;
    public final DragonPartEntity tail3;

    public final  DragonPartEntity leftWing;
    public final  DragonPartEntity rightWing;

    private final DragonPartEntity[] parts;

    //Flying
    private boolean flying = false;
    private double flyingHeight = 30.0D; //20 - 40
    private int flyTime = 0;
    private double flightTargetX;
    private double flightTargetZ;
    private int flightDecisionCooldown = 100;


    //Animations
    private final AnimatableInstanceCache cache;
    private RawAnimation customAnimationToPlay;

    private final AnimationController<AbstractDragonEntity> movementController;

    //Attack (Возможно стоит придумать что-то другое, но в теории это работает)
//    Entity target = null;
//    private int bitTick = 0; // 19 тиков длится анимация - на 12 тике атакует
//    private int wingTick = 0;
//    private int tailTick = 0;

    public AbstractDragonEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 5;

        //Parts
        this.head = new DragonPartEntity(this, "head", 2.5f, 2.5f);
        this.neck = new DragonPartEntity(this, "neck", 2.5f, 1.4f);
        this.neck2 = new DragonPartEntity(this, "neck2", 2.5f, 1.4f);

        this.tail1 = new DragonPartEntity(this, "tail1", 2.5f, 2.5f);
        this.tail2 = new DragonPartEntity(this, "tail2", 2.5f, 2.5f);
        this.tail3 = new DragonPartEntity(this, "tail3", 2.5f, 2.5f);

        this.leftWing = new DragonPartEntity(this, "leftWing", 5.0f, 5.0f);
        this.rightWing = new DragonPartEntity(this, "rightWing", 5.0f, 5.0f);

        this.parts = new DragonPartEntity[] {head, neck, neck2, tail1, tail2, tail3, leftWing, rightWing};
        this.setId(ENTITY_COUNTER.getAndAdd(this.parts.length + 1) + 1);

        this.movementController = new AnimationController<>(this, "movement", 2, this::movementPredicate);
        this.cache = GeckoLibUtil.createInstanceCache(this);
    }

    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        return 0;
    }

    private PlayState movementPredicate(AnimationState<AbstractDragonEntity> state) {
//        if (Objects.equals(getAnimState(), AnimationsEnum.BIT.getAnimId())) {
//            return PlayState.CONTINUE;
//        }

        //Ходьба
        if (state.isMoving() && this.onGround()) {
            setAnimState(AnimationsEnum.WALK);
            state.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }
        //Полет
        if(isFlying())
        {
            setAnimState(AnimationsEnum.FLY);
            state.getController().setAnimation(FLY_ANIM);
            return PlayState.CONTINUE;
        }
        //Завис в воздухе (приземляется)
        if(this.getDeltaMovement().x <= 0 && this.getDeltaMovement().y <= 0 && !this.onGround()) {
            setAnimState(AnimationsEnum.FLY_IDLE);
            state.getController().setAnimation(FLY_IDLE_ANIM);
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(IDLE_ANIM);
        setAnimState(AnimationsEnum.IDLE);
        return PlayState.CONTINUE;
    }

    @Override
    public void setId(int pId) {
        super.setId(pId);
        for (int i = 0; i < this.parts.length; i++) {
            this.parts[i].setId(pId + i + 1);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIM_STATE, "IDLE");
        this.entityData.define(FLYING, false);
    }

    public String getAnimState() {
        return this.entityData.get(ANIM_STATE);
    }

    public void setAnimState(AnimationsEnum animId) {
        String newState = animId.getAnimId();

        if (!Objects.equals(getAnimState(), newState)) {
            this.entityData.set(ANIM_STATE, newState);
        }
    }

    public boolean isFlying() {
        return this.entityData.get(FLYING);
    }

    public void setFlying(boolean flying) {
        this.entityData.set(FLYING, flying);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 30.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return ATTRIBUTES;
    }

    @Override
    public void tick() {
        super.tick();

        //Теоритичеси это работает, но нужна кастомная цель атаки, которая бы учитывала реальное расстояние. Стандартный MeleeAttack работает криво.
//        if(bitTick > 0) {
//            --bitTick;
//
//            if(bitTick == 10) {
//                if(target != null && target.isAlive() && this.distanceTo(target) <= 15 && this.hasLineOfSight(target)) {
//                    super.doHurtTarget(target);
//                }
//
//            }
//
//            if(bitTick == 0) {
//                 setAnimState(AnimationsEnum.IDLE);
//            }
//        }
    }

//    @Override
//    public boolean doHurtTarget(Entity pEntity) {
//        setAnimState(AnimationsEnum.BIT);
//        this.target = pEntity;
//        this.bitTick = 20;
//        return true;
//    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide) {
            updateFlightDecision();
            if (isFlying())
                tickFlight();
        }

        HitboxController.updateParts(this);

        if (!this.level().isClientSide && this.tickCount % 10 == 0) {
            destroyBlocksAround(10, 5);
        }

        //Плавное приземление (не придумал, как сделать по другому)
        Vec3 motion = this.getDeltaMovement();
        if (!this.onGround() && motion.y < 0.0D) {
            this.setDeltaMovement(motion.multiply(1.0D, 0.9D, 1.0D));
        }
    }

    private void tickFlight() {
        if (!isFlying())
            return;

        if (flyTime > 0) {
            flyTime--;
        } else {
            land();
            return;
        }

        double groundY = getGroundHeight();

        // Желаемая высота относительно земли
        double targetY = groundY + flyingHeight;

        double dx = flightTargetX - this.getX();
        double dz = flightTargetZ - this.getZ();

        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        // Если долетели до точки — выбираем новую
        if (horizontalDistance < 5.0D) {
            double angle = this.random.nextDouble() * Math.PI * 2.0D;

            double distance = 20.0D + this.random.nextDouble() * 30.0D;

            flightTargetX = this.getX() + Math.cos(angle) * distance;
            flightTargetZ = this.getZ() + Math.sin(angle) * distance;
        }

        // Направление
        dx = flightTargetX - this.getX();
        dz = flightTargetZ - this.getZ();

        horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        if (horizontalDistance > 0.001D) {
            dx /= horizontalDistance;
            dz /= horizontalDistance;
        }

        // Вертикальное движение
        double dy = targetY - this.getY();

        // Ограничиваем вертикальную скорость
        double verticalSpeed = Mth.clamp(dy * 0.08D, -0.25D, 0.25D);

        // Горизонтальная скорость
        double speed = 0.45D;
        Vec3 movement = new Vec3(dx * speed, verticalSpeed, dz * speed);
        this.setDeltaMovement(movement);

        // Поворачиваем дракона по направлению движения
        float targetYaw = (float) (Mth.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;
        this.setYRot(Mth.rotLerp(0.15F, this.getYRot(), targetYaw));
        this.yRotO = this.getYRot();
    }

    private void updateFlightDecision() {
        if(flightDecisionCooldown > 0) {
            flightDecisionCooldown--;
            return;
        }

        //5-10 seconds
        flightDecisionCooldown = 100 + this.random.nextInt(100);

        if(isFlying()) {
            if(this.random.nextFloat() < 0.35F)
                land();
            return;
        }

        if(getTarget() != null && getTarget().isAlive()) {
            if(this.random.nextFloat() < 0.65F)
                takeOff();
        }

        if(this.random.nextFloat() < 0.25F)
            takeOff();
    }

    private void takeOff() {
        if(isFlying())
            return;

        setFlying(true);

        flyingHeight = 20.0D + this.random.nextDouble() * 20.0D;
        double angle = this.random.nextDouble() * Math.PI * 2.0D;
        double distance = 20.0D + this.random.nextDouble() * 30.0D;

        flightTargetX = this.getX() + Math.cos(angle) * distance;
        flightTargetZ = this.getZ() + Math.sin(angle) * distance;

        flyTime = 200 + this.random.nextInt(200);

        this.setNoGravity(true);

        this.setDeltaMovement(this.getDeltaMovement().x, 0.35D, this.getDeltaMovement().z);
    }

    private void land() {
        if (!isFlying())
            return;

        setFlying(false);

        this.setNoGravity(false);
        Vec3 motion = this.getDeltaMovement();

        this.setDeltaMovement(motion.x, Math.min(motion.y, 0.0D), motion.z);
    }

    private double getGroundHeight() {
        BlockPos pos = this.blockPosition();

        for(int y = pos.getY(); y >= this.level().getMinBuildHeight(); y--) {
            BlockPos check = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState state = this.level().getBlockState(check);

            if(!state.isAir() && !state.liquid()) {
                return y + 1.0D;
            }
        }

        return this.level().getMinBuildHeight();
    }

    //Это, наверно, тупо и не очень оптимизировано, но по другому я не смог это сделать
    private void destroyBlocksAround(int radius, int verticalRadius) {
        BlockPos center = this.blockPosition();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -verticalRadius; y <= verticalRadius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z > radius * radius) continue;
                    if (y < 0) continue;

                    BlockPos pos = center.offset(x, y, z);

                    if (shouldDestroyBlock(pos))
                        this.level().destroyBlock(pos, false, this);
                }
            }
        }
    }

    private boolean shouldDestroyBlock(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);

        return !state.isAir() && !state.liquid() && !state.is(ModTags.NO_DRAGON_BREAK);
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public @Nullable DragonPartEntity[] getParts() {
        return this.parts;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);

        if (true) return;
        DragonPartEntity[] adragonpart = this.parts;

        for(int i = 0; i < adragonpart.length; ++i) {
            adragonpart[i].setId(i + pPacket.getId());
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(movementController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void playAnimation(String s) {

    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return new AABB(
                this.getX() - 8.0,
                this.getY() - 2.0,
                this.getZ() - 12.0,
                this.getX() + 8.0,
                this.getY() + 8.0,
                this.getZ() + 12.0
        );
    }
}