package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.spells.lightning.LightningLanceSpell;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.items.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LightningLanceSpell.class)
public class LightningLanceMixin {
    @Inject(method = "getUniqueInfo", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyGetUniqueInfo(int spellLevel, LivingEntity caster, CallbackInfoReturnable<List<MutableComponent>> cir) {
        List<MutableComponent> original = cir.getReturnValue();
        // Создаем новый список с дополнительной информацией
        List<MutableComponent> modified = new java.util.ArrayList<>(original);
        modified.add(Component.translatable("ui.dnmmod.spell_component", SpellUtils.getComponentName(ModItems.IRON_TRIDENT.get())));
        cir.setReturnValue(modified);
    }
}
