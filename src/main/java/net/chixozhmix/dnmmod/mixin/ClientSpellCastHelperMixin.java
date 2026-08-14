//package net.chixozhmix.dnmmod.mixin;
//
//import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
//import io.redspace.ironsspellbooks.api.util.AnimationHolder;
//import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
//import io.redspace.ironsspellbooks.render.animation.AnimationHelper;
//import net.chixozhmix.dnmmod.api.spell.DnMSpellAnimationHelper;
//import net.minecraft.client.Minecraft;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.UUID;
//
//@Mixin(ClientSpellCastHelper.class)
//public class ClientSpellCastHelperMixin {
//    @Inject(method = "handleClientBoundOnCastStarted", at = @At("HEAD"), cancellable = true, remap = false)
//    private static void dnmmod$handleCastStarted(UUID castingEntityId, String spellId, int spellLevel, CallbackInfo ci) {
//        var minecraft = Minecraft.getInstance();
//
//        if (minecraft.level == null) {
//            return;
//        }
//
//        var player = minecraft.level.getPlayerByUUID(castingEntityId);
//
//        if (player == null) {
//            return;
//        }
//
//        var spell = SpellRegistry.getSpell(spellId);
//
//        AnimationHolder animation = DnMSpellAnimationHelper.getCastStartAnimation(spell, player);
//
//        animation.getForPlayer().ifPresent(resourceLocation -> AnimationHelper.animatePlayerStart(player, resourceLocation));
//
//        spell.onClientPreCast(player.level(), spellLevel, player, player.getUsedItemHand(), null);
//
//        ci.cancel();
//    }
//}
