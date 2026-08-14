//package net.chixozhmix.dnmmod.api.registers;
//
//import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
//import net.chixozhmix.dnmmod.DnMmod;
//import net.chixozhmix.dnmmod.api.spell.CastingItemType;
//import net.chixozhmix.dnmmod.api.spell.DnMSpellAnimationHelper;
//import net.chixozhmix.dnmmod.api.spell.DnMSpellAnimations;
//import net.minecraft.resources.ResourceLocation;
//
//public class DnMSpellAnimationOverrides {
//    public static void register() {
//
//        // =========================
//        // DnMmod
//        // =========================
//
//        DnMSpellAnimationHelper.register(DnMmod.id("meteor_swarm"), caster -> switch (CastingItemType.getCastingItemType(caster)) {
//                    case STAFF -> DnMSpellAnimations.RAISE_SOMETHING;
//                    case WAND -> SpellAnimations.CHARGE_ANIMATION;
//                    case OTHER -> DnMSpellAnimations.METEOR_SWARM_START;
//                }
//        );
//
//
//        // =========================
//        // Iron's Spellbooks
//        // =========================
//
//        DnMSpellAnimationHelper.register(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "fireball"),
//                caster -> switch (CastingItemType.getCastingItemType(caster)) {
//                    case STAFF -> DnMSpellAnimations.RAISE_SOMETHING;
//                    case WAND -> SpellAnimations.CHARGE_ANIMATION;
//                    case OTHER -> null;
//                }
//        );
//    }
//}
