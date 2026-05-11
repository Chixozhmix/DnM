package net.chixozhmix.dnmmod.entity.defiled_wizard;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;

public class DefiledWizardModel extends AbstractSpellCastingMobModel {
    private static final ResourceLocation TEXTURE = new ResourceLocation("dnmmod", "textures/entity/defiled_wizard/defiled_wizard.png");
    private static final ResourceLocation MODEL = new ResourceLocation("dnmmod", "geo/defiled_wizard_geo.json");
    private static final ResourceLocation ANIM = new ResourceLocation("dnmmod", "animations/defiled_wizard_animation.json");


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
