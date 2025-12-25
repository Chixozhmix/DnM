package net.chixozhmix.dnmmod.mixin;

import com.gametechbc.gtbcs_geomancy_plus.spells.geo.SeismicSurfSpell;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SeismicSurfSpell.class)
public class SeismicSurfMixin {
    @Inject(method = "getUniqueInfo", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyGetUniqueInfo(int spellLevel, LivingEntity caster, CallbackInfoReturnable<List<MutableComponent>> cir) {
        List<MutableComponent> original = cir.getReturnValue();
        List<MutableComponent> modified = new java.util.ArrayList<>(original);
        modified.add(Component.translatable("ui.dnmmod.spell_component", SpellUtils.getComponentName(Item.byBlock(ACBlockRegistry.AZURE_MAGNET.get()))));
        cir.setReturnValue(modified);
    }
}
