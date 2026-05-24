package net.chixozhmix.dnmmod.effect.custom;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class LossMagicEffect extends MobEffect {
    public LossMagicEffect() {
        super(MobEffectCategory.HARMFUL, 0x003338);
        this.addAttributeModifier(AttributeRegistry.SPELL_POWER.get(),
                "1d19a1d3-6b2f-11ee-4c10-0222ac123007",
                -0.8,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
