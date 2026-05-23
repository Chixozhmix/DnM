package net.chixozhmix.dnmmod.entity.spell.red_cristall;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RedCristallRenderer extends EntityRenderer<RedCristallEntity> {
    private final RedCristallModel model;

    public RedCristallRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new RedCristallModel(pContext.bakeLayer(RedCristallModel.LAYER_LOCATION));
    }

    @Override
    public void render(RedCristallEntity entity, float pEntityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (entity.tickCount >= entity.getWaitTime()) {
            float f = (float)entity.tickCount + partialTicks;
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
            float anim = entity.getPositionOffset(partialTicks);
            poseStack.scale(1.0F, -1.0F, 1.0F);
            float scale = entity.getCristallSize();
            scale = (scale - 1.0F) * 0.25F + 1.0F;
            poseStack.scale(scale, scale, scale);
            poseStack.translate(0.0F, -anim * 68.0F / 16.0F, 0.0F);
            this.model.setupAnim(entity, partialTicks, 0.0F, 0.0F, entity.getYRot(), entity.getXRot());
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));
            this.model.renderToBuffer(poseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(RedCristallEntity redCristallEntity) {
        return DnMmod.id("textures/entity/red_cristall.png");
    }
}
