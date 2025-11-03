package net.chixozhmix.dnmmod.entity.greemon;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.ghost.GhostEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GreemonModel extends GeoModel<GreemonEntity> {
    @Override
    public ResourceLocation getModelResource(GreemonEntity greemonEntity) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/greemon_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GreemonEntity greemonEntity) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/entity/greemon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GreemonEntity greemonEntity) {
        return new ResourceLocation(DnMmod.MOD_ID, "animations/greemon_animation.json");
    }

    @Override
    public void setCustomAnimations(GreemonEntity animatable, long instanceId, AnimationState<GreemonEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if( head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
