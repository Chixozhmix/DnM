package net.chixozhmix.dnmmod.entity.spell.ray_of_enfeeblement;

import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class RayOfEnfeeblement extends Entity implements IEntityAdditionalSpawnData {
    public static final int lifetime = 15;
    public float distance;

    public RayOfEnfeeblement(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RayOfEnfeeblement(Level level, Vec3 start, Vec3 end, LivingEntity owner) {
        super((EntityType) ModEntityType.RAY_OF_ENFEEBLEMENT.get(), level);
        this.setPos(start.subtract((double)0.0F, (double)0.75F, (double)0.0F));
        this.distance = (float)start.distanceTo(end);
        this.setRot(owner.getYRot(), owner.getXRot());
    }

    @Override
    public void tick() {
        if (++this.tickCount > 15) {
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt((int)(this.distance * 10.0F));
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.distance = (float)additionalData.readInt() / 10.0F;
    }
}
