package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell;
import io.redspace.ironsspellbooks.spells.ender.BlackHoleSpell;
import io.redspace.ironsspellbooks.spells.evocation.GustSpell;
import io.redspace.ironsspellbooks.spells.evocation.InvisibilitySpell;
import io.redspace.ironsspellbooks.spells.fire.BurningDashSpell;
import io.redspace.ironsspellbooks.spells.fire.WallOfFireSpell;
import io.redspace.ironsspellbooks.spells.holy.GreaterHealSpell;
import io.redspace.ironsspellbooks.spells.ice.SummonPolarBearSpell;
import io.redspace.ironsspellbooks.spells.lightning.*;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.configs.SpellComponentConfig;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(value = {LightningBoltSpell.class, LightningLanceSpell.class, BallLightningSpell.class, BlackHoleSpell.class, BurningDashSpell.class,
        ChargeSpell.class, GreaterHealSpell.class, GustSpell.class, InvisibilitySpell.class, RaiseDeadSpell.class, SummonPolarBearSpell.class,
        ThunderstormSpell.class, WallOfFireSpell.class})
public abstract class InfoSpellMixin {
    private AbstractSpell getThisSpell() {
        return (AbstractSpell)(Object)this;
    }

    private String getSpellClassName() {
        return getThisSpell().getClass().getName();
    }

    @Inject(method = "getUniqueInfo", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyGetUniqueInfo(int spellLevel, LivingEntity caster, CallbackInfoReturnable<List<MutableComponent>> cir) {
        String spellClassName = getSpellClassName();

        // Получаем компонент из конфига
        Supplier<Item> componentSupplier = SpellComponentConfig.getSpellComponents().get(spellClassName);

        if (componentSupplier != null) {
            Item requiredComponent = componentSupplier.get();
            if (requiredComponent != null && requiredComponent != Items.AIR) {
                List<MutableComponent> original = cir.getReturnValue();
                List<MutableComponent> modified = new java.util.ArrayList<>();

                // Копируем только НЕ компонентные строки (на случай, если уже был добавлен компонент)
                for (MutableComponent comp : original) {
                    String str = comp.getString();
                    if (!str.contains("ui.dnmmod.spell_component") &&
                            !str.contains("Component:") &&
                            !str.contains(SpellUtils.getComponentName(Items.AIR).getString())) {
                        modified.add(comp);
                    }
                }

                // Добавляем актуальный компонент из конфига
                modified.add(Component.translatable("ui.dnmmod.spell_component",
                        SpellUtils.getComponentName(requiredComponent)));

                cir.setReturnValue(modified);
            }
        }
    }
}
