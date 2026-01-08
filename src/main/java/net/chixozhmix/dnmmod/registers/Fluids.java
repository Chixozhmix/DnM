package net.chixozhmix.dnmmod.registers;

import io.redspace.ironsspellbooks.fluids.NoopFluid;
import io.redspace.ironsspellbooks.fluids.SimpleTintedClientFluidType;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Fluids {
    private static final DeferredRegister<Fluid> FLUIDS;
    private static final DeferredRegister<FluidType> FLUID_TYPES;

    public static final RegistryObject<Fluid> PHANTOM_POTION;
    public static final RegistryObject<FluidType> PHANTOM_POTION_TYPE;



    private static RegistryObject<Fluid> registerNoop(String name, Supplier<FluidType> fluidType) {
        RegistryObject<Fluid> holder = RegistryObject.create(DnMmod.id(name), ForgeRegistries.FLUIDS);
        ForgeFlowingFluid.Properties properties = (new ForgeFlowingFluid.Properties(fluidType, holder, holder)).bucket(() -> Items.AIR);
        FLUIDS.register(name, () -> new NoopFluid(properties));
        return holder;
    }

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }

    static {
        FLUIDS = DeferredRegister.create(Registries.FLUID, "dnmmod");
        FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, "dnmmod");

        PHANTOM_POTION_TYPE = FLUID_TYPES.register("phantom_potion", () -> new FluidType(FluidType.Properties.create()) {
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new SimpleTintedClientFluidType(ResourceLocation.fromNamespaceAndPath("forge", "block/milk_still"), 0xb0b0b0));
            }
        });

        PHANTOM_POTION = registerNoop("phantom_potion", PHANTOM_POTION_TYPE);
    }
}
