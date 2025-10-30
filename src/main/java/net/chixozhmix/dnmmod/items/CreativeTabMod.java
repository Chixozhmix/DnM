package net.chixozhmix.dnmmod.items;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabMod {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DnMmod.MOD_ID);

    //Weapons
    public static final RegistryObject<CreativeModeTab> WEAPONS_TAB = CREATIVE_MOD_TABS.register("weapons",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.CUMMON_DAGGER.get()))
                    .title(Component.translatable("creativetab.dnmmod.weapons"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.CUMMON_DAGGER.get());
                        output.accept(ModItems.CUMMON_SPEAR.get());
                        output.accept(ModItems.CUMMON_GREATAXE.get());
                        output.accept(ModItems.COMMON_HALBERD.get());
                        output.accept(ModItems.COMMON_BATTLEAXE.get());
                        output.accept(ModItems.COMMON_GREATSWORD.get());
                        output.accept(ModItems.COMMON_MACE.get());
                        output.accept(ModItems.COMMON_SCIMITAR.get());
                        output.accept(ModItems.COMMON_GLAIVE.get());
                        output.accept(ModItems.COMMON_SICKLE.get());
                        output.accept(ModItems.COMMON_KLEVETS.get());
                    })
                    .build());

    //Staffs
    public static final RegistryObject<CreativeModeTab> STAFFS_TAB = CREATIVE_MOD_TABS.register("staffs_and_wand",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WOODEN_WAND.get()))
                    .title(Component.translatable("creativetab.dnmmod.staffs"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.WOODEN_WAND.get());
                        output.accept(ModItems.ELECTROMANCER_WAND.get());
                        output.accept(ModItems.CRYOMANCER_WAND.get());
                        output.accept(ModItems.DRUID_WAND.get());
                        output.accept(ModItems.BLOOD_WAND.get());
                        output.accept(ModItems.EVOKER_WAND.get());
                        output.accept(ModItems.PYROMANCER_WAND.get());
                        output.accept(ModItems.SACRED_SYMBOL.get());
                    })
                    .build());

    //Items
    public static final RegistryObject<CreativeModeTab> ITEMS_TAB = CREATIVE_MOD_TABS.register("items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.STEEL_INGOT.get()))
                    .title(Component.translatable("creativetab.dnmmod.items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.STEEL_INGOT.get());
                        output.accept(ModItems.RAW_STEEL.get());
                        output.accept(ModItems.COKE_COAL.get());
                        output.accept(ModItems.FLUX.get());
                        output.accept(ModItems.STEEL_NUGGET.get());
                    })
                    .build());

    //Magic Items
    public static final RegistryObject<CreativeModeTab> MAGIC_ITEMS_TAB = CREATIVE_MOD_TABS.register("magic_items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.HAG_EYE.get()))
                    .title(Component.translatable("creativetab.dnmmod.magic_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.HAG_EYE.get());
                        output.accept(ModItems.WAND_CORE.get());
                        output.accept(ModItems.CRYOMANCER_WAND_CORE.get());
                        output.accept(ModItems.PYROMANCER_WAND_CORE.get());
                        output.accept(ModItems.ELECTROMANCER_WAND_CORE.get());
                        output.accept(ModItems.DRUID_WAND_CORE.get());
                        output.accept(ModItems.BLOOD_WAND_CORE.get());
                        output.accept(ModItems.EVOKER_WAND_CORE.get());
                    })
                    .build());

    //Blocks
    public static final RegistryObject<CreativeModeTab> BLOCKS_TAB = CREATIVE_MOD_TABS.register("blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CLAY_SHALE.get()))
                    .title(Component.translatable("creativetab.dnmmod.blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.CLAY_SHALE.get());
                        output.accept(ModBlocks.COKE_OVEN.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);

    }
}
