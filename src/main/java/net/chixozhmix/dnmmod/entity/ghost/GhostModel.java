package net.chixozhmix.dnmmod.entity.ghost;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.custom.UndeadSpiritEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GhostModel extends GeoModel<GhostEntity> {
    @Override
    public ResourceLocation getModelResource(GhostEntity ghostEntity) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/ghost_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GhostEntity ghostEntity) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/entity/ghost.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GhostEntity ghostEntity) {
        return new ResourceLocation(DnMmod.MOD_ID, "animations/ghost_animation.json");
    }

    @Override
    public void setCustomAnimations(GhostEntity animatable, long instanceId, AnimationState<GhostEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if( head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
