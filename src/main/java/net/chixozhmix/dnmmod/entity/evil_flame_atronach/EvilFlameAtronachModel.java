package net.chixozhmix.dnmmod.entity.evil_flame_atronach;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;

public class EvilFlameAtronachModel extends AbstractSpellCastingMobModel {
    private static final ResourceLocation TEXTURE = new ResourceLocation("dnmmod", "textures/entity/flame_atronach/flame_atronach.png");
    private static final ResourceLocation MODEL = new ResourceLocation("dnmmod", "geo/flame_atronach_geo.json");
    private static final ResourceLocation ANIM = new ResourceLocation("dnmmod", "animations/casting_animations.json");

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
