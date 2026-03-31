package net.chixozhmix.dnmmod.entity.spell.hunger_of_hadar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class HungerOfHadarRenderer extends EntityRenderer<HungerOfHadar> {
    private static final ResourceLocation CENTER_TEXTURE = IronsSpellbooks.id("textures/entity/black_hole/black_hole.png");
    private static final ResourceLocation BEAM_TEXTURE = IronsSpellbooks.id("textures/entity/black_hole/beam.png");
    private static final ResourceLocation FIELD_TEXTURE = IronsSpellbooks.id("textures/entity/black_hole/field.png");

    public HungerOfHadarRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(HungerOfHadar entity, float pEntityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight) {
        float radius = entity.getRadius();
        float entityScale = radius * 0.05F; // Масштабируем относительно радиуса

        // Рендер центральной сферы - передаем radius
        //renderCenter(entity, partialTicks, poseStack, bufferSource, entityScale, radius);

        // Рендер энергетического поля на всю зону
        renderEnergyField(entity, partialTicks, poseStack, bufferSource, radius);

        // Рендер лучей/искр по всей зоне
        //renderBeamsAndParticles(entity, partialTicks, poseStack, bufferSource, radius, entityScale);

        super.render(entity, pEntityYaw, partialTicks, poseStack, bufferSource, pPackedLight);
    }

    private void renderCenter(HungerOfHadar entity, float partialTicks, PoseStack poseStack,
                              MultiBufferSource bufferSource, float entityScale, float radius) {
        poseStack.pushPose();

        // Позиционируем центр
        Vec3 center = entity.getCenter();
        if (center != null) {
            poseStack.translate(center.x - entity.getX(),
                    center.y - entity.getY() + entity.getBoundingBox().getYsize() / 2.0F,
                    center.z - entity.getZ());
        } else {
            poseStack.translate(0.0F, entity.getBoundingBox().getYsize() / 2.0F, 0.0F);
        }

        // Поворачиваем к камере (billboard)
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));

        // Масштабируем центральную сферу относительно радиуса
        float centerScale = Math.min(radius * 0.3F, 3.0F);
        poseStack.scale(centerScale, centerScale, centerScale);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(CENTER_TEXTURE));
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();

        // Рисуем центральную сферу
        consumer.vertex(poseMatrix, -1.0F, -1.0F, 0.0F)
                .color(255, 255, 255, 200)
                .uv(0.0F, 1.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        consumer.vertex(poseMatrix, -1.0F, 1.0F, 0.0F)
                .color(255, 255, 255, 200)
                .uv(0.0F, 0.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        consumer.vertex(poseMatrix, 1.0F, 1.0F, 0.0F)
                .color(255, 255, 255, 200)
                .uv(1.0F, 0.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
        consumer.vertex(poseMatrix, 1.0F, -1.0F, 0.0F)
                .color(255, 255, 255, 200)
                .uv(1.0F, 1.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();

        poseStack.popPose();
    }

    private void renderEnergyField(HungerOfHadar entity, float partialTicks, PoseStack poseStack,
                                   MultiBufferSource bufferSource, float radius) {
        if (radius <= 0) return;

        poseStack.pushPose();

        // Позиционируемся в центр области
        Vec3 center = entity.getCenter();
        if (center != null) {
            poseStack.translate(center.x - entity.getX(),
                    center.y - entity.getY(),
                    center.z - entity.getZ());
        }

        float animationProgress = ((float) entity.tickCount + partialTicks) / 200.0F;
        float alpha = Math.min(1.0F, entity.tickCount / 20.0F) * 0.6F; // Постепенное появление

        // Рисуем несколько сферических слоев
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(FIELD_TEXTURE));

        for (int layer = 0; layer < 3; layer++) {
            float layerRadius = radius * (0.6F + layer * 0.2F);
            float layerAlpha = alpha * (1.0F - layer * 0.3F);
            float pulse = 0.8F + (float) Math.sin(animationProgress * Math.PI * 2 * 2) * 0.2F;

            poseStack.pushPose();
            poseStack.scale(layerRadius, layerRadius, layerRadius);
            poseStack.mulPose(Axis.XP.rotationDegrees(animationProgress * 50F));
            poseStack.mulPose(Axis.YP.rotationDegrees(animationProgress * 30F));

            Matrix4f matrix = poseStack.last().pose();
            Matrix3f normalMatrix = poseStack.last().normal();

            // Рисуем сферу через треугольники
            drawSphere(consumer, matrix, normalMatrix, 1.0F, layerAlpha * pulse);

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private void renderBeamsAndParticles(HungerOfHadar entity, float partialTicks, PoseStack poseStack,
                                         MultiBufferSource bufferSource, float radius, float entityScale) {
        if (radius <= 0) return;

        poseStack.pushPose();

        Vec3 center = entity.getCenter();
        if (center != null) {
            poseStack.translate(center.x - entity.getX(),
                    center.y - entity.getY(),
                    center.z - entity.getZ());
        }

        float animationProgress = ((float) entity.tickCount + partialTicks) / 200.0F;
        float fadeProgress = Math.min(1.0F, entity.tickCount / 40.0F);
        RandomSource randomSource = RandomSource.create(entity.getId() + entity.tickCount);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.energySwirl(BEAM_TEXTURE, 0.0F, 0.0F));

        // Количество лучей зависит от радиуса
        int beamCount = (int) (radius * 8);
        float segments = Math.min(animationProgress, 0.8F);

        for (int i = 0; i < beamCount; i++) {
            poseStack.pushPose();

            // Случайное направление луча
            float theta = randomSource.nextFloat() * (float) Math.PI * 2;
            float phi = (float) Math.acos(2.0F * randomSource.nextFloat() - 1.0F);

            Vec3 direction = new Vec3(
                    Math.sin(phi) * Math.cos(theta),
                    Math.sin(phi) * Math.sin(theta),
                    Math.cos(phi)
            );

            // Поворачиваем в направлении луча
            float yaw = (float) Math.toDegrees(Math.atan2(direction.z, direction.x));
            float pitch = (float) Math.toDegrees(Math.asin(direction.y));

            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

            // Длина луча зависит от радиуса
            float beamLength = radius * (0.5F + randomSource.nextFloat() * 0.8F);

            // Анимация пульсации
            float pulse = 0.5F + (float) Math.sin(animationProgress * Math.PI * 2 * 3 + i) * 0.3F;
            float beamAlpha = fadeProgress * 0.7F * (1.0F - segments * 0.5F) * pulse;

            // Рисуем луч
            drawBeam(vertexConsumer, poseStack.last().pose(), poseStack.last().normal(),
                    beamLength, beamAlpha);

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private void drawSphere(VertexConsumer consumer, Matrix4f poseMatrix, Matrix3f normalMatrix,
                            float radius, float alpha) {
        int segments = 24; // Увеличил для лучшей детализации
        int rings = 18;
        int alphaInt = (int) (alpha * 100); // Полупрозрачный

        for (int i = 0; i < rings; i++) {
            float phi1 = (float) Math.PI * i / rings;
            float phi2 = (float) Math.PI * (i + 1) / rings;

            for (int j = 0; j <= segments; j++) {
                float theta = (float) (2 * Math.PI * j / segments);

                // Первая точка
                float x1 = radius * (float) (Math.sin(phi1) * Math.cos(theta));
                float y1 = radius * (float) (Math.cos(phi1));
                float z1 = radius * (float) (Math.sin(phi1) * Math.sin(theta));

                // Вторая точка
                float x2 = radius * (float) (Math.sin(phi2) * Math.cos(theta));
                float y2 = radius * (float) (Math.cos(phi2));
                float z2 = radius * (float) (Math.sin(phi2) * Math.sin(theta));

                // Третья точка (следующий сегмент)
                float thetaNext = (float) (2 * Math.PI * (j + 1) / segments);
                float x3 = radius * (float) (Math.sin(phi1) * Math.cos(thetaNext));
                float y3 = radius * (float) (Math.cos(phi1));
                float z3 = radius * (float) (Math.sin(phi1) * Math.sin(thetaNext));

                float x4 = radius * (float) (Math.sin(phi2) * Math.cos(thetaNext));
                float y4 = radius * (float) (Math.cos(phi2));
                float z4 = radius * (float) (Math.sin(phi2) * Math.sin(thetaNext));

                // Рисуем два треугольника для квадрата
                // Треугольник 1
                consumer.vertex(poseMatrix, x1, y1, z1)
                        .color(150, 0, 255, alphaInt)
                        .uv(0, 0)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(x1, y1, z1)
                        .endVertex();
                consumer.vertex(poseMatrix, x2, y2, z2)
                        .color(150, 0, 255, alphaInt)
                        .uv(0, 1)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(x2, y2, z2)
                        .endVertex();
                consumer.vertex(poseMatrix, x3, y3, z3)
                        .color(150, 0, 255, alphaInt)
                        .uv(1, 0)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(x3, y3, z3)
                        .endVertex();

                // Треугольник 2
                consumer.vertex(poseMatrix, x2, y2, z2)
                        .color(150, 0, 255, alphaInt)
                        .uv(0, 1)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(x2, y2, z2)
                        .endVertex();
                consumer.vertex(poseMatrix, x4, y4, z4)
                        .color(150, 0, 255, alphaInt)
                        .uv(1, 1)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(x4, y4, z4)
                        .endVertex();
                consumer.vertex(poseMatrix, x3, y3, z3)
                        .color(150, 0, 255, alphaInt)
                        .uv(1, 0)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(15728880)
                        .normal(x3, y3, z3)
                        .endVertex();
            }
        }
    }

    private void drawBeam(VertexConsumer consumer, Matrix4f poseMatrix, Matrix3f normalMatrix,
                          float length, float alpha) {
        float width = 0.1F;
        int alphaInt = (int) (alpha * 255);

        // Используем более яркие цвета для лучей
        int r = 150;
        int g = 50;
        int b = 255;

        // Рисуем четырехугольник для луча
        consumer.vertex(poseMatrix, -width, 0, 0)
                .color(r, g, b, alphaInt)
                .uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(0, 1, 0)
                .endVertex();
        consumer.vertex(poseMatrix, width, 0, 0)
                .color(r, g, b, alphaInt)
                .uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(0, 1, 0)
                .endVertex();
        consumer.vertex(poseMatrix, width, length, 0)
                .color(r, g, b, alphaInt / 2)
                .uv(1, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(0, 1, 0)
                .endVertex();
        consumer.vertex(poseMatrix, -width, length, 0)
                .color(r, g, b, alphaInt / 2)
                .uv(0, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(0, 1, 0)
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(HungerOfHadar hungerOfHadar) {
        return IcicleRenderer.TEXTURE;
    }
}