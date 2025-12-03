package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.custom.ShadowArcher;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ShadowArcherModel extends DefaultedItemGeoModel<ShadowArcher> {
    public ShadowArcherModel() {
        super(new ResourceLocation(DnMmod.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(ShadowArcher animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/shadow_archer_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShadowArcher animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/armor/shadow_archer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShadowArcher animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "animations/archer_armor.animation.json");
    }
}
