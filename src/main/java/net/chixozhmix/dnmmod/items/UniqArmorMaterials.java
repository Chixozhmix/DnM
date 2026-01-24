package net.chixozhmix.dnmmod.items;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.IronsExtendedArmorMaterial;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public enum UniqArmorMaterials implements IronsExtendedArmorMaterial {
    ARCHER("archer", 10, lightArmorMap(), 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemLike[]{(ItemLike) Items.LEATHER}),
            Map.of((Attribute) Attributes.MOVEMENT_SPEED,
                    new AttributeModifier("Speed", (double)0.05F, AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute) ALObjects.Attributes.ARROW_DAMAGE.get(),
                    new AttributeModifier("Arrow Power", 0.05F, AttributeModifier.Operation.MULTIPLY_BASE)
                    )),
    IMPROVED_ARCHER("improved_archer", 15, mediumArmorMap(), 15,
            SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemLike[]{(ItemLike) Items.IRON_INGOT}),
            Map.of((Attribute) Attributes.MOVEMENT_SPEED,
                    new AttributeModifier("Speed", (double)0.05F, AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute) ALObjects.Attributes.ARROW_DAMAGE.get(),
                    new AttributeModifier("Arrow Power", 0.1F, AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute) ALObjects.Attributes.CRIT_CHANCE.get(),
                    new AttributeModifier("Crit chance", 0.05F, AttributeModifier.Operation.MULTIPLY_BASE)
            )),
    SHADOW_ARCHER("shadow_archer", 37, heavyArmorMap(), 15,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemLike[]{(ItemLike) Items.NETHERITE_INGOT}),
            Map.of((Attribute) Attributes.MOVEMENT_SPEED,
                    new AttributeModifier("Speed", (double)0.1F, AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute) ALObjects.Attributes.ARROW_DAMAGE.get(),
                    new AttributeModifier("Arrow Power", 0.15F, AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute) ALObjects.Attributes.CRIT_CHANCE.get(),
                    new AttributeModifier("Crit chance", 0.05F, AttributeModifier.Operation.MULTIPLY_BASE)
            )),
    BLADESINGER_ARMOR("bladesinger_armor", 37, heavyArmorMap(), 15,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F,
            () -> Ingredient.of(new ItemLike[]{(ItemLike) Items.NETHERITE_INGOT}),
            Map.of((Attribute) Attributes.ATTACK_DAMAGE,
                    new AttributeModifier("Attack Damage", (double)0.1F, AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute) AttributeRegistry.SPELL_POWER.get(),
                    new AttributeModifier("Spell Power", 0.05F, AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute) AttributeRegistry.MAX_MANA.get(),
                    new AttributeModifier("Max Mana", 100.0F, AttributeModifier.Operation.ADDITION)
            )),
    MITHRILL_ARMOR("mithrill_armor", 39, heavyArmorMap(), 15,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.0F,
            () -> Ingredient.of(new ItemLike[]{(ItemLike) ItemRegistry.MITHRIL_INGOT.get()}),
            Map.of((Attribute) AttributeRegistry.SPELL_RESIST.get(),
                    new AttributeModifier("Spell resist", (double)0.1F, AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute) Attributes.MOVEMENT_SPEED,
                    new AttributeModifier("Speed", 0.05F, AttributeModifier.Operation.MULTIPLY_BASE),
                    (Attribute) ALObjects.Attributes.CRIT_CHANCE.get(),
                    new AttributeModifier("Crit chance", 0.05F, AttributeModifier.Operation.MULTIPLY_BASE)
            )),
    HALFPLATE_ARMOR("halfplate_armor", 35, makeArmorMap(3,7,5,2), 15,
            SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemLike[]{(ItemLike) Items.IRON_INGOT}),
            Map.of((Attribute) ALObjects.Attributes.DODGE_CHANCE.get(),
                    new AttributeModifier("Dodge", (double)0.02F, AttributeModifier.Operation.MULTIPLY_BASE)
            )),
    PLATE_ARMOR("plate_armor", 37, heavyArmorMap(), 15,
            SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 0.1F,
            () -> Ingredient.of(new ItemLike[]{(ItemLike) Items.IRON_INGOT}),
            Map.of((Attribute) ALObjects.Attributes.DODGE_CHANCE.get(),
                    new AttributeModifier("Dodge", (double)0.04F, AttributeModifier.Operation.MULTIPLY_BASE)
            ));

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;
    private final Map<Attribute, AttributeModifier> additionalAttributes;
    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = (EnumMap) Util.make(new EnumMap(ArmorItem.Type.class), (p_266653_) -> {
        p_266653_.put(ArmorItem.Type.BOOTS, 13);
        p_266653_.put(ArmorItem.Type.LEGGINGS, 15);
        p_266653_.put(ArmorItem.Type.CHESTPLATE, 16);
        p_266653_.put(ArmorItem.Type.HELMET, 11);
    });

    private UniqArmorMaterials(String pName, int pDurabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionMap, int pEnchantmentValue, SoundEvent pSound, float pToughness, float pKnockbackResistance, Supplier<Ingredient> pRepairIngredient, Map<Attribute, AttributeModifier> additionalAttributes) {
        this.name = pName;
        this.durabilityMultiplier = pDurabilityMultiplier;
        this.protectionFunctionForType = protectionMap;
        this.enchantmentValue = pEnchantmentValue;
        this.sound = pSound;
        this.toughness = pToughness;
        this.knockbackResistance = pKnockbackResistance;
        this.repairIngredient = new LazyLoadedValue(pRepairIngredient);
        this.additionalAttributes = additionalAttributes;
    }

    public static EnumMap<ArmorItem.Type, Integer> makeArmorMap(int helmet, int chestplate, int leggings, int boots) {
        return (EnumMap)Util.make(new EnumMap(ArmorItem.Type.class), (p_266655_) -> {
            p_266655_.put(ArmorItem.Type.BOOTS, boots);
            p_266655_.put(ArmorItem.Type.LEGGINGS, leggings);
            p_266655_.put(ArmorItem.Type.CHESTPLATE, chestplate);
            p_266655_.put(ArmorItem.Type.HELMET, helmet);
        });
    }

    public static EnumMap<ArmorItem.Type, Integer> lightArmorMap() {
        return makeArmorMap(1, 4, 3, 1);
    }
    public static EnumMap<ArmorItem.Type, Integer> mediumArmorMap() {
        return makeArmorMap(2, 6, 5, 2);
    }
    public static EnumMap<ArmorItem.Type, Integer> heavyArmorMap() {
        return makeArmorMap(3, 8, 6, 3);
    }

    public int getDurabilityForSlot(EquipmentSlot pSlot) {
        return HEALTH_PER_SLOT[pSlot.getIndex()] * this.durabilityMultiplier;
    }

    public int getDurabilityForType(ArmorItem.Type p_266745_) {
        return (Integer)HEALTH_FUNCTION_FOR_TYPE.get(p_266745_) * this.durabilityMultiplier;
    }

    public int getDefenseForType(ArmorItem.Type p_266752_) {
        return (Integer)this.protectionFunctionForType.get(p_266752_);
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return (Ingredient)this.repairIngredient.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
        return this.additionalAttributes;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
