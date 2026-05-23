package net.chixozhmix.dnmmod.entity.spell.red_cristall;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RedCristallEntity extends AoeEntity {
    private static final EntityDataAccessor<Float> DATA_SIZE;
    private static final EntityDataAccessor<Integer> DATA_WAIT_TIME;
    public static final int RISE_TIME = 6;
    public static final int REST_TIME = 40;
    public static final int LOWER_TIME = 30;
    private final List<Entity> victims;

    public RedCristallEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.victims = new ArrayList();
    }

    public RedCristallEntity(Level level, LivingEntity owner) {
        this((EntityType) ModEntityType.RED_CRYSTAL.get(), level);
        this.setOwner(owner);
    }

    @Override
    public void applyEffect(LivingEntity livingEntity) {

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SIZE, 1.0F);
        this.entityData.define(DATA_WAIT_TIME, 10);
    }

    public void setCristallSize(float f) {
        this.entityData.set(DATA_SIZE, f);
        this.refreshDimensions();
    }

    public float getCristallSize() {
        return (Float)this.entityData.get(DATA_SIZE);
    }

    public int getWaitTime() {
        return (Integer)this.entityData.get(DATA_WAIT_TIME);
    }

    public void setWaitTime(int i) {
        this.entityData.set(DATA_WAIT_TIME, i);
    }

    public float getPositionOffset(float partialTick) {
        float f = (float)this.tickCount + partialTick;
        int waitTime = this.getWaitTime();
        if (f < (float)waitTime) {
            return -1.0F;
        } else if (f < (float)(waitTime + 6)) {
            f = (f - (float)waitTime) / 6.0F;
            return Mth.sin(f * (float)Math.PI) / (float)Math.PI + f - 1.0F;
        } else if (f < (float)(waitTime + 6 + 40)) {
            return 0.0F;
        } else {
            f = Mth.clamp((f - (float)(waitTime + 6 + 40)) / 30.0F, 0.0F, 1.0F) + 1.0F;
            return -(Mth.sin(f * (float)Math.PI) / (float)Math.PI + f - 1.0F);
        }
    }

    @Override
    public void tick() {
        this.refreshDimensions();
        int waitTime = this.getWaitTime();
        if (this.tickCount == waitTime) {
            if (!this.level().isClientSide) {
                float f = this.getCristallSize();
                if (!this.isSilent()) {
                    this.level().playSound((Player)null, this.blockPosition(), (SoundEvent) SoundRegistry.ICE_SPIKE_EMERGE.get(), SoundSource.NEUTRAL, 1.25F * this.getCristallSize(), (float)Mth.randomBetweenInclusive(Utils.random, 6, 12) * 0.1F);
                }

                MagicManager.spawnParticles(this.level(), ParticleTypes.SCULK_SOUL, this.getX(), this.level().clip(new ClipContext(this.position().add((double)0.0F, (double)2.0F, (double)0.0F), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)null)).getLocation().y() + 0.1, this.getZ(), (int)(10.0F * f * f), 0.1 * (double)f, 0.1 * (double)f, (double)(0.1F * f), 0.12 * (double)f, false);
                MagicManager.spawnParticles(this.level(), ParticleTypes.SCULK_CHARGE_POP, this.getX(), this.level().clip(new ClipContext(this.position().add((double)0.0F, (double)2.0F, (double)0.0F), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)null)).getLocation().y() + 0.1, this.getZ(), (int)(15.0F * f * f), 0.1 * (double)f, 0.1 * (double)f, (double)(0.1F * f), 0.08 * (double)f, false);
            }
        } else if (this.tickCount > waitTime && this.tickCount < waitTime + 6) {
            AABB damager = this.getBoundingBox();
            damager.setMaxY(this.getY() + damager.getYsize() * (double)(this.getPositionOffset(0.0F) + 1.0F));

            for(Entity entity : this.level().getEntities(this, damager).stream().filter((target) -> this.canHitEntity(target) && !this.victims.contains(target)).collect(Collectors.toSet())) {
                entity.hurt(this.damageSources().indirectMagic(this, this), this.damage);
                entity.setDeltaMovement(entity.getDeltaMovement().add((double)0.0F, (double)this.getCristallSize() * 0.3, (double)0.0F));
                entity.hurtMarked = true;

                this.victims.add(entity);
            }
        } else if (this.tickCount > waitTime + 6 + 40 + 30) {
            this.discard();
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("waitTime", this.getWaitTime());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setWaitTime(pCompound.getInt("waitTime"));
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getCristallSize() * 0.4F, this.getCristallSize() * 1.25F * (this.getPositionOffset(1.0F) + 1.0F));
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public float getParticleCount() {
        return 0.0f;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }

    static {
        DATA_SIZE = SynchedEntityData.defineId(RedCristallEntity.class, EntityDataSerializers.FLOAT);
        DATA_WAIT_TIME = SynchedEntityData.defineId(RedCristallEntity.class, EntityDataSerializers.INT);
    }
}
