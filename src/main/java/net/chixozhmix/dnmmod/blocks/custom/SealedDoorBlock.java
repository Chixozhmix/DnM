package net.chixozhmix.dnmmod.blocks.custom;

import net.chixozhmix.dnmmod.blocks.entity.SealedDoorBlockEntity;
import net.chixozhmix.dnmmod.registers.ModBlockEntities;
import net.chixozhmix.dnmmod.registers.ModBlocks;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class SealedDoorBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<SealedDoorPart> PART = EnumProperty.create("door_part", SealedDoorPart.class);
    public static final IntegerProperty Y_OFFSET = IntegerProperty.create("y_offset", 0, 7);

    private static final VoxelShape CLOSED_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    private static final int LIGHT_LEVEL = 4;


    public SealedDoorBlock() {
        super(Properties.of().mapColor(MapColor.METAL)
                .noOcclusion()
                .dynamicShape()
                .strength(-1.0F, 3600000.0F)
                .noLootTable()
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL)
                .lightLevel(state -> state.getValue(LIT) ? LIGHT_LEVEL : 0));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, Boolean.valueOf(false))
                .setValue(OPEN, Boolean.valueOf(false))
                .setValue(PART, SealedDoorPart.CENTER)
                .setValue(Y_OFFSET, 0));
    }


    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING,OPEN,LIT,PART,Y_OFFSET);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SealedDoorBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos basePos = getBasePos(state, pos);
        BlockState baseState = level.getBlockState(basePos);

        ItemStack item = player.getItemInHand(hand);

        if (item.is(ModItems.BURNT_SUGAR.get())) {
            if(!player.isCreative())
                item.shrink(1);

            return this.onHit(level, baseState, new BlockHitResult(
                    hit.getLocation().add(basePos.getX() - pos.getX(), basePos.getY() - pos.getY(), basePos.getZ() - pos.getZ()),
                    hit.getDirection(), basePos, hit.isInside()
            ), player, hand, true)
                    ? InteractionResult.sidedSuccess(level.isClientSide)
                    : InteractionResult.PASS;
        }

        return InteractionResult.PASS;
    }

    public boolean onHit(Level level,BlockState blockState, BlockHitResult hitResult, Player player, InteractionHand hand, boolean b) {
        BlockPos blockpos = hitResult.getBlockPos();
        if (b) {
            this.attemptToRing(player, level,hand,blockState, blockpos);
            return true;
        } else {
            return false;
        }
    }


    public boolean attemptToRing(Player player, Level level, InteractionHand hand, BlockState blockState, BlockPos pos) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (!level.isClientSide && blockentity instanceof SealedDoorBlockEntity && !blockState.getValue(LIT)) {
            ((SealedDoorBlockEntity)blockentity).onHit(level);
            level.setBlock(pos, blockState.setValue(LIT, Boolean.valueOf(true)), 3);
            //  p_152190_.playSound((Player)null, p_152191_, ModSounds.MALEDICTUS_SHORT_ROAR.get(), SoundSource.BLOCKS, 2.0F, 1.0F);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return true;
        } else {
            return false;
        }
    }



    private BlockPos getBasePos(BlockState state, BlockPos pos) {
        BlockPos toReturn = pos.below(state.getValue(Y_OFFSET));
        if (state.getValue(PART) == SealedDoorPart.SIDE_LEFT) {
            toReturn = toReturn.relative(state.getValue(FACING).getCounterClockWise());
        }
        else if (state.getValue(PART) == SealedDoorPart.SIDE_RIGHT) {
            toReturn = toReturn.relative(state.getValue(FACING).getClockWise());
        }
        if (state.getValue(PART) == SealedDoorPart.END_LEFT) {
            toReturn = toReturn.relative(state.getValue(FACING).getCounterClockWise(),2);
        }
        else if (state.getValue(PART) == SealedDoorPart.END_RIGHT) {
            toReturn = toReturn.relative(state.getValue(FACING).getClockWise(),2);
        }
        return toReturn;
    }


    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.DOOR_OF_SEAL.get(), SealedDoorBlockEntity::tick);
    }


    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return CLOSED_SHAPE;
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return Shapes.empty();
    }


    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getValue(OPEN)) return Shapes.empty();
        return CLOSED_SHAPE;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext collisionContext) {
        if (state.getValue(OPEN)) return Shapes.empty();
        return CLOSED_SHAPE;
    }

    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }


    public boolean isPathfindable(BlockState blockState, BlockGetter level, BlockPos pos, PathComputationType pathComputationType) {
        return false;
    }

    private boolean doesGongFitInDirection(BlockPos pos, Direction direction, Level level) {
        for (int i = 0; i <= 7; i++) {
            BlockPos abovePos = pos.above(i);
            BlockPos blockpos1 = abovePos.relative(direction.getClockWise());
            BlockPos blockpos2 = abovePos;
            BlockPos blockpos3 = abovePos.relative(direction.getCounterClockWise());
            BlockPos blockpos4 = abovePos.relative(direction.getClockWise(),2);
            BlockPos blockpos5 = abovePos.relative(direction.getCounterClockWise(),2);
            BlockPos[] toBreakPoses = {blockpos1, blockpos2, blockpos3,blockpos4,blockpos5};
            for (BlockPos toBreakPos : toBreakPoses) {
                BlockState blockstate = level.getBlockState(toBreakPos);
                if (!blockstate.canBeReplaced()) return false;
            }
        }
        return true;
    }

    @javax.annotation.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        Direction.Axis direction$axis = direction.getAxis();
        if (direction$axis == Direction.Axis.Y) {
            Direction dir = context.getHorizontalDirection();
            BlockState blockstate = this.defaultBlockState().setValue(FACING, dir);
            if (blockstate.canSurvive(context.getLevel(), blockpos) && doesGongFitInDirection(blockpos, dir, context.getLevel())) {
                return blockstate;
            }
        } else {
            Direction dir = direction.getOpposite();
            BlockState blockstate1 = this.defaultBlockState().setValue(FACING, dir);
            if (blockstate1.canSurvive(context.getLevel(), context.getClickedPos()) && doesGongFitInDirection(context.getClickedPos(), dir, context.getLevel())) {
                return blockstate1;
            }
        }

        return null;
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @javax.annotation.Nullable LivingEntity entity, ItemStack itemStack) {
        super.setPlacedBy(level, pos, state, entity, itemStack);
        if (!level.isClientSide) {
            for (int i = 0; i < 8; i++) {
                BlockPos abovePos = pos.above(i);

                BlockPos blockpos1 = abovePos.relative(state.getValue(FACING).getClockWise());
                BlockPos blockpos2 = abovePos;
                BlockPos blockpos3 = abovePos.relative(state.getValue(FACING).getCounterClockWise());
                BlockPos blockpos4 = abovePos.relative(state.getValue(FACING).getClockWise(),2);
                BlockPos blockpos5 = abovePos.relative(state.getValue(FACING).getCounterClockWise(),2);


                BlockState defaultGongPart = ModBlocks.DOOR_OF_SEAL.get().defaultBlockState();
                level.setBlock(blockpos1, defaultGongPart.setValue(FACING, state.getValue(FACING)).setValue(SealedDoorBlock.PART, SealedDoorPart.SIDE_LEFT).setValue(SealedDoorBlock.Y_OFFSET, i), 3);
                level.setBlock(blockpos3, defaultGongPart.setValue(FACING, state.getValue(FACING)).setValue(SealedDoorBlock.PART, SealedDoorPart.SIDE_RIGHT).setValue(SealedDoorBlock.Y_OFFSET, i), 3);
                level.setBlock(blockpos4, defaultGongPart.setValue(FACING, state.getValue(FACING)).setValue(SealedDoorBlock.PART, SealedDoorPart.END_LEFT).setValue(SealedDoorBlock.Y_OFFSET, i), 3);
                level.setBlock(blockpos5, defaultGongPart.setValue(FACING, state.getValue(FACING)).setValue(SealedDoorBlock.PART, SealedDoorPart.END_RIGHT).setValue(SealedDoorBlock.Y_OFFSET, i), 3);
                if (blockpos2 != pos) {
                    level.setBlock(blockpos2, defaultGongPart.setValue(FACING, state.getValue(FACING)).setValue(SealedDoorBlock.PART, SealedDoorPart.CENTER).setValue(SealedDoorBlock.Y_OFFSET, i), 3);
                }
                level.blockUpdated(abovePos, Blocks.AIR);
                state.updateNeighbourShapes(level, abovePos, 3);
            }
        }

    }

    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {

            BlockPos basePos = getBasePos(state, pos);
            BlockState baseState = level.getBlockState(basePos);

            if (baseState.is(ModBlocks.DOOR_OF_SEAL.get())) {
                for (int i = 0; i <= 7; i++) {
                    BlockPos abovePos = basePos.above(i);
                    BlockPos blockpos1 = abovePos.relative(baseState.getValue(FACING).getClockWise());
                    BlockPos blockpos2 = abovePos;
                    BlockPos blockpos3 = abovePos.relative(baseState.getValue(FACING).getCounterClockWise());
                    BlockPos blockpos4 = abovePos.relative(baseState.getValue(FACING).getClockWise(), 2);
                    BlockPos blockpos5 = abovePos.relative(baseState.getValue(FACING).getCounterClockWise(), 2);
                    BlockPos[] toBreakPoses = {blockpos1, blockpos2, blockpos3, blockpos4, blockpos5};

                    for (BlockPos toBreakPos : toBreakPoses) {
                        BlockState blockstate = level.getBlockState(toBreakPos);
                        if (blockstate.is(ModBlocks.DOOR_OF_SEAL.get())) {
                            level.setBlock(toBreakPos, Blocks.AIR.defaultBlockState(), 35);
                            level.levelEvent(player, 2001, toBreakPos, Block.getId(blockstate));
                        }
                    }
                }

                level.setBlock(basePos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, basePos, Block.getId(baseState));
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    public enum SealedDoorPart implements StringRepresentable {
        SIDE_LEFT("side_left"),
        SIDE_RIGHT("side_right"),
        END_LEFT("end_left"),
        END_RIGHT("end_right"),
        CENTER("center");

        private final String name;

        private SealedDoorPart(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}
