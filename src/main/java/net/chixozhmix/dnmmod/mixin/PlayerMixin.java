package net.chixozhmix.dnmmod.mixin;

import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class PlayerMixin {
    @Inject(at = @At("HEAD"), method = "isInWall", cancellable = true)
    private void isInWall(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity $this = (LivingEntity) (Object) this;
        if ($this != null && $this.hasEffect(ModEffects.PHANTOM_EFFECT.get()))
            cir.setReturnValue(false);
    }
}
