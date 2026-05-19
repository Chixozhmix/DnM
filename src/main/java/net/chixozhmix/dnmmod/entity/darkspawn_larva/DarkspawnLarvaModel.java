package net.chixozhmix.dnmmod.entity.darkspawn_larva;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DarkspawnLarvaModel extends DefaultedEntityGeoModel<DarkspawnLarva> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/entity/darkspawn_larva/darkspawn_larva.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "geo/darkspawn_larva_geo.json");
    public static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "animations/darkspawn_larva_animation.json");

    public DarkspawnLarvaModel() {
        super(DnMmod.id("spellcastingmob"));
    }

    @Override
    public ResourceLocation getModelResource(DarkspawnLarva animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getAnimationResource(DarkspawnLarva animatable) {
        return ANIMATION;
    }

    @Override
    public ResourceLocation getTextureResource(DarkspawnLarva animatable) {
        return TEXTURE;
    }
}
