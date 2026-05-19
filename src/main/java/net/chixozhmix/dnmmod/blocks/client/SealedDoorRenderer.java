package net.chixozhmix.dnmmod.blocks.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.blocks.custom.SealedDoorBlock;
import net.chixozhmix.dnmmod.blocks.entity.SealedDoorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SealedDoorRenderer implements BlockEntityRenderer<SealedDoorBlockEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DnMmod.MOD_ID, "textures/block/sealed_door.png");
    private static final ResourceLocation GLOWING =
            new ResourceLocation(DnMmod.MOD_ID, "textures/block/sealed_door_glowing.png");

    private final SealedDoorModel model;

    public SealedDoorRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SealedDoorModel(context.bakeLayer(SealedDoorModel.LAYER_LOCATION));
    }

    @Override
    public void render(SealedDoorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        // ★ Выходим сразу, если это не базовый блок ★
        if (!blockEntity.isBaseBlock()) return;

        poseStack.pushPose();

        // Смещаемся так, чтобы модель встала ровно по центру всей структуры
        // Центр нижнего ряда находится ровно в этом BlockEntity
        poseStack.translate(0.5D, 0.5D, 0.5D);

        Direction facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        switch (facing) {
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case WEST  -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            case EAST  -> poseStack.mulPose(Axis.YP.rotationDegrees(270));
            default    -> {} // NORTH
        }

        // Анимация
        this.model.left_door.yRot = 0.0F;
        this.model.right_door.yRot = 0.0F;

        if (blockEntity.getBlockState().getValue(SealedDoorBlock.LIT)) {
            float progress = Math.min(blockEntity.Animaitonticks / 145.0F, 1.0F);
            float angle = progress * 90.0F;
            this.model.left_door.yRot = -(float)Math.toRadians(angle);
            this.model.right_door.yRot = (float)Math.toRadians(angle);

            if (blockEntity.getBlockState().getValue(SealedDoorBlock.OPEN)) {
                this.model.left_door.yRot = -(float)Math.toRadians(90);
                this.model.right_door.yRot = (float)Math.toRadians(90);
            }
        }

        VertexConsumer normalConsumer = bufferSource.getBuffer(RenderType.entitySolid(TEXTURE));
        this.model.renderToBuffer(poseStack, normalConsumer, packedLight, packedOverlay, 1, 1, 1, 1);

        VertexConsumer emissiveConsumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(GLOWING));
        this.model.renderToBuffer(poseStack, emissiveConsumer, 15728880, packedOverlay, 1, 1, 1, 1);

        poseStack.popPose();
    }
}
