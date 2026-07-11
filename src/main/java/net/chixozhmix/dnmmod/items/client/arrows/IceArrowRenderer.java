package net.chixozhmix.dnmmod.items.client.arrows;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class IceArrowRenderer extends ArrowRenderer {
    private static final String TEXTURE = "textures/entity/arrows/ice_arrow.png";

    public IceArrowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity pEntity) {
        return new ResourceLocation(DnMmod.MOD_ID, TEXTURE);
    }
}
