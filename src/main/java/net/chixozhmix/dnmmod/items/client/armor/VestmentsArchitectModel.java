package net.chixozhmix.dnmmod.items.client.armor;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.custom.armor.VestmentsArchitectArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class VestmentsArchitectModel extends DefaultedItemGeoModel<VestmentsArchitectArmor> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/armor/vestmets_architect.png");
    public static final ResourceLocation ANIM = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "animations/archer_armor.animation.json");
    public static final ResourceLocation MODEl = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "geo/vestments_architect_armor_geo.json");

    public VestmentsArchitectModel() {
        super(new ResourceLocation(DnMmod.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getTextureResource(VestmentsArchitectArmor animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(VestmentsArchitectArmor animatable) {
        return ANIM;
    }

    @Override
    public ResourceLocation getModelResource(VestmentsArchitectArmor animatable) {
        return MODEl;
    }
}
