package net.chixozhmix.dnmmod.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

public class BaseJigsawStructure extends Structure {
    public final Holder<StructureTemplatePool> startPool;
    public final int maxDepth;

    public BaseJigsawStructure(StructureSettings settings,
                               Holder<StructureTemplatePool> startPool,
                               int maxDepth) {
        super(settings);
        this.startPool = startPool;
        this.maxDepth = maxDepth;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getBlockX(0);
        int z = chunkPos.getBlockZ(0);
        int y = context.chunkGenerator().getFirstOccupiedHeight(
                x, z,
                Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(),
                context.randomState()
        );

        BlockPos blockPos = new BlockPos(x, y, z);

        return JigsawPlacement.addPieces(
                context,
                this.startPool,
                Optional.empty(),
                this.maxDepth,
                blockPos,
                false,
                Optional.empty(),
                96
        );
    }

    @Override
    public StructureType<?> type() {
        return null;
    }

    public static <T extends BaseJigsawStructure> Codec<T> createCodec(
            StructureFactory<T> factory) {
        return RecordCodecBuilder.<T>mapCodec(instance ->
                instance.group(
                        Structure.settingsCodec(instance),
                        StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                        Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.maxDepth)
                ).apply(instance, factory::create)
        ).codec();
    }

    @FunctionalInterface
    public interface StructureFactory<T extends BaseJigsawStructure> {
        T create(StructureSettings settings, Holder<StructureTemplatePool> startPool, int maxDepth);
    }
}
