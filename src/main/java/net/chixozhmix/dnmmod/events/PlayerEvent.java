package net.chixozhmix.dnmmod.events;

import net.chixozhmix.dnmmod.entity.reaper.ReaperEntity;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEvent {
    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();

        if(!(target instanceof Player)) return;
        if(attacker == null) return;
        if(attacker.getMobType() == MobType.UNDEAD && attacker.getClass() != ReaperEntity.class) {
            RandomSource random = target.getRandom();

            if(random.nextFloat() <= 0.10F) {
                target.addEffect(new MobEffectInstance(
                        ModEffects.REAPER_EFFECT.get(),
                        600,
                        0
                ));
            }
        }
    }
}
