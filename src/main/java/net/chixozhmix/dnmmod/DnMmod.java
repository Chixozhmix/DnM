package net.chixozhmix.dnmmod;

import com.mojang.logging.LogUtils;
import net.chixozhmix.dnmmod.blocks.ModBlocks;
import net.chixozhmix.dnmmod.blocks.entity.ModBlockEntity;
import net.chixozhmix.dnmmod.effect.ModEffects;
import net.chixozhmix.dnmmod.entity.ModEntityType;
import net.chixozhmix.dnmmod.entity.custom.client.UndeadSpiritRenderer;
import net.chixozhmix.dnmmod.entity.goblin_shaman.GoblinShamanRenderer;
import net.chixozhmix.dnmmod.entity.green_hag.GreenHagRenderer;
import net.chixozhmix.dnmmod.entity.spell.acid_projectile.AcidProjectileRenderer;
import net.chixozhmix.dnmmod.entity.spell.chromatic_orb.ChromaticOrbRenderer;
import net.chixozhmix.dnmmod.entity.spell.cloud_dagger.CloudDaggerRenderer;
import net.chixozhmix.dnmmod.entity.spell.ice_dagger.IceDaggerRenderer;
import net.chixozhmix.dnmmod.entity.summoned.client.SummonedUndeadSpiritRenderer;
import net.chixozhmix.dnmmod.items.CreativeTabMod;
import net.chixozhmix.dnmmod.items.ModItems;
import net.chixozhmix.dnmmod.screen.CokeOvenScreen;
import net.chixozhmix.dnmmod.screen.ModMenuTypes;
import net.chixozhmix.dnmmod.spell.RegistrySpells;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DnMmod.MOD_ID)
public class DnMmod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "dnmmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public DnMmod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        //Entity
        ModEntityType.register(modEventBus);
        ModBlockEntity.register(modEventBus);

        //Items
        ModItems.register(modEventBus);

        //Blocks
        ModBlocks.register(modEventBus);

        //Effects
        ModEffects.register(modEventBus);

        //Spells
        RegistrySpells.register(modEventBus);

        //CreativeTab
        CreativeTabMod.register(modEventBus);

        //Menus
        ModMenuTypes.register(modEventBus);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntityType.FIREBALT.get(), ThrownItemRenderer::new);
            EntityRenderers.register(ModEntityType.MAGIC_DAGGER.get(), CloudDaggerRenderer::new);
            EntityRenderers.register(ModEntityType.ICE_DAGGER.get(), IceDaggerRenderer::new);
            EntityRenderers.register(ModEntityType.ACID_PROJECTILE.get(), AcidProjectileRenderer::new);
            EntityRenderers.register(ModEntityType.CHROMATIC_ORB.get(), ChromaticOrbRenderer::new);

            EntityRenderers.register(ModEntityType.UNDEAD_SPIRIT.get(), UndeadSpiritRenderer::new);
            EntityRenderers.register(ModEntityType.SUMMONED_UNDEAD_SPIRIT.get(), SummonedUndeadSpiritRenderer::new);
            EntityRenderers.register(ModEntityType.GOBLIN_SHAMAN.get(), GoblinShamanRenderer::new);
            EntityRenderers.register(ModEntityType.GREEN_HAG.get(), GreenHagRenderer::new);

            MenuScreens.register(ModMenuTypes.COKE_OVEN_MENU.get(), CokeOvenScreen::new);
        }

        @SubscribeEvent
        public static void onRegisterLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(IceDaggerRenderer.MODEL_LAYER_LOCATION, IceDaggerRenderer::createBodyLayer);
            event.registerLayerDefinition(AcidProjectileRenderer.MODEL_LAYER_LOCATION, AcidProjectileRenderer::createBodyLayer);
            event.registerLayerDefinition(ChromaticOrbRenderer.MODEL_LAYER_LOCATION, ChromaticOrbRenderer::createBodyLayer);
        }
    }

    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation("dnmmod", path);
    }
}
