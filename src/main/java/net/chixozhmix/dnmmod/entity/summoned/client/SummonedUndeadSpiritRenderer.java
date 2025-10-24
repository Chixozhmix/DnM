package net.chixozhmix.dnmmod.entity.summoned.client;

import net.chixozhmix.dnmmod.entity.summoned.SummonedUndeadSpirit;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SummonedUndeadSpiritRenderer extends GeoEntityRenderer<SummonedUndeadSpirit> {
    public SummonedUndeadSpiritRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SummonedUndeadSpiritModel());
    }
}
