package net.chixozhmix.dnmmod.items.custom;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ArmorCapeProvider;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import net.chixozhmix.dnmmod.items.UniqArmorMaterials;
import net.chixozhmix.dnmmod.items.client.ArcherArmorModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArcherArmor extends ExtendedArmorItem implements ArmorCapeProvider {
    public ArcherArmor(Type type, Properties settings) {
        super(UniqArmorMaterials.ARCHER, type, settings);
    }

    @Override
    public ResourceLocation getCapeResourceLocation() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer(new ArcherArmorModel());
    }
}
