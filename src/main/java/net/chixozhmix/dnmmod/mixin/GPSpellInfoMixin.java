//package net.chixozhmix.dnmmod.mixin;
//
//import com.gametechbc.gtbcs_geomancy_plus.spells.geo.ChunkerSpell;
//import com.gametechbc.gtbcs_geomancy_plus.spells.geo.TremorSpikeSpell;
//import com.gametechbc.gtbcs_geomancy_plus.spells.geo.TremorStepSpell;
//import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
//import net.chixozhmix.dnmmod.Util.SpellUtils;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.Items;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Supplier;
//
//@Mixin(value = {TremorStepSpell.class, TremorSpikeSpell.class, ChunkerSpell.class})
//public class GPSpellInfoMixin {
//    private static final Map<Class<?>, Supplier<Item>> SPELL_COMPONENTS = new HashMap<>();
//
//    static {
//        SPELL_COMPONENTS.put(TremorStepSpell.class, () -> Items.RAW_IRON_BLOCK);
//        SPELL_COMPONENTS.put(TremorSpikeSpell.class, () -> Items.POINTED_DRIPSTONE);
//        SPELL_COMPONENTS.put(ChunkerSpell.class, () -> Items.DIRT);
//    }
//
//    private AbstractSpell getThisSpell() {
//        return (AbstractSpell)(Object)this;
//    }
//
//    private Class<? extends AbstractSpell> getSpellClass() {
//        return getThisSpell().getClass();
//    }
//
//    @Inject(method = "getUniqueInfo", at = @At("RETURN"), cancellable = true, remap = false)
//    private void modifyGetUniqueInfo(int spellLevel, LivingEntity caster, CallbackInfoReturnable<List<MutableComponent>> cir) {
//        Supplier<Item> componentSupplier = SPELL_COMPONENTS.get(this.getSpellClass());
//        if (componentSupplier != null) {
//            Item component = componentSupplier.get();
//            if (component != null) {
//                List<MutableComponent> original = cir.getReturnValue();
//                List<MutableComponent> modified = new java.util.ArrayList<>(original);
//                modified.add(Component.translatable("ui.dnmmod.spell_component",
//                        SpellUtils.getComponentName(component)));
//                cir.setReturnValue(modified);
//            }
//        }
//    }
//}
