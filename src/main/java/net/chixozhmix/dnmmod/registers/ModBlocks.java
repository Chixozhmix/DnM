package net.chixozhmix.dnmmod.registers;

import io.redspace.ironslib.statue.block.statue_block.AbstractStatueBlock;
import io.redspace.ironslib.statue.block.statue_block.decorative.DecorativeStatueBlock;
import io.redspace.ironslib.statue.block.statue_block.decorative.DecorativeStatueBlockEntity;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.blocks.custom.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DnMmod.MOD_ID);

    //CommonBLocks
    public static final RegistryObject<Block> CLAY_SHALE = registerBlock("clay_shale", () ->
            new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).sound(SoundType.DEEPSLATE)));
    public static final RegistryObject<Block> LESHY_ALTAR = registerBlock("leshy_altar",
            () -> new LeshyAltarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()
                    .isRedstoneConductor((state, getter, pos) -> false)
                    .isSuffocating((state, getter, pos) -> false)
                    .isViewBlocking((state, getter, pos) -> false)));
    public static final RegistryObject<Block> BLOCK_OF_SEAL = registerBlock("block_of_seal", () ->
            new BlockOfSeal(BlockBehaviour.Properties.copy(Blocks.BEDROCK).sound(SoundType.DEEPSLATE).noLootTable()));
    public static final RegistryObject<AbstractStatueBlock> WARRIOR_STATUE_BLOCK =
            registerBlock("warrior_statue", () -> new AbstractStatueBlock(1, 2, 1) {
                @Override
                public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
                    return new DecorativeStatueBlockEntity(ModBlockEntities.WARRIOR_STATUE_BLOCK.get(), pPos, pState);
                }
            });


    public static final RegistryObject<Block> SCROLL_TABLE = registerBlock("scroll_table", () ->
            new ScrollTableBlock(BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.STONE).noOcclusion()
                    .isRedstoneConductor((state, getter, pos) -> false)
                    .isSuffocating((state, getter, pos) -> false)
                    .isViewBlocking((state, getter, pos) -> false)));

    public static final RegistryObject<Block> DOOR_OF_SEAL = registerBlock("door_of_seal",
            () -> new SealedDoorBlock());
    public static final RegistryObject<Block> ANTIBUILDER_BLOCK = registerBlock("antibuilder_block",
            () -> new AntibuilderBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 10).noLootTable()
                    .pushReaction(PushReaction.BLOCK)
                    .mapColor(MapColor.SAND)
                    .requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(-1.0F, 3600000.0F)));
    public static final RegistryObject<Block> UNBREAKING_BRICKS = registerBlock("unbreaking_bricks", () ->
            new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK).sound(SoundType.DEEPSLATE).noLootTable()));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);

        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
