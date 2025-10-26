package net.chixozhmix.dnmmod.entity.goblin_shaman;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;

public class GoblinShamanModel extends AbstractSpellCastingMobModel {

    private static final ResourceLocation TEXTURE = new ResourceLocation("dnmmod", "textures/entity/goblin_shaman.png");
    private static final ResourceLocation MODEL = new ResourceLocation("dnmmod", "geo/goblin_shaman_geo.json");

    public GoblinShamanModel(){
    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob abstractSpellCastingMob) {
        return TEXTURE;
    }
}
