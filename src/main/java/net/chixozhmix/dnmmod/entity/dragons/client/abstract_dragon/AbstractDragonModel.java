package net.chixozhmix.dnmmod.entity.dragons.client.abstract_dragon;

import net.chixozhmix.chilib.utils.entity.geckolib.GeckoUtils;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.dragons.AbstractDragonEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AbstractDragonModel extends DefaultedEntityGeoModel<AbstractDragonEntity> {
    private static final ResourceLocation TEXTURE = GeckoUtils.textureLocationFolder(DnMmod.MOD_ID, "abstract_dragon");
    private static final ResourceLocation MODEL = GeckoUtils.geoLocation(DnMmod.MOD_ID, "abstract_dragon.geo");
    private static final ResourceLocation ANIMATIONS = GeckoUtils.animLocation(DnMmod.MOD_ID, "abstract_dragon.animation");

    public AbstractDragonModel() {
        super(DnMmod.id("abstractdragon"));
    }

    @Override
    public ResourceLocation getModelResource(AbstractDragonEntity abstractDragonEntity) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AbstractDragonEntity abstractDragonEntity) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractDragonEntity abstractDragonEntity) {
        return ANIMATIONS;
    }
}
