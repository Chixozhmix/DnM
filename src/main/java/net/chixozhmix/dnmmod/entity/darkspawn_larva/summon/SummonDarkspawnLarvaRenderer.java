package net.chixozhmix.dnmmod.entity.darkspawn_larva.summon;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SummonDarkspawnLarvaRenderer extends GeoEntityRenderer<SummonDarkspawnLarva> {
    public SummonDarkspawnLarvaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SummonDarkspawnLarvaModel());
    }
}
