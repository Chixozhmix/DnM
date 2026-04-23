package net.chixozhmix.dnmmod.mixin;

import com.gametechbc.traveloptics.spells.blood.VigorSiphonSpell;
import com.gametechbc.traveloptics.spells.fire.AnnihilationSpell;
import com.gametechbc.traveloptics.spells.fire.LavaBombSpell;
import com.gametechbc.traveloptics.spells.ice.CursedRevenantsSpell;
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

@Mixin(value = {LavaBombSpell.class, AnnihilationSpell.class, CursedRevenantsSpell.class, VigorSiphonSpell.class})
public abstract class TOSpellInfoMixin {
    private static final String LAVA_BOMB_CLASS = "com.gametechbc.traveloptics.spells.fire.LavaBombSpell";
    private static final String ANNIHILATION_CLASS = "com.gametechbc.traveloptics.spells.fire.AnnihilationSpell";
    private static final String CURSED_REVENANTS_CLASS = "com.gametechbc.traveloptics.spells.ice.CursedRevenantsSpell";
    private static final String VIGOR_SIPHON_CLASS = "com.gametechbc.traveloptics.spells.blood.VigorSiphonSpell";

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
        if (spell instanceof LavaBombSpell) return LAVA_BOMB_CLASS;
        if (spell instanceof AnnihilationSpell) return ANNIHILATION_CLASS;
        if (spell instanceof CursedRevenantsSpell) return CURSED_REVENANTS_CLASS;
        if (spell instanceof VigorSiphonSpell) return VIGOR_SIPHON_CLASS;
        return "";
    }
}