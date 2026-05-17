package net.chixozhmix.dnmmod.blocks.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SealedDoorModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "sealed_door"), "main");
    public final ModelPart center;
    public final ModelPart left_door;
    public final ModelPart right_door;

    public SealedDoorModel(ModelPart root) {
        this.center = root.getChild("center");
        this.left_door = this.center.getChild("left_door");
        this.right_door = this.center.getChild("right_door");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition center = partdefinition.addOrReplaceChild("center", CubeListBuilder.create(), PartPose.offset(0.0F, 56.0F, 0.0F));

        PartDefinition left_door = center.addOrReplaceChild("left_door", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -3.0F, 40.0F, 128.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(62, 134).addBox(23.9F, 21.0F, -3.9F, 12.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(118, 134).addBox(20.9F, 19.0F, -3.9F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(128, 134).addBox(20.9F, 27.0F, -3.9F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(90, 134).addBox(23.9F, 29.0F, -3.9F, 12.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-39.9F, -64.0F, 0.0F));

        PartDefinition seal_stone_r1 = left_door.addOrReplaceChild("seal_stone_r1", CubeListBuilder.create().texOffs(34, 134).addBox(-7.0F, -8.0F, -1.0F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 134).addBox(-8.0F, -9.0F, 0.1F, 14.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(42.0F, 27.0F, -5.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition right_door = center.addOrReplaceChild("right_door", CubeListBuilder.create().texOffs(92, 0)
                .addBox(-39.8F, 0.0F, -3.0F, 40.0F, 128.0F, 6.0F,
                        new CubeDeformation(0.0F)), PartPose.offset(39.9F, -64.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        center.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {

    }
}