package net.chixozhmix.dnmmod.registers;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.chixozhmix.chilib.registers.CLBrewingRecipeRegister;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class DnMBrewingRegistry {
    public static void registerRecipes() {
      CLBrewingRecipeRegister.register(Potions.POISON, ModItems.GREEMON_FANG.get(), ModPotions.CORPSE_POISON.get());
      CLBrewingRecipeRegister.register(ModPotions.RESISTANCE.get(), Items.ENDER_PEARL, ModPotions.ENDER_RESIST_POTION.get());
      CLBrewingRecipeRegister.register(ModPotions.RESISTANCE.get(), Items.BLAZE_ROD, ModPotions.FIRE_RESIST_POTION.get());
      CLBrewingRecipeRegister.register(ModPotions.RESISTANCE.get(), ItemRegistry.FROZEN_BONE_SHARD.get(), ModPotions.ICE_RESIST_POTION.get());
      CLBrewingRecipeRegister.register(ModPotions.RESISTANCE.get(), ItemRegistry.BLOOD_VIAL.get(), ModPotions.BLOOD_RESIST_POTION.get());
      CLBrewingRecipeRegister.register(ModPotions.RESISTANCE.get(), Items.EMERALD, ModPotions.EVOCATION_RESIST_POTION.get());
      CLBrewingRecipeRegister.register(ModPotions.RESISTANCE.get(), ItemRegistry.DIVINE_PEARL.get(), ModPotions.HOLY_RESIST_POTION.get());
      CLBrewingRecipeRegister.register(ModPotions.RESISTANCE.get(), Items.POISONOUS_POTATO, ModPotions.NATURE_RESIST_POTION.get());
      CLBrewingRecipeRegister.register(ModPotions.RESISTANCE.get(), ItemRegistry.LIGHTNING_BOTTLE.get(), ModPotions.LIGHTNING_RESIST_POTION.get());
      CLBrewingRecipeRegister.register(ModPotions.RESISTANCE.get(), Items.ECHO_SHARD, ModPotions.ELDRITCH_RESIST_POTION.get());
      CLBrewingRecipeRegister.register(Potions.AWKWARD, Items.GOLDEN_APPLE, ModPotions.RESISTANCE.get());
    }
}
