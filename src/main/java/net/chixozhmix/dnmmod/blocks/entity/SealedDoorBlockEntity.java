package net.chixozhmix.dnmmod.blocks.entity;

import net.chixozhmix.dnmmod.blocks.custom.SealedDoorBlock;
import net.chixozhmix.dnmmod.registers.ModBlockEntities;
import net.chixozhmix.dnmmod.registers.ModBlocks;
import net.chixozhmix.dnmmod.registers.SoundsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

public class SealedDoorBlockEntity extends BlockEntity {
    public int Animaitonticks;
    public int tickCount;
    public int animation = 0;
    public Direction facing;
    public AnimationState openingAnimationState = new AnimationState();
    public AnimationState openAnimationState = new AnimationState();

    public SealedDoorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DOOR_OF_SEAL.get(), pos, state);
        facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    public AnimationState getAnimationState(String input) {
        if (input == "opening") {
            return this.openingAnimationState;
        } else if (input == "open") {
            return this.openAnimationState;
        }else {
            return new AnimationState();
        }
    }

    public boolean triggerEvent(int p_58837_, int p_58838_) {
        if (p_58837_ == 1) {
            this.openingAnimationState.start(this.tickCount);
            return true;
        } else {
            return super.triggerEvent(p_58837_, p_58838_);
        }
    }


    public static void tick(Level level, BlockPos pos, BlockState blockState, SealedDoorBlockEntity entity) {
        if (!entity.isBaseBlock()) return;

        entity.tickCount++;
        if(blockState.getBlock() instanceof SealedDoorBlock) {
            if (blockState.getValue(SealedDoorBlock.LIT)) {
                entity.Animaitonticks++;

                if (!blockState.getValue(SealedDoorBlock.OPEN)) {

                    if (entity.Animaitonticks == 28) {
                        level.playSound((Player)null, pos, SoundsRegistry.NECRO_MAGIC.get(), SoundSource.BLOCKS, 4F, level.random.nextFloat() * 0.2F + 1.0F);

                        float x = pos.getX() + 0.5F;
                        float y = pos.getY();
                        float z = pos.getZ() + 0.5F;
                        if (!level.isClientSide) {
                            level.explode(null, x, y + 1, z, 2.0F, Level.ExplosionInteraction.NONE);
                        }
                    }
                    if (entity.Animaitonticks >= 145) {
                        if (!level.isClientSide) {
                            level.setBlock(pos, blockState.setValue(SealedDoorBlock.OPEN, Boolean.valueOf(true)), 2);
                            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(null, blockState));

                            for (int i = 0; i <= 7; i++) {
                                BlockPos abovePos = pos.above(i);
                                BlockPos blockpos1 = abovePos.relative(blockState.getValue(SealedDoorBlock.FACING).getClockWise());
                                BlockPos blockpos2 = abovePos;
                                BlockPos blockpos3 = abovePos.relative(blockState.getValue(SealedDoorBlock.FACING).getCounterClockWise());
                                BlockPos blockpos4 = abovePos.relative(blockState.getValue(SealedDoorBlock.FACING).getClockWise(), 2);
                                BlockPos blockpos5 = abovePos.relative(blockState.getValue(SealedDoorBlock.FACING).getCounterClockWise(), 2);
                                BlockPos[] toBreakPoses = {blockpos1, blockpos2, blockpos3, blockpos4, blockpos5};
                                for (BlockPos toBreakPos : toBreakPoses) {
                                    BlockState state = level.getBlockState(toBreakPos);
                                    if (state.is(ModBlocks.DOOR_OF_SEAL.get())) {
                                        level.setBlock(toBreakPos, state.setValue(SealedDoorBlock.OPEN, Boolean.valueOf(true)), 2);
                                        level.gameEvent(GameEvent.BLOCK_CHANGE, toBreakPos, GameEvent.Context.of(null, state));
                                    }
                                }
                            }
                        }
                    }
                }else{
                    entity.Animaitonticks = 0;
                    if (level.isClientSide) {
                        entity.openingAnimationState.stop();
                        entity.openAnimationState.startIfStopped(entity.tickCount);
                    }
                }

            }
        }
    }

    public void onHit(Level level) {
        BlockPos blockpos = this.getBlockPos();
        BlockState state = this.getBlockState();
        if (!state.getValue(SealedDoorBlock.LIT)) {
            level.setBlock(blockpos, state.setValue(SealedDoorBlock.LIT, Boolean.valueOf(true)), 2);
            level.blockEvent(blockpos, this.getBlockState().getBlock(), 1, 0);
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.Animaitonticks = compound.getInt("animationTicks");

    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("animationTicks", this.Animaitonticks);
    }

    public AABB getRenderBoundingBox() {
        if (isBaseBlock()) {
            return new AABB(getBlockPos()).inflate(4, 0, 4).expandTowards(0, 8, 0);
        }
        return super.getRenderBoundingBox();
    }

    public boolean isBaseBlock() {
        BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof SealedDoorBlock)) return false;
        return state.getValue(SealedDoorBlock.PART) == SealedDoorBlock.SealedDoorPart.CENTER
                && state.getValue(SealedDoorBlock.Y_OFFSET) == 0;
    }
}
