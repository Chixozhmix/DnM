package net.chixozhmix.dnmmod.events;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SpellEvents {
    @SubscribeEvent
    public static void antimagicEvent(SpellPreCastEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.hasEffect(ModEffects.ANTIMAGIC.get())) {
            event.setCanceled(true);
        }
    }
}
