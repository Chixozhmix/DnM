package net.chixozhmix.dnmmod.items;

//import com.gametechbc.gtbcs_geomancy_plus.api.init.GGAttributes;
import com.gametechbc.traveloptics.api.init.TravelopticsAttributes;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.consumables.SimpleElixir;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.spell_books.SimpleAttributeSpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.PropertiesHelper;
import net.chixozhmix.dnmmod.effect.ModEffects;
import net.chixozhmix.dnmmod.entity.ModEntityType;
import net.chixozhmix.dnmmod.items.custom.*;
import net.chixozhmix.dnmmod.spell.RegistrySpells;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DnMmod.MOD_ID);

    private static Optional<DeferredRegister<Item>> TRAVELOPTIC_ITEMS = Optional.empty();
    private static Optional<DeferredRegister<Item>> ALSHANEX_ITEMS = Optional.empty();
//    private static Optional<DeferredRegister<Item>> GEOMANCY_ITEMS = Optional.empty();
    private static Optional<DeferredRegister<Item>> LOCKS_ITEMS = Optional.empty();

    private static final UUID WAND_UUID = UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3");
    private static final String ATTRIBUTE_NAME = "Weapon modifier";

    private static RegistryObject<Item> registerSword(String name, Tier tier, int damage, float speed, int durability) {
        return ITEMS.register(name, () ->
                new SwordItem(tier, damage, speed,
                        PropertiesHelper.durabilityItemProperties(durability)));
    }
    private static RegistryObject<Item> registerAxe(String name, Tier tier, float damage, float speed, int durability) {
        return ITEMS.register(name, () ->
                new AxeItem(tier, damage, speed,
                        PropertiesHelper.durabilityItemProperties(durability)));
    }

    //Weapons
    public static final RegistryObject<Item> IRON_DAGGER = registerSword("iron_dagger", Tiers.IRON,
            1, -1.4f, 300);
    public static final RegistryObject<Item> IRON_KLEVETS = registerSword("iron_klevets", Tiers.IRON,
            3, -1.6f, 330);
    public static final RegistryObject<Item> IRON_SICKLE = registerSword("iron_sickle", Tiers.IRON,
            2, -1.5f, 320);
    public static final RegistryObject<Item> IRON_MACE = registerSword("iron_mace", Tiers.IRON,
            2, -2.0f, 360);
    public static final RegistryObject<Item> IRON_SCIMITAR = registerSword("iron_scimitar", Tiers.IRON,
            3, -1.8f, 340);
    public static final RegistryObject<Item> IRON_BATTLEAXE = registerAxe("iron_battleaxe", Tiers.IRON,
            3.5F, -2.5f, 260);
    public static final RegistryObject<Item> IRON_SPEAR = registerSword("iron_spear", Tiers.IRON,
            4, -2.4f, 350);
    public static final RegistryObject<Item> IRON_HALBERD = registerSword("iron_halberd", Tiers.IRON,
            4, -2.0f, 310);
    public static final RegistryObject<Item> IRON_GREATAXE = registerAxe("iron_greataxe", Tiers.IRON,
            9, -3.4f, 400);
    public static final RegistryObject<Item> IRON_GREATSWORD = registerSword("iron_greatsword", Tiers.IRON,
            8, -2.8f, 460);
    public static final RegistryObject<Item> IRON_GLAIVE = registerSword("iron_glaive", Tiers.IRON,
            4, -2.4f, 460);
    public static final RegistryObject<Item> IRON_TRIDENT = registerSword("iron_trident", Tiers.IRON,
            4, -2.6f, 430);
    public static final RegistryObject<Item> IRON_KATANA = registerSword("iron_katana", Tiers.IRON,
            4, -2.0f, 330);
    public static final RegistryObject<Item> DIAMOND_DAGGER = registerSword("diamond_dagger", Tiers.DIAMOND,
            1, -1.4f, 1200);
    public static final RegistryObject<Item> DIAMOND_KLEVETS = registerSword("diamond_klevets", Tiers.DIAMOND,
            3, -1.6f, 1130);
    public static final RegistryObject<Item> DIAMOND_SICKLE = registerSword("diamond_sickle", Tiers.DIAMOND,
            2, -1.5f, 1020);
    public static final RegistryObject<Item> DIAMOND_MACE = registerSword("diamond_mace", Tiers.DIAMOND,
            2, -2.0f, 1160);
    public static final RegistryObject<Item> DIAMOND_SCIMITAR = registerSword("diamond_scimitar", Tiers.DIAMOND,
            3, -1.8f, 1040);
    public static final RegistryObject<Item> DIAMOND_BATTLEAXE = registerAxe("diamond_battleaxe", Tiers.DIAMOND,
            3.5F, -2.5f, 660);
    public static final RegistryObject<Item> DIAMOND_SPEAR = registerSword("diamond_spear", Tiers.DIAMOND,
            4, -2.4f, 1250);
    public static final RegistryObject<Item> DIAMOND_HALBERD = registerSword("diamond_halberd", Tiers.DIAMOND,
            4, -2.0f, 1110);
    public static final RegistryObject<Item> DIAMOND_GREATAXE = registerAxe("diamond_greataxe", Tiers.DIAMOND,
            9, -3.4f, 1500);
    public static final RegistryObject<Item> DIAMOND_GREATSWORD = registerSword("diamond_greatsword", Tiers.DIAMOND,
            8, -2.8f, 1560);
    public static final RegistryObject<Item> DIAMOND_GLAIVE = registerSword("diamond_glaive", Tiers.DIAMOND,
            4, -2.4f, 1300);
    public static final RegistryObject<Item> DIAMOND_TRIDENT = registerSword("diamond_trident", Tiers.DIAMOND,
            4, -2.6f, 1230);
    public static final RegistryObject<Item> DIAMOND_KATANA = registerSword("diamond_katana", Tiers.DIAMOND,
            4, -2.0f, 1030);
    public static final RegistryObject<Item> RITUAL_DAGGER = registerSword("ritual_dagger", Tiers.DIAMOND,
            1, -1.4f, 400);
    public static final RegistryObject<Item> BLADESINGER_SWORD = ITEMS.register("bladesinger_sword", () ->
            new BladesingerSword(SpellDataRegistryHolder.of(new SpellDataRegistryHolder[]{
                    new SpellDataRegistryHolder(RegistrySpells.THICK_OF_FIGHT, 1)})));
    //Staffs and wands
    public static final RegistryObject<Item> WOODEN_WAND = ITEMS.register("wooden_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, (double)0.25F,
                            MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.SPELL_POWER.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.1,
                            MULTIPLY_BASE))));
    public static final RegistryObject<Item> DRUID_WAND = ITEMS.register("druid_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.NATURE_SPELL_POWER.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> ELECTROMANCER_WAND = ITEMS.register("electromancer_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.LIGHTNING_SPELL_POWER.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> CRYOMANCER_WAND = ITEMS.register("cryomancer_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.ICE_SPELL_POWER.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> PYROMANCER_WAND = ITEMS.register("pyromancer_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.FIRE_SPELL_POWER.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> BLOOD_WAND = ITEMS.register("blood_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.BLOOD_SPELL_POWER.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> ENDER_WAND = ITEMS.register("ender_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.ENDER_SPELL_POWER.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> SACRED_SYMBOL = ITEMS.register("sacred_symbol",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.HOLY_SPELL_POWER.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    public static final RegistryObject<Item> EVOKER_WAND = ITEMS.register("evoker_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).rarity(Rarity.UNCOMMON),
                    (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, (double)0.25F,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.EVOCATION_SPELL_POWER.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.15,
                            AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute)AttributeRegistry.SUMMON_DAMAGE.get(),
                    new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.10,
                            AttributeModifier.Operation.MULTIPLY_BASE))));
    //Items
    public static final RegistryObject<Item> ECTOPLASM = ITEMS.register("ectoplasm", () ->
            new Item(PropertiesHelper.stackItemProperties(64)));
    public static final RegistryObject<Item> RAVEN_FEATHER = ITEMS.register("raven_feather", () ->
            new Item(PropertiesHelper.stackItemProperties(64)));
    public static final RegistryObject<Item> MIRROR = ITEMS.register("mirror", () ->
            new MirrorItem(new Item.Properties()));
    public static final RegistryObject<Item> DICE20 = ITEMS.register("d20", () ->
            new D20Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> BLADE_RUNE = ITEMS.register("blade_rune", () ->
            new Item(PropertiesHelper.stackItemProperties(64)));
    //Magic Item
    public static final RegistryObject<Item> HAG_EYE = ITEMS.register("hag_eye", () ->
            new HagEye(PropertiesHelper.stackItemProperties(1)
                    .rarity(Rarity.RARE)));
    public static final RegistryObject<Item> WAND_CORE = ITEMS.register("wand_core", () ->
            new WandCore(PropertiesHelper.stackItemProperties(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRUID_WAND_CORE = ITEMS.register("druid_wand_core", () ->
            new WandCore(PropertiesHelper.stackItemProperties(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BLOOD_WAND_CORE = ITEMS.register("blood_wand_core", () ->
            new WandCore(PropertiesHelper.stackItemProperties(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> PYROMANCER_WAND_CORE = ITEMS.register("pyromancer_wand_core", () ->
            new WandCore(PropertiesHelper.stackItemProperties(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CRYOMANCER_WAND_CORE = ITEMS.register("cryomancer_wand_core", () ->
            new WandCore(PropertiesHelper.stackItemProperties(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ELECTROMANCER_WAND_CORE = ITEMS.register("electromancer_wand_core", () ->
            new WandCore(PropertiesHelper.stackItemProperties(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> EVOKER_WAND_CORE = ITEMS.register("evoker_wand_core", () ->
            new WandCore(PropertiesHelper.stackItemProperties(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ENDER_WAND_CORE = ITEMS.register("ender_wand_core", () ->
            new WandCore(PropertiesHelper.stackItemProperties(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> FOREST_HEART = ITEMS.register("forest_heart", () ->
            new ForestHeart(PropertiesHelper.stackItemProperties(1)
                    .rarity(Rarity.RARE)));
    public static final RegistryObject<Item> THUNDERSTORM_BOTTLE = ITEMS.register("thunderstorm_bottle", () ->
            new Item(PropertiesHelper.stackItemProperties(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BURNT_SUGAR = ITEMS.register("burnt_sugar", () ->
            new Item(PropertiesHelper.stackItemProperties(64)));
    //Potions
    public static final RegistryObject<Item> PHANTOM_POTION = ITEMS.register("phantom_potion",
            () -> new SimpleElixir(ItemPropertiesHelper.material(), () ->
                    new MobEffectInstance(ModEffects.PHANTOM_EFFECT.get(), 900, 0)));
    //Armor
    public static final RegistryObject<Item> MAID_DRESS = ITEMS.register("maid_dress",
            () -> new MaidDress(ModArmorMaterials.MAIDDRESS, ArmorItem.Type.CHESTPLATE, PropertiesHelper.itemProperties()));
    public static final RegistryObject<Item> MAID_CAP = ITEMS.register("maid_cap",
            () -> new MaidDress(ModArmorMaterials.MAIDDRESS, ArmorItem.Type.HELMET, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> ARCHER_ARMOR_HELMET = ITEMS.register("archer_armor_helmet",
            () -> new ArcherArmor(ArmorItem.Type.HELMET, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> ARCHER_ARMOR_CHESTPLATE = ITEMS.register("archer_armor_chestplate",
            () -> new ArcherArmor(ArmorItem.Type.CHESTPLATE, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> ARCHER_ARMOR_LEGGINGS = ITEMS.register("archer_armor_leggings",
            () -> new ArcherArmor(ArmorItem.Type.LEGGINGS, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> ARCHER_ARMOR_BOOTS = ITEMS.register("archer_armor_boots",
            () -> new ArcherArmor(ArmorItem.Type.BOOTS, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> IMPROVED_ARCHER_HELMET = ITEMS.register("improved_archer_helmet",
            () -> new ImprovedArcher(ArmorItem.Type.HELMET, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> IMPROVED_ARCHER_CHESTPLATE = ITEMS.register("improved_archer_chestplate",
            () -> new ImprovedArcher(ArmorItem.Type.CHESTPLATE, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> IMPROVED_ARCHER_LEGGINGS = ITEMS.register("improved_archer_leggings",
            () -> new ImprovedArcher(ArmorItem.Type.LEGGINGS, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> IMPROVED_ARCHER_BOOTS = ITEMS.register("improved_archer_boots",
            () -> new ImprovedArcher(ArmorItem.Type.BOOTS, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> SHADOW_ARCHER_HELMET = ITEMS.register("shadow_archer_helmet",
            () -> new ShadowArcher(ArmorItem.Type.HELMET, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> SHADOW_ARCHER_CHESTPLATE = ITEMS.register("shadow_archer_chestplate",
            () -> new ShadowArcher(ArmorItem.Type.CHESTPLATE, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> SHADOW_ARCHER_LEGGINGS = ITEMS.register("shadow_archer_leggings",
            () -> new ShadowArcher(ArmorItem.Type.LEGGINGS, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> SHADOW_ARCHER_BOOTS = ITEMS.register("shadow_archer_boots",
            () -> new ShadowArcher(ArmorItem.Type.BOOTS, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> BLADESINGER_HELMET = ITEMS.register("bladesinger_helmet",
            () -> new BladesingerArmor(ArmorItem.Type.HELMET, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> BLADESINGER_CHESTPLATE = ITEMS.register("bladesinger_chestplate",
            () -> new BladesingerArmor(ArmorItem.Type.CHESTPLATE, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> BLADESINGER_LEGGINGS = ITEMS.register("bladesinger_leggings",
            () -> new BladesingerArmor(ArmorItem.Type.LEGGINGS, PropertiesHelper.itemProperties()));

    public static final RegistryObject<Item> BLADESINGER_BOOTS = ITEMS.register("bladesinger_boots",
            () -> new BladesingerArmor(ArmorItem.Type.BOOTS, PropertiesHelper.itemProperties()));

    //Spellbooks
    public static final RegistryObject<Item> MAGICAL_GRIMOIRE = ITEMS.register("magical_grimoire",
            () -> new SimpleAttributeSpellBook(8, SpellRarity.RARE, AttributeRegistry.COOLDOWN_REDUCTION.get(), 0.05, (double)50.0F));
    //Curios
    public static final RegistryObject<CurioBaseItem> PROTECTION_RING = ITEMS.register("protection_ring",
            () -> (new CurioBaseItem(PropertiesHelper.stackItemProperties(1))
                    .withAttributes(Curios.RING_SLOT, new AttributeContainer[]{
                            new AttributeContainer(() -> Attributes.ARMOR, 2.0D, AttributeModifier.Operation.ADDITION)
                    })));
    public static final RegistryObject<Item> COMPONENT_BAG = ITEMS.register("component_bag",
            () -> new ComponentBag(PropertiesHelper.itemProperties()));
    public static final RegistryObject<Item> MEDIUM_COMPONENT_BAG = ITEMS.register("medium_component_bag",
            () -> new MediumComponentBag(PropertiesHelper.itemProperties()));
    //Eggs
    public static final RegistryObject<ForgeSpawnEggItem> RAVEN_SPAWN_EGG = ITEMS.register("raven_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.RAVEN, 0x111111, 0x262626, PropertiesHelper.stackItemProperties(64)));
    public static final RegistryObject<ForgeSpawnEggItem> UNDEAD_SPIRIT_SPAWN_EGG = ITEMS.register("undead_spirit_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.UNDEAD_SPIRIT, 0x111111, 0x989898, PropertiesHelper.stackItemProperties(64)));
    public static final RegistryObject<ForgeSpawnEggItem> GHOST_SPAWN_EGG = ITEMS.register("ghost_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.GHOST, 0x585858, 0x787878, PropertiesHelper.stackItemProperties(64)));
    public static final RegistryObject<ForgeSpawnEggItem> GOBLIN_SHAMAN_SPAWN_EGG = ITEMS.register("goblin_shaman_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.GOBLIN_SHAMAN, 0x224f23, 0x2f6e30, PropertiesHelper.stackItemProperties(64)));
    public static final RegistryObject<ForgeSpawnEggItem> GOBLIN_WARRIOR_SPAWN_EGG = ITEMS.register("goblin_warrior_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.GOBLIN_WARRIOR, 0x224f23, 0x4e7a4f, PropertiesHelper.stackItemProperties(64)));
    public static final RegistryObject<ForgeSpawnEggItem> GREEMON_SPAWN_EGG = ITEMS.register("greemon_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.GREEMON, 0x955252, 0x74786c, PropertiesHelper.stackItemProperties(64)));
    public static final RegistryObject<ForgeSpawnEggItem> GREEN_HAG_SPAWN_EGG = ITEMS.register("green_hag_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.GREEN_HAG, 0x1c4316, 0x25631c, PropertiesHelper.stackItemProperties(64)));
    public static final RegistryObject<ForgeSpawnEggItem> LESHY_SPAWN_EGG = ITEMS.register("leshy_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityType.LESHY, 0x295423, 0x37240d, PropertiesHelper.stackItemProperties(64)));

    // Опциональные предметы
    public static Optional<RegistryObject<Item>> AQUA_WAND = Optional.empty();
    public static Optional<RegistryObject<Item>> KAPELLMEISTER_WAND = Optional.empty();
    public static Optional<RegistryObject<Item>> AQUA_WAND_CORE = Optional.empty();
    public static Optional<RegistryObject<Item>> KAPELLMEISTER_WAND_CORE = Optional.empty();
//    public static Optional<RegistryObject<Item>> GEOMANCY_WAND = Optional.empty();
//    public static Optional<RegistryObject<Item>> GEOMANCY_WAND_CORE = Optional.empty();
    public static Optional<RegistryObject<Item>> KEY = Optional.empty();


    public static void registerOptionalItems(IEventBus eventBus) {
        // TravelOptics
        if (ModList.get().isLoaded("traveloptics")) {
            TRAVELOPTIC_ITEMS = Optional.of(DeferredRegister.create(ForgeRegistries.ITEMS, DnMmod.MOD_ID));

            AQUA_WAND = Optional.of(TRAVELOPTIC_ITEMS.get().register("aqua_wand",
                    () -> new StaffItem(PropertiesHelper.stackItemProperties(1).rarity(Rarity.UNCOMMON),
                            (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                            new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, (double)0.25F,
                                    AttributeModifier.Operation.MULTIPLY_BASE),
                            (Attribute) TravelopticsAttributes.AQUA_SPELL_POWER.get(),
                            new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.15,
                                    AttributeModifier.Operation.MULTIPLY_BASE)))));
            AQUA_WAND_CORE = Optional.of(TRAVELOPTIC_ITEMS.get().register("aqua_wand_core",
                    () -> new WandCore(PropertiesHelper.stackItemProperties(16)
                            .rarity(Rarity.UNCOMMON))));

            TRAVELOPTIC_ITEMS.get().register(eventBus);
        }

        // Alshanex's Items
        if (ModList.get().isLoaded("alshanex_familiars")) {
            ALSHANEX_ITEMS = Optional.of(DeferredRegister.create(ForgeRegistries.ITEMS, DnMmod.MOD_ID));

            KAPELLMEISTER_WAND = Optional.of(ALSHANEX_ITEMS.get().register("kapellmeister_wand",
                    () -> new StaffItem(PropertiesHelper.stackItemProperties(1).rarity(Rarity.UNCOMMON),
                            (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
                            new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, (double)0.25F,
                                    AttributeModifier.Operation.MULTIPLY_BASE),
                            (Attribute) net.alshanex.alshanex_familiars.registry.AttributeRegistry.SOUND_SPELL_POWER.get(),
                            new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.15,
                                    AttributeModifier.Operation.MULTIPLY_BASE)))));
            KAPELLMEISTER_WAND_CORE = Optional.of(ALSHANEX_ITEMS.get().register("kapellmeister_wand_core",
                    () -> new WandCore(PropertiesHelper.stackItemProperties(16)
                            .rarity(Rarity.UNCOMMON))));

            ALSHANEX_ITEMS.get().register(eventBus);
        }
        // Geomancy Items
//        if (ModList.get().isLoaded("gtbcs_geomancy_plus")) {
//            GEOMANCY_ITEMS = Optional.of(DeferredRegister.create(ForgeRegistries.ITEMS, DnMmod.MOD_ID));
//
//            GEOMANCY_WAND = Optional.of(GEOMANCY_ITEMS.get().register("geomancy_wand",
//                    () -> new StaffItem(PropertiesHelper.stackItemProperties(1).rarity(Rarity.UNCOMMON),
//                            (double)1.0F, (double)-2.0F, Map.of((Attribute) AttributeRegistry.MANA_REGEN.get(),
//                            new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, (double)0.25F,
//                                    AttributeModifier.Operation.MULTIPLY_BASE),
//                            (Attribute) GGAttributes.GEO_SPELL_POWER.get(),
//                            new AttributeModifier(WAND_UUID, ATTRIBUTE_NAME, 0.15,
//                                    AttributeModifier.Operation.MULTIPLY_BASE)))));
//            GEOMANCY_WAND_CORE = Optional.of(GEOMANCY_ITEMS.get().register("geomancy_wand_core",
//                    () -> new WandCore(PropertiesHelper.stackItemProperties(16)
//                            .rarity(Rarity.UNCOMMON))));
//
//            GEOMANCY_ITEMS.get().register(eventBus);
//        }

        // Locks Items
        if (ModList.get().isLoaded("locks")) {
            LOCKS_ITEMS = Optional.of(DeferredRegister.create(ForgeRegistries.ITEMS, DnMmod.MOD_ID));

            KEY = Optional.of(LOCKS_ITEMS.get().register("key",
                    () -> new Key(PropertiesHelper.durabilityItemProperties(50))));

            LOCKS_ITEMS.get().register(eventBus);
        }
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        registerOptionalItems(eventBus);
    }
}
