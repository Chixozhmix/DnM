package net.chixozhmix.dnmmod.entity.dragons;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.entity.PartEntity;


public class DragonPartEntity extends PartEntity<AbstractDragonEntity> {
    public final AbstractDragonEntity parentMob;
    public final String name;

    private EntityDimensions size;

    public DragonPartEntity(AbstractDragonEntity parent, String name, float width, float height) {
        super(parent);

        this.parentMob = parent;
        this.name = name;
        this.size = EntityDimensions.scalable(width, height);

        this.refreshDimensions();
    }

    public void setPartSize(float width, float height) {
        if (this.size.width == width && this.size.height == height) {
            return;
        }

        this.size = EntityDimensions.scalable(width, height);
        this.refreshDimensions();
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.size;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.parentMob == entity;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }

        return this.parentMob.hurt(source, amount);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException("DragonPartEntity should not be spawned independently");
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }
}
