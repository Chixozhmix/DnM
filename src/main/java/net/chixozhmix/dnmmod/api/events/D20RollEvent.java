package net.chixozhmix.dnmmod.api.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class D20RollEvent extends PlayerEvent {
    private final int rollResult;

    public D20RollEvent(Player player, int rollResult) {
        super(player);
        this.rollResult = rollResult;
    }

    public int getRollResult() {
        return this.rollResult;
    }
}
