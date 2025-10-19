package net.chixozhmix.dnmmod.entity.spell.cloud_dagger;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CloudDaggerRenderer extends GeoEntityRenderer<CloudDagger> {
    public CloudDaggerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CloudDaggerModel());
    }

    @Override
    public boolean shouldShowName(CloudDagger animatable) {
        return false;
    }

    @Override
    public ResourceLocation getTextureLocation(CloudDagger animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/entity/magic_dagger.png");
    }
}
