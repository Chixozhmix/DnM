package net.chixozhmix.dnmmod.entity.darkspawn_larva.summon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SummonDarkspawnLarvaRenderer extends GeoEntityRenderer<SummonDarkspawnLarva> {
    private static final ResourceLocation EMISSIVE_TEXTURE =
            new ResourceLocation(DnMmod.MOD_ID, "textures/entity/darkspawn_larva/darkspawn_larva.png");

    public SummonDarkspawnLarvaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SummonDarkspawnLarvaModel());
    }

    @Override
    public void preRender(PoseStack poseStack, SummonDarkspawnLarva animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, SummonDarkspawnLarva animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (!isReRender) {
            VertexConsumer emissiveBuffer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(EMISSIVE_TEXTURE));
            float dimFactor = 0.5f;
            super.actuallyRender(poseStack, animatable, model, renderType,
                    bufferSource, emissiveBuffer, true, partialTick,
                    15728880, packedOverlay, red * dimFactor, green * dimFactor, blue * dimFactor, alpha);
        }
    }

    @Override
    public RenderType getRenderType(SummonDarkspawnLarva animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
