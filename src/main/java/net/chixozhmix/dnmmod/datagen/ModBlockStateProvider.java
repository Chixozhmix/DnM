package net.chixozhmix.dnmmod.datagen;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.blocks.custom.ScrollTableBlock;
import net.chixozhmix.dnmmod.registers.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, DnMmod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.CLAY_SHALE);

        simpleBlockWithItem(ModBlocks.LESHY_ALTAR.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/leshy_altar")));

        scrollTableBlock(ModBlocks.SCROLL_TABLE.get());
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void scrollTableBlock(Block block) {
        ModelFile model = new ModelFile.UncheckedModelFile(modLoc("block/scroll_table"));

        getVariantBuilder(block).forAllStates(state -> {
            var facing = state.getValue(ScrollTableBlock.FACING);
            int yRotation = switch (facing) {
                case NORTH -> 180;
                case SOUTH -> 0;
                case EAST -> 270;
                case WEST -> 90;
                default -> 0;
            };

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(yRotation)
                    .build();
        });

        // Простой блок с item моделью
        simpleBlockItem(block, model);
    }
}
