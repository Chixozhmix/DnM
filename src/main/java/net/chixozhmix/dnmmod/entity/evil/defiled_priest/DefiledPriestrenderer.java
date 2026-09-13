package net.chixozhmix.dnmmod.entity.evil.defiled_priest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.chixozhmix.chilib.utils.entity.geckolib.GeckoEmissiveRendererHelper;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DefiledPriestrenderer extends GeoEntityRenderer<DefiledPriest> {
    private static final ResourceLocation EMISSIVE_TEXTURE =
            new ResourceLocation(DnMmod.MOD_ID, "textures/entity/defiled_priest/defiled_priest_glowing.png");

    public DefiledPriestrenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefiledPriestModel());
    }

    @Override
    public void actuallyRender(PoseStack poseStack, DefiledPriest animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        GeckoEmissiveRendererHelper.renderEmissiveLayer(this, animatable, poseStack, model, renderType, bufferSource, isReRender, partialTick,
                packedOverlay, red, green, blue, alpha, EMISSIVE_TEXTURE);
    }

    @Override
    public RenderType getRenderType(DefiledPriest animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
