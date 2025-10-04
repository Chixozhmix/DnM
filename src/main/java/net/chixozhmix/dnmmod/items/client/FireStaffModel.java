package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.custom.FireStaff;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FireStaffModel extends GeoModel<FireStaff> {
    @Override
    public ResourceLocation getModelResource(FireStaff fireStaff) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/fire_staff_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FireStaff fireStaff) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/item/fire_staff.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FireStaff fireStaff) {
        return new ResourceLocation(DnMmod.MOD_ID, "animations/fire_staff_animation.json");
    }
}
