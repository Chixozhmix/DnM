package net.chixozhmix.dnmmod.mixin;

import com.gametechbc.traveloptics.spells.fire.AnnihilationSpell;
import com.gametechbc.traveloptics.spells.fire.LavaBombSpell;
import com.gametechbc.traveloptics.spells.ice.CursedRevenantsSpell;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.spells.lightning.*;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(value = {LavaBombSpell.class, AnnihilationSpell.class, CursedRevenantsSpell.class})
public abstract class TOSpellInfoMixin {
    private static final Map<Class<?>, Supplier<Item>> SPELL_COMPONENTS = new HashMap<>();

    static {
        SPELL_COMPONENTS.put(LavaBombSpell.class, () -> Items.MAGMA_BLOCK);
        SPELL_COMPONENTS.put(AnnihilationSpell.class, () -> ACItemRegistry.URANIUM_SHARD.get());
        SPELL_COMPONENTS.put(CursedRevenantsSpell.class, () -> ItemRegistry.FROZEN_BONE_SHARD.get());
    }

    private AbstractSpell getThisSpell() {
        return (AbstractSpell)(Object)this;
    }

    private Class<? extends AbstractSpell> getSpellClass() {
        return getThisSpell().getClass();
    }

    @Inject(method = "getUniqueInfo", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyGetUniqueInfo(int spellLevel, LivingEntity caster, CallbackInfoReturnable<List<MutableComponent>> cir) {
        Supplier<Item> componentSupplier = SPELL_COMPONENTS.get(this.getSpellClass());
        if (componentSupplier != null) {
            Item component = componentSupplier.get();
            if (component != null) {
                List<MutableComponent> original = cir.getReturnValue();
                List<MutableComponent> modified = new java.util.ArrayList<>(original);
                modified.add(Component.translatable("ui.dnmmod.spell_component",
                        SpellUtils.getComponentName(component)));
                cir.setReturnValue(modified);
            }
        }
    }
}
