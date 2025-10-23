package net.chixozhmix.dnmmod.entity.custom.client;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.custom.UndeadSpiritEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class UndeadSpiritRenderer extends GeoEntityRenderer<UndeadSpiritEntity> {
    public UndeadSpiritRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new UndeadSpiritModel());
    }

    @Override
    public ResourceLocation getTextureLocation(UndeadSpiritEntity animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/entity/undead_spirit.png");
    }
}
