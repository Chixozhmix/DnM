package net.chixozhmix.dnmmod.blocks.entity;

import net.chixozhmix.dnmmod.registers.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class LeshyAltarEntity extends BlockEntity {
    private int useCount = 0;
    private static final int MAX_USES = 4;

    public LeshyAltarEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.LESHY_ALTAR.get(), pPos, pBlockState);
    }

    public boolean incrementUseCount() {
        if (useCount < MAX_USES) {
            useCount++;
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
            return true;
        }
        return false;
    }

    public int getUseCount() {
        return useCount;
    }

    public boolean canUse() {
        return useCount < MAX_USES;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("UseCount", useCount);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        useCount = pTag.getInt("UseCount");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
