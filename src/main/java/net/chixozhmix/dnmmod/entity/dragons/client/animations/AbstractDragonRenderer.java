//package net.chixozhmix.dnmmod.entity.dragons.client.renderer;
//
//import net.chixozhmix.chilib.utils.entity.geckolib.GeckoUtils;
//import net.chixozhmix.dnmmod.DnMmod;
//import net.chixozhmix.dnmmod.entity.dragons.AbstractDragonEntity;
//import net.chixozhmix.dnmmod.entity.dragons.client.models.AbstractDragonModel;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.client.renderer.entity.MobRenderer;
//import net.minecraft.resources.ResourceLocation;
//
//public class AbstractDragonRenderer extends MobRenderer<AbstractDragonEntity, AbstractDragonModel<AbstractDragonEntity>> {
//    private static final ResourceLocation TEXTURE = GeckoUtils.textureLocationFolder(DnMmod.MOD_ID, "abstract_dragon");
//
//    public AbstractDragonRenderer(EntityRendererProvider.Context pContext) {
//        super(pContext, new AbstractDragonModel<>(pContext.bakeLayer(AbstractDragonModel.LAYER_LOCATION)), 3f);
//    }
//
//    @Override
//    public ResourceLocation getTextureLocation(AbstractDragonEntity pEntity) {
//        return TEXTURE;
//    }
//}
