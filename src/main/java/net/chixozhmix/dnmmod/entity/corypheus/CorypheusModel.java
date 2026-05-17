package net.chixozhmix.dnmmod.entity.corypheus;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class CorypheusModel extends DefaultedEntityGeoModel<CorypheusBoss> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/entity/corypheus/corypheus_texture.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "geo/corypheus_geo.json");
    public static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "animations/corypheus_animation.json");


    public CorypheusModel() {
        super(DnMmod.id("spellcastingmob"));
    }

    @Override
    public ResourceLocation getTextureResource(CorypheusBoss animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(CorypheusBoss animatable) {
        return ANIMATION;
    }

    @Override
    public ResourceLocation getModelResource(CorypheusBoss animatable) {
        return MODEL;
    }
}
