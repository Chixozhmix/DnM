package net.chixozhmix.dnmmod.api.spell;

import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.chixozhmix.dnmmod.items.WandItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public enum CastingItemType {
    STAFF,
    WAND,
    OTHER;

    public static CastingItemType getCastingItemType(LivingEntity entity) {

        Item castItemMainHand = entity.getMainHandItem().getItem();
        Item castItemOffHand = entity.getOffhandItem().getItem();

        if (castItemMainHand instanceof StaffItem || castItemOffHand instanceof StaffItem) {
            return CastingItemType.STAFF;
        }

        if (castItemMainHand instanceof WandItem || castItemOffHand instanceof WandItem) {
            return CastingItemType.WAND;
        }

        return CastingItemType.OTHER;
    }
}
