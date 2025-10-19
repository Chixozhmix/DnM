package net.chixozhmix.dnmmod.entity.spell.ice_dagger;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class IceDaggerModel extends GeoModel<IceDagger> {
    @Override
    public ResourceLocation getModelResource(IceDagger iceDagger) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/ice_dagger_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(IceDagger iceDagger) {
        return null;
    }

    @Override
    public ResourceLocation getAnimationResource(IceDagger iceDagger) {
        return null;
    }
}
