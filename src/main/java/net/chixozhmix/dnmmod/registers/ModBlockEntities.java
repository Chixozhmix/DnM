package net.chixozhmix.dnmmod.registers;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.blocks.entity.ScrollTableEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DnMmod.MOD_ID);

    public static final RegistryObject<BlockEntityType<ScrollTableEntity>> SCROLL_TABLE_BE = BLOCK_ENTITY.register("scroll_table_be",
            () -> BlockEntityType.Builder.of(ScrollTableEntity::new, ModBlocks.SCROLL_TABLE.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY.register(eventBus);
    }


}
