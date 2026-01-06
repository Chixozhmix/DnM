package net.chixozhmix.dnmmod.entity.spell.contagion_ray;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.render.RenderHelper;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ContagionRayRenderer extends EntityRenderer<ContagionRay> {
    public ContagionRayRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public boolean shouldRender(ContagionRay pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public void render(ContagionRay entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        float maxRadius = 2.5F;
        float minRadius = 0.005F;
        float deltaTicks = (float)entity.tickCount + partialTicks;
        float deltaUV = -deltaTicks % 10.0F;
        float max = Mth.frac(deltaUV * 0.2F - (float)Mth.floor(deltaUV * 0.1F));
        float min = -1.0F + max;
        float f = deltaTicks / 15.0F;
        f *= f;
        float radius = Mth.clampedLerp(maxRadius, minRadius, f);
        VertexConsumer inner = bufferSource.getBuffer(RenderHelper.CustomerRenderType.magic(SpellRenderingHelper.BEACON));
        float halfRadius = radius * 0.5F;
        float quarterRadius = halfRadius * 0.5F;
        float yMin = entity.onGround() ? 0.0F : Utils.findRelativeGroundLevel(entity.level(), entity.position(), 8) - (float)entity.getY();

        for(int i = 0; i < 4; ++i) {
            int r = (int)(Mth.clamp(0.55F * f, 0.0F, 1.0F) * 255.0F);
            int g = (int)(Mth.clamp(0.1F * f * f, 0.0F, 1.0F) * 255.0F);
            int b = (int)(Mth.clamp(0.05F * f * f, 0.0F, 1.0F) * 255.0F);
            int a = 255;
            Matrix4f poseMatrix = poseStack.last().pose();
            Matrix3f normalMatrix = poseStack.last().normal();
            inner.vertex(poseMatrix, -halfRadius, yMin, -halfRadius).color(r, g, b, a).uv(0.0F, min).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0x540b0b).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            inner.vertex(poseMatrix, -halfRadius, yMin, halfRadius).color(r, g, b, a).uv(1.0F, min).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0x540b0b).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            inner.vertex(poseMatrix, -halfRadius, 250.0F, halfRadius).color(r, g, b, a).uv(1.0F, max).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0x540b0b).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            inner.vertex(poseMatrix, -halfRadius, 250.0F, -halfRadius).color(r, g, b, a).uv(0.0F, max).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0x540b0b).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            int color = RenderHelper.colorf(
                    Mth.clamp(0.7F * f, 0.0F, 1.0F),
                    Mth.clamp(0.05F * f, 0.0F, 1.0F),
                    Mth.clamp(0.02F * f * f, 0.0F, 1.0F)
            );
            inner.vertex(poseMatrix, -quarterRadius, yMin, -quarterRadius).color(color).uv(0.0F, min).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0x540b0b).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            inner.vertex(poseMatrix, -quarterRadius, yMin, quarterRadius).color(color).uv(1.0F, min).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0x540b0b).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            inner.vertex(poseMatrix, -quarterRadius, 250.0F, quarterRadius).color(color).uv(1.0F, max).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0x540b0b).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            inner.vertex(poseMatrix, -quarterRadius, 250.0F, -quarterRadius).color(color).uv(0.0F, max).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0x540b0b).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        }

        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    @Override
    public ResourceLocation getTextureLocation(ContagionRay contagionRay) {
        return SpellRenderingHelper.BEACON;
    }
}
