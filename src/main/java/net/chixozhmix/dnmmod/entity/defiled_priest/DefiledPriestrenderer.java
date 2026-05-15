package net.chixozhmix.dnmmod.entity.defiled_priest;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DefiledPriestrenderer extends GeoEntityRenderer<DefiledPriest> {
    public DefiledPriestrenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefiledPriestModel());
    }
}
