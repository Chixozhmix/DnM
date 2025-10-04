package net.chixozhmix.dnmmod.handlers;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.DaggerItem;
import net.chixozhmix.dnmmod.Util.GreatAxeItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DnMmod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RangedHandler {
    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        ItemStack itemStack = player.getMainHandItem();

        double distance = 0.0f;

        // Проверяем, является ли оружие нашим кастомным оружием
        if (itemStack.getItem() instanceof DaggerItem) {
            // Получаем текущую дистанцию до цели
            distance = player.distanceTo(event.getTarget());

            // Если дистанция больше допустимой, отменяем атаку
            if (distance > 3.0D) { // Максимальная дистанция 3 блока
                event.setCanceled(true);
            }
        }

        // Проверяем, является ли оружие нашим кастомным оружием
        if (itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof AxeItem) {
            // Получаем текущую дистанцию до цели
            distance = player.distanceTo(event.getTarget());

            // Если дистанция больше допустимой, отменяем атаку
            if (distance > 4.0D) { // Максимальная дистанция 4 блока
                event.setCanceled(true);
            }
        }

        if (itemStack.getItem() instanceof GreatAxeItem) {
            // Получаем текущую дистанцию до цели
            distance = player.distanceTo(event.getTarget());

            // Если дистанция больше допустимой, отменяем атаку
            if (distance > 4.5D) { // Максимальная дистанция 4 блока
                event.setCanceled(true);
            }
        }
    }
}
