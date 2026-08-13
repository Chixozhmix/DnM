package net.chixozhmix.dnmmod.events;

import net.chixozhmix.dnmmod.Util.EventUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEvent {
    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        if (!(target instanceof Player)) return;
        Entity source = event.getSource().getEntity();

        EventUtils.ManaShield(event, target);
        EventUtils.ProtectiveBracelet(event, target, source);
        EventUtils.DeathWisp(event, target, source);
    }
}
