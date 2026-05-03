package net.chixozhmix.dnmmod.Util.structures;

import com.mojang.serialization.Codec;
import net.chixozhmix.dnmmod.registers.ModPlacement;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.Optional;

public class SingleStructurePlacement extends StructurePlacement {
    public static final Codec<SingleStructurePlacement> CODEC =
            Codec.unit(SingleStructurePlacement::new);

    public SingleStructurePlacement() {
        super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1.0F, 0, Optional.empty());
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int chunkX, int chunkZ) {

        long seed = state.getLevelSeed();

        WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(seed));

        int range = 2000;

        int targetX = random.nextInt(range * 2) - range;
        int targetZ = random.nextInt(range * 2) - range;

        return chunkX == targetX && chunkZ == targetZ;
    }

    @Override
    public StructurePlacementType<?> type() {
        return ModPlacement.SINGLE.get();
    }
}
