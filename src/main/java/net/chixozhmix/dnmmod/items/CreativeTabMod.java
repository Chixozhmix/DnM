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
                    })
                    .build());

    //Staffs
    public static final RegistryObject<CreativeModeTab> STAFFS_TAB = CREATIVE_MOD_TABS.register("staffs",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.FIRE_STAFF.get()))
                    .title(Component.translatable("creativetab.dnmmod.staffs"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.FIRE_STAFF.get());
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
