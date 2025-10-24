package net.chixozhmix.dnmmod.entity.summoned.client;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.summoned.SummonedUndeadSpirit;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SummonedUndeadSpiritRenderer extends GeoEntityRenderer<SummonedUndeadSpirit> {
    public SummonedUndeadSpiritRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SummonedUndeadSpiritModel());
    }

    @Override
    public ResourceLocation getTextureLocation(SummonedUndeadSpirit animatable) {
        return new ResourceLocation(DnMmod.MOD_ID, "textures/entity/undead_spirit.png");
    }
}
