package net.chixozhmix.dnmmod.entity.spell.cloud_dagger;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CloudDaggerRenderer extends GeoEntityRenderer<CloudDagger> {
    public CloudDaggerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CloudDaggerModel());
    }

    @Override
    public boolean shouldShowName(CloudDagger animatable) {
        return false;
    }

    @Override
    public ResourceLocation getTextureLocation(CloudDagger animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/entity/magic_dagger.png");
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, CloudDagger animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        float scale = 0.5F;
        poseStack.scale(scale, scale, scale);
        this.entityRenderTranslations = new Matrix4f().setTranslation(0.5F,0.5F,0.5F);
    }
}
