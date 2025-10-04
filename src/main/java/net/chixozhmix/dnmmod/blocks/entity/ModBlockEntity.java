package net.chixozhmix.dnmmod.blocks.entity;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DnMmod.MOD_ID);

    public static final RegistryObject<BlockEntityType<CokeOvenBlockEntity>> COKE_OVEN_BE =
            BLOCK_ENTITIES.register("coke_oven", () -> BlockEntityType.Builder.of(CokeOvenBlockEntity::new,
                    ModBlocks.COKE_OVEN.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
