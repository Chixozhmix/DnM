package net.chixozhmix.dnmmod.items;

import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.ExtendedWeaponTier;
import net.chixozhmix.chilib.attributes.ChiAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class DnMWeaponTier extends ExtendedWeaponTier {
    public static DnMWeaponTier RITUAL_DAGGER;

    public DnMWeaponTier(int uses, float damage, float speed, int enchantmentValue, Supplier<Ingredient> repairIngredient, AttributeContainer... attributes) {
        super(uses, damage, speed, enchantmentValue, repairIngredient, attributes);
    }

    static {
        RITUAL_DAGGER = new DnMWeaponTier(500, 1.0F, -1.4F, 12, () -> Ingredient.of(new ItemLike[]{Items.DIAMOND}),
                new AttributeContainer[]{new AttributeContainer(ChiAttributes.LIFE_STEAL, 0.05, AttributeModifier.Operation.ADDITION)});
    }
}
