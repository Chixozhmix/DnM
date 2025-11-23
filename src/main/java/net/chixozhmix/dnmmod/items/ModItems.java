package net.chixozhmix.dnmmod.items;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.item.consumables.SimpleElixir;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.curios.SimpleAttributeCurio;
import io.redspace.ironsspellbooks.item.spell_books.SimpleAttributeSpellBook;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.effect.ModEffects;
import net.chixozhmix.dnmmod.entity.ModEntityType;
import net.chixozhmix.dnmmod.items.custom.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.UUID;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DnMmod.MOD_ID);

    //Weapons
    public static final RegistryObject<Item> IRON_DAGGER = ITEMS.register("iron_dagger", () ->
            new SwordItem(Tiers.IRON,
                    1,
                    -1.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(300)));
    public static final RegistryObject<Item> IRON_KLEVETS = ITEMS.register("iron_klevets", () ->
            new SwordItem(Tiers.IRON,
                    3,
                    -1.6f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(330)));
    public static final RegistryObject<Item> IRON_SICKLE = ITEMS.register("iron_sickle", () ->
            new SwordItem(Tiers.IRON,
                    2,
                    -1.5f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(320)));
    public static final RegistryObject<Item> IRON_MACE = ITEMS.register("iron_mace", () ->
            new SwordItem(Tiers.IRON,
                    2,
                    -2.0f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(360)));
    public static final RegistryObject<Item> IRON_SCIMITAR = ITEMS.register("iron_scimitar", () ->
            new SwordItem(Tiers.IRON,
                    3,
                    -1.8f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(340)));
    public static final RegistryObject<Item> IRON_BATTLEAXE = ITEMS.register("iron_battleaxe", () ->
            new AxeItem(Tiers.IRON,
                    3.5F,
                    -2.5f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(260)));
    public static final RegistryObject<Item> IRON_SPEAR = ITEMS.register("iron_spear", () ->
            new SwordItem(Tiers.IRON,
                    4,
                    -2.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(350)));
    public static final RegistryObject<Item> IRON_HALBERD = ITEMS.register("iron_halberd", () ->
            new SwordItem(Tiers.IRON,
                    4,
                    -2.0f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(310)));
    public static final RegistryObject<Item> IRON_GREATAXE = ITEMS.register("iron_greataxe", () ->
            new AxeItem(Tiers.IRON,
                    9,
                    -3.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(400)));
    public static final RegistryObject<Item> IRON_GREATSWORD = ITEMS.register("iron_greatsword", () ->
            new SwordItem(Tiers.IRON,
                    8,
                    -2.8f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(460)));
    public static final RegistryObject<Item> IRON_GLAIVE = ITEMS.register("iron_glaive", () ->
            new SwordItem(Tiers.IRON,
                    4,
                    -2.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(460)));
    public static final RegistryObject<Item> IRON_TRIDENT = ITEMS.register("iron_trident", () ->
            new SwordItem(Tiers.IRON,
                    4,
                    -2.6f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(430)));
    public static final RegistryObject<Item> IRON_KATANA = ITEMS.register("iron_katana", () ->
            new SwordItem(Tiers.IRON,
                    4,
                    -2.0f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(330)));
    public static final RegistryObject<Item> DIAMOND_DAGGER = ITEMS.register("diamond_dagger", () ->
            new SwordItem(Tiers.DIAMOND,
                    1,
                    -1.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(1200)));
    public static final RegistryObject<Item> DIAMOND_KLEVETS = ITEMS.register("diamond_klevets", () ->
            new SwordItem(Tiers.DIAMOND,
                    3,
                    -1.6f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(1130)));
    public static final RegistryObject<Item> DIAMOND_SICKLE = ITEMS.register("diamond_sickle", () ->
            new SwordItem(Tiers.DIAMOND,
                    2,
                    -1.5f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(1020)));
    public static final RegistryObject<Item> DIAMOND_MACE = ITEMS.register("diamond_mace", () ->
            new SwordItem(Tiers.DIAMOND,
                    2,
                    -2.0f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(1160)));
    public static final RegistryObject<Item> DIAMOND_SCIMITAR = ITEMS.register("diamond_scimitar", () ->
            new SwordItem(Tiers.DIAMOND,
                    3,
                    -1.8f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(1040)));
    public static final RegistryObject<Item> DIAMOND_BATTLEAXE = ITEMS.register("diamond_battleaxe", () ->
            new AxeItem(Tiers.DIAMOND,
                    3.5F,
                    -2.5f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(560)));
    public static final RegistryObject<Item> DIAMOND_SPEAR = ITEMS.register("diamond_spear", () ->
            new SwordItem(Tiers.DIAMOND,
                    4,
                    -2.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(1250)));
    public static final RegistryObject<Item> DIAMOND_HALBERD = ITEMS.register("diamond_halberd", () ->
            new SwordItem(Tiers.DIAMOND,
                    4,
                    -2.0f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(1110)));
    public static final RegistryObject<Item> DIAMOND_GREATAXE = ITEMS.register("diamond_greataxe", () ->
            new AxeItem(Tiers.DIAMOND,
                    9,
                    -3.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(1500)));
    public static final RegistryObject<Item> DIAMOND_GREATSWORD = ITEMS.register("diamond_greatsword", () ->
            new SwordItem(Tiers.DIAMOND,
                    8,
                    -2.8f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(1560)));
    public static final RegistryObject<Item> DIAMOND_GLAIVE = ITEMS.register("diamond_glaive", () ->
            new SwordItem(Tiers.DIAMOND,
                    4,
                    -2.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(1300)));
    public static final RegistryObject<Item> DIAMOND_TRIDENT = ITEMS.register("diamond_trident", () ->
            new SwordItem(Tiers.DIAMOND,
                    4,
                    -2.6f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(1230)));
    public static final RegistryObject<Item> DIAMOND_KATANA = ITEMS.register("diamond_katana", () ->
            new SwordItem(Tiers.DIAMOND,
                    4,
                    -2.0f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(1030)));
    public static final RegistryObject<Item> RITUAL_DAGGER = ITEMS.register("ritual_dagger", () ->
            new SwordItem(Tiers.DIAMOND,
                    1,
                    -1.4f,
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(400)));
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
    public static final RegistryObject<Item> ECTOPLASM = ITEMS.register("ectoplasm", () ->
            new Item(new Item.Properties()
                    .stacksTo(64)));
    public static final RegistryObject<Item> RAVEN_FEATHER = ITEMS.register("raven_feather", () ->
            new Item(new Item.Properties()
                    .stacksTo(64)));
    public static final RegistryObject<Item> MIRROR = ITEMS.register("mirror", () ->
            new MirrorItem(new Item.Properties()));
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
    public static final RegistryObject<Item> FOREST_HEART = ITEMS.register("forest_heart", () ->
            new ForestHeart(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.RARE)));
    public static final RegistryObject<Item> THUNDERSTORM_BOTTLE = ITEMS.register("thunderstorm_bottle", () ->
            new Item(new Item.Properties()
                    .stacksTo(64)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BURNT_SUGAR = ITEMS.register("burnt_sugar", () ->
            new Item(new Item.Properties()
                    .stacksTo(64)));
    //Potions
    public static final RegistryObject<Item> PHANTOM_POTION = ITEMS.register("phantom_potion",
            () -> new SimpleElixir(ItemPropertiesHelper.material(), () ->
                    new MobEffectInstance(ModEffects.PHANTOM_EFFECT.get(), 900, 0)));
    //Armor
    public static final RegistryObject<Item> MAID_DRESS = ITEMS.register("maid_dress",
            () -> new MaidDress(ModArmorMaterials.MAIDDRESS, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MAID_CAP = ITEMS.register("maid_cap",
            () -> new MaidDress(ModArmorMaterials.MAIDDRESS, ArmorItem.Type.HELMET, new Item.Properties()));
    //Spellbooks
    public static final RegistryObject<Item> MAGICAL_GRIMOIRE = ITEMS.register("magical_grimoire",
            () -> new SimpleAttributeSpellBook(8, SpellRarity.RARE, AttributeRegistry.COOLDOWN_REDUCTION.get(), 0.05, (double)50.0F));
    //Curios
    public static final RegistryObject<CurioBaseItem> PROTECTION_RING = ITEMS.register("protection_ring",
            () -> new SimpleAttributeCurio(new Item.Properties().stacksTo(1),
                    Attributes.ARMOR, new AttributeModifier("ar", 2,AttributeModifier.Operation.ADDITION)));
    public static final RegistryObject<Item> COMPONENT_BAG = ITEMS.register("component_bag",
            () -> new ComponentBag(new Item.Properties()));
    public static final RegistryObject<Item> MEDIUM_COMPONENT_BAG = ITEMS.register("medium_component_bag",
            () -> new MediumComponentBag(new Item.Properties()));
    //Eggs
    public static final RegistryObject<ForgeSpawnEggItem> RAVEN_SPAWN_EGG = ITEMS.register("raven_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.RAVEN, 0x111111, 0x262626, new Item.Properties().stacksTo(64)));
    public static final RegistryObject<ForgeSpawnEggItem> UNDEAD_SPIRIT_SPAWN_EGG = ITEMS.register("undead_spirit_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.UNDEAD_SPIRIT, 0x111111, 0x989898, new Item.Properties().stacksTo(64)));
    public static final RegistryObject<ForgeSpawnEggItem> GHOST_SPAWN_EGG = ITEMS.register("ghost_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.GHOST, 0x585858, 0x787878, new Item.Properties().stacksTo(64)));
    public static final RegistryObject<ForgeSpawnEggItem> GOBLIN_SHAMAN_SPAWN_EGG = ITEMS.register("goblin_shaman_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.GOBLIN_SHAMAN, 0x224f23, 0x2f6e30, new Item.Properties().stacksTo(64)));
    public static final RegistryObject<ForgeSpawnEggItem> GOBLIN_WARRIOR_SPAWN_EGG = ITEMS.register("goblin_warrior_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.GOBLIN_WARRIOR, 0x224f23, 0x4e7a4f, new Item.Properties().stacksTo(64)));
    public static final RegistryObject<ForgeSpawnEggItem> GREEMON_SPAWN_EGG = ITEMS.register("greemon_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.GREEMON, 0x955252, 0x74786c, new Item.Properties().stacksTo(64)));
    public static final RegistryObject<ForgeSpawnEggItem> GREEN_HAG_SPAWN_EGG = ITEMS.register("green_hag_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.GREEN_HAG, 0x1c4316, 0x25631c, new Item.Properties().stacksTo(64)));
    public static final RegistryObject<ForgeSpawnEggItem> LESHY_SPAWN_EGG = ITEMS.register("leshy_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.LESHY, 0x295423, 0x37240d, new Item.Properties().stacksTo(64)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
