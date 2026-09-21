package net.chixozhmix.dnmmod.entity.spell;

import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JumpAOE extends AoeEntity {
    public static Map<UUID, JumpAOE> clientJumpOrigins = new HashMap();
    private CameraShakeData cameraShakeData;
    private int slownessAmplifier;
    int waveAnim;


    public JumpAOE(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.waveAnim = -1;
        this.reapplicationDelay = 25;
        this.setCircular();
    }

    public JumpAOE(Level level) {
        this(ModEntityType.JUMP_AOE.get(), level);
    }

    @Override
    public void applyEffect(LivingEntity livingEntity) {

    }

    public int getSlownessAmplifier() {
        return this.slownessAmplifier;
    }

    public void setSlownessAmplifier(int slownessAmplifier) {
        this.slownessAmplifier = slownessAmplifier;
    }

    @Override
    public float getParticleCount() {
        return 0;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount == 1) {
            this.createScreenShake();
        }

        if (!this.level().isClientSide) {
            float radius = 5;
            Level level = this.level();
            int intensity = Math.min((int)(radius * radius * 0.09F), 15);

            for(int i = 0; i < intensity; ++i) {
                Vec3 vec3 = this.position().add(this.uniformlyDistributedPointInRadius(radius));
                BlockPos blockPos = BlockPos.containing(Utils.moveToRelativeGroundLevel(level, vec3, 4)).below();
                Utils.createTremorBlock(level, blockPos, 0.2F + this.random.nextFloat() * 0.2F);
            }

            if (this.waveAnim >= 0) {
                float circumference = (float)(this.waveAnim * 2) * 3.14F;
                int blocks = Mth.clamp((int)circumference, 0, 250);
                float anglePerBlock = 360.0F / (float)blocks;

                for(int i = 0; i < blocks; ++i) {
                    Vec3 vec3 = new Vec3((double)((float)this.waveAnim * Mth.cos(anglePerBlock * (float)i)), (double)0.0F, (double)((float)this.waveAnim * Mth.sin(anglePerBlock * (float)i)));
                    BlockPos blockPos = BlockPos.containing(Utils.moveToRelativeGroundLevel(level, this.position().add(vec3), 4)).below();
                    Utils.createTremorBlock(level, blockPos, 0.1F + this.random.nextFloat() * 0.2F);
                }

                if ((float)(this.waveAnim++) >= radius) {
                    this.waveAnim = -1;
                    if (this.tickCount + this.reapplicationDelay >= this.duration) {
                        this.discard();
                    }
                }
            }
        }
    }

    protected void createScreenShake() {
        if (!this.level().isClientSide && !this.isRemoved()) {
            this.cameraShakeData = new CameraShakeData(this.level(), this.duration - this.tickCount, this.position(), 15.0F);
            CameraShakeManager.addCameraShake(this.cameraShakeData);
        }
    }

    @Override
    protected Vec3 getInflation() {
        return new Vec3((double)0.0F, (double)5.0F, (double)0.0F);
    }

    @Override
    public void remove(RemovalReason pReason) {
        super.remove(pReason);

        if (!this.level().isClientSide) {
            CameraShakeManager.removeCameraShake(this.cameraShakeData);
        }
    }

    protected Vec3 uniformlyDistributedPointInRadius(float r) {
        float distance = r * (1.0F - this.random.nextFloat() * this.random.nextFloat());
        float theta = this.random.nextFloat() * 6.282F;
        return new Vec3((double)(distance * Mth.cos(theta)), (double)0.2F, (double)(distance * Mth.sin(theta)));
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(10, 3.0F);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Slowness", this.slownessAmplifier);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.slownessAmplifier = pCompound.getInt("Slowness");
        this.createScreenShake();
    }
}
