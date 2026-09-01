package net.chixozhmix.dnmmod.world.structure;

import com.mojang.serialization.Codec;
import net.chixozhmix.chilib.world.structures.BaseJigsawStructure;
import net.chixozhmix.dnmmod.registers.ModStructures;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

public class LeshyAltarStructure extends BaseJigsawStructure {
    public static final Codec<LeshyAltarStructure> CODEC =
            createCodec(LeshyAltarStructure::new);

    protected LeshyAltarStructure(StructureSettings settings, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int maxDepth, HeightProvider startHeight, boolean useExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter) {
        super(settings, startPool, startJigsawName, maxDepth, startHeight, useExpansionHack, projectStartToHeightmap, maxDistanceFromCenter);
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.LESHY_ALTAR_STRUCTURE.get();
    }
}
