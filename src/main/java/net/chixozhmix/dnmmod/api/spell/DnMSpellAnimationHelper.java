//package net.chixozhmix.dnmmod.api.spell;
//
//import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
//import io.redspace.ironsspellbooks.api.util.AnimationHolder;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.LivingEntity;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//public class DnMSpellAnimationHelper {
//    private static final Map<ResourceLocation, Function<LivingEntity, AnimationHolder>> START_ANIMATIONS = new HashMap<>();
//
//    public static void register(ResourceLocation spellId, Function<LivingEntity, AnimationHolder> animation) {
//        START_ANIMATIONS.put(spellId, animation);
//    }
//
//    public static AnimationHolder getCastStartAnimation(AbstractSpell spell, LivingEntity caster) {
//        Function<LivingEntity, AnimationHolder> override = START_ANIMATIONS.get(spell.getSpellResource());
//
//        if (override != null) {
//            AnimationHolder animation = override.apply(caster);
//
//            if (animation != null) {
//                return animation;
//            }
//        }
//
//        return spell.getCastStartAnimation();
//    }
//}
