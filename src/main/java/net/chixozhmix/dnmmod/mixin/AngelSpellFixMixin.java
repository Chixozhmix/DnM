package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.effect.OakskinEffect;
import net.alshanex.alshanex_familiars.spells.AngelSpell;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AngelSpell.class)
public abstract class AngelSpellFixMixin {
    @Shadow
    public abstract int getAmplifier(int spellLevel, LivingEntity caster);

    @Overwrite (remap = false)
    private float getPercentDamage(int spellLevel, LivingEntity entity) {
        return OakskinEffect.getReductionAmount(
                getAmplifier(spellLevel, entity),
                entity
        ) * 100F;
    }

}
