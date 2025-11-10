package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.custom.MaidDress;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MaidDressModel extends GeoModel<MaidDress> {
    @Override
    public ResourceLocation getModelResource(MaidDress maidDress) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/maid_dress_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MaidDress maidDress) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/armor/maid_dress.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MaidDress maidDress) {
        return null;
    }
}
