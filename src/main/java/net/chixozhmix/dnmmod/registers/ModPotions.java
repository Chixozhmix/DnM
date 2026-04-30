package net.chixozhmix.dnmmod.registers;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, DnMmod.MOD_ID);

    public static final RegistryObject<Potion> CORPSE_POISON = POTIONS.register("corpse_poison", () ->
            new Potion(new MobEffectInstance(ModEffects.CORPSE_POISON.get(), 200, 0)));
    public static final RegistryObject<Potion> ENDER_RESIST_POTION = POTIONS.register("ender_resist_potion", () ->
            new Potion(new MobEffectInstance(ModEffects.ENDER_RESISTANCE.get(), 1800, 0)));
    public static final RegistryObject<Potion> FIRE_RESIST_POTION = POTIONS.register("fire_resist_potion", () ->
            new Potion(new MobEffectInstance(ModEffects.FIRE_RESISTANCE.get(), 1800, 0)));
    public static final RegistryObject<Potion> ICE_RESIST_POTION = POTIONS.register("ice_resist_potion", () ->
            new Potion(new MobEffectInstance(ModEffects.ICE_RESISTANCE.get(), 1800, 0)));
    public static final RegistryObject<Potion> BLOOD_RESIST_POTION = POTIONS.register("blood_resist_potion", () ->
            new Potion(new MobEffectInstance(ModEffects.BLOOD_RESISTANCE.get(), 1800, 0)));
    public static final RegistryObject<Potion> EVOCATION_RESIST_POTION = POTIONS.register("evocation_resist_potion", () ->
            new Potion(new MobEffectInstance(ModEffects.EVOCATION_RESISTANCE.get(), 1800, 0)));
    public static final RegistryObject<Potion> HOLY_RESIST_POTION = POTIONS.register("holy_resist_potion", () ->
            new Potion(new MobEffectInstance(ModEffects.HOLY_RESISTANCE.get(), 1800, 0)));
    public static final RegistryObject<Potion> NATURE_RESIST_POTION = POTIONS.register("nature_resist_potion", () ->
            new Potion(new MobEffectInstance(ModEffects.NATURE_RESISTANCE.get(), 1800, 0)));
    public static final RegistryObject<Potion> LIGHTNING_RESIST_POTION = POTIONS.register("lightning_resist_potion", () ->
            new Potion(new MobEffectInstance(ModEffects.LIGHTNING_RESISTANCE.get(), 1800, 0)));
    public static final RegistryObject<Potion> ELDRITCH_RESIST_POTION = POTIONS.register("eldritch_resist_potion", () ->
            new Potion(new MobEffectInstance(ModEffects.ELDRITCH_RESISTANCE.get(), 1800, 0)));
    public static final RegistryObject<Potion> RESISTANCE = POTIONS.register("resistance_potion", () ->
            new Potion(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 0)));



    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
