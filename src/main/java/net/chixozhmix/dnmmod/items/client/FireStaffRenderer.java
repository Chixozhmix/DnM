package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.items.custom.FireStaff;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FireStaffRenderer extends GeoItemRenderer<FireStaff> {
    public FireStaffRenderer() {
        super(new FireStaffModel());
    }
}
