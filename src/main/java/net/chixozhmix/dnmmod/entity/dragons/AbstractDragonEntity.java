package net.chixozhmix.dnmmod.entity.dragons;

import net.chixozhmix.dnmmod.entity.dragons.client.AnimationsEnum;
import net.chixozhmix.dnmmod.entity.dragons.client.animations.AbstractDragonModelAnimation;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class AbstractDragonEntity extends PathfinderMob implements Enemy {

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
    public final  DragonPartEntity torso;

    public final  DragonPartEntity attack_zone;

    private final DragonPartEntity[] parts;

    //Animations
    private AnimationsEnum currentAnimation = AnimationsEnum.IDLE;

    public final AnimationState idle = new AnimationState();
    private int idleAnimationTimeout = 0;
    public static final AnimationState walk = new AnimationState();

    public AbstractDragonEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 5;

        //Parts
        this.head = new DragonPartEntity(this, "head", 1.5f, 1f);
        this.neck = new DragonPartEntity(this, "neck", 1.5f, 0.7f);
        this.neck2 = new DragonPartEntity(this, "neck2", 1.5f, 0.7f);
        this.torso = new DragonPartEntity(this, "torso", 2.5f, 1.5f);

        this.attack_zone = new DragonPartEntity(this, "attack_zone", 2.5f, 2.5f);

        this.parts = new DragonPartEntity[] {head, neck, neck2, torso};
        this.setId(ENTITY_COUNTER.getAndAdd(this.parts.length + 1) + 1);
    }

    @Override
    public void setId(int pId) {
        super.setId(pId);
        for (int i = 0; i < this.parts.length; i++) {
            this.parts[i].setId(pId + i + 1);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2F, true));
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

        if(this.level().isClientSide) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idle.start(this.tickCount);
        } else
            --this.idleAnimationTimeout;
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else f = 0f;

        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        updateParts();
    }

    private void updateParts() {
        float bodyYaw = this.yBodyRot * Mth.DEG_TO_RAD;
        float sin = Mth.sin(bodyYaw);
        float cos = Mth.cos(bodyYaw);

        updateSinglePart(torso, 0.0, 1.0, 0.0, -sin, -cos);
        updateSinglePart(neck, 0.0, 2.5, -2.0, -sin, -cos);
        updateSinglePart(neck2, 0.0, 3.0, -3.0, -sin, -cos);
        updateSinglePart(head, 0.0, 3.0, -4.0, -sin, -cos);
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
        part.yRotO = this.yRotO; part.xRotO = this.xRotO;
    }

    public AnimationsEnum getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(AnimationsEnum animation) {
        this.currentAnimation = animation;
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

        if (true) return; // Forge: Fix MC-158205: Moved into setId()
        DragonPartEntity[] adragonpart = this.parts;

        for(int i = 0; i < adragonpart.length; ++i) {
            adragonpart[i].setId(i + pPacket.getId());
        }
    }
}