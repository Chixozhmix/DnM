package net.chixozhmix.dnmmod.entity.tainted_observer;

import net.chixozhmix.dnmmod.entity.darkspawn_larva.DarkspawnLarva;
import net.chixozhmix.dnmmod.entity.modeus.ModeusBoss;
import net.chixozhmix.dnmmod.goals.IBeamAttackMob;
import net.chixozhmix.dnmmod.goals.CapturingTargetAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

public class DarkspawnObserver extends FlyingMob implements GeoEntity, Enemy, IBeamAttackMob {
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TARGET_ID =
            SynchedEntityData.defineId(DarkspawnObserver.class, EntityDataSerializers.INT);

    private int attackDuration = 80;
    public int clientSideAttackTime;

    private static final int MAX_FLY_HEIGHT = 40;
    Vec3 moveTargetPoint;
    BlockPos anchorPoint;
    DarkspawnObserver.AttackPhase attackPhase;
    private BlockPos spawnPoint;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("fly");

    private final AnimationController<DarkspawnObserver> movementController;

    private static final AttributeSupplier ATTRIBUTES = Monster.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 15.0D)
            .add(Attributes.ARMOR, 0.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.29D)
            .add(Attributes.ATTACK_DAMAGE, 5.0D)
            .add(Attributes.FOLLOW_RANGE, 35.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
            .add(Attributes.FLYING_SPEED, 0.40D).build();

    public DarkspawnObserver(EntityType<? extends FlyingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 5;
        this.moveControl = new DarkspawnObserverMoveControl(this);

        this.moveTargetPoint = Vec3.ZERO;
        this.anchorPoint = BlockPos.ZERO;
        this.attackPhase = DarkspawnObserver.AttackPhase.CIRCLE;

        if (this.spawnPoint == null) {
            this.spawnPoint = this.blockPosition();
        }

        this.movementController = new AnimationController<>(this, "movement", 0, this::movementPredicate);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            if (this.hasActiveAttackTarget()) {
                if (this.clientSideAttackTime < this.attackDuration) {
                    ++this.clientSideAttackTime;
                }
            } else {
                this.clientSideAttackTime = 0;
            }
        }

        if (this.spawnPoint != null) {
            int relativeHeight = this.blockPosition().getY() - this.spawnPoint.getY();

            if (relativeHeight > MAX_FLY_HEIGHT) {
                double excess = relativeHeight - MAX_FLY_HEIGHT;
                this.setDeltaMovement(
                        this.getDeltaMovement().x,
                        -0.2 * Math.min(excess / 5, 1),
                        this.getDeltaMovement().z
                );

                if (this.moveTargetPoint.y > this.spawnPoint.getY() + MAX_FLY_HEIGHT) {
                    this.moveTargetPoint = new Vec3(
                            this.moveTargetPoint.x,
                            this.spawnPoint.getY() + MAX_FLY_HEIGHT - 5,
                            this.moveTargetPoint.z
                    );
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(movementController);
    }

    private PlayState movementPredicate(AnimationState<DarkspawnObserver> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(FLY_ANIM);
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static AttributeSupplier createAttributes() {
        return ATTRIBUTES;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CapturingTargetAttackGoal(this, 40, true,
                MobEffects.MOVEMENT_SLOWDOWN, 0.0F, 0.0F));
        this.goalSelector.addGoal(1, new DarkspawnObserverAttackStrategyGoal());
        this.goalSelector.addGoal(2, new DarkspawnObserverSweepAttackGoal());
        this.goalSelector.addGoal(3, new DarkspawnObserverCircleAroundAnchorGoal());
        this.targetSelector.addGoal(1, new DarkspawnObserverAttackTargetGoal());
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel){
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return true;
            }

            @Override
            public boolean canCutCorner(BlockPathTypes pPathType) {
                return true;
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        if(this.spawnPoint != null) {
            pCompound.putInt("spawnX", this.spawnPoint.getX());
            pCompound.putInt("spawnY", this.spawnPoint.getY());
            pCompound.putInt("spawnZ", this.spawnPoint.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("spawnX")) {
            this.spawnPoint = new BlockPos(
                    pCompound.getInt("spawnX"),
                    pCompound.getInt("spawnY"),
                    pCompound.getInt("spawnZ")
            );
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.spawnPoint = this.blockPosition();
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ATTACK_TARGET_ID, 0);
    }

    @Override
    public void setActiveAttackTarget(int entityId) {
        this.entityData.set(DATA_ATTACK_TARGET_ID, entityId);
    }

    @Override
    public boolean hasActiveAttackTarget() {
        return this.entityData.get(DATA_ATTACK_TARGET_ID) != 0;
    }

    @Override
    public @Nullable LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) return null;
        if (this.level().isClientSide) {
            Entity entity = this.level().getEntity(this.entityData.get(DATA_ATTACK_TARGET_ID));
            return entity instanceof LivingEntity ? (LivingEntity) entity : null;
        }
        return this.getTarget();
    }

    @Override
    public int getAttackDuration() {
        return this.attackDuration;
    }

    @Override
    public float getAttackAnimationScale(float partialTicks) {
        return ((float)this.clientSideAttackTime + partialTicks) / (float)this.attackDuration;
    }

    abstract class DarkspawnObserverMoveTargetGoal extends Goal {
        public DarkspawnObserverMoveTargetGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return DarkspawnObserver.this.moveTargetPoint.distanceToSqr(DarkspawnObserver.this.getX(), DarkspawnObserver.this.getY(), DarkspawnObserver.this.getZ()) < (double)4.0F;
        }
    }

    class DarkspawnObserverMoveControl extends MoveControl {
        private float speed = 0.5F;

        public DarkspawnObserverMoveControl(Mob pMob) {
            super(pMob);
        }

        public void tick() {
            if (DarkspawnObserver.this.horizontalCollision) {
                DarkspawnObserver.this.setYRot(DarkspawnObserver.this.getYRot() + 180.0F);
                this.speed = 0.1F;
            }

            double $$0 = DarkspawnObserver.this.moveTargetPoint.x - DarkspawnObserver.this.getX();
            double $$1 = DarkspawnObserver.this.moveTargetPoint.y - DarkspawnObserver.this.getY();
            double $$2 = DarkspawnObserver.this.moveTargetPoint.z - DarkspawnObserver.this.getZ();
            double $$3 = Math.sqrt($$0 * $$0 + $$2 * $$2);
            if (Math.abs($$3) > (double)1.0E-5F) {
                double $$4 = (double)1.0F - Math.abs($$1 * (double)0.7F) / $$3;
                $$0 *= $$4;
                $$2 *= $$4;
                $$3 = Math.sqrt($$0 * $$0 + $$2 * $$2);
                double $$5 = Math.sqrt($$0 * $$0 + $$2 * $$2 + $$1 * $$1);
                float $$6 = DarkspawnObserver.this.getYRot();
                float $$7 = (float)Mth.atan2($$2, $$0);
                float $$8 = Mth.wrapDegrees(DarkspawnObserver.this.getYRot() + 90.0F);
                float $$9 = Mth.wrapDegrees($$7 * (180F / (float)Math.PI));
                DarkspawnObserver.this.setYRot(Mth.approachDegrees($$8, $$9, 4.0F) - 90.0F);
                DarkspawnObserver.this.yBodyRot = DarkspawnObserver.this.getYRot();
                if (Mth.degreesDifferenceAbs($$6, DarkspawnObserver.this.getYRot()) < 3.0F) {
                    this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
                } else {
                    this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
                }

                float $$10 = (float)(-(Mth.atan2(-$$1, $$3) * (double)(180F / (float)Math.PI)));
                DarkspawnObserver.this.setXRot($$10);
                float $$11 = DarkspawnObserver.this.getYRot() + 90.0F;
                double $$12 = (double)(this.speed * Mth.cos($$11 * ((float)Math.PI / 180F))) * Math.abs($$0 / $$5);
                double $$13 = (double)(this.speed * Mth.sin($$11 * ((float)Math.PI / 180F))) * Math.abs($$2 / $$5);
                double $$14 = (double)(this.speed * Mth.sin($$10 * ((float)Math.PI / 180F))) * Math.abs($$1 / $$5);
                Vec3 $$15 = DarkspawnObserver.this.getDeltaMovement();
                DarkspawnObserver.this.setDeltaMovement($$15.add((new Vec3($$12, $$14, $$13)).subtract($$15).scale(0.2)));
            }

        }
    }

    class DarkspawnObserverCircleAroundAnchorGoal extends DarkspawnObserverMoveTargetGoal {
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        public boolean canUse() {
            return DarkspawnObserver.this.getTarget() == null || DarkspawnObserver.this.attackPhase == DarkspawnObserver.AttackPhase.CIRCLE;
        }

        public void start() {
            this.distance = 5.0F + DarkspawnObserver.this.random.nextFloat() * 10.0F;
            this.height = -4.0F + DarkspawnObserver.this.random.nextFloat() * 9.0F;
            this.clockwise = DarkspawnObserver.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        public void tick() {
            if (DarkspawnObserver.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
                this.height = -4.0F + DarkspawnObserver.this.random.nextFloat() * 9.0F;
            }

            if (DarkspawnObserver.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
                ++this.distance;
                if (this.distance > 15.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }

            if (DarkspawnObserver.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
                this.angle = DarkspawnObserver.this.random.nextFloat() * 2.0F * (float)Math.PI;
                this.selectNext();
            }

            if (this.touchingTarget()) {
                this.selectNext();
            }

            if (DarkspawnObserver.this.moveTargetPoint.y < DarkspawnObserver.this.getY() && !DarkspawnObserver.this.level().isEmptyBlock(DarkspawnObserver.this.blockPosition().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (DarkspawnObserver.this.moveTargetPoint.y > DarkspawnObserver.this.getY() && !DarkspawnObserver.this.level().isEmptyBlock(DarkspawnObserver.this.blockPosition().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }

        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(DarkspawnObserver.this.anchorPoint)) {
                DarkspawnObserver.this.anchorPoint = DarkspawnObserver.this.blockPosition();
            }

            this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180F);
            DarkspawnObserver.this.moveTargetPoint = Vec3.atLowerCornerOf(DarkspawnObserver.this.anchorPoint).add((double)(this.distance * Mth.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * Mth.sin(this.angle)));
        }
    }

    class DarkspawnObserverSweepAttackGoal extends DarkspawnObserverMoveTargetGoal {
        private static final int CAT_SEARCH_TICK_DELAY = 20;
        private boolean isScaredOfCat;
        private int catSearchTick;

        public boolean canUse() {
            return DarkspawnObserver.this.getTarget() != null && DarkspawnObserver.this.attackPhase == DarkspawnObserver.AttackPhase.SWOOP;
        }

        public boolean canContinueToUse() {
            LivingEntity target = DarkspawnObserver.this.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else {
                if (target instanceof Player player) {
                    if (target.isSpectator() || player.isCreative()) {
                        return false;
                    }
                }

                if (!this.canUse()) {
                    return false;
                } else {
                    if (DarkspawnObserver.this.tickCount > this.catSearchTick) {
                        this.catSearchTick = DarkspawnObserver.this.tickCount + 20;
                        List<Cat> cats = DarkspawnObserver.this.level().getEntitiesOfClass(Cat.class, DarkspawnObserver.this.getBoundingBox().inflate((double)16.0F), EntitySelector.ENTITY_STILL_ALIVE);

                        for(Cat $$3 : cats) {
                            $$3.hiss();
                        }

                        this.isScaredOfCat = !cats.isEmpty();
                    }

                    return !this.isScaredOfCat;
                }
            }
        }

        public void start() {
        }

        public void stop() {
            DarkspawnObserver.this.setTarget((LivingEntity)null);
            DarkspawnObserver.this.attackPhase = DarkspawnObserver.AttackPhase.CIRCLE;
        }

        public void tick() {
            LivingEntity target = DarkspawnObserver.this.getTarget();
            if (target != null) {
                DarkspawnObserver.this.moveTargetPoint = new Vec3(target.getX(), target.getY((double)0.7F), target.getZ());
                if (DarkspawnObserver.this.getBoundingBox().inflate((double)0.2F).intersects(target.getBoundingBox())) {
                    DarkspawnObserver.this.doHurtTarget(target);
                    DarkspawnObserver.this.attackPhase = DarkspawnObserver.AttackPhase.CIRCLE;
                    if (!DarkspawnObserver.this.isSilent()) {
                        DarkspawnObserver.this.level().levelEvent(1039, DarkspawnObserver.this.blockPosition(), 0);
                    }
                } else if (DarkspawnObserver.this.horizontalCollision || DarkspawnObserver.this.hurtTime > 0) {
                    DarkspawnObserver.this.attackPhase = DarkspawnObserver.AttackPhase.CIRCLE;
                }

            }
        }
    }

    class DarkspawnObserverAttackStrategyGoal extends Goal {
        private int nextSweepTick;

        public boolean canUse() {
            LivingEntity target = DarkspawnObserver.this.getTarget();
            return target != null ? DarkspawnObserver.this.canAttack(target, TargetingConditions.DEFAULT) : false;
        }

        public void start() {
            this.nextSweepTick = this.adjustedTickDelay(10);
            DarkspawnObserver.this.attackPhase = DarkspawnObserver.AttackPhase.CIRCLE;
            this.setAnchorAboveTarget();
        }

        public void stop() {
            DarkspawnObserver.this.anchorPoint = DarkspawnObserver.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, DarkspawnObserver.this.anchorPoint).above(10 + DarkspawnObserver.this.random.nextInt(20));
        }

        public void tick() {
            if (DarkspawnObserver.this.attackPhase == DarkspawnObserver.AttackPhase.CIRCLE) {
                --this.nextSweepTick;
                if (this.nextSweepTick <= 0) {
                    DarkspawnObserver.this.attackPhase = DarkspawnObserver.AttackPhase.SWOOP;
                    this.setAnchorAboveTarget();
                    this.nextSweepTick = this.adjustedTickDelay((8 + DarkspawnObserver.this.random.nextInt(4)) * 20);
                    DarkspawnObserver.this.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + DarkspawnObserver.this.random.nextFloat() * 0.1F);
                }
            }

        }

        private void setAnchorAboveTarget() {
            DarkspawnObserver.this.anchorPoint = DarkspawnObserver.this.getTarget().blockPosition().above(20 + DarkspawnObserver.this.random.nextInt(20));
            if (DarkspawnObserver.this.anchorPoint.getY() < DarkspawnObserver.this.level().getSeaLevel()) {
                DarkspawnObserver.this.anchorPoint = new BlockPos(DarkspawnObserver.this.anchorPoint.getX(), DarkspawnObserver.this.level().getSeaLevel() + 1, DarkspawnObserver.this.anchorPoint.getZ());
            }

        }
    }

    class DarkspawnObserverAttackTargetGoal extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range((double)64.0F);
        private int nextScanTick = reducedTickDelay(20);

        private static final Set<Class<? extends Mob>> BLACKLIST = Set.of(
                DarkspawnObserver.class,
                DarkspawnLarva.class,
                ModeusBoss.class
        );

        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            } else {
                this.nextScanTick = reducedTickDelay(60);
                List<Mob> mobs = DarkspawnObserver.this.level().getNearbyEntities(Mob.class, this.attackTargeting, DarkspawnObserver.this,
                        DarkspawnObserver.this.getBoundingBox().inflate((double)16.0F, (double)64.0F, (double)16.0F));
                List<Player> players = DarkspawnObserver.this.level().getNearbyPlayers(this.attackTargeting, DarkspawnObserver.this,
                        DarkspawnObserver.this.getBoundingBox().inflate((double)16.0F, (double)64.0F, (double)16.0F));

                List<LivingEntity> targets = new ArrayList<>();
                targets.addAll(players);


                for (Mob mob : mobs) {
                    if(!isBlackListMob(mob)) {
                        targets.add(mob);
                    }
                }

                if (!targets.isEmpty()) {
                    targets.sort(Comparator.<LivingEntity>comparingDouble(p -> p.getY()).reversed());

                    for(LivingEntity target : targets) {
                        if (DarkspawnObserver.this.canAttack(target, TargetingConditions.DEFAULT)) {
                            DarkspawnObserver.this.setTarget(target);
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        public boolean canContinueToUse() {
            LivingEntity target = DarkspawnObserver.this.getTarget();
            if (target == null) {
                return false;
            }
            // Дополнительная проверка при продолжении преследования
            if (isBlackListMob(target)) {
                return false;
            }
            return DarkspawnObserver.this.canAttack(target, TargetingConditions.DEFAULT);
        }

        private boolean isBlackListMob(LivingEntity entity) {
            for(Class<? extends Mob> blackListClasses : BLACKLIST) {
                if(blackListClasses.isInstance(entity))
                    return true;
            }

            return false;
        }
    }

    static enum AttackPhase {
        CIRCLE,
        SWOOP;
    }
}
