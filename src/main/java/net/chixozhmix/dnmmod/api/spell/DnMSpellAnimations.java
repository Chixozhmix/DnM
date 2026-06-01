package net.chixozhmix.dnmmod.api.spell;

import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;

public class DnMSpellAnimations {
    public static ResourceLocation ANIMATION_RESOURCE = ResourceLocation.fromNamespaceAndPath("dnmmod", "animation");
    public static final AnimationHolder RAISE_SOMETHING = new AnimationHolder(DnMmod.id("raise_something"), true, true);
    public static final AnimationHolder AOE_END = new AnimationHolder(DnMmod.id("aoe_end"), true, true);

    //WAND ANIMATIONS (очень криво работает анимация instant. Почему-то он запомнил первую анимацию и не меняет ее вообще)
//    public static final AnimationHolder WAND_INSTANT_CAST = new AnimationHolder(DnMmod.id("wand_instant_cast"), true, false);
//    public static final AnimationHolder WAND_LONG_CAST = new AnimationHolder(DnMmod.id("wand_long_cast"), true, false);
//    public static final AnimationHolder WAND_LONG_CAST_FINISH = new AnimationHolder(DnMmod.id("wand_long_cast_finish"), true, false);
//    public static final AnimationHolder WAND_CONTINUOUS_CAST = new AnimationHolder(DnMmod.id("wand_continuous_cast"), false);

}
