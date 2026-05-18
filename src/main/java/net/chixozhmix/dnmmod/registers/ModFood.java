package net.chixozhmix.dnmmod.registers;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFood {
    public static final FoodProperties BURNED_SUGAR = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).fast()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200), 0.1F).build();
}
