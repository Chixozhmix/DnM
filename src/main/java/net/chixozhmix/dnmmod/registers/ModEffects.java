package net.chixozhmix.dnmmod.registers;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.effect.custom.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, DnMmod.MOD_ID);

    public static final String ENDER_RESIST_UUID = "3f2c1a7e-9b4d-4a6f-8c12-7d9e5f1a2b3c";
    public static final String FIRE_RESIST_UUID = "a8d3f6c1-2e7b-4d91-9c5a-1f0b6d8e3a27";
    public static final String ICE_RESIST_UUID = "7b91e2d4-6c3a-4f8d-9a21-5e0c3b7d1f9a";
    public static final String BLOOD_RESIST_UUID = "c4e8a1d9-3f2b-4c7e-8d6a-9b1f0e5c2a3d";
    public static final String EVOCATION_RESIST_UUID = "1a6f9c3e-8b4d-4e2a-9d7c-5f0b3a8e6c1d";
    public static final String HOLY_RESIST_UUID = "e9b2c4a7-5d1f-4a8c-9e3b-6c7d1f0a2b5e";
    public static final String NATURE_RESIST_UUID = "6d3a8f1c-9e4b-4c7a-8b2d-1f5e9c0a3d6b";
    public static final String LIGHTNING_RESIST_UUID = "b7c1e5a9-2f8d-4b3c-9a6e-4d1f7c2b8e0a";
    public static final String ELDRITCH_RESIST_UUID = "e7d4e4a9-2d8f-4b3c-9a6e-4d1f7c5a8e1a";

    public static final RegistryObject<MobEffect> MAGE_ARMOR = EFFECTS.register("mage_armor", MageArmorEffect::new);
    public static final RegistryObject<MobEffect> ACID = EFFECTS.register("acid_effect",
            () -> new AcidEffect(MobEffectCategory.HARMFUL, 0x4CAF50, 1.0f));
    public static final RegistryObject<MobEffect> PHANTOM_EFFECT = EFFECTS.register("phantom_effect",
            () -> new PhantomEffect(MobEffectCategory.BENEFICIAL, 0x78938c));
    public static final RegistryObject<MobEffect> SHRINK_EFFECT = EFFECTS.register("shrink_effect", () -> new ShrinkEffect());
    public static final RegistryObject<MobEffect> AGATHYS_ARMOR = EFFECTS.register("agathys_armor", AgathysArmor::new);
    public static final RegistryObject<MobEffect> THICK_OF_FIGHT = EFFECTS.register("thick_of_fight", ThickOfFight::new);
    public static final RegistryObject<MobEffect> CORPSE_POISON = EFFECTS.register("corpse_poison", () -> new CorpsePoison(1));
    public static final RegistryObject<MobEffect> REAPER_EFFECT = EFFECTS.register("reaper_effect", () -> new ReaperEffect());
    public static final RegistryObject<MobEffect> ENDER_RESISTANCE = EFFECTS.register("ender_resistance", () ->
            new ResistanceEffect(0x490552).addAttributeModifier(AttributeRegistry.ENDER_MAGIC_RESIST.get(), ENDER_RESIST_UUID, 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> FIRE_RESISTANCE = EFFECTS.register("fire_resistance", () ->
            new ResistanceEffect(0xD18002).addAttributeModifier(AttributeRegistry.FIRE_MAGIC_RESIST.get(), FIRE_RESIST_UUID, 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> ICE_RESISTANCE = EFFECTS.register("ice_resistance", () ->
            new ResistanceEffect(0x6EE2FA).addAttributeModifier(AttributeRegistry.ICE_MAGIC_RESIST.get(), ICE_RESIST_UUID, 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> BLOOD_RESISTANCE = EFFECTS.register("blood_resistance", () ->
            new ResistanceEffect(0x780C0C).addAttributeModifier(AttributeRegistry.BLOOD_MAGIC_RESIST.get(), BLOOD_RESIST_UUID, 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> EVOCATION_RESISTANCE = EFFECTS.register("evocation_resistance", () ->
            new ResistanceEffect(0x61BD42).addAttributeModifier(AttributeRegistry.EVOCATION_MAGIC_RESIST.get(), EVOCATION_RESIST_UUID, 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> HOLY_RESISTANCE = EFFECTS.register("holy_resistance", () ->
            new ResistanceEffect(0xE0DC36).addAttributeModifier(AttributeRegistry.HOLY_MAGIC_RESIST.get(), HOLY_RESIST_UUID, 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> NATURE_RESISTANCE = EFFECTS.register("nature_resistance", () ->
            new ResistanceEffect(0x3B7A33).addAttributeModifier(AttributeRegistry.NATURE_MAGIC_RESIST.get(), NATURE_RESIST_UUID, 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> LIGHTNING_RESISTANCE = EFFECTS.register("lightning_resistance", () ->
            new ResistanceEffect(0x47C1D6).addAttributeModifier(AttributeRegistry.LIGHTNING_MAGIC_RESIST.get(), LIGHTNING_RESIST_UUID, 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> ELDRITCH_RESISTANCE = EFFECTS.register("eldritch_resistance", () ->
            new ResistanceEffect(0x052F36).addAttributeModifier(AttributeRegistry.ELDRITCH_MAGIC_RESIST.get(), ELDRITCH_RESIST_UUID, 0.2, AttributeModifier.Operation.MULTIPLY_BASE));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
