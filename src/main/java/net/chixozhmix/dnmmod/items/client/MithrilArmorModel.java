package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.custom.MithrillArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class MithrilArmorModel extends DefaultedItemGeoModel<MithrillArmor> {
    public MithrilArmorModel() {
        super(new ResourceLocation(DnMmod.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getAnimationResource(MithrillArmor animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "animations/archer_armor.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(MithrillArmor animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/mithrill_armor_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MithrillArmor animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/armor/mithril_armor.png");
    }
}
