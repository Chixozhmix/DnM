package net.chixozhmix.dnmmod.entity.dragons.client.models;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.dragons.AbstractDragonEntity;
import net.chixozhmix.dnmmod.entity.dragons.client.animations.AbstractDragonModelAnimation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class AbstractDragonModel<T extends Entity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DnMmod.MOD_ID, "abstractdragonmodel"), "main");
	private final ModelPart dragon;
	private final ModelPart body;
	private final ModelPart torso;
	private final ModelPart neck;
	private final ModelPart neck2;
	private final ModelPart head;
	private final ModelPart rLeg;
	private final ModelPart rFoot;
	private final ModelPart lLeg;
	private final ModelPart lFoot;
	private final ModelPart lWing;
	private final ModelPart rWing;
	private final ModelPart tail;
	private final ModelPart tail2;
	private final ModelPart tail3;

	public AbstractDragonModel(ModelPart root) {
		this.dragon = root.getChild("dragon");
		this.body = this.dragon.getChild("body");
		this.torso = this.body.getChild("torso");
		this.neck = this.torso.getChild("neck");
		this.neck2 = this.neck.getChild("neck2");
		this.head = this.neck2.getChild("head");
		this.rLeg = this.body.getChild("rLeg");
		this.rFoot = this.rLeg.getChild("rFoot");
		this.lLeg = this.body.getChild("lLeg");
		this.lFoot = this.lLeg.getChild("lFoot");
		this.lWing = this.body.getChild("lWing");
		this.rWing = this.body.getChild("rWing");
		this.tail = this.body.getChild("tail");
		this.tail2 = this.tail.getChild("tail2");
		this.tail3 = this.tail2.getChild("tail3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition dragon = partdefinition.addOrReplaceChild("dragon", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = dragon.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 70).addBox(-10.0F, -46.0F, -16.0F, 20.0F, 22.0F, 38.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition neck = torso.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(174, 164).addBox(-6.0F, -6.0F, -21.0F, 13.0F, 12.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -38.0F, -16.0F));

		PartDefinition neck2 = neck.addOrReplaceChild("neck2", CubeListBuilder.create().texOffs(0, 173).addBox(-6.0F, -6.0F, -25.0F, 13.0F, 12.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -21.0F));

		PartDefinition head = neck2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(90, 164).addBox(-8.0F, -8.0F, -25.0F, 17.0F, 16.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -25.0F));

		PartDefinition rLeg = body.addOrReplaceChild("rLeg", CubeListBuilder.create().texOffs(174, 198).addBox(-2.0F, -8.0F, -6.0F, 5.0F, 15.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(90, 130).addBox(-1.0F, 7.0F, -1.0F, 3.0F, 15.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -25.0F, 11.0F));

		PartDefinition rFoot = rLeg.addOrReplaceChild("rFoot", CubeListBuilder.create().texOffs(114, 205).addBox(-3.0F, -1.0F, -13.0F, 6.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, 5.0F));

		PartDefinition lLeg = body.addOrReplaceChild("lLeg", CubeListBuilder.create().texOffs(76, 205).addBox(-2.0F, -8.0F, -6.0F, 5.0F, 15.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(206, 19).addBox(-1.0F, 7.0F, -1.0F, 3.0F, 15.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -25.0F, 11.0F));

		PartDefinition lFoot = lLeg.addOrReplaceChild("lFoot", CubeListBuilder.create().texOffs(206, 0).addBox(-3.0F, -1.0F, -13.0F, 6.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, 5.0F));

		PartDefinition lWing = body.addOrReplaceChild("lWing", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -13.0F, 70.0F, 2.0F, 33.0F, new CubeDeformation(0.0F)), PartPose.offset(11.0F, -41.0F, 0.0F));

		PartDefinition rWing = body.addOrReplaceChild("rWing", CubeListBuilder.create().texOffs(0, 35).addBox(-69.0F, -2.0F, -13.0F, 70.0F, 2.0F, 33.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.0F, -41.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(116, 70).addBox(-7.0F, -8.0F, -1.0F, 15.0F, 13.0F, 37.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -37.0F, 23.0F));

		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 130).addBox(-6.0F, -6.0F, -1.0F, 13.0F, 11.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 37.0F));

		PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(116, 120).addBox(-5.0F, -5.0F, -1.0F, 11.0F, 9.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 32.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

        this.animateWalk(AbstractDragonModelAnimation.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((AbstractDragonEntity) entity).idle, AbstractDragonModelAnimation.idle, ageInTicks, 1f);
	}

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		dragon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return dragon;
    }
}