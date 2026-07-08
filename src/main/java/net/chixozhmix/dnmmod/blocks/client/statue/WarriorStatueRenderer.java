package net.chixozhmix.dnmmod.blocks.client.statue;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.redspace.ironslib.statue.block.statue_block.AbstractStatueBlock;
import io.redspace.ironslib.statue.block.statue_block.StaticModel;
import io.redspace.ironslib.statue.block.statue_block.decorative.DecorativeStatueBlockEntity;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public class WarriorStatueRenderer implements BlockEntityRenderer<DecorativeStatueBlockEntity> {
    StaticModel model;

    public WarriorStatueRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new StaticModel(DnMmod.id("geo/warrior_statue_geo.json"), DnMmod.id("textures/block/statues/warrior_statue.png"));

    }

    @Override
    public void render(DecorativeStatueBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (!pBlockEntity.isPrimary()) {
            return;
        }
        if (!(pBlockEntity.getBlockState().getBlock() instanceof AbstractStatueBlock statue)) {
            return;
        }
        pPoseStack.pushPose();
        pPoseStack.translate(statue.xSize * .5f, 0, statue.zSize * 0.5f);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-RotationSegment.convertToDegrees(pBlockEntity.getBlockState().getValue(SkullBlock.ROTATION))));
        //pPoseStack.scale(1.7f, 1.7f, 1.7f);
        this.model.render(pPoseStack, RenderType::entityCutoutNoCull, pBuffer, pPackedLight, pPackedOverlay);
        pPoseStack.popPose();
    }
}
