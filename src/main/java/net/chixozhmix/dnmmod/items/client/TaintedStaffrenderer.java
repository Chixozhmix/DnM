package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.items.custom.TaintedStaff;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TaintedStaffrenderer extends GeoItemRenderer<TaintedStaff> {
    public TaintedStaffrenderer() {
        super(new TaintedStaffModel());
    }
}
