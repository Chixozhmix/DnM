package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.custom.TaintedStaff;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TaintedStaffModel extends GeoModel<TaintedStaff> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/item/tainted_staff.png");
    public static final ResourceLocation ANIM = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "animations/archer_armor.animation.json");
    public static final ResourceLocation MODEl = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "geo/tainted_staff_geo.json");

    @Override
    public ResourceLocation getModelResource(TaintedStaff taintedStaff) {
        return MODEl;
    }

    @Override
    public ResourceLocation getTextureResource(TaintedStaff taintedStaff) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(TaintedStaff taintedStaff) {
        return ANIM;
    }
}
