package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mixin(AbstractSpell.class)
public class SpellInfoMixin {
    @Inject(method = "getUniqueInfo",
            at = @At("RETURN"),
            cancellable = true,
            remap = false)
    public void injectedGetUniqueInfo(int spellLevel, LivingEntity caster,
                                      CallbackInfoReturnable<List<MutableComponent>> cir) {
        String spellClassName = ((AbstractSpell)(Object)this).getClass().getName();

        if (SpellUtils.shouldSkipSpell(spellClassName)) {
            return;
        }

        Supplier<Item> componentSupplier = SpellComponentConfig.getSpellComponents().get(spellClassName);

        if (componentSupplier != null) {
            Item requiredComponent = componentSupplier.get();
            if (requiredComponent != Items.AIR) {
                List<MutableComponent> original = cir.getReturnValue();
                List<MutableComponent> modified = new ArrayList<>();

                for (MutableComponent comp : original) {
                    String str = comp.getString();
                    if (!str.contains("ui.dnmmod.spell_component") &&
                            !str.contains("Component:") &&
                            !str.contains(SpellUtils.getComponentName(Items.AIR).getString())) {
                        modified.add(comp);
                    }
                }

                modified.add(Component.translatable("ui.dnmmod.spell_component",
                        SpellUtils.getComponentName(requiredComponent)));

                cir.setReturnValue(modified);
            }
        }
    }
}
