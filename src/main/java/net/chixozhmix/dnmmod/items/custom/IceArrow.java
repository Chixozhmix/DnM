package net.chixozhmix.dnmmod.items.custom;

import net.chixozhmix.dnmmod.entity.custom.IceArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IceArrow extends ArrowItem {
    public IceArrow(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public AbstractArrow createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter) {
        IceArrowEntity arrowEntity = new IceArrowEntity(pLevel, pShooter);
        arrowEntity.setBaseDamage(6.0f);

        return arrowEntity;
    }
}
