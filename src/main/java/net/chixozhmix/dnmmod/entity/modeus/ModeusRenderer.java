package net.chixozhmix.dnmmod.entity.modeus;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.chixozhmix.chilib.client.danger_zone.DangerZoneRenderLayer;
import net.chixozhmix.chilib.utils.entity.beamAttacker.BeamAttackRenderer;
import net.chixozhmix.chilib.utils.entity.geckolib.GeckoEmissiveRendererHelper;
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

public class ModeusRenderer extends GeoEntityRenderer<ModeusBoss> {
    private static final ResourceLocation EMISSIVE_TEXTURE =
            new ResourceLocation(DnMmod.MOD_ID, "textures/entity/modeus/modeus_glowing.png");
    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(BEAM_TEXTURE);

    public ModeusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModeusModel());
        this.addRenderLayer(new DangerZoneRenderLayer<>(this));
    }

    @Override
    public void preRender(PoseStack poseStack, ModeusBoss animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, ModeusBoss animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        GeckoEmissiveRendererHelper.renderEmissiveLayer(this, animatable, poseStack, model, renderType, bufferSource, isReRender, partialTick,
                packedOverlay, red, green, blue, alpha, EMISSIVE_TEXTURE);
    }

    @Override
    public void render(ModeusBoss entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        BeamAttackRenderer.render(entity, poseStack, bufferSource, partialTick);
    }

    @Override
    public RenderType getRenderType(ModeusBoss animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    protected float getDeathMaxRotation(ModeusBoss animatable) {
        return 0.0F;
    }
}
