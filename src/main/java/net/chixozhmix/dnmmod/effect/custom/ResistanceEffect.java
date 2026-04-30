package net.chixozhmix.dnmmod.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class ResistanceEffect extends MobEffect {
    public ResistanceEffect(int pColor) {
        super(MobEffectCategory.BENEFICIAL, pColor);
    }

    @Override
    public MobEffect addAttributeModifier(Attribute pAttribute, String pUuid, double pAmount, AttributeModifier.Operation pOperation) {
        return super.addAttributeModifier(pAttribute, pUuid, pAmount, pOperation);
    }
}
