package net.chixozhmix.dnmmod.api.spell;

import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;

public class DnMSpellAnimations {
    public static ResourceLocation ANIMATION_RESOURCE = ResourceLocation.fromNamespaceAndPath("dnmmod", "animation");
    public static final AnimationHolder RAISE_SOMETHING = new AnimationHolder(DnMmod.id("raise_something"), true, true);
    public static final AnimationHolder AOE_END = new AnimationHolder(DnMmod.id("aoe_end"), true, true);

}
