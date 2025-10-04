package net.chixozhmix.dnmmod.handlers;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.GreatAxeItem;
import net.chixozhmix.dnmmod.Util.SpearItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DnMmod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TwoHandWeaponEvent {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Player player = event.player;
            ItemStack mainHand = player.getMainHandItem();

            // Если игрок держит оружие в основной руке и что-то во второй руке
            if (mainHand.getItem() instanceof SpearItem || mainHand.getItem() instanceof GreatAxeItem && !player.getOffhandItem().isEmpty()) {
                // Выбрасываем предмет из второй руки
                player.drop(player.getOffhandItem(), false);
                player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            }
        }
    }
}
