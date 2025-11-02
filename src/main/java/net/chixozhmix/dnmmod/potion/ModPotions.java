package net.chixozhmix.dnmmod.potion;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, DnMmod.MOD_ID);

//    public static final RegistryObject<Potion> PHANTOM_POTION = POTIONS.register("phantom_potion",
//            () -> new Potion(new MobEffectInstance(ModEffects.PHANTOM_EFFECT.get(), 1200, 0)));


    public static void reggister(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
