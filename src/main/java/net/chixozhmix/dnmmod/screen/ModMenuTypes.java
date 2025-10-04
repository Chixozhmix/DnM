package net.chixozhmix.dnmmod.screen;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
    DeferredRegister.create(ForgeRegistries.MENU_TYPES, DnMmod.MOD_ID);

    public static final RegistryObject<MenuType<CokeOvenMenu>> COKE_OVEN_MENU =
            registryMenuTypes("coke_oven_menu", CokeOvenMenu::new);


    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registryMenuTypes (String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }


    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
