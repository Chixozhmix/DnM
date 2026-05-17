package net.chixozhmix.dnmmod.entity.corypheus;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CorypheusRenderer extends GeoEntityRenderer<CorypheusBoss> {
    public CorypheusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CorypheusModel());
    }
}
