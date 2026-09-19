package net.chixozhmix.dnmmod.entity.dragons;

import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import net.chixozhmix.dnmmod.Util.ModTags;
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

        //Flying (Нужно придумать что-то нормальное)
        //this.moveControl = new FlyingMoveControl(this, 10, false);

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

//    @Override
//    protected PathNavigation createNavigation(Level pLevel) {
//        FlyingPathNavigation flyingpathnavigator = new FlyingPathNavigation(this, pLevel);
//        flyingpathnavigator.setCanOpenDoors(false);
//        flyingpathnavigator.setCanFloat(true);
//        flyingpathnavigator.setCanPassDoors(true);
//        return flyingpathnavigator;
//    }

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
//        if(state.isMoving() && !this.onGround())
//        {
//            setAnimState(AnimationsEnum.FLY);
//            state.getController().setAnimation(FLY_ANIM);
//            return PlayState.CONTINUE;
//        }
//        //Завис в воздухе (приземляется)
//        if(this.getDeltaMovement().x <= 0 && this.getDeltaMovement().y <= 0 && !this.onGround()) {
//            setAnimState(AnimationsEnum.FLY_IDLE);
//            state.getController().setAnimation(FLY_IDLE_ANIM);
//            return PlayState.CONTINUE;
//        }

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
        updateParts();

        if (!this.level().isClientSide && this.tickCount % 10 == 0) {
            destroyBlocksAround(10, 5);
        }

        //Плавное приземление (не придумал, как сделать по другому)
//        Vec3 motion = this.getDeltaMovement();
//        if (!this.onGround() && motion.y < 0.0D) {
//            this.setDeltaMovement(motion.multiply(1.0D, 0.6D, 1.0D));
//        }
    }

    //Управляет хитбоксами частей (Да, я устанавливаю их вручную. Я просто не знаю, как сделать это по другому, так что приходится выкручиваться)
    private void updateParts() {
        float bodyYaw = this.yBodyRot * Mth.DEG_TO_RAD;
        float sin = Mth.sin(bodyYaw);
        float cos = Mth.cos(bodyYaw);

        //Стоит
        if(Objects.equals(getAnimState(), AnimationsEnum.IDLE.getAnimId())) {
            updateSinglePart(head, 0.0, 2.5, -8.5, -sin, -cos);
            updateSinglePart(neck, 0.0, 2.5, -3.5, -sin, -cos);
            updateSinglePart(neck2, 0.0, 3.5, -6.0, -sin, -cos);

            updateSinglePart(tail1, 0, 1.5, 3.5, -sin, -cos);
            updateSinglePart(tail2, 0, 0.0, 7.0, -sin, -cos);
            updateSinglePart(tail3, 0, 0.0, 11.5, -sin, -cos);

            updateSinglePart(leftWing, 5.0, 1.0, 0.0, -sin, -cos);
            updateSinglePart(rightWing, -5.0, 1.0, 0.0, -sin, -cos);

            leftWing.setPartSize(5.0f, 5.0f);
            rightWing.setPartSize(5.0f, 5.0f);
        }
        //Завис в воздухе
        if(Objects.equals(getAnimState(), AnimationsEnum.FLY_IDLE.getAnimId())) {
            updateSinglePart(head, 0.0, 6.5, -5.0, -sin, -cos);
            updateSinglePart(neck, 0.0, 5.0, -1.5, -sin, -cos);
            updateSinglePart(neck2, 0.0, 6.5, -2.0, -sin, -cos);

            updateSinglePart(tail1, 0, -1.5, 2.5, -sin, -cos);
            updateSinglePart(tail2, 0, -3.0, 3.0, -sin, -cos);
            updateSinglePart(tail3, 0, -6.0, 3.5, -sin, -cos);

            updateSinglePart(leftWing, 8.0, 1.0, 0.0, -sin, -cos);
            updateSinglePart(rightWing, -8.0, 1.0, 0.0, -sin, -cos);

            leftWing.setPartSize(8.0f, 5.0f);
            rightWing.setPartSize(8.0f, 5.0f);
        }
        //Идет
        if(Objects.equals(getAnimState(), AnimationsEnum.WALK.getAnimId())) {
            updateSinglePart(head, 0.0, 2.5, -8.5, -sin, -cos);
            updateSinglePart(neck, 0.0, 2.5, -3.5, -sin, -cos);
            updateSinglePart(neck2, 0.0, 3.0, -6.0, -sin, -cos);

            updateSinglePart(tail1, 0, 1.5, 3.5, -sin, -cos);
            updateSinglePart(tail2, 0, 1.0, 7.0, -sin, -cos);
            updateSinglePart(tail3, 0, 1.0, 11.5, -sin, -cos);

            updateSinglePart(leftWing, 5.0, 1.0, 0.0, -sin, -cos);
            updateSinglePart(rightWing, -5.0, 1.0, 0.0, -sin, -cos);

            leftWing.setPartSize(5.0f, 5.0f);
            rightWing.setPartSize(5.0f, 5.0f);
        }
        //Летит
        if(Objects.equals(getAnimState(), AnimationsEnum.FLY.getAnimId())) {
            updateSinglePart(head, 0.0, 2.5, -8.5, -sin, -cos);
            updateSinglePart(neck, 0.0, 2.5, -3.5, -sin, -cos);
            updateSinglePart(neck2, 0.0, 3.0, -6.0, -sin, -cos);

            updateSinglePart(tail1, 0, 2.5, 3.5, -sin, -cos);
            updateSinglePart(tail2, 0, 2.0, 7.0, -sin, -cos);
            updateSinglePart(tail3, 0, 2.0, 11.5, -sin, -cos);

            updateSinglePart(leftWing, 8.0, 1.0, 0.0, -sin, -cos);
            updateSinglePart(rightWing, -8.0, 1.0, 0.0, -sin, -cos);

            leftWing.setPartSize(8.0f, 5.0f);
            rightWing.setPartSize(8.0f, 5.0f);
        }

//        if(Objects.equals(getAnimState(), AnimationsEnum.BIT.getAnimId())) {
//            updateSinglePart(head, 0.0, 0.5, -8.5, -sin, -cos);
//            updateSinglePart(neck, 0.0, 2.5, -3.5, -sin, -cos);
//            updateSinglePart(neck2, 0.0, 3.0, -6.0, -sin, -cos);
//
//            updateSinglePart(tail1, 0, 1.5, 3.5, -sin, -cos);
//            updateSinglePart(tail2, 0, 1.0, 7.0, -sin, -cos);
//            updateSinglePart(tail3, 0, 1.0, 11.5, -sin, -cos);
//        }
    }

    private void updateSinglePart(DragonPartEntity part, double localX, double localY, double localZ, float sin, float cos) {
        double worldX = this.getX() + (localX * cos - localZ * sin);
        double worldY = this.getY() + localY;
        double worldZ = this.getZ() + (localX * sin + localZ * cos);

        part.xo = part.getX();
        part.yo = part.getY();
        part.zo = part.getZ();

        part.xOld = part.getX();
        part.yOld = part.getY();
        part.zOld = part.getZ();

        part.setPos(worldX, worldY, worldZ);
        part.setYRot(this.getYRot());
        part.setXRot(this.getXRot());
        part.yRotO = this.yRotO;
        part.xRotO = this.xRotO;
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