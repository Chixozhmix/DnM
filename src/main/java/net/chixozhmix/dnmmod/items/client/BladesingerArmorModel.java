package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.custom.BladesingerArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class BladesingerArmorModel extends DefaultedItemGeoModel<BladesingerArmor> {
    public BladesingerArmorModel() {
        super(new ResourceLocation(DnMmod.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getAnimationResource(BladesingerArmor animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "animations/archer_armor.animation.json");
    }

    @Override
    public ResourceLocation getTextureResource(BladesingerArmor animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/armor/bladesinger_armor.png");
    }

    @Override
    public ResourceLocation getModelResource(BladesingerArmor animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/bladesinger_armor_geo.json");
    }
}
