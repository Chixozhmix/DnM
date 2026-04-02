package net.chixozhmix.dnmmod.entity.spell.hunger_of_hadar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleRenderer;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class HungerOfHadarRenderer extends EntityRenderer<HungerOfHadar> {
    private static final ResourceLocation FIELD_TEXTURE = DnMmod.id("textures/entity/hunger_of_hadar/hunger_of_hadar.png");

    public HungerOfHadarRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(HungerOfHadar entity, float pEntityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int pPackedLight) {
        float radius = entity.getRadius();

        renderBlackSphere(entity, partialTicks, poseStack, bufferSource, radius);

        super.render(entity, pEntityYaw, partialTicks, poseStack, bufferSource, pPackedLight);
    }

    private void renderBlackSphere(HungerOfHadar entity, float partialTicks, PoseStack poseStack,
                                   MultiBufferSource bufferSource, float radius) {
        if (radius <= 0) return;

        poseStack.pushPose();

        Vec3 center = entity.getCenter();
        if (center != null) {
            poseStack.translate(center.x - entity.getX(),
                    center.y - entity.getY(),
                    center.z - entity.getZ());
        }

        float animationProgress = ((float) entity.tickCount + partialTicks) / 200.0F;

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(FIELD_TEXTURE));

        // Рисуем основную черную сферу
        poseStack.pushPose();
        poseStack.scale(radius, radius, radius);
        poseStack.mulPose(Axis.XP.rotationDegrees(animationProgress * 50F));
        poseStack.mulPose(Axis.YP.rotationDegrees(animationProgress * 30F));

        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normalMatrix = poseStack.last().normal();

        drawBlackSphere(consumer, matrix, normalMatrix, 1.0F);

        poseStack.popPose();

        // Добавляем внешний полупрозрачный слой для эффекта тьмы
        VertexConsumer translucentConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(FIELD_TEXTURE));

        poseStack.pushPose();
        poseStack.scale(radius * 1.05F, radius * 1.05F, radius * 1.05F);
        poseStack.mulPose(Axis.XP.rotationDegrees(-animationProgress * 30F));
        poseStack.mulPose(Axis.YP.rotationDegrees(-animationProgress * 20F));

        Matrix4f matrixOuter = poseStack.last().pose();
        Matrix3f normalMatrixOuter = poseStack.last().normal();

        drawOuterDarkness(translucentConsumer, matrixOuter, normalMatrixOuter, 1.0F);

        poseStack.popPose();

        poseStack.popPose();
    }

    private void drawBlackSphere(VertexConsumer consumer, Matrix4f poseMatrix, Matrix3f normalMatrix,
                                 float radius) {
        int segments = 32; // Еще больше сегментов для гладкости
        int rings = 24;
        int alphaInt = 255; // Полностью непрозрачный

        for (int i = 0; i < rings; i++) {
            float phi1 = (float) Math.PI * i / rings;
            float phi2 = (float) Math.PI * (i + 1) / rings;

            for (int j = 0; j <= segments; j++) {
                float theta = (float) (2 * Math.PI * j / segments);

                float x1 = radius * (float) (Math.sin(phi1) * Math.cos(theta));
                float y1 = radius * (float) (Math.cos(phi1));
                float z1 = radius * (float) (Math.sin(phi1) * Math.sin(theta));

                float x2 = radius * (float) (Math.sin(phi2) * Math.cos(theta));
                float y2 = radius * (float) (Math.cos(phi2));
                float z2 = radius * (float) (Math.sin(phi2) * Math.sin(theta));

                float thetaNext = (float) (2 * Math.PI * (j + 1) / segments);
                float x3 = radius * (float) (Math.sin(phi1) * Math.cos(thetaNext));
                float y3 = radius * (float) (Math.cos(phi1));
                float z3 = radius * (float) (Math.sin(phi1) * Math.sin(thetaNext));

                float x4 = radius * (float) (Math.sin(phi2) * Math.cos(thetaNext));
                float y4 = radius * (float) (Math.cos(phi2));
                float z4 = radius * (float) (Math.sin(phi2) * Math.sin(thetaNext));

                // Нормали для правильного освещения (хотя мы его отключили)
                float nx1 = x1 / radius;
                float ny1 = y1 / radius;
                float nz1 = z1 / radius;
                float nx2 = x2 / radius;
                float ny2 = y2 / radius;
                float nz2 = z2 / radius;
                float nx3 = x3 / radius;
                float ny3 = y3 / radius;
                float nz3 = z3 / radius;
                float nx4 = x4 / radius;
                float ny4 = y4 / radius;
                float nz4 = z4 / radius;

                // Треугольник 1 - полностью черный
                consumer.vertex(poseMatrix, x1, y1, z1)
                        .color(0, 0, 0, alphaInt)
                        .uv(0, 0)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(nx1, ny1, nz1)
                        .endVertex();
                consumer.vertex(poseMatrix, x2, y2, z2)
                        .color(0, 0, 0, alphaInt)
                        .uv(0, 1)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(nx2, ny2, nz2)
                        .endVertex();
                consumer.vertex(poseMatrix, x3, y3, z3)
                        .color(0, 0, 0, alphaInt)
                        .uv(1, 0)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(nx3, ny3, nz3)
                        .endVertex();

                // Треугольник 2 - полностью черный
                consumer.vertex(poseMatrix, x2, y2, z2)
                        .color(0, 0, 0, alphaInt)
                        .uv(0, 1)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(nx2, ny2, nz2)
                        .endVertex();
                consumer.vertex(poseMatrix, x4, y4, z4)
                        .color(0, 0, 0, alphaInt)
                        .uv(1, 1)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(nx4, ny4, nz4)
                        .endVertex();
                consumer.vertex(poseMatrix, x3, y3, z3)
                        .color(0, 0, 0, alphaInt)
                        .uv(1, 0)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(nx3, ny3, nz3)
                        .endVertex();
            }
        }
    }

    private void drawOuterDarkness(VertexConsumer consumer, Matrix4f poseMatrix, Matrix3f normalMatrix,
                                   float radius) {
        int segments = 32;
        int rings = 24;
        int alphaInt = 120;

        for (int i = 0; i < rings; i++) {
            float phi1 = (float) Math.PI * i / rings;
            float phi2 = (float) Math.PI * (i + 1) / rings;

            for (int j = 0; j <= segments; j++) {
                float theta = (float) (2 * Math.PI * j / segments);

                float x1 = radius * (float) (Math.sin(phi1) * Math.cos(theta));
                float y1 = radius * (float) (Math.cos(phi1));
                float z1 = radius * (float) (Math.sin(phi1) * Math.sin(theta));

                float x2 = radius * (float) (Math.sin(phi2) * Math.cos(theta));
                float y2 = radius * (float) (Math.cos(phi2));
                float z2 = radius * (float) (Math.sin(phi2) * Math.sin(theta));

                float thetaNext = (float) (2 * Math.PI * (j + 1) / segments);
                float x3 = radius * (float) (Math.sin(phi1) * Math.cos(thetaNext));
                float y3 = radius * (float) (Math.cos(phi1));
                float z3 = radius * (float) (Math.sin(phi1) * Math.sin(thetaNext));

                float x4 = radius * (float) (Math.sin(phi2) * Math.cos(thetaNext));
                float y4 = radius * (float) (Math.cos(phi2));
                float z4 = radius * (float) (Math.sin(phi2) * Math.sin(thetaNext));

                float nx1 = x1 / radius;
                float ny1 = y1 / radius;
                float nz1 = z1 / radius;
                float nx2 = x2 / radius;
                float ny2 = y2 / radius;
                float nz2 = z2 / radius;
                float nx3 = x3 / radius;
                float ny3 = y3 / radius;
                float nz3 = z3 / radius;
                float nx4 = x4 / radius;
                float ny4 = y4 / radius;
                float nz4 = z4 / radius;

                consumer.vertex(poseMatrix, x1, y1, z1)
                        .color(0, 0, 0, alphaInt)
                        .uv(0, 0)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(nx1, ny1, nz1)
                        .endVertex();
                consumer.vertex(poseMatrix, x2, y2, z2)
                        .color(0, 0, 0, alphaInt)
                        .uv(0, 1)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(nx2, ny2, nz2)
                        .endVertex();
                consumer.vertex(poseMatrix, x3, y3, z3)
                        .color(0, 0, 0, alphaInt)
                        .uv(1, 0)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(nx3, ny3, nz3)
                        .endVertex();

                consumer.vertex(poseMatrix, x2, y2, z2)
                        .color(0, 0, 0, alphaInt)
                        .uv(0, 1)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(nx2, ny2, nz2)
                        .endVertex();
                consumer.vertex(poseMatrix, x4, y4, z4)
                        .color(0, 0, 0, alphaInt)
                        .uv(1, 1)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(nx4, ny4, nz4)
                        .endVertex();
                consumer.vertex(poseMatrix, x3, y3, z3)
                        .color(0, 0, 0, alphaInt)
                        .uv(1, 0)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(nx3, ny3, nz3)
                        .endVertex();
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(HungerOfHadar hungerOfHadar) {
        return IcicleRenderer.TEXTURE;
    }
}