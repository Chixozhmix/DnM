package net.chixozhmix.dnmmod.entity.spell.trident_strike_area;

import io.redspace.ironsspellbooks.api.util.Utils;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.UUID;

public class TridentStrikeAreaEntity extends Entity {
    private static final EntityDataAccessor<Float> DATA_LENGTH;
    private static final EntityDataAccessor<Float> DATA_WIDTH;
    private static final EntityDataAccessor<Integer> DATA_COLOR;
    private static final EntityDataAccessor<Float> DATA_DIRECTION_X;
    private static final EntityDataAccessor<Float> DATA_DIRECTION_Z;

    private int duration = 40;
    private boolean shouldFade = true;

    @Nullable
    private UUID ownerUUID;
    private int ownerNetworkId = -1;
    @Nullable
    private Entity cachedOwner;
    private boolean hasOwner;

    private Vec3 beamDirection = new Vec3(1, 0, 0);
    private Vec3 originalDirection; // Сохраняем исходное направление

    public TridentStrikeAreaEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public TridentStrikeAreaEntity(Level level, Vec3 startPos, Vec3 direction, float length, float width, int color) {
        this(ModEntityType.TRIDENT_STRIKE_AREA.get(), level);
        this.setPos(startPos);
        this.originalDirection = direction.normalize();
        this.beamDirection = this.originalDirection;
        this.setBeamDirection((float) this.beamDirection.x, (float) this.beamDirection.z);
        this.setLength(length);
        this.setWidth(width);
        this.setColor(color);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (DATA_DIRECTION_X.equals(pKey) || DATA_DIRECTION_Z.equals(pKey)) {
            this.beamDirection = new Vec3(getBeamDirectionX(), 0, getBeamDirectionZ()).normalize();
        }
    }

    public void setOwner(@Nullable Entity pOwner) {
        if (pOwner != null) {
            this.ownerUUID = pOwner.getUUID();
            this.cachedOwner = pOwner;
            this.hasOwner = true;
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && this.cachedOwner.isAlive()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            this.cachedOwner = serverLevel.getEntity(this.ownerUUID);
            return this.cachedOwner;
        }
        return null;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_LENGTH, 5.0F);
        this.entityData.define(DATA_WIDTH, 1.0F);
        this.entityData.define(DATA_COLOR, 0xFF4444);
        this.entityData.define(DATA_DIRECTION_X, 1.0F);
        this.entityData.define(DATA_DIRECTION_Z, 0.0F);
    }

    public void setLength(float length) {
        this.entityData.set(DATA_LENGTH, Mth.clamp(length, 0.1F, 64.0F));
    }

    public float getLength() {
        return this.entityData.get(DATA_LENGTH);
    }

    public void setWidth(float width) {
        this.entityData.set(DATA_WIDTH, Mth.clamp(width, 0.1F, 8.0F));
    }

    public float getWidth() {
        return this.entityData.get(DATA_WIDTH);
    }

    public void setBeamDirection(float x, float z) {
        this.entityData.set(DATA_DIRECTION_X, x);
        this.entityData.set(DATA_DIRECTION_Z, z);
        this.beamDirection = new Vec3(x, 0, z).normalize();
    }

    public Vec3 getBeamDirection() {
        return new Vec3(getBeamDirectionX(), 0, getBeamDirectionZ()).normalize();
    }

    public float getBeamDirectionX() {
        return this.entityData.get(DATA_DIRECTION_X);
    }

    public float getBeamDirectionZ() {
        return this.entityData.get(DATA_DIRECTION_Z);
    }

    public void setColor(int color) {
        this.entityData.set(DATA_COLOR, color);
    }

    public Vector3f getColor() {
        return Utils.deconstructRGB(this.entityData.get(DATA_COLOR));
    }

    public int getColorRaw() {
        return this.entityData.get(DATA_COLOR);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setShouldFade(boolean shouldFade) {
        this.shouldFade = shouldFade;
    }

    public boolean shouldFade() {
        return this.shouldFade;
    }

    @Override
    public void tick() {
        super.tick();

        if(level().isClientSide && !hasOwner && ownerNetworkId != -1) {
            Entity entity = level().getEntity(ownerNetworkId);
            if(entity != null) {
                setOwner(entity);
            }
        }

        if (!this.level().isClientSide && this.tickCount >= this.duration) {
            this.discard();
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getWidth(), 0.5F);
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Length", this.getLength());
        tag.putFloat("Width", this.getWidth());
        tag.putInt("Color", this.getColorRaw());
        tag.putInt("Duration", this.duration);
        tag.putBoolean("ShouldFade", this.shouldFade);
        tag.putFloat("DirX", this.getBeamDirectionX());
        tag.putFloat("DirZ", this.getBeamDirectionZ());
        if (ownerUUID != null) {
            tag.putUUID("Owner", ownerUUID);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setLength(tag.getFloat("Length"));
        this.setWidth(tag.getFloat("Width"));
        this.setColor(tag.getInt("Color"));
        this.duration = tag.getInt("Duration");
        this.shouldFade = tag.getBoolean("ShouldFade");
        this.setBeamDirection(tag.getFloat("DirX"), tag.getFloat("DirZ"));
        if (tag.contains("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
            this.hasOwner = true;
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        this.ownerNetworkId = pPacket.getData();
        if (this.ownerNetworkId != 0) {
            Entity entity = this.level().getEntity(this.ownerNetworkId);
            if (entity != null) {
                this.setOwner(entity);
            }
        }
    }

    static {
        DATA_LENGTH = SynchedEntityData.defineId(TridentStrikeAreaEntity.class, EntityDataSerializers.FLOAT);
        DATA_WIDTH = SynchedEntityData.defineId(TridentStrikeAreaEntity.class, EntityDataSerializers.FLOAT);
        DATA_COLOR = SynchedEntityData.defineId(TridentStrikeAreaEntity.class, EntityDataSerializers.INT);
        DATA_DIRECTION_X = SynchedEntityData.defineId(TridentStrikeAreaEntity.class, EntityDataSerializers.FLOAT);
        DATA_DIRECTION_Z = SynchedEntityData.defineId(TridentStrikeAreaEntity.class, EntityDataSerializers.FLOAT);
    }
}