package net.chixozhmix.dnmmod.effect.custom;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.chixozhmix.dnmmod.effect.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ThickOfFight extends MobEffect {
    public ThickOfFight() {
        super(MobEffectCategory.BENEFICIAL, 0xd11e39);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                "1d20a4a1-5b1f-12ee-4c90-1200ac121001",
                0.05F,
                AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(Attributes.ATTACK_SPEED,
                "1d21a4a1-5b1f-13ee-4c90-1200ac120002",
                0.15F,
                AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(AttributeRegistry.SPELL_POWER.get(),
                "1d20a4a0-5b1f-13ee-4c90-1201ac120013",
                0.10F,
                AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                "1d27a0a1-5b1f-10ee-4c00-1204ac126013",
                0.15F,
                AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @SubscribeEvent
    public static void reduceDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effect = entity.getEffect(ModEffects.THICK_OF_FIGHT.get());
        if (effect != null) {
            float multiplier = 0.8F;
            event.setAmount(event.getAmount() * multiplier);
        }

    }
}
