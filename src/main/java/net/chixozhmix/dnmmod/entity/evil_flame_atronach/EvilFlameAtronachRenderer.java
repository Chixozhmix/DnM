package net.chixozhmix.dnmmod.entity.evil_flame_atronach;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.entity.GeckoEmissiveRendererHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class EvilFlameAtronachRenderer extends AbstractSpellCastingMobRenderer {
    private static final ResourceLocation EMISSIVE_TEXTURE =
            new ResourceLocation(DnMmod.MOD_ID, "textures/entity/flame_atronach/flame_atronach_fire.png");

    public EvilFlameAtronachRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EvilFlameAtronachModel());
    }

    @Override
    public void actuallyRender(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType,
                bufferSource, buffer, isReRender, partialTick,
                packedLight, packedOverlay, red, green, blue, alpha);

        GeckoEmissiveRendererHelper.renderEmissiveLayer(this, animatable, poseStack, model, renderType, bufferSource, isReRender, partialTick,
                packedOverlay, red, green, blue, alpha, EMISSIVE_TEXTURE);
    }

    @Override
    public RenderType getRenderType(AbstractSpellCastingMob animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
