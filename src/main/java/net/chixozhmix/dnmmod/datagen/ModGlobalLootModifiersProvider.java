package net.chixozhmix.dnmmod.datagen;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;



public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, DnMmod.MOD_ID);
    }

    @Override
    protected void start() {

    }
}
