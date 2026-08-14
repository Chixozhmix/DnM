package net.chixozhmix.dnmmod.api.spell;

import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.chixozhmix.dnmmod.items.WandItem;
import net.minecraft.world.entity.LivingEntity;

public class ItemTypeHelper {
    public static boolean isHoldingStaff(LivingEntity entity) {
        return entity.getMainHandItem().getItem() instanceof StaffItem || entity.getOffhandItem().getItem() instanceof StaffItem;
    }

    public static boolean isHoldingWand(LivingEntity entity) {
        return entity.getMainHandItem().getItem() instanceof WandItem || entity.getOffhandItem().getItem() instanceof WandItem;
    }
}
