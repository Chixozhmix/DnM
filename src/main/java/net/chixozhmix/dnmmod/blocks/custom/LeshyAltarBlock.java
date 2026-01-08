package net.chixozhmix.dnmmod.blocks.custom;

import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.entity.leshy.LeshyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LeshyAltarBlock extends Block {
    public static final VoxelShape SHAPE = Block.box(0,0,0,16,16,16);

    public LeshyAltarBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(!pLevel.isClientSide && !pState.is(pNewState.getBlock())) {
            destroyBlocksInRadius((ServerLevel) pLevel, pPos, 3);
            spawnCreature((ServerLevel) pLevel, pPos);
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    private void spawnCreature(ServerLevel level, BlockPos pos) {
        LeshyEntity leshy = ModEntityType.LESHY.get().create(level);

        if(leshy != null) {
            leshy.setPos(pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f);

            level.addFreshEntity(leshy);
        }
    }

    private void destroyBlocksInRadius(ServerLevel level, BlockPos centerPos, int radius) {
        // Перебираем все блоки в кубе с радиусом
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = centerPos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(targetPos);

                    // Проверяем, что блок не является воздухом и не является самим алтарем
                    if (!blockState.isAir() && !targetPos.equals(centerPos) && y >= 0) {
                        // Удаляем блок, вызывая его деструкцию с дропом предметов
                        level.destroyBlock(targetPos, false);
                    }
                }
            }
        }
    }

}
