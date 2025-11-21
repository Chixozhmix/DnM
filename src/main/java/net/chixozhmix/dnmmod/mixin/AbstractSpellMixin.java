package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSpell.class)
public class AbstractSpellMixin {
    @Inject(method = "checkPreCastConditions", at = @At("HEAD"), cancellable = true, remap = false)
    private void injectCheckPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData, CallbackInfoReturnable<Boolean> cir) {
        // Проверяем, что это именно RaiseDeadSpell
        if (((Object)this) instanceof RaiseDeadSpell) {
            if (!SpellUtils.ckeckSpellComponent(entity, Items.ROTTEN_FLESH)) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
