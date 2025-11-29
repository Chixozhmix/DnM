package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.items.custom.ArcherArmor;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArcherArmorRenderer extends GeoArmorRenderer<ArcherArmor> {
    public ArcherArmorRenderer() {
        super(new ArcherArmorModel());
    }
}
