package net.chixozhmix.dnmmod.entity.tainted_observer;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class DarkspawnObserverModel extends GeoModel<DarkspawnObserver> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(DnMmod.MOD_ID, "textures/entity/tainted_observer/tainted_observer.png");
    private static final ResourceLocation ANIM = new ResourceLocation(DnMmod.MOD_ID, "animations/tainted_observer_animation.json");
    private static final ResourceLocation MODEL = new ResourceLocation(DnMmod.MOD_ID, "geo/tainted_observer_geo.json");

    @Override
    public ResourceLocation getModelResource(DarkspawnObserver darkspawnObserver) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(DarkspawnObserver darkspawnObserver) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(DarkspawnObserver darkspawnObserver) {
        return ANIM;
    }

    @Override
    public void setCustomAnimations(DarkspawnObserver animatable, long instanceId, AnimationState<DarkspawnObserver> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if( head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
