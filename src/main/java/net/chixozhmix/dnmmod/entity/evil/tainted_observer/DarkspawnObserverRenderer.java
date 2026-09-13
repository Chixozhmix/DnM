package net.chixozhmix.dnmmod.entity.evil.tainted_observer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.chixozhmix.chilib.utils.entity.beamAttacker.BeamAttackRenderer;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DarkspawnObserverRenderer extends GeoEntityRenderer<DarkspawnObserver> {
    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(BEAM_TEXTURE);
    private static final ResourceLocation GLOWING = new ResourceLocation(DnMmod.MOD_ID, "textures/entity/tainted_observer/tainted_observer_glowing.png");

    public DarkspawnObserverRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DarkspawnObserverModel());
    }

    @Override
    public void preRender(PoseStack poseStack, DarkspawnObserver animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void render(DarkspawnObserver entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        BeamAttackRenderer.render(entity, poseStack, bufferSource, partialTick);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, DarkspawnObserver animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (!isReRender) {
            VertexConsumer emissiveBuffer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(GLOWING));
            float dimFactor = 0.7f;
            super.actuallyRender(poseStack, animatable, model, renderType,
                    bufferSource, emissiveBuffer, true, partialTick,
                    15728880, packedOverlay, red * dimFactor, green * dimFactor, blue * dimFactor, alpha);
        }
    }

    @Override
    public RenderType getRenderType(DarkspawnObserver animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    private Vec3 getPosition(LivingEntity entity, double yOffset, float partialTick) {
        double x = Mth.lerp(partialTick, entity.xOld, entity.getX());
        double y = Mth.lerp(partialTick, entity.yOld, entity.getY()) + yOffset;
        double z = Mth.lerp(partialTick, entity.zOld, entity.getZ());
        return new Vec3(x, y, z);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal,
                               float x, float y, float z, int red, int green, int blue,
                               float u, float v) {
        consumer.vertex(pose, x, y, z)
                .color(red, green, blue, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(normal, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }
}
