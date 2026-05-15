package net.chixozhmix.dnmmod.entity.defiled_priest;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DefiledPriestModel extends DefaultedEntityGeoModel<DefiledPriest> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/entity/defiled_priest/defiled_priest.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "geo/defiled_priest_geo.json");
    public static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "animations/defiled_priest_animation.json");

    public DefiledPriestModel() {
        super(DnMmod.id("spellcastingmob"));
    }

    @Override
    public ResourceLocation getAnimationResource(DefiledPriest animatable) {
        return ANIMATION;
    }

    @Override
    public ResourceLocation getModelResource(DefiledPriest animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(DefiledPriest animatable) {
        return TEXTURE;
    }
}
