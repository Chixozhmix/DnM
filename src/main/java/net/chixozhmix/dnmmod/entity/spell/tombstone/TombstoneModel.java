package net.chixozhmix.dnmmod.entity.spell.tombstone;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TombstoneModel extends GeoModel<Tombstone> {
    @Override
    public ResourceLocation getModelResource(Tombstone tombstone) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/tombstone_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Tombstone tombstone) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/entity/tombstone.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Tombstone tombstone) {
        return new ResourceLocation(DnMmod.MOD_ID, "animations/archer_armor.animation.json");
    }
}
