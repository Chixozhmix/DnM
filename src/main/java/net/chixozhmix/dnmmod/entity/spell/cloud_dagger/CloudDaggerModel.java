package net.chixozhmix.dnmmod.entity.spell.cloud_dagger;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CloudDaggerModel extends GeoModel<CloudDagger> {
    @Override
    public ResourceLocation getModelResource(CloudDagger cloudDagger) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/magic_dagger_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CloudDagger cloudDagger) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/entity/magic_dagger.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CloudDagger cloudDagger) {
        return new ResourceLocation(DnMmod.MOD_ID, "animations/magic_dagger_animation.json");
    }
}
