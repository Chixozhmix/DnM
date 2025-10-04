package net.chixozhmix.dnmmod.entity;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.projectiles.custom.FIrebolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DnMmod.MOD_ID);

    public static final RegistryObject<EntityType<FIrebolt>> FIREBALT =
            ENTITY_TYPES.register("fire_balt",
                    () -> EntityType.Builder.<FIrebolt>of(FIrebolt::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("fire_balt"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
