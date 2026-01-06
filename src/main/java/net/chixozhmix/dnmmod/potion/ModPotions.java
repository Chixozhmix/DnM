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
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, DnMmod.MOD_ID);

    public static final RegistryObject<Potion> CORPSE_POISON = POTIONS.register("corpse_poison", () ->
            new Potion(new MobEffectInstance(ModEffects.CORPSE_POISON.get(), 200, 0)));



    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
