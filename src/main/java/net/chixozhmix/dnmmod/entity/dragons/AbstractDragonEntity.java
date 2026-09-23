package net.chixozhmix.dnmmod.entity.dragons;

import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import net.chixozhmix.dnmmod.Util.ModTags;
import net.chixozhmix.dnmmod.api.entity.dragons.HitboxController;
import net.chixozhmix.dnmmod.entity.dragons.client.AnimationsEnum;
import net.chixozhmix.dnmmod.goals.dragons.DragonFlyingGoal;
import net.chixozhmix.dnmmod.goals.dragons.DragonMeleeAttackGoal;
import net.chixozhmix.dnmmod.goals.dragons.DragonPatrolLocationGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
    private static final EntityDataAccessor<String> ANIM_STATE = SynchedEntityData.defineId(AbstractDragonEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(AbstractDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LANDING = SynchedEntityData.defineId(AbstractDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DRAGON_TYPE = SynchedEntityData.defineId(AbstractDragonEntity.class, EntityDataSerializers.STRING);

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("fly");
    private static final RawAnimation FLY_IDLE_ANIM = RawAnimation.begin().thenLoop("fly_idle");

    private static final RawAnimation BIT_ANIM = RawAnimation.begin().thenPlay("bit");

    private static final AttributeSupplier.Builder ATTRIBUTES = LivingEntity.createLivingAttributes()
            .add(Attributes.ATTACK_DAMAGE, (double)12.0F)
            .add(Attributes.ATTACK_KNOCKBACK, (double)0.15F)
            .add(Attributes.MAX_HEALTH, (double)400.0F)
            .add(Attributes.FOLLOW_RANGE, (double)96.0F)
            .add(Attributes.KNOCKBACK_RESISTANCE, (double)1.0F)
            .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 3)
            .add(ForgeMod.SWIM_SPEED.get(), 1.2F)
            .add(Attributes.MOVEMENT_SPEED, (double)0.32F);

    private Vec3 spawnPos = null;

    //Parts
    public final  DragonPartEntity head;
    public final  DragonPartEntity neck;
    public final  DragonPartEntity neck2;

    public final DragonPartEntity torso;

    public final DragonPartEntity tail1;
    public final DragonPartEntity tail2;
    public final DragonPartEntity tail3;

    public final  DragonPartEntity leftWing;
    public final  DragonPartEntity rightWing;

    private final DragonPartEntity[] parts;

    //Flying
    private int flightDecisionCooldown = 50;

    //Animations
    private final AnimatableInstanceCache cache;
    private RawAnimation customAnimationToPlay;

    private final AnimationController<AbstractDragonEntity> movementController;
    private final AnimationController<AbstractDragonEntity> attackController;

    //Attack (Возможно стоит придумать что-то другое, но в теории это работает)
    private int bitTick = 0; // 19 тиков длится анимация - на 12 тике атакует
    private int wingTick = 0;
    private int tailTick = 0;
    private int breathTick = 0;

    public AbstractDragonEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 5;

        //Parts
        this.head = new DragonPartEntity(this, "head", 2.5f, 2.5f);
        this.neck = new DragonPartEntity(this, "neck", 2.5f, 1.4f);
        this.neck2 = new DragonPartEntity(this, "neck2", 2.5f, 1.4f);

        this.torso = new DragonPartEntity(this, "torso", 4.0f, 4.0f);

        this.tail1 = new DragonPartEntity(this, "tail1", 2.5f, 2.5f);
        this.tail2 = new DragonPartEntity(this, "tail2", 2.5f, 2.5f);
        this.tail3 = new DragonPartEntity(this, "tail3", 2.5f, 2.5f);

        this.leftWing = new DragonPartEntity(this, "leftWing", 5.0f, 5.0f);
        this.rightWing = new DragonPartEntity(this, "rightWing", 5.0f, 5.0f);

        this.parts = new DragonPartEntity[] {head, neck, neck2, torso, tail1, tail2, tail3, leftWing, rightWing};
        this.setId(ENTITY_COUNTER.getAndAdd(this.parts.length + 1) + 1);

        this.movementController = new AnimationController<>(this, "movement", 2, this::movementPredicate);
        this.attackController = new AnimationController<>(this, "attack", 2, this::attackPredicate);
        this.cache = GeckoLibUtil.createInstanceCache(this);

        this.setPersistenceRequired();
    }

    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        return 0;
    }

    private PlayState movementPredicate(AnimationState<AbstractDragonEntity> state) {
        //Полет
        if(isFlying()) {
            if(state.isMoving())
            {
                setAnimState(AnimationsEnum.FLY);
                state.getController().setAnimation(FLY_ANIM);
                return PlayState.CONTINUE;
            }
        }
        //Приземление\Завис в воздухе (потом разделю (или нет))
        if(isLanding()) {
            setAnimState(AnimationsEnum.FLY_IDLE);
            state.getController().setAnimation(FLY_IDLE_ANIM);
            return PlayState.CONTINUE;
        }

        //Ходьба
        if (state.isMoving() && this.onGround() && !Objects.equals(getAnimState(), AnimationsEnum.BIT.getAnimId())) {
            setAnimState(AnimationsEnum.WALK);
            state.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }

        if(!Objects.equals(getAnimState(), AnimationsEnum.BIT.getAnimId())) {
            state.getController().setAnimation(IDLE_ANIM);
            setAnimState(AnimationsEnum.IDLE);
        }

        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationState<AbstractDragonEntity> state) {
        //Атака
        if (Objects.equals(getAnimState(), AnimationsEnum.BIT.getAnimId())) {
            state.getController().setAnimation(BIT_ANIM);
            return PlayState.CONTINUE;
        }

        state.getController().forceAnimationReset();
        return PlayState.STOP;
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
        this.entityData.define(DRAGON_TYPE, "RED");
        this.entityData.define(FLYING, false);
        this.entityData.define(LANDING, false);
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

    public boolean isLanding() {
        return this.entityData.get(LANDING);
    }
    public void setLanding(boolean landing) {
        this.entityData.set(LANDING, landing);
    }

    public void setDragonType(DragonTypes type) {
        this.entityData.set(DRAGON_TYPE, type.getType());
    }
    public String getDragonType() {
        return this.entityData.get(DRAGON_TYPE);
    }

    public Vec3 getSpawnPos() {
        return spawnPos;
    }
    public void setSpawnPos(Vec3 spawnPos) {
        this.spawnPos = spawnPos;
    }

    public void setBitTick(int bitTick) {
        this.bitTick = bitTick;
    }
    public int getBitTick() {
        return bitTick;
    }

    public void setWingTick(int wingAttackTick) {
        this.wingTick = wingAttackTick;
    }
    public int getWingTick() {
        return wingTick;
    }

    public void setTailTick(int tailTick) {
        this.tailTick = tailTick;
    }
    public int getTailTick() {
        return tailTick;
    }

    public void setBreathTick(int breathTick) {
        this.breathTick = breathTick;
    }
    public int getBreathTick() {
        return breathTick;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new DragonFlyingGoal(this, 120));

        this.goalSelector.addGoal(1, new DragonMeleeAttackGoal(this));
        this.goalSelector.addGoal(3, new DragonPatrolLocationGoal(this, 120, 1.0D));

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
    public void aiStep() {
        super.aiStep();

        if (isLanding() && this.onGround()) {
            if(!this.level().isClientSide)
                CameraShakeManager.addCameraShake(new CameraShakeData(20, this.position(), 40.0F));
            setLanding(false);
        }

        HitboxController.updateParts(this);

        if (!this.level().isClientSide) {
            destroyBlocksAround(10, 5);
        }

        //Плавное приземление (не придумал, как сделать по другому)
        Vec3 motion = this.getDeltaMovement();
        if (!this.onGround() && motion.y < 0.0D) {
            this.setDeltaMovement(motion.multiply(1.0D, 0.9D, 1.0D));
        }
    }

    public int getFlightDecisionCooldown() {
        return flightDecisionCooldown;
    }

    public void decreaseFlightDecisionCooldown() {
        if (flightDecisionCooldown > 0) {
            flightDecisionCooldown--;
        }
    }

    public void resetFlightDecisionCooldown() {
        flightDecisionCooldown = 100 + this.random.nextInt(100);
    }

    public double getGroundHeight() {
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

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        if(!this.level().isClientSide && getSpawnPos() == null) {
            setSpawnPos(this.position());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        if(pCompound.contains("SpawnX")) {
            double x = pCompound.getDouble("SpawnX");
            double y = pCompound.getDouble("SpawnY");
            double z = pCompound.getDouble("SpawnZ");

            Vec3 spawnPos = new Vec3(x, y, z);
            setSpawnPos(spawnPos);
        }

        setFlying(false);
        setLanding(true);
        setNoGravity(false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        if(getSpawnPos() != null) {
            pCompound.putDouble("SpawnX", getSpawnPos().x);
            pCompound.putDouble("SpawnY", getSpawnPos().y);
            pCompound.putDouble("SpawnZ", getSpawnPos().z);
        }
    }

    //Это, наверно, тупо и не очень оптимизировано, но по другому я не смог это сделать
    private void destroyBlocksAround(int radius, int verticalRadius) {
        BlockPos center = this.blockPosition();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -verticalRadius; y <= verticalRadius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z > radius * radius) continue;
                    if (y < 3) continue;

                    BlockPos pos = center.offset(x, y, z);

                    if (shouldDestroyBlock(pos))
                        this.level().destroyBlock(pos, false, this);
                }
            }
        }
    }
//
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
        controllerRegistrar.add(attackController);
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
                this.getX() - 32.0,
                this.getY() - 32.0,
                this.getZ() - 32.0,
                this.getX() + 32.0,
                this.getY() + 32.0,
                this.getZ() + 32.0
        );
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    //Потому что майнкрафт удалял дракона, как только он оказывался в выгруженных чанках.
    @Override
    public void checkDespawn() {

    }
}