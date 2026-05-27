package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.custom.armor.WaningMoonArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class WaningMoonArmorModel extends DefaultedItemGeoModel<WaningMoonArmor> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "geo/waning_moon_armor_geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/armor/waning_moon_armor.png");
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "animations/archer_armor.animation.json");

    public WaningMoonArmorModel() {
        super(new ResourceLocation(DnMmod.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(WaningMoonArmor animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getAnimationResource(WaningMoonArmor animatable) {
        return ANIMATION;
    }

    @Override
    public ResourceLocation getTextureResource(WaningMoonArmor animatable) {
        return TEXTURE;
    }
}
