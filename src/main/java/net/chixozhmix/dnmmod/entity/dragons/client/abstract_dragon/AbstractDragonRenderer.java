package net.chixozhmix.dnmmod.entity.dragons.client.abstract_dragon;

import net.chixozhmix.dnmmod.entity.dragons.AbstractDragonEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AbstractDragonRenderer extends GeoEntityRenderer<AbstractDragonEntity> {
    public AbstractDragonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AbstractDragonModel());
    }

    @Override
    public boolean shouldRender(AbstractDragonEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}
