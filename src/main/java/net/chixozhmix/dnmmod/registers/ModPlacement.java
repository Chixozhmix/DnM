package net.chixozhmix.dnmmod.registers;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.structures.SingleStructurePlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModPlacement {
    public static final DeferredRegister<StructurePlacementType<?>> PLACEMENTS =
            DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, DnMmod.MOD_ID);

    public static final RegistryObject<StructurePlacementType<SingleStructurePlacement>> SINGLE =
            PLACEMENTS.register("single", () -> () -> SingleStructurePlacement.CODEC);

    public void register(IEventBus eventBus) {
        PLACEMENTS.register(eventBus);
    }
}
