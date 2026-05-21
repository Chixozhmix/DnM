package net.chixozhmix.dnmmod.effect.custom;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DnMmod.MOD_ID, value = Dist.CLIENT)
public class DisorientationEffect extends MobEffect {
    public DisorientationEffect() {
        super(MobEffectCategory.HARMFUL, 0x09242B);
    }

    @SubscribeEvent
    public static void onMovementInput(MovementInputUpdateEvent event) {
        Player player = event.getEntity();

        if(player != null && player.hasEffect(ModEffects.DISORIENTATION.get())) {
            event.getInput().leftImpulse *= -1;
            event.getInput().forwardImpulse *= -1;

        }
    }
}
