package net.chixozhmix.dnmmod.mixin;

import com.gametechbc.gtbcs_geomancy_plus.spells.geo.ChunkerSpell;
import com.gametechbc.gtbcs_geomancy_plus.spells.geo.TremorSpikeSpell;
import com.gametechbc.gtbcs_geomancy_plus.spells.geo.TremorStepSpell;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.configs.SpellComponentConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Supplier;

@Mixin(value = {TremorStepSpell.class, TremorSpikeSpell.class, ChunkerSpell.class})
public class GPSpellInfoMixin {
    private static final String TREMOR_STEP_CLASS = "com.gametechbc.gtbcs_geomancy_plus.spells.geo.TremorStepSpell";
    private static final String TREMOR_SPIKE_CLASS = "com.gametechbc.gtbcs_geomancy_plus.spells.geo.TremorSpikeSpell";
    private static final String CHUNKER_CLASS = "com.gametechbc.gtbcs_geomancy_plus.spells.geo.ChunkerSpell";

    @Inject(method = "getUniqueInfo", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyGetUniqueInfo(int spellLevel, LivingEntity caster,
                                     CallbackInfoReturnable<List<MutableComponent>> cir) {
        String spellClassName = getSpellClassName();
        Supplier<Item> componentSupplier = SpellComponentConfig.getSpellComponents().get(spellClassName);

        if (componentSupplier != null) {
            Item requiredComponent = componentSupplier.get();
            if (requiredComponent != Items.AIR) {
                List<MutableComponent> modified = new java.util.ArrayList<>(cir.getReturnValue());
                modified.add(Component.translatable("ui.dnmmod.spell_component",
                        SpellUtils.getComponentName(requiredComponent)));
                cir.setReturnValue(modified);
            }
        }
    }

    private String getSpellClassName() {
        Object spell = this;
        if (spell instanceof TremorStepSpell) return TREMOR_STEP_CLASS;
        if (spell instanceof TremorSpikeSpell) return TREMOR_SPIKE_CLASS;
        if (spell instanceof ChunkerSpell) return CHUNKER_CLASS;
        return "";
    }
}