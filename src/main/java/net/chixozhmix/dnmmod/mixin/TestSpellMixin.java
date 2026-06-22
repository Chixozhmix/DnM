package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.configs.SpellComponentConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(AbstractSpell.class)
public class TestSpellMixin {

    @Inject(method = "attemptInitiateCast",
            at = @At(value = "INVOKE",
                    target = "Lio/redspace/ironsspellbooks/api/spells/AbstractSpell;checkPreCastConditions(Lnet/minecraft/world/level/Level;ILnet/minecraft/world/entity/LivingEntity;Lio/redspace/ironsspellbooks/api/magic/MagicData;)Z"),
            cancellable = true,
            remap = false)
    private void injectComponentCheck(ItemStack stack, int spellLevel, Level level, Player player,
                                      CastSource castSource, boolean triggerCooldown,
                                      String castingEquipmentSlot, CallbackInfoReturnable<Boolean> cir) {
        String spellClassName = ((AbstractSpell)(Object)this).getClass().getName();

        if (SpellUtils.shouldSkipSpell(spellClassName)) {
            return;
        }

        Supplier<Item> componentSupplier = SpellComponentConfig.getSpellComponents().get(spellClassName);

        if (componentSupplier != null) {
            Item requiredComponent = componentSupplier.get();
            if (requiredComponent != Items.AIR && !SpellUtils.checkSpellComponent(player, requiredComponent)) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.displayClientMessage(
                            Component.translatable("ui.dnmmod.component_loss",
                                    SpellUtils.getComponentName(requiredComponent)), true);
                }
                cir.setReturnValue(false);
            }
        }
    }
}
