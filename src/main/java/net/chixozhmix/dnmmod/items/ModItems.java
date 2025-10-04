package net.chixozhmix.dnmmod.items;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.custom.*;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DnMmod.MOD_ID);

    //Weapons
    public static final RegistryObject<Item> CUMMON_DAGGER = ITEMS.register("cummon_dagger", () ->
            new DaggerItem(Tiers.IRON,           // Материал (Tier)
                    0,                    // Модификатор урона (игнорируется)
                    -1.4f,                // Скорость атаки (очень быстрая)
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(250)));

    public static final RegistryObject<Item> CUMMON_SPEAR = ITEMS.register("cummon_spear", () ->
            new Spear(Tiers.IRON,
                    0,
                    -2.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(200)));

    public static final RegistryObject<Item> COMMON_HALBERD = ITEMS.register("common_halberd", () ->
            new Spear(Tiers.IRON,
                    0,
                    -2.0f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(210)));

    public static final RegistryObject<Item> CUMMON_GREATAXE = ITEMS.register("cummon_greataxe", () ->
            new GreatAxe(Tiers.IRON,
                    0,
                    -3.0f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(300)));

    public static final RegistryObject<Item> COMMON_BATTLEAXE = ITEMS.register("common_battleaxe", () ->
            new Battleaxe(Tiers.IRON,
                    0,
                    -2.5f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(260)));

    //Staffs
    public static final RegistryObject<Item> FIRE_STAFF = ITEMS.register("fire_staff", () ->
            new FireStaff(new Item.Properties()
                    .stacksTo(1)));

    //Projectiles
    public static final RegistryObject<Item> FIREBALT = ITEMS.register("firebalt", () ->
            new Firebalt(new Item.Properties()));

    //Items
    public static final RegistryObject<Item> RAW_STEEL = ITEMS.register("raw_steel", () ->
            new Item(new Item.Properties()
                    .stacksTo(64)));

    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () ->
            new Item(new Item.Properties()
                    .stacksTo(64)));

    public static final RegistryObject<Item> FLUX = ITEMS.register("flux", () ->
            new Item(new Item.Properties()
                    .stacksTo(64)));

    public static final RegistryObject<Item> COKE_COAL = ITEMS.register("coke_coal", () ->
            new FuelItem(new Item.Properties().stacksTo(64), 2000));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
