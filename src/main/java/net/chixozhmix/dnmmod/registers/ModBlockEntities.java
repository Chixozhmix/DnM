package net.chixozhmix.dnmmod.registers;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.blocks.entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DnMmod.MOD_ID);

    public static final RegistryObject<BlockEntityType<ScrollTableEntity>> SCROLL_TABLE_BE = BLOCK_ENTITY.register("scroll_table_be",
            () -> BlockEntityType.Builder.of(ScrollTableEntity::new, ModBlocks.SCROLL_TABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<SealedDoorBlockEntity>> DOOR_OF_SEAL = BLOCK_ENTITY.register("door_of_seal", () ->
            BlockEntityType.Builder.of(SealedDoorBlockEntity::new, ModBlocks.DOOR_OF_SEAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<AntibuilderBlockEntity>> ANTIBUILDER_BLOCK_ENTITY = BLOCK_ENTITY.register("antibuilder",
            () -> BlockEntityType.Builder.of(AntibuilderBlockEntity::new, ModBlocks.ANTIBUILDER_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlockOfSealEntity>> BLOCK_OF_SEAL_ENTITY = BLOCK_ENTITY.register("block_of_seal",
            () -> BlockEntityType.Builder.of(BlockOfSealEntity::new, ModBlocks.BLOCK_OF_SEAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<LeshyAltarEntity>> LESHY_ALTAR = BLOCK_ENTITY.register("leshy_altar",
            () -> BlockEntityType.Builder.of(LeshyAltarEntity::new, ModBlocks.LESHY_ALTAR.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY.register(eventBus);
    }


}
