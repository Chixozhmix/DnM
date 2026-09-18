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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
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

    private static final AttributeSupplier.Builder ATTRIBUTES = LivingEntity.createLivingAttributes()
            .add(Attributes.ATTACK_DAMAGE, (double)12.0F)
            .add(Attributes.ATTACK_KNOCKBACK, (double)0.15F)
            .add(Attributes.MAX_HEALTH, (double)400.0F)
            .add(Attributes.FOLLOW_RANGE, (double)32.0F)
            .add(Attributes.MOVEMENT_SPEED, (double)0.27F);

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

    private PlayState movementPredicate(AnimationState<AbstractDragonEntity> state) {
        if (state.isMoving() && this.onGround()) {
            setAnimState(AnimationsEnum.WALK);
            state.getController().setAnimation(WALK_ANIM);
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
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return ATTRIBUTES;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        updateParts();

        if (!this.level().isClientSide && this.tickCount % 10 == 0) {
            destroyBlocksAround(10, 5);
        }
    }

    private void updateParts() {
        float bodyYaw = this.yBodyRot * Mth.DEG_TO_RAD;
        float sin = Mth.sin(bodyYaw);
        float cos = Mth.cos(bodyYaw);

        if(Objects.equals(getAnimState(), AnimationsEnum.IDLE.getAnimId())) {
            updateSinglePart(head, 0.0, 2.5, -8.5, -sin, -cos);
            updateSinglePart(neck, 0.0, 2.5, -3.5, -sin, -cos);
            updateSinglePart(neck2, 0.0, 3.5, -6.0, -sin, -cos);

            updateSinglePart(tail1, 0, 1.5, 3.5, -sin, -cos);
            updateSinglePart(tail2, 0, 0.0, 7.0, -sin, -cos);
            updateSinglePart(tail3, 0, 0.0, 11.5, -sin, -cos);

//            leftWing.setPartSize(3.0f, 4.0f);
//            rightWing.setPartSize(3.0f, 4.0f);
        }
        if(Objects.equals(getAnimState(), AnimationsEnum.WALK.getAnimId())) {
            updateSinglePart(head, 0.0, 2.5, -8.5, -sin, -cos);
            updateSinglePart(neck, 0.0, 2.5, -3.5, -sin, -cos);
            updateSinglePart(neck2, 0.0, 3.0, -6.0, -sin, -cos);

            updateSinglePart(tail1, 0, 1.5, 3.5, -sin, -cos);
            updateSinglePart(tail2, 0, 1.0, 7.0, -sin, -cos);
            updateSinglePart(tail3, 0, 1.0, 11.5, -sin, -cos);

//            leftWing.setPartSize(4.0f, 2.0f);
//            rightWing.setPartSize(4.0f, 2.0f);
        }

        updateSinglePart(leftWing, 5.0, 1.0, 0.0, -sin, -cos);
        updateSinglePart(rightWing, -5.0, 1.0, 0.0, -sin, -cos);
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