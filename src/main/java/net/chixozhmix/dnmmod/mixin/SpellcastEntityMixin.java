package net.chixozhmix.dnmmod.mixin;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSpellCastingMob.class)
public class SpellcastEntityMixin {
    private static final TagKey<EntityType<?>> BOSSES =
            ForgeRegistries.ENTITY_TYPES.tags().createTagKey(new ResourceLocation("forge", "bosses"));

    @Inject(at = @At("HEAD"), method = "initiateCastSpell", cancellable = true , remap = false)
    public void dnm$initiateCastSpell(AbstractSpell spell, int spellLevel, CallbackInfo ci) {
        AbstractSpellCastingMob mob = (AbstractSpellCastingMob) (Object) this;

        if(mob.hasEffect(ModEffects.ANTIMAGIC.get()) && !mob.getType().is(BOSSES)) {
            ci.cancel();
        }

    }
}
