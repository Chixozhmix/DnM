package net.chixozhmix.dnmmod.world.structure;

import com.mojang.serialization.Codec;
import net.chixozhmix.dnmmod.registers.ModStructures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class LeshyAltarStructure extends BaseJigsawStructure{
    public static final Codec<LeshyAltarStructure> CODEC =
            createCodec(LeshyAltarStructure::new);

    public LeshyAltarStructure(StructureSettings settings, Holder<StructureTemplatePool> startPool, int maxDepth) {
        super(settings, startPool, maxDepth);
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.LESHY_ALTAR_STRUCTURE.get();
    }
}
