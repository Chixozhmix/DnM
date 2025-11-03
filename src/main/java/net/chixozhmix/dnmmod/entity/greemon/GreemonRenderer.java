package net.chixozhmix.dnmmod.entity.greemon;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GreemonRenderer extends GeoEntityRenderer<GreemonEntity> {
    public GreemonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GreemonModel());
    }

    @Override
    public ResourceLocation getTextureLocation(GreemonEntity animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/entity/greemon.png");
    }
}
