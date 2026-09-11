package net.chixozhmix.dnmmod.entity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import net.chixozhmix.chilib.utils.entity.geckolib.DangerZoneProvider;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.EasingType;
import software.bernie.geckolib.loading.json.raw.FaceUV;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;

import java.lang.Math;
import java.util.Collection;

public class DangerZoneRenderLayer <T extends GeoAnimatable & DangerZoneProvider> extends GeoRenderLayer<T> {
    private static final ResourceLocation DANGER_ZONE_TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/entity/danger_zone.png");
    private static final Double2DoubleFunction GHOST_EASING = EasingType.easeOut(EasingType::quadratic);
    private static final GeoQuad QUAD;

    public DangerZoneRenderLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    protected ResourceLocation getTextureResource(T animatable) {
        return DANGER_ZONE_TEXTURE;
    }

    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        Collection<DangerZoneProvider.DangerZone> dangerZones = ((DangerZoneProvider)animatable).getDangerZones();
        if (!dangerZones.isEmpty()) {
            renderType = RenderType.entityTranslucent(DANGER_ZONE_TEXTURE);
            if (renderType != null) {
                buffer = bufferSource.getBuffer(renderType);

                for(DangerZoneProvider.DangerZone dangerZone : dangerZones) {
                    this.renderDangerZone(dangerZone, poseStack, animatable, buffer, partialTick, packedLight, packedOverlay);
                }

            }
        }
    }

    public void renderDangerZone(DangerZoneProvider.DangerZone dangerZone, PoseStack poseStack, T animatable, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        packedLight = 15728640;
        poseStack.pushPose();
        Vector3f offset = dangerZone.getOffset();
        poseStack.translate(offset.x, offset.y + 0.251F, offset.z);
        poseStack.mulPose(new Quaternionf().rotateY(dangerZone.getRotation()));
        Vector3f scale = dangerZone.getSize();
        poseStack.scale(scale.x, 1.0F, scale.y);
        AnimatableTexture.setAndUpdate(DANGER_ZONE_TEXTURE);
        this.renderQuad(poseStack, buffer, packedLight, packedOverlay, dangerZone.getColor());

        double floatingProgress = RenderUtils.getCurrentTick() % (double)10.0F / (double)10.0F;
        poseStack.translate(0.0F, (Double)GHOST_EASING.apply(floatingProgress) * 0.3F, 0.0F);
        this.renderQuad(poseStack, buffer, packedLight, packedOverlay, setColorAlpha(dangerZone.getColor(), 1.0F - floatingProgress * 1.2));

        poseStack.popPose();
    }

    private static int setColorAlpha(int color, double alpha) {
        return color & 16777215 | (int)Math.floor((double)255.0F * Mth.clamp(alpha, (double)0.0F, (double)1.0F)) << 24;
    }

    private void renderQuad(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        Vector3f normal = poseStack.last().normal().transform(new Vector3f(QUAD.normal()));
        Matrix4f poseState = new Matrix4f(poseStack.last().pose());
        this.getRenderer().createVerticesOfQuad(QUAD, poseState, normal, buffer, packedLight, packedOverlay, (float) FastColor.ARGB32.red(color) / 255.0F, (float) FastColor.ARGB32.green(color) / 255.0F, (float) FastColor.ARGB32.blue(color) / 255.0F, (float) FastColor.ARGB32.alpha(color) / 255.0F);
    }

    static {
        QUAD = GeoQuad.build(new GeoVertex[]{
         new GeoVertex((double)-0.5F, (double)0.0F, (double)-0.5F),
         new GeoVertex((double)-0.5F, (double)0.0F, (double)0.5F),
         new GeoVertex((double)0.5F, (double)0.0F, (double)0.5F),
         new GeoVertex((double)0.5F, (double)0.0F, (double)-0.5F)}
         , 0.0F, 0.0F, 48.0F, 48.0F, FaceUV.Rotation.NONE, 48.0F, 48.0F, false, Direction.UP);
    }
}
