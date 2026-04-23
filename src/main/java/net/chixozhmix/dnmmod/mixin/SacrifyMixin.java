package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.spells.blood.SacrificeSpell;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.configs.SpellComponentConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Supplier;

@Mixin(SacrificeSpell.class)
public class SacrifyMixin {
    private static final String SPELL_CLASS_NAME = "io.redspace.ironsspellbooks.spells.blood.SacrificeSpell";

    @Inject(method = "checkPreCastConditions", at = @At("HEAD"), cancellable = true, remap = false)
    private void injectCheckPreCastConditions(Level level, int spellLevel, LivingEntity entity,
                                              MagicData playerMagicData, CallbackInfoReturnable<Boolean> cir) {
        Supplier<Item> componentSupplier = SpellComponentConfig.getSpellComponents().get(SPELL_CLASS_NAME);

        if (componentSupplier != null) {
            Item requiredComponent = componentSupplier.get();
            if (requiredComponent != Items.AIR && !SpellUtils.checkSpellComponent(entity, requiredComponent)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "getUniqueInfo", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyGetUniqueInfo(int spellLevel, LivingEntity caster,
                                     CallbackInfoReturnable<List<MutableComponent>> cir) {
        Supplier<Item> componentSupplier = SpellComponentConfig.getSpellComponents().get(SPELL_CLASS_NAME);

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
}