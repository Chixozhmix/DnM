package net.chixozhmix.dnmmod.entity.tainted_observer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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

        LivingEntity target = entity.getActiveAttackTarget();
        if (target != null) {
            float animationScale = entity.getAttackAnimationScale(partialTick);
            float attackTime = (float)entity.clientSideAttackTime + partialTick;
            float textureShift = attackTime * 0.5F % 1.0F;
            float eyeHeight = entity.getEyeHeight();

            poseStack.pushPose();
            poseStack.translate(0.0F, eyeHeight, 0.0F);

            Vec3 targetPos = getPosition(target, target.getBbHeight() * 0.5, partialTick);
            Vec3 entityPos = getPosition(entity, eyeHeight, partialTick);
            Vec3 beamVec = targetPos.subtract(entityPos);

            float beamLength = (float)(beamVec.length() + 1.0);
            beamVec = beamVec.normalize();

            float pitch = (float)Math.acos(beamVec.y);
            float yaw = (float)Math.atan2(beamVec.z, beamVec.x);

            poseStack.mulPose(Axis.YP.rotationDegrees((((float)Math.PI / 2F) - yaw) * (180F / (float)Math.PI)));
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch * (180F / (float)Math.PI)));

            // Параметры анимации
            float waveAngle = attackTime * 0.05F * -1.5F;
            float scaleSqr = animationScale * animationScale;

            // Цвет луча (фиолетовый)
            int red = 64 + (int)(scaleSqr * 191.0F);
            int green = 32 + (int)(scaleSqr * 191.0F);
            int blue = 128 - (int)(scaleSqr * 64.0F);

            // Вершины внешней оболочки луча
            float outerWidth1 = 0.2F;
            float outerWidth2 = 0.282F;
            float innerWidth1 = 0.282F;
            float innerWidth2 = 0.282F;

            float cos1 = Mth.cos(waveAngle + 2.3561945F) * innerWidth1;
            float sin1 = Mth.sin(waveAngle + 2.3561945F) * innerWidth1;
            float cos2 = Mth.cos(waveAngle + ((float)Math.PI / 4F)) * innerWidth1;
            float sin2 = Mth.sin(waveAngle + ((float)Math.PI / 4F)) * innerWidth1;
            float cos3 = Mth.cos(waveAngle + 3.926991F) * innerWidth2;
            float sin3 = Mth.sin(waveAngle + 3.926991F) * innerWidth2;
            float cos4 = Mth.cos(waveAngle + 5.4977875F) * innerWidth2;
            float sin4 = Mth.sin(waveAngle + 5.4977875F) * innerWidth2;

            float outerCos1 = Mth.cos(waveAngle + (float)Math.PI) * outerWidth1;
            float outerSin1 = Mth.sin(waveAngle + (float)Math.PI) * outerWidth1;
            float outerCos2 = Mth.cos(waveAngle + 0.0F) * outerWidth1;
            float outerSin2 = Mth.sin(waveAngle + 0.0F) * outerWidth1;
            float outerCos3 = Mth.cos(waveAngle + ((float)Math.PI / 2F)) * outerWidth1;
            float outerSin3 = Mth.sin(waveAngle + ((float)Math.PI / 2F)) * outerWidth1;
            float outerCos4 = Mth.cos(waveAngle + ((float)Math.PI * 1.5F)) * outerWidth1;
            float outerSin4 = Mth.sin(waveAngle + ((float)Math.PI * 1.5F)) * outerWidth1;

            float uvStart = -1.0F + textureShift;
            float uvEnd = beamLength * 2.5F + uvStart;

            VertexConsumer vertexConsumer = bufferSource.getBuffer(BEAM_RENDER_TYPE);
            PoseStack.Pose pose = poseStack.last();
            Matrix4f matrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();

            // Внешняя оболочка (4 стороны)
            vertex(vertexConsumer, matrix, normalMatrix, outerCos1, beamLength, outerSin1, red, green, blue, 0.4999F, uvEnd);
            vertex(vertexConsumer, matrix, normalMatrix, outerCos1, 0.0F, outerSin1, red, green, blue, 0.4999F, uvStart);
            vertex(vertexConsumer, matrix, normalMatrix, outerCos2, 0.0F, outerSin2, red, green, blue, 0.0F, uvStart);
            vertex(vertexConsumer, matrix, normalMatrix, outerCos2, beamLength, outerSin2, red, green, blue, 0.0F, uvEnd);

            vertex(vertexConsumer, matrix, normalMatrix, outerCos3, beamLength, outerSin3, red, green, blue, 0.4999F, uvEnd);
            vertex(vertexConsumer, matrix, normalMatrix, outerCos3, 0.0F, outerSin3, red, green, blue, 0.4999F, uvStart);
            vertex(vertexConsumer, matrix, normalMatrix, outerCos4, 0.0F, outerSin4, red, green, blue, 0.0F, uvStart);
            vertex(vertexConsumer, matrix, normalMatrix, outerCos4, beamLength, outerSin4, red, green, blue, 0.0F, uvEnd);

            // Внутренняя часть луча (для объёма)
            float innerUvShift = 0.0F;
            if (entity.tickCount % 2 == 0) {
                innerUvShift = 0.5F;
            }

            vertex(vertexConsumer, matrix, normalMatrix, cos1, beamLength, sin1, red, green, blue, 0.5F, innerUvShift + 0.5F);
            vertex(vertexConsumer, matrix, normalMatrix, cos2, beamLength, sin2, red, green, blue, 1.0F, innerUvShift + 0.5F);
            vertex(vertexConsumer, matrix, normalMatrix, cos4, beamLength, sin4, red, green, blue, 1.0F, innerUvShift);
            vertex(vertexConsumer, matrix, normalMatrix, cos3, beamLength, sin3, red, green, blue, 0.5F, innerUvShift);

            poseStack.popPose();
        }
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
