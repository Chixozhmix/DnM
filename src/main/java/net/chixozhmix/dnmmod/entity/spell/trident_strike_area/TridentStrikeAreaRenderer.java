package net.chixozhmix.dnmmod.entity.spell.trident_strike_area;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TridentStrikeAreaRenderer extends EntityRenderer<TridentStrikeAreaEntity> {

    public TridentStrikeAreaRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(TridentStrikeAreaEntity entity) {
        return null;
    }

    @Override
    public void render(TridentStrikeAreaEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int light) {

        float length = entity.getLength();
        float width = entity.getWidth();
        Vector3f color = entity.getColor();
        Vec3 direction = entity.getBeamDirection();

        float alpha = 1.0f;
        if (entity.shouldFade() && entity.tickCount > entity.getDuration() - 10) {
            alpha = (entity.getDuration() - entity.tickCount) / 10.0f;
            alpha = Mth.clamp(alpha, 0, 1);
        }

        poseStack.pushPose();

        float angle = (float) Math.toDegrees(Math.atan2(direction.z, direction.x));
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(angle));

        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.LINES);

        float halfWidth = width / 2;

        addLine(consumer, poseMatrix, normalMatrix, 0, 0, 0, length, 0, 0, color, alpha, light);

        addLine(consumer, poseMatrix, normalMatrix, 0, 0, -halfWidth, length, 0, -halfWidth, color, alpha, light);

        addLine(consumer, poseMatrix, normalMatrix, 0, 0, halfWidth, length, 0, halfWidth, color, alpha, light);

        addLine(consumer, poseMatrix, normalMatrix, length, 0, -halfWidth, length, 0, halfWidth, color, alpha, light);

        addLine(consumer, poseMatrix, normalMatrix, 0, 0, -halfWidth, 0, 0, halfWidth, color, alpha, light);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, light);
    }

    private void addLine(VertexConsumer consumer, Matrix4f poseMatrix, Matrix3f normalMatrix,
                         float x1, float y1, float z1, float x2, float y2, float z2,
                         Vector3f color, float alpha, int light) {
        consumer.vertex(poseMatrix, x1, y1, z1)
                .color(color.x(), color.y(), color.z(), alpha)
                .normal(normalMatrix, 0, 1, 0)
                .endVertex();
        consumer.vertex(poseMatrix, x2, y2, z2)
                .color(color.x(), color.y(), color.z(), alpha)
                .normal(normalMatrix, 0, 1, 0)
                .endVertex();
    }
}