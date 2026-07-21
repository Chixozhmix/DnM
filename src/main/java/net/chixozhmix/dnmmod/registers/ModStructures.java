package net.chixozhmix.dnmmod.registers;

import com.mojang.serialization.Codec;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.world.structure.BaldHillStructure;
import net.chixozhmix.dnmmod.world.structure.LeshyAltarStructure;
import net.chixozhmix.dnmmod.world.structure.ShamanHouseStructure;
import net.chixozhmix.dnmmod.world.structure.TaintedTempleStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE = DeferredRegister.create(Registries.STRUCTURE_TYPE, DnMmod.MOD_ID);

    public static final RegistryObject<StructureType<BaldHillStructure>> BALD_HILL_STRUCTURE = STRUCTURE_TYPE.register("bald_hill_structure",
            () -> explicitStructureTypeTyping(BaldHillStructure.CODEC));
    public static final RegistryObject<StructureType<ShamanHouseStructure>> SHAMAN_HOUSE_STRUCTURE = STRUCTURE_TYPE.register("shaman_house_structure",
            () -> explicitStructureTypeTyping(ShamanHouseStructure.CODEC));
    public static final RegistryObject<StructureType<LeshyAltarStructure>> LESHY_ALTAR_STRUCTURE = STRUCTURE_TYPE.register("lesy_altar_structure",
            () -> explicitStructureTypeTyping(LeshyAltarStructure.CODEC));

    public static final RegistryObject<StructureType<TaintedTempleStructure>> TAINTED_TEMPLE_STRUCTURE = STRUCTURE_TYPE.register("tainted_temple_structure",
            () -> explicitStructureTypeTyping(TaintedTempleStructure.CODEC));

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }

    public static void register(IEventBus eventBus) {
        STRUCTURE_TYPE.register(eventBus);
    }
}
