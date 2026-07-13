package net.chixozhmix.dnmmod.events;

import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.blocks.client.SealedDoorModel;
import net.chixozhmix.dnmmod.blocks.client.SealedDoorRenderer;
import net.chixozhmix.dnmmod.blocks.client.statue.WarriorStatueRenderer;
import net.chixozhmix.dnmmod.blocks.client.statue.WizardStatueRenderer;
import net.chixozhmix.dnmmod.entity.custom.client.UndeadSpiritRenderer;
import net.chixozhmix.dnmmod.entity.darkspawn_larva.DarkspawnLarvaRenderer;
import net.chixozhmix.dnmmod.entity.darkspawn_larva.summon.SummonDarkspawnLarvaRenderer;
import net.chixozhmix.dnmmod.entity.defiled_priest.DefiledPriestrenderer;
import net.chixozhmix.dnmmod.entity.defiled_wizard.DefiledWizardRenderer;
import net.chixozhmix.dnmmod.entity.evil_flame_atronach.EvilFlameAtronachRenderer;
import net.chixozhmix.dnmmod.entity.evil_storm_atronach.EvilStormAtronachRenderer;
import net.chixozhmix.dnmmod.entity.flame_atronach.FlameAtronachrenderer;
import net.chixozhmix.dnmmod.entity.ghost.GhostRenderer;
import net.chixozhmix.dnmmod.entity.goblin_shaman.GoblinShamanRenderer;
import net.chixozhmix.dnmmod.entity.goblin_warior.GoblinWariorRenderer;
import net.chixozhmix.dnmmod.entity.greemon.GreemonRenderer;
import net.chixozhmix.dnmmod.entity.green_hag.GreenHagRenderer;
import net.chixozhmix.dnmmod.entity.leshy.LeshyRenderer;
import net.chixozhmix.dnmmod.entity.modeus.ModeusRenderer;
import net.chixozhmix.dnmmod.entity.raven.RavenModel;
import net.chixozhmix.dnmmod.entity.raven.RavenRenderer;
import net.chixozhmix.dnmmod.entity.reaper.ReaperRenderer;
import net.chixozhmix.dnmmod.entity.small_ice_spider.SmallIceSpiderRenderer;
import net.chixozhmix.dnmmod.entity.spell.acid_projectile.AcidProjectileRenderer;
import net.chixozhmix.dnmmod.entity.spell.chromatic_orb.ChromaticOrbRenderer;
import net.chixozhmix.dnmmod.entity.spell.cloud_dagger.CloudDaggerRenderer;
import net.chixozhmix.dnmmod.entity.spell.contagion_ray.ContagionRayRenderer;
import net.chixozhmix.dnmmod.entity.spell.hunger_of_hadar.HungerOfHadarRenderer;
import net.chixozhmix.dnmmod.entity.spell.ice_dagger.IceDaggerRenderer;
import net.chixozhmix.dnmmod.entity.spell.ray_of_enfeeblement.RayOfEnfeeblementRenderer;
import net.chixozhmix.dnmmod.entity.spell.red_cristall.RedCristallModel;
import net.chixozhmix.dnmmod.entity.spell.red_cristall.RedCristallRenderer;
import net.chixozhmix.dnmmod.entity.spell.tall_the_dead.TallTheDeadRenderer;
import net.chixozhmix.dnmmod.entity.spell.tombstone.TombstoneRenderer;
import net.chixozhmix.dnmmod.entity.spell.trident_strike_area.TridentStrikeAreaRenderer;
import net.chixozhmix.dnmmod.entity.storm_atronach.StormAtronachRenderer;
import net.chixozhmix.dnmmod.entity.summoned.client.SummonedRavenModel;
import net.chixozhmix.dnmmod.entity.summoned.client.SummonedRavenRenderer;
import net.chixozhmix.dnmmod.entity.summoned.client.SummonedUndeadSpiritRenderer;
import net.chixozhmix.dnmmod.entity.tainted_observer.DarkspawnObserverRenderer;
import net.chixozhmix.dnmmod.items.client.arrows.IceArrowRenderer;
import net.chixozhmix.dnmmod.particle.RavenParticle;
import net.chixozhmix.dnmmod.particle.ShieldParticle;
import net.chixozhmix.dnmmod.registers.*;
import net.chixozhmix.dnmmod.screen.component_bag.ComponentBagScreen;
import net.chixozhmix.dnmmod.screen.medium_bag.MediumBagScreen;
import net.chixozhmix.dnmmod.screen.scroll_table.ScrollTableScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = DnMmod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void registresParticleFactory(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.SHIELD_PARTICLES.get(), ShieldParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.RAVEN_PARTICLES.get(), RavenParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            CuriosRendererRegistry.register(ModItems.MAGICAL_GRIMOIRE.get(), SpellBookCurioRenderer::new);

            BlockEntityRenderers.register(ModBlockEntities.DOOR_OF_SEAL.get(), SealedDoorRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.WARRIOR_STATUE_BLOCK.get(), WarriorStatueRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.WIZARD_STATUE_BLOCK.get(), WizardStatueRenderer::new);
        });

        EntityRenderers.register(ModEntityType.MAGIC_DAGGER.get(), CloudDaggerRenderer::new);
        EntityRenderers.register(ModEntityType.ICE_DAGGER.get(), IceDaggerRenderer::new);
        EntityRenderers.register(ModEntityType.ACID_PROJECTILE.get(), AcidProjectileRenderer::new);
        EntityRenderers.register(ModEntityType.CHROMATIC_ORB.get(), ChromaticOrbRenderer::new);
        EntityRenderers.register(ModEntityType.RAY_OF_ENFEEBLEMENT.get(), RayOfEnfeeblementRenderer::new);
        EntityRenderers.register(ModEntityType.CONTAGION_RAY.get(), ContagionRayRenderer::new);
        EntityRenderers.register(ModEntityType.TALL_THE_DEAD.get(), TallTheDeadRenderer::new);
        EntityRenderers.register(ModEntityType.HUNGER_OF_HADADR.get(), HungerOfHadarRenderer::new);
        EntityRenderers.register(ModEntityType.TOMBSTONE.get(), TombstoneRenderer::new);
        EntityRenderers.register(ModEntityType.RED_CRYSTAL.get(), RedCristallRenderer::new);
        EntityRenderers.register(ModEntityType.TRIDENT_STRIKE_AREA.get(), TridentStrikeAreaRenderer::new);

        EntityRenderers.register(ModEntityType.ICE_ARROW_ENTITY.get(), IceArrowRenderer::new);

        EntityRenderers.register(ModEntityType.UNDEAD_SPIRIT.get(), UndeadSpiritRenderer::new);
        EntityRenderers.register(ModEntityType.SUMMONED_UNDEAD_SPIRIT.get(), SummonedUndeadSpiritRenderer::new);
        EntityRenderers.register(ModEntityType.GOBLIN_SHAMAN.get(), GoblinShamanRenderer::new);
        EntityRenderers.register(ModEntityType.GREEN_HAG.get(), GreenHagRenderer::new);
        EntityRenderers.register(ModEntityType.RAVEN.get(), RavenRenderer::new);
        EntityRenderers.register(ModEntityType.SUMMON_RAVEN.get(), SummonedRavenRenderer::new);
        EntityRenderers.register(ModEntityType.LESHY.get(), LeshyRenderer::new);
        EntityRenderers.register(ModEntityType.GHOST.get(), GhostRenderer::new);
        EntityRenderers.register(ModEntityType.GREEMON.get(), GreemonRenderer::new);
        EntityRenderers.register(ModEntityType.GOBLIN_WARRIOR.get(), GoblinWariorRenderer::new);
        EntityRenderers.register(ModEntityType.FLAME_ATRONACH.get(), FlameAtronachrenderer::new);
        EntityRenderers.register(ModEntityType.STORM_ATRONACH.get(), StormAtronachRenderer::new);
        EntityRenderers.register(ModEntityType.EVIL_STORM_ATRONACH.get(), EvilStormAtronachRenderer::new);
        EntityRenderers.register(ModEntityType.EVIL_FLAME_ATRONACH.get(), EvilFlameAtronachRenderer::new);
        EntityRenderers.register(ModEntityType.REAPER.get(), ReaperRenderer::new);
        EntityRenderers.register(ModEntityType.SMALL_ICE_SPIDER.get(), SmallIceSpiderRenderer::new);
        EntityRenderers.register(ModEntityType.DEFILED_WIZARD.get(), DefiledWizardRenderer::new);
        EntityRenderers.register(ModEntityType.DEFILED_PRIEST.get(), DefiledPriestrenderer::new);
        EntityRenderers.register(ModEntityType.MODEUS.get(), ModeusRenderer::new);
        EntityRenderers.register(ModEntityType.DARKSPAWN_LARVA.get(), DarkspawnLarvaRenderer::new);
        EntityRenderers.register(ModEntityType.SUMMON_DARKSPAWN_LARVA.get(), SummonDarkspawnLarvaRenderer::new);
        EntityRenderers.register(ModEntityType.DARKSPAWN_OBSERVER.get(), DarkspawnObserverRenderer::new);

        MenuScreens.register(ModMenuTypes.COMPONENT_BAG_MENU.get(), ComponentBagScreen::new);
        MenuScreens.register(ModMenuTypes.MEDIUM_COMPONENT_BAG_MENU.get(), MediumBagScreen::new);
        MenuScreens.register(ModMenuTypes.SCROLL_TABLE_MENU.get(), ScrollTableScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(IceDaggerRenderer.MODEL_LAYER_LOCATION, IceDaggerRenderer::createBodyLayer);
        event.registerLayerDefinition(AcidProjectileRenderer.MODEL_LAYER_LOCATION, AcidProjectileRenderer::createBodyLayer);
        event.registerLayerDefinition(ChromaticOrbRenderer.MODEL_LAYER_LOCATION, ChromaticOrbRenderer::createBodyLayer);
        event.registerLayerDefinition(RavenRenderer.MODEL_LAYER_LOCATION, RavenModel::createBodyLayer);
        event.registerLayerDefinition(SummonedRavenRenderer.MODEL_LAYER_LOCATION, SummonedRavenModel::createBodyLayer);
        event.registerLayerDefinition(RayOfEnfeeblementRenderer.MODEL_LAYER_LOCATION, RayOfEnfeeblementRenderer::createBodyLayer);
        event.registerLayerDefinition(RedCristallModel.LAYER_LOCATION, RedCristallModel::createBodyLayer);

        event.registerLayerDefinition(SealedDoorModel.LAYER_LOCATION, SealedDoorModel::createBodyLayer);
    }

}
