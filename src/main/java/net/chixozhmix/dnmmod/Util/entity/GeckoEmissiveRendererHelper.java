package net.chixozhmix.dnmmod.Util.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class GeckoEmissiveRendererHelper {
    public static <T extends GeoEntity> void renderEmissiveLayer(
            GeoRenderer<T> renderer,
            T animatable,
            PoseStack poseStack,
            BakedGeoModel model,
            RenderType renderType,
            MultiBufferSource bufferSource,
            boolean isReRender,
            float partialTick,
            int packedOverlay,
            float red, float green, float blue, float alpha,
            ResourceLocation emissiveTexture
    ) {
        if (!isReRender) {
            VertexConsumer emissiveBuffer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(emissiveTexture));
            renderer.actuallyRender(
                    poseStack, animatable, model, renderType,
                    bufferSource, emissiveBuffer, true, partialTick,
                    15728880, packedOverlay, red, green, blue, alpha
            );
        }
    }
}
