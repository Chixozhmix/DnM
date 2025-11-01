package net.chixozhmix.dnmmod.items;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.GreatAxeItem;
import net.chixozhmix.dnmmod.Util.SpearItem;
import net.chixozhmix.dnmmod.items.custom.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.UUID;

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
    public static final RegistryObject<Item> COMMON_TRIDENT = ITEMS.register("common_trident", () ->
            new GreatAxeItem(Tiers.IRON,
                    4,
                    -2.6f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(730)));
    //Staffs and wands
    public static final RegistryObject<Item> WOODEN_WAND = ITEMS.register("wooden_wand",
            () -> new StaffItem(new Item.Properties().stacksTo(1),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.1,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> DRUID_WAND = ITEMS.register("druid_wand",
            () -> new StaffItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.05,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.NATURE_SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> ELECTROMANCER_WAND = ITEMS.register("electromancer_wand",
            () -> new StaffItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.05,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.LIGHTNING_SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> CRYOMANCER_WAND = ITEMS.register("cryomancer_wand",
            () -> new StaffItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.05,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.ICE_SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> PYROMANCER_WAND = ITEMS.register("pyromancer_wand",
            () -> new StaffItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.05,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.FIRE_SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> BLOOD_WAND = ITEMS.register("blood_wand",
            () -> new StaffItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.05,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.BLOOD_SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> ENDER_WAND = ITEMS.register("ender_wand",
            () -> new StaffItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.05,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.ENDER_SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> SACRED_SYMBOL = ITEMS.register("sacred_symbol",
            () -> new StaffItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.05,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.HOLY_SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> EVOKER_WAND = ITEMS.register("evoker_wand",
            () -> new StaffItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.05,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.EVOCATION_SPELL_POWER.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.SUMMON_DAMAGE.get(),
                    new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.10,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
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
    //Magic Item
    public static final RegistryObject<Item> HAG_EYE = ITEMS.register("hag_eye", () ->
            new HagEye(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.RARE)));
    public static final RegistryObject<Item> WAND_CORE = ITEMS.register("wand_core", () ->
            new WandCore(new Item.Properties()
                    .stacksTo(64)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRUID_WAND_CORE = ITEMS.register("druid_wand_core", () ->
            new WandCore(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BLOOD_WAND_CORE = ITEMS.register("blood_wand_core", () ->
            new WandCore(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> PYROMANCER_WAND_CORE = ITEMS.register("pyromancer_wand_core", () ->
            new WandCore(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CRYOMANCER_WAND_CORE = ITEMS.register("cryomancer_wand_core", () ->
            new WandCore(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ELECTROMANCER_WAND_CORE = ITEMS.register("electromancer_wand_core", () ->
            new WandCore(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> EVOKER_WAND_CORE = ITEMS.register("evoker_wand_core", () ->
            new WandCore(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ENDER_WAND_CORE = ITEMS.register("ender_wand_core", () ->
            new WandCore(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
