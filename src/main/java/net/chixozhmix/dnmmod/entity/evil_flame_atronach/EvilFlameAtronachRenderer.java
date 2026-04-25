package net.chixozhmix.dnmmod.entity.evil_flame_atronach;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class EvilFlameAtronachRenderer extends AbstractSpellCastingMobRenderer {
    public EvilFlameAtronachRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EvilFlameAtronachModel());
    }
}
