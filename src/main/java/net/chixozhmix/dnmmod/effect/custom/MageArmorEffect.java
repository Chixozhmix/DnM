package net.chixozhmix.dnmmod.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MageArmorEffect extends MobEffect {
    public MageArmorEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x0B87EC);
        this.addAttributeModifier(Attributes.ARMOR,
                "1d10a0a4-6b1f-11ee-4c90-0222ac121005",
                4,
                AttributeModifier.Operation.ADDITION);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
