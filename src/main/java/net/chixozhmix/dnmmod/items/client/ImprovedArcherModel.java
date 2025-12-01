package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.custom.ImprovedArcher;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ImprovedArcherModel extends DefaultedItemGeoModel<ImprovedArcher> {
    public ImprovedArcherModel() {
        super(new ResourceLocation(DnMmod.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getAnimationResource(ImprovedArcher animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "animations/archer_armor.animation.json");
    }

    @Override
    public ResourceLocation getTextureResource(ImprovedArcher animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/armor/improved_archer.png");
    }

    @Override
    public ResourceLocation getModelResource(ImprovedArcher animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/improved_archer_geo.json");
    }
}
