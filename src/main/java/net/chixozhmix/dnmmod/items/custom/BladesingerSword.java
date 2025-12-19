package net.chixozhmix.dnmmod.items.custom;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import net.chixozhmix.dnmmod.Util.PropertiesHelper;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;

import java.util.Map;

public class BladesingerSword extends MagicSwordItem {
    public BladesingerSword(SpellDataRegistryHolder[] imbuedSpells) {
        super(Tiers.NETHERITE, 8, -2.4F, imbuedSpells, Map.of(), PropertiesHelper.durabilityItemProperties(2100).rarity(Rarity.EPIC));
    }
}
