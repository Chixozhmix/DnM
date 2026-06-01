package net.chixozhmix.dnmmod.api.spell;

import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ItemTypeHelper {
    public static boolean isHoldingStaff(LivingEntity entity) {

        //???? не уверен, как лучше
        ItemStack mainHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = entity.getItemInHand(InteractionHand.OFF_HAND);

        return (mainHand.getItem() instanceof StaffItem) || (offHand.getItem() instanceof StaffItem);

        //return isHoldingItemOfClass(entity, "io.redspace.ironsspellbooks.item.weapons.StaffItem");
    }

    private static boolean isHoldingItemOfClass(LivingEntity entity, String className) {
        ItemStack mainHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = entity.getItemInHand(InteractionHand.OFF_HAND);

        return mainHand.getItem().getClass().getName().equals(className) ||
                offHand.getItem().getClass().getName().equals(className);
    }
}
