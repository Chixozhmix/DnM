package net.chixozhmix.dnmmod.entity.modeus;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ModeusModel extends DefaultedEntityGeoModel<ModeusBoss> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/entity/modeus/modeus_texture.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "geo/modeus_geo.json");
    public static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "animations/modeus_animation.json");


    public ModeusModel() {
        super(DnMmod.id("spellcastingmob"));
    }

    @Override
    public ResourceLocation getTextureResource(ModeusBoss animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(ModeusBoss animatable) {
        return ANIMATION;
    }

    @Override
    public ResourceLocation getModelResource(ModeusBoss animatable) {
        return MODEL;
    }
}
