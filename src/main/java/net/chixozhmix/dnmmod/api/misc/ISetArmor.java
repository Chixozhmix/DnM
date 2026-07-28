package net.chixozhmix.dnmmod.api.misc;

import net.minecraft.world.entity.player.Player;

public interface ISetArmor {
    void armorSetBonus(Player player);
    void removeAllBonuses(Player player);
}
