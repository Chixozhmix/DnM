package net.chixozhmix.dnmmod.world.structure;

import com.mojang.serialization.Codec;
import net.chixozhmix.dnmmod.registers.ModStructures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class ShamanHouseStructure extends BaseJigsawStructure {
    public static final Codec<ShamanHouseStructure> CODEC =
            createCodec(ShamanHouseStructure::new);

    public ShamanHouseStructure(StructureSettings settings,
                                Holder<StructureTemplatePool> startPool,
                                int maxDepth) {
        super(settings, startPool, maxDepth);
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.SHAMAN_HOUSE_STRUCTURE.get();
    }

}
