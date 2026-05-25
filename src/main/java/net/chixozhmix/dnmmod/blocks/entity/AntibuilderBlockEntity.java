package net.chixozhmix.dnmmod.blocks.entity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.chixozhmix.dnmmod.Util.ModTags;
import net.chixozhmix.dnmmod.registers.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class AntibuilderBlockEntity extends BlockEntity {
    private static final int REVERT_CHANCE = 10;

    private static final int RADIUS = 15;
    private static final int DIAMETER = 2 * RADIUS + 1;
    private static final double PLAYER_RANGE = 16.0;

    private final RandomSource rand = RandomSource.create();

    private int tickCount;
    private boolean slowScan;
    private int ticksSinceChange;

    private short[] compactBlockData;  // индексы в палитре
    private List<BlockState> palette;  // уникальные блоки
    private static final int BITS_PER_BLOCK = 12;

    private boolean dataLoaded = false;

    public AntibuilderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ANTIBUILDER_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AntibuilderBlockEntity te) {
        if (te.anyPlayerInRange()) {
            te.tickCount++;

            if (level.isClientSide()) {
                double x = pos.getX() + level.getRandom().nextFloat();
                double y = pos.getY() + level.getRandom().nextFloat();
                double z = pos.getZ() + level.getRandom().nextFloat();
                level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
            } else {
                if (!te.dataLoaded && level.isAreaLoaded(pos, AntibuilderBlockEntity.RADIUS)) {
                    te.captureBlockData();
                    te.slowScan = true;
                }

                if (te.dataLoaded && (!te.slowScan || te.tickCount % 20 == 0)) {
                    if (te.scanAndRevertChanges()) {
                        te.slowScan = false;
                        te.ticksSinceChange = 0;
                    } else {
                        te.ticksSinceChange++;

                        if (te.ticksSinceChange > 20) {
                            te.slowScan = true;
                        }
                    }
                }
            }
        } else {
            te.dataLoaded = false;
            te.compactBlockData = null;
            te.palette = null;
            te.tickCount = 0;
        }
    }

    private boolean scanAndRevertChanges() {
        int index = 0;
        boolean reverted = false;

        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    BlockState stateThere = this.getLevel().getBlockState(this.getBlockPos().offset(x, y, z));

                    BlockState originalState = this.getOriginalBlock(index);

                    if (originalState.getBlock() != stateThere.getBlock()) {
                        if (revertBlock(this.getBlockPos().offset(x, y, z), stateThere, originalState)) {
                            reverted = true;
                        } else {
                            this.updateStoredBlock(index, stateThere);
                        }
                    }

                    index++;
                }
            }
        }

        return reverted;
    }

    private boolean revertBlock(BlockPos pos, BlockState stateThere, BlockState replaceWith) {
        if (stateThere.isAir() && !replaceWith.blocksMotion()) {
            return false;
        }
        if (stateThere.getDestroySpeed(this.getLevel(), pos) < 0 || this.isUnrevertable(stateThere, replaceWith)) {
            return false;
        } else if (this.rand.nextInt(REVERT_CHANCE) == 0) {
            if (stateThere.isAir()) {
                this.getLevel().levelEvent(2001, pos, Block.getId(replaceWith));
            }
            Block.updateOrDestroy(stateThere, replaceWith, this.getLevel(), pos, 2);
        }

        return true;
    }

    private boolean isUnrevertable(BlockState stateThere, BlockState replaceWith) {
        return stateThere.is(ModTags.ANTIBUILDER_IGNORES) || replaceWith.is(ModTags.ANTIBUILDER_IGNORES);
    }

    private void captureBlockData() {
        Object2IntMap<Block> blockMap = new Object2IntOpenHashMap<>();
        List<Block> uniqueBlocks = new ArrayList<>();

        int totalBlocks = DIAMETER * DIAMETER * DIAMETER;
        short[] indices = new short[totalBlocks];

        int index = 0;

        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    BlockState state = this.getLevel().getBlockState(this.getBlockPos().offset(x, y, z));
                    Block block = state.getBlock();

                    int blockIndex;
                    if (blockMap.containsKey(block)) {
                        blockIndex = blockMap.getInt(block);
                    } else {
                        blockIndex = uniqueBlocks.size();
                        blockMap.put(block, blockIndex);
                        uniqueBlocks.add(block);
                    }

                    indices[index] = (short) blockIndex;
                    index++;
                }
            }
        }

        this.palette = new ArrayList<>();
        for (Block block : uniqueBlocks) {
            this.palette.add(block.defaultBlockState());
        }
        this.compactBlockData = indices;
        this.dataLoaded = true;
    }

    private BlockState getOriginalBlock(int index) {
        if (index >= 0 && index < this.compactBlockData.length) {
            int paletteIndex = this.compactBlockData[index];
            if (paletteIndex >= 0 && paletteIndex < this.palette.size()) {
                return this.palette.get(paletteIndex);
            }
        }
        return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(); // Запасной вариант
    }

    private void updateStoredBlock(int index, BlockState newState) {
        if (index >= 0 && index < this.compactBlockData.length) {
            Block block = newState.getBlock();

            int paletteIndex = -1;
            for (int i = 0; i < this.palette.size(); i++) {
                if (this.palette.get(i).getBlock() == block) {
                    paletteIndex = i;
                    break;
                }
            }

            if (paletteIndex == -1) {
                paletteIndex = this.palette.size();
                this.palette.add(block.defaultBlockState());
            }

            this.compactBlockData[index] = (short) paletteIndex;
        }
    }

    private boolean anyPlayerInRange() {
        return this.getLevel().hasNearbyAlivePlayer(
                this.getBlockPos().getX() + 0.5D,
                this.getBlockPos().getY() + 0.5D,
                this.getBlockPos().getZ() + 0.5D,
                AntibuilderBlockEntity.PLAYER_RANGE
        );
    }
}