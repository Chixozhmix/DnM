package net.chixozhmix.dnmmod.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

public abstract class BaseJigsawStructure extends Structure {

    protected final Holder<StructureTemplatePool> startPool;
    protected final Optional<ResourceLocation> startJigsawName;
    protected final int maxDepth;
    protected final HeightProvider startHeight;
    protected final boolean useExpansionHack;
    protected final Optional<Heightmap.Types> projectStartToHeightmap;
    protected final int maxDistanceFromCenter;

    protected BaseJigsawStructure(
            StructureSettings settings,
            Holder<StructureTemplatePool> startPool,
            Optional<ResourceLocation> startJigsawName,
            int maxDepth,
            HeightProvider startHeight,
            boolean useExpansionHack,
            Optional<Heightmap.Types> projectStartToHeightmap,
            int maxDistanceFromCenter
    ) {
        super(settings);

        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.maxDepth = maxDepth;
        this.startHeight = startHeight;
        this.useExpansionHack = useExpansionHack;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        ChunkPos chunkPos = context.chunkPos();

        int y = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));

        BlockPos pos = new BlockPos(chunkPos.getMinBlockX(), y, chunkPos.getMinBlockZ());

        return JigsawPlacement.addPieces(
                context,
                this.startPool,
                this.startJigsawName,
                this.maxDepth,
                pos,
                this.useExpansionHack,
                this.projectStartToHeightmap,
                this.maxDistanceFromCenter
        );
    }

    protected static <T extends BaseJigsawStructure> Codec<T> createCodec(
            StructureFactory<T> factory) {

        return ExtraCodecs.validate(
                RecordCodecBuilder.<T>mapCodec(instance ->
                        instance.group(
                                Structure.settingsCodec(instance),

                                StructureTemplatePool.CODEC
                                        .fieldOf("start_pool")
                                        .forGetter(s -> s.startPool),

                                ResourceLocation.CODEC
                                        .optionalFieldOf("start_jigsaw_name")
                                        .forGetter(s -> s.startJigsawName),

                                Codec.intRange(0, 30)
                                        .fieldOf("size")
                                        .forGetter(s -> s.maxDepth),

                                HeightProvider.CODEC
                                        .fieldOf("start_height")
                                        .forGetter(s -> s.startHeight),

                                Codec.BOOL
                                        .fieldOf("use_expansion_hack")
                                        .forGetter(s -> s.useExpansionHack),

                                Heightmap.Types.CODEC
                                        .optionalFieldOf("project_start_to_heightmap")
                                        .forGetter(s -> s.projectStartToHeightmap),

                                Codec.intRange(1, 512)
                                        .fieldOf("max_distance_from_center")
                                        .forGetter(s -> s.maxDistanceFromCenter)

                        ).apply(instance, factory::create)
                ),
                BaseJigsawStructure::verifyRange
        ).codec();
    }

    private static <T extends BaseJigsawStructure> DataResult<T> verifyRange(T structure) {

        int offset = switch (structure.terrainAdaptation()) {
            case NONE -> 0;
            case BURY, BEARD_BOX, BEARD_THIN -> 12;
            default -> throw new IllegalStateException();
        };

        if (structure.maxDistanceFromCenter + offset > 512) {
            return DataResult.error(() ->
                    "Structure size including terrain adaptation must not exceed 128");
        }

        return DataResult.success(structure);
    }

    @FunctionalInterface
    public interface StructureFactory<T extends BaseJigsawStructure> {

        T create(
                StructureSettings settings,
                Holder<StructureTemplatePool> startPool,
                Optional<ResourceLocation> startJigsawName,
                int maxDepth,
                HeightProvider startHeight,
                boolean useExpansionHack,
                Optional<Heightmap.Types> projectStartToHeightmap,
                int maxDistanceFromCenter
        );
    }

    @Override
    public abstract StructureType<?> type();
}