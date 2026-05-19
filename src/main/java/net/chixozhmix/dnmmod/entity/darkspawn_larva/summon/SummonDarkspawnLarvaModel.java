package net.chixozhmix.dnmmod.entity.darkspawn_larva.summon;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SummonDarkspawnLarvaModel extends DefaultedEntityGeoModel<SummonDarkspawnLarva> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/entity/darkspawn_larva/darkspawn_larva.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "geo/darkspawn_larva_geo.json");
    public static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "animations/darkspawn_larva_animation.json");

    public SummonDarkspawnLarvaModel() {
        super(DnMmod.id("spellcastingmob"));
    }

    @Override
    public ResourceLocation getModelResource(SummonDarkspawnLarva animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getAnimationResource(SummonDarkspawnLarva animatable) {
        return ANIMATION;
    }

    @Override
    public ResourceLocation getTextureResource(SummonDarkspawnLarva animatable) {
        return TEXTURE;
    }

    @Override
    public void setCustomAnimations(SummonDarkspawnLarva animatable, long instanceId, AnimationState<SummonDarkspawnLarva> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if( head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
