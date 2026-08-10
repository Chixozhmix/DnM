package net.chixozhmix.dnmmod.registers;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.screen.overlays.SpellEffectsOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DnMmod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OverlayRegister {
    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("spell_effects", SpellEffectsOverlay.instance);
    }

}
