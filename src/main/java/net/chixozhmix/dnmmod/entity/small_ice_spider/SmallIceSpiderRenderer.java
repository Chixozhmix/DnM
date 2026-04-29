package net.chixozhmix.dnmmod.entity.small_ice_spider;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SmallIceSpiderRenderer extends GeoEntityRenderer<SmallIceSpiderEntity> {
    public SmallIceSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SmallIceSpiderModel());
    }
}
