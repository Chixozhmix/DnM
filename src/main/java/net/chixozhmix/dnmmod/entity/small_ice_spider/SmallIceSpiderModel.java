package net.chixozhmix.dnmmod.entity.small_ice_spider;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SmallIceSpiderModel extends DefaultedEntityGeoModel<SmallIceSpiderEntity> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/entity/small_ice_spider.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "geo/small_ice_spider_geo.json");
    public static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "animations/small_ice_spider_animation.json");


    public SmallIceSpiderModel() {
        super(DnMmod.id("spellcastingmob"));
    }

    @Override
    public ResourceLocation getAnimationResource(SmallIceSpiderEntity animatable) {
        return ANIMATION;
    }

    @Override
    public ResourceLocation getModelResource(SmallIceSpiderEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SmallIceSpiderEntity animatable) {
        return TEXTURE;
    }
}
