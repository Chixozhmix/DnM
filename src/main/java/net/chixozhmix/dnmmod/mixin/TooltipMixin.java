package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.configs.SpellComponentConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mixin(TooltipsUtils.class)
public class TooltipMixin {

    @Inject(method = "formatActiveSpellTooltip",
            at = @At("RETURN"),
            cancellable = true,
            remap = false)
    private static void addComponentToActiveTooltip(ItemStack stack, SpellData spellData,
                                                    CastSource castSource, LocalPlayer player,
                                                    CallbackInfoReturnable<List<MutableComponent>> cir) {
        List<MutableComponent> modified = new ArrayList<>(cir.getReturnValue());
        addSpellComponentInfo(spellData.getSpell(), modified);
        cir.setReturnValue(modified);
    }

    @Inject(method = "formatScrollTooltip",
            at = @At("RETURN"),
            cancellable = true,
            remap = false)
    private static void addComponentToScrollTooltip(ItemStack stack, Player player,
                                                    CallbackInfoReturnable<List<Component>> cir) {
        if (stack.getItem() instanceof Scroll && ISpellContainer.isSpellContainer(stack)) {
            ISpellContainer container = ISpellContainer.get(stack);
            if (!container.isEmpty()) {
                SpellData spellData = container.getSpellAtIndex(0);
                List<Component> modified = new ArrayList<>(cir.getReturnValue());
                addSpellComponentInfoForComponent(spellData.getSpell(), modified);

                cir.setReturnValue(modified);
            }
        }
    }

    @Unique
    private static void addSpellComponentInfo(AbstractSpell spell, List<MutableComponent> list) {
        addComponentInfoInternal(spell, list);
    }

    @Unique
    private static void addSpellComponentInfoForComponent(AbstractSpell spell, List<Component> list) {
        addComponentInfoInternal(spell, list);
    }

    @Unique
    private static void addComponentInfoInternal(AbstractSpell spell, List<? super MutableComponent> list) {
        String spellClassName = spell.getClass().getName();
        Supplier<Item> componentSupplier = SpellComponentConfig.getSpellComponents().get(spellClassName);

        if (componentSupplier != null) {
            Item requiredComponent = componentSupplier.get();
            if (requiredComponent != Items.AIR) {
                String requiredName = SpellUtils.getComponentName(requiredComponent).getString();

                boolean alreadyExists = list.stream()
                        .filter(obj -> obj instanceof Component)
                        .map(obj -> ((Component) obj).getString())
                        .anyMatch(text -> text.contains(requiredName));

                if(!alreadyExists) {
                    MutableComponent componentText = Component.translatable("ui.dnmmod.spell_component",
                                    SpellUtils.getComponentName(requiredComponent))
                            .withStyle(style -> style.withColor(0x30992B));

                    int targetIndex = Math.max(0, list.size() - 5);
                    list.add(targetIndex, componentText);
                }
            }
        }
    }
}
