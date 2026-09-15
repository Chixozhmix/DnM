package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.chixozhmix.dnmmod.registers.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSpellCastingMob.class)
public class SpellcastEntityMixin {
    @Inject(at = @At("HEAD"), method = "initiateCastSpell", cancellable = true , remap = false)
    public void dnm$initiateCastSpell(AbstractSpell spell, int spellLevel, CallbackInfo ci) {
        AbstractSpellCastingMob mob = (AbstractSpellCastingMob) (Object) this;

        if(mob.hasEffect(ModEffects.ANTIMAGIC.get())) {
            ci.cancel();
        }

    }
}
