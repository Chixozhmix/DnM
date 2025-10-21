package net.chixozhmix.dnmmod.items;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.GreatAxeItem;
import net.chixozhmix.dnmmod.Util.SpearItem;
import net.chixozhmix.dnmmod.items.custom.*;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DnMmod.MOD_ID);

    //Weapons
    public static final RegistryObject<Item> CUMMON_DAGGER = ITEMS.register("cummon_dagger", () ->
            new DaggerItem(Tiers.IRON,
                    1,
                    -1.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(600)));

    public static final RegistryObject<Item> COMMON_KLEVETS = ITEMS.register("common_klevets", () ->
            new DaggerItem(Tiers.IRON,
                    3,
                    -1.6f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(630)));

    public static final RegistryObject<Item> COMMON_SICKLE = ITEMS.register("common_sickle", () ->
            new DaggerItem(Tiers.IRON,
                    2,
                    -1.5f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(620)));

    public static final RegistryObject<Item> COMMON_MACE = ITEMS.register("common_mace", () ->
            new DaggerItem(Tiers.IRON,
                    2,
                    -2.0f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(660)));

    public static final RegistryObject<Item> COMMON_SCIMITAR = ITEMS.register("common_scimitar", () ->
            new SwordItem(Tiers.IRON,
                    3,
                    -1.8f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(640)));

    public static final RegistryObject<Item> COMMON_BATTLEAXE = ITEMS.register("common_battleaxe", () ->
            new AxeItem(Tiers.IRON,
                    3.5F,
                    -2.5f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(560)));

    public static final RegistryObject<Item> CUMMON_SPEAR = ITEMS.register("cummon_spear", () ->
            new SpearItem(Tiers.IRON,
                    4,
                    -2.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(650)));

    public static final RegistryObject<Item> COMMON_HALBERD = ITEMS.register("common_halberd", () ->
            new SpearItem(Tiers.IRON,
                    4,
                    -2.0f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(610)));

    public static final RegistryObject<Item> CUMMON_GREATAXE = ITEMS.register("cummon_greataxe", () ->
            new GreatAxeItem(Tiers.DIAMOND,
                    8,
                    -3.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(700)));

    public static final RegistryObject<Item> COMMON_GREATSWORD = ITEMS.register("common_greatsword", () ->
            new GreatAxeItem(Tiers.DIAMOND,
                    7,
                    -2.8f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(760)));

    public static final RegistryObject<Item> COMMON_GLAIVE = ITEMS.register("common_glaive", () ->
            new GreatAxeItem(Tiers.IRON,
                    4,
                    -2.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(760)));

    //Staffs
    public static final RegistryObject<Item> FIRE_STAFF = ITEMS.register("fire_staff", () ->
            new FireStaff(Tiers.IRON,
                    2,
                    -2.0F,
                    new Item.Properties()
                            .durability(-1)
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

    public static final RegistryObject<Item> STEEL_NUGGET = ITEMS.register("steel_nugget", () ->
            new Item(new Item.Properties()
                    .stacksTo(64)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
