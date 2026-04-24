//package net.chixozhmix.dnmmod.events;
//
//import io.redspace.ironsspellbooks.registries.ItemRegistry;
//import net.chixozhmix.dnmmod.DnMmod;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
//import net.minecraftforge.event.TickEvent;
//import net.minecraftforge.event.entity.EntityJoinLevelEvent;
//import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
//import net.minecraftforge.event.entity.player.PlayerInteractEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = DnMmod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
//public class RemoveEvents {
//    //*REMOVE ITEMS*//
//    @SubscribeEvent
//    public static void onItemPickup(EntityItemPickupEvent event) {
//        if (event.getItem().getItem().getItem() == ItemRegistry.SCROLL_FORGE_BLOCK.get()) {
//            event.setCanceled(true);
//        }
//    }
//
//    @SubscribeEvent
//    public static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
//        var iterator = event.getEntries().iterator();
//
//        while (iterator.hasNext()) {
//            var entry = iterator.next();
//
//            if (entry.getKey().is(ItemRegistry.SCROLL_FORGE_BLOCK.get())) {
//                iterator.remove();
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        if (event.phase != TickEvent.Phase.END) return;
//
//        var player = event.player;
//
//        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
//            var stack = player.getInventory().getItem(i);
//
//            if (stack.is(ItemRegistry.SCROLL_FORGE_BLOCK.get())) {
//                player.getInventory().setItem(i, ItemStack.EMPTY);
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void onUse(PlayerInteractEvent.RightClickItem event) {
//        if (event.getItemStack().is(ItemRegistry.SCROLL_FORGE_BLOCK.get())) {
//            event.setCanceled(true);
//        }
//    }
//
//    @SubscribeEvent
//    public static void onEntityJoin(EntityJoinLevelEvent event) {
//        if (event.getEntity() instanceof ItemEntity item) {
//            if (item.getItem().is(ItemRegistry.SCROLL_FORGE_BLOCK.get())) {
//                item.discard();
//            }
//        }
//    }
//}
