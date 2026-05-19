package net.chixozhmix.dnmmod.entity.darkspawn_larva;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DarkspawnLarvaRenderer extends GeoEntityRenderer<DarkspawnLarva> {
    public DarkspawnLarvaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DarkspawnLarvaModel());
    }
}
