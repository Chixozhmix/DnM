package net.chixozhmix.dnmmod.items.client;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.custom.armor.VoileOfDeathArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class VoileOfDeathModel extends DefaultedItemGeoModel<VoileOfDeathArmor> {
    public VoileOfDeathModel() {
        super(new ResourceLocation(DnMmod.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getAnimationResource(VoileOfDeathArmor animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "animations/archer_armor.animation.json");
    }

    @Override
    public ResourceLocation getTextureResource(VoileOfDeathArmor animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/armor/voile_of_death.png");
    }

    @Override
    public ResourceLocation getModelResource(VoileOfDeathArmor animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/voile_of_death_geo.json");
    }
}
