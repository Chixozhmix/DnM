package net.chixozhmix.dnmmod.events;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.Level;

import java.util.*;

@Mod.EventBusSubscriber
public class SpellTickHelper {
    private static final List<DelayedTask> TASKS = new ArrayList<>();

    public static void runLater(Level level, int delayTicks, Runnable action) {
        if (level.isClientSide()) return;
        TASKS.add(new DelayedTask(delayTicks, action));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        for (int i = TASKS.size() - 1; i >= 0; i--) {
            DelayedTask task = TASKS.get(i);

            if (--task.ticksRemaining <= 0) {
                try {
                    task.action.run();
                } catch (Exception e) {
                    DnMmod.LOGGER.error("Exception while executing delayed spell task", e);
                }

                TASKS.remove(i);
            }
        }
    }

    private static class DelayedTask {
        int ticksRemaining;
        Runnable action;

        public DelayedTask(int ticksRemaining, Runnable action) {
            this.ticksRemaining = ticksRemaining;
            this.action = action;
        }
    }
}
