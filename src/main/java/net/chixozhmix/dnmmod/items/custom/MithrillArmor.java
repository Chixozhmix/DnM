package net.chixozhmix.dnmmod.items.custom;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import net.chixozhmix.dnmmod.items.UniqArmorMaterials;
import net.chixozhmix.dnmmod.items.client.MithrilArmorModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MithrillArmor extends ExtendedArmorItem {
    public MithrillArmor(Type type, Properties properties) {
        super(UniqArmorMaterials.MITHRILL_ARMOR, type, properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer(new MithrilArmorModel());
    }
}
