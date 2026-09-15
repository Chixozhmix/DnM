package net.chixozhmix.dnmmod.effect.custom;

import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SealingGatesEffect extends MobEffect {
    public SealingGatesEffect() {
        super(MobEffectCategory.HARMFUL, 0x471B52);
    }

    @SubscribeEvent
    public static void teleportEvent(EntityTeleportEvent event) {
        if(event instanceof EntityTeleportEvent.TeleportCommand)
            return;

        if(event.getEntity() instanceof LivingEntity entity && entity.hasEffect(ModEffects.SEALING_GATES.get())) {
            event.setCanceled(true);
        }

    }
}
