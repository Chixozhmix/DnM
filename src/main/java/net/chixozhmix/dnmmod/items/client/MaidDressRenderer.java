package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.items.custom.MaidDress;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MaidDressRenderer extends GeoArmorRenderer<MaidDress> {
    public MaidDressRenderer() {
        super(new MaidDressModel());
    }
}
