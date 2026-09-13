package net.chixozhmix.dnmmod.entity.evil.hidden_tentacle;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HiddenTentacleModel extends GeoModel<HiddenTentacleEntity> {
    @Override
    public ResourceLocation getModelResource(HiddenTentacleEntity hiddenTentacleEntity) {
        return new ResourceLocation(DnMmod.MOD_ID, "geo/hidden_tentacle_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HiddenTentacleEntity hiddenTentacleEntity) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/entity/hidden_tentacle/hidden_tentacle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HiddenTentacleEntity hiddenTentacleEntity) {
        return new ResourceLocation(DnMmod.MOD_ID, "animations/hidden_tentacle_animation.json");
    }
}
