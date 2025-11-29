package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.custom.ArcherArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ArcherArmorModel extends DefaultedItemGeoModel<ArcherArmor> {

    public ArcherArmorModel() {
        super(new ResourceLocation(DnMmod.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getAnimationResource(ArcherArmor animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "animations/archer_armor.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(ArcherArmor animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/archer_armor_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ArcherArmor animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/armor/archer_armor.png");
    }
}
