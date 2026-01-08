package net.chixozhmix.dnmmod.entity.storm_atronach;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class StormAtronachRenderer extends AbstractSpellCastingMobRenderer {

    public StormAtronachRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StormAtronachModel());
    }
}
