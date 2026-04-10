package net.chixozhmix.dnmmod.entity.spell.tombstone;

import io.redspace.ironsspellbooks.render.GeoLivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TombstoneRenderer extends GeoLivingEntityRenderer<Tombstone> {
    public TombstoneRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TombstoneModel());
    }
}
