package net.chixozhmix.dnmmod.entity.storm_atronach;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;

public class StormAtronachModel extends AbstractSpellCastingMobModel {
    private static final ResourceLocation TEXTURE = new ResourceLocation("dnmmod", "textures/entity/storm_atronach.png");
    private static final ResourceLocation MODEL = new ResourceLocation("dnmmod", "geo/storm_atronach_geo.json");
    private static final ResourceLocation ANIM = new ResourceLocation("dnmmod", "animations/storm_atronach_animation.json");

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob abstractSpellCastingMob) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractSpellCastingMob animatable) {
        return ANIM;
    }
}
