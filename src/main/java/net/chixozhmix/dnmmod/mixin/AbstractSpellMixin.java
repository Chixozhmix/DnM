package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell;
import io.redspace.ironsspellbooks.spells.ender.BlackHoleSpell;
import io.redspace.ironsspellbooks.spells.ender.RecallSpell;
import io.redspace.ironsspellbooks.spells.ender.SummonEnderChestSpell;
import io.redspace.ironsspellbooks.spells.evocation.GustSpell;
import io.redspace.ironsspellbooks.spells.evocation.InvisibilitySpell;
import io.redspace.ironsspellbooks.spells.fire.*;
import io.redspace.ironsspellbooks.spells.holy.GreaterHealSpell;
import io.redspace.ironsspellbooks.spells.ice.SummonPolarBearSpell;
import io.redspace.ironsspellbooks.spells.lightning.*;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.ModCapabilities;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.configs.SpellComponentConfig;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(AbstractSpell.class)
public class AbstractSpellMixin {
    @Inject(method = "checkPreCastConditions", at = @At("HEAD"), cancellable = true, remap = false)
    private void injectCheckPreCastConditions(Level level, int spellLevel, LivingEntity entity,
                                              MagicData playerMagicData, CallbackInfoReturnable<Boolean> cir) {
        String spellClassName = ((AbstractSpell)(Object)this).getClass().getName();

        if (shouldSkipSpell(spellClassName)) {
            return;
        }

        Supplier<Item> componentSupplier = SpellComponentConfig.getSpellComponents().get(spellClassName);

        if (componentSupplier != null) {
            Item requiredComponent = componentSupplier.get();
            if (requiredComponent != Items.AIR && !SpellUtils.checkSpellComponent(entity, requiredComponent)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "getUniqueInfo", at = @At("RETURN"), cancellable = true, remap = false)
    public void injectedGetUniqueInfo(int spellLevel, LivingEntity caster,
                                      CallbackInfoReturnable<List<MutableComponent>> cir) {
        String spellClassName = ((AbstractSpell)(Object)this).getClass().getName();

        if (shouldSkipSpell(spellClassName)) {
            return;
        }

        // Всегда получаем свежие данные из конфига
        Supplier<Item> componentSupplier = SpellComponentConfig.getSpellComponents().get(spellClassName);

        if (componentSupplier != null) {
            Item requiredComponent = componentSupplier.get();
            if (requiredComponent != Items.AIR) {
                List<MutableComponent> original = cir.getReturnValue();
                List<MutableComponent> modified = new java.util.ArrayList<>();

                // Копируем только НЕ компонентные строки
                for (MutableComponent comp : original) {
                    String str = comp.getString();
                    if (!str.contains("ui.dnmmod.spell_component") &&
                            !str.contains("Component:") &&
                            !str.contains(SpellUtils.getComponentName(Items.AIR).getString())) {
                        modified.add(comp);
                    }
                }

                // Добавляем актуальный компонент
                modified.add(Component.translatable("ui.dnmmod.spell_component",
                        SpellUtils.getComponentName(requiredComponent)));

                cir.setReturnValue(modified);
            }
        }
    }

    @Unique
    private boolean shouldSkipSpell(String spellClass) {
        if (spellClass.startsWith("com.gametechbc.traveloptics") && !ModCapabilities.isTravelOpticsLoaded()) {
            return true;
        }
        if (spellClass.startsWith("com.gametechbc.gtbcs_geomancy_plus") && !ModCapabilities.isGeomancyPlusLoaded()) {
            return true;
        }
        if (spellClass.startsWith("net.alshanex.alshanex_familiars") && !ModCapabilities.isAlshanexFamiliarsLoaded()) {
            return true;
        }
        return false;
    }
}
