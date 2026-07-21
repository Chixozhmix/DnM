package net.chixozhmix.dnmmod.registers;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING =
            DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, DnMmod.MOD_ID);

    public static final RegistryObject<PaintingVariant> GOLDEN_CITY = PAINTING.register("golden_city", () ->
            new PaintingVariant(32, 64));
    public static final RegistryObject<PaintingVariant> URTIMIEL = PAINTING.register("urtimiel", () ->
            new PaintingVariant(32, 64));

    public static void register(IEventBus eventBus) {
        PAINTING.register(eventBus);
    }
}
