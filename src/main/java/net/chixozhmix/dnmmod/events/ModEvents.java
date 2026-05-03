package net.chixozhmix.dnmmod.events;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.spawnrules.ModSpawnRule;
import net.chixozhmix.dnmmod.entity.evil_flame_atronach.EvilFlameAtronach;
import net.chixozhmix.dnmmod.entity.evil_storm_atronach.EvilStormAtronach;
import net.chixozhmix.dnmmod.entity.reaper.ReaperEntity;
import net.chixozhmix.dnmmod.entity.small_ice_spider.SmallIceSpiderEntity;
import net.chixozhmix.dnmmod.entity.spell.tombstone.Tombstone;
import net.chixozhmix.dnmmod.entity.storm_atronach.StormAtronach;
import net.chixozhmix.dnmmod.particle.ShieldParticle;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.entity.custom.UndeadSpiritEntity;
import net.chixozhmix.dnmmod.entity.flame_atronach.FlameAtronachEntity;
import net.chixozhmix.dnmmod.entity.ghost.GhostEntity;
import net.chixozhmix.dnmmod.entity.goblin_shaman.GoblinShamanEntity;
import net.chixozhmix.dnmmod.entity.goblin_warior.GoblinWariorEntity;
import net.chixozhmix.dnmmod.entity.greemon.GreemonEntity;
import net.chixozhmix.dnmmod.entity.green_hag.GreenHagEntity;
import net.chixozhmix.dnmmod.entity.leshy.LeshyEntity;
import net.chixozhmix.dnmmod.entity.raven.RavenEntity;
import net.chixozhmix.dnmmod.entity.spell.cloud_dagger.CloudDagger;
import net.chixozhmix.dnmmod.entity.summoned.SummonedRavenEntity;
import net.chixozhmix.dnmmod.entity.summoned.SummonedUndeadSpirit;
import net.chixozhmix.dnmmod.registers.ParticleRegistry;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DnMmod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityType.MAGIC_DAGGER.get(), CloudDagger.createLivingAttributes().build());
        event.put(ModEntityType.TOMBSTONE.get(), Tombstone.createLivingAttributes().build());
        event.put(ModEntityType.UNDEAD_SPIRIT.get(), UndeadSpiritEntity.createAttributes());
        event.put(ModEntityType.SUMMONED_UNDEAD_SPIRIT.get(), SummonedUndeadSpirit.createAttributes());
        event.put(ModEntityType.GOBLIN_SHAMAN.get(), GoblinShamanEntity.prepareAttributes().build());
        event.put(ModEntityType.GREEN_HAG.get(), GreenHagEntity.prepareAttributes().build());
        event.put(ModEntityType.RAVEN.get(), RavenEntity.createAttributes());
        event.put(ModEntityType.SUMMON_RAVEN.get(), SummonedRavenEntity.createAttributes());
        event.put(ModEntityType.LESHY.get(), LeshyEntity.prepareAttributes().build());
        event.put(ModEntityType.GHOST.get(), GhostEntity.createAttributes());
        event.put(ModEntityType.GREEMON.get(), GreemonEntity.createAttributes());
        event.put(ModEntityType.GOBLIN_WARRIOR.get(), GoblinWariorEntity.createAttributes());
        event.put(ModEntityType.FLAME_ATRONACH.get(), FlameAtronachEntity.prepareAttributes().build());
        event.put(ModEntityType.STORM_ATRONACH.get(), StormAtronach.prepareAttributes().build());
        event.put(ModEntityType.EVIL_STORM_ATRONACH.get(), EvilStormAtronach.prepareAttributes().build());
        event.put(ModEntityType.EVIL_FLAME_ATRONACH.get(), EvilFlameAtronach.prepareAttributes().build());
        event.put(ModEntityType.REAPER.get(), ReaperEntity.prepareAttributes().build());
        event.put(ModEntityType.SMALL_ICE_SPIDER.get(), SmallIceSpiderEntity.prepareAttributes().build());
    }

    @SubscribeEvent
    public static void spawnEntityEvent(SpawnPlacementRegisterEvent event) {
        event.register(ModEntityType.GREEMON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntityType.GOBLIN_WARRIOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntityType.UNDEAD_SPIRIT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntityType.GHOST.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntityType.EVIL_STORM_ATRONACH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModSpawnRule::ThunderSpawnRule, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntityType.EVIL_FLAME_ATRONACH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntityType.SMALL_ICE_SPIDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModSpawnRule::CastingMonsterSpawnRule, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
    @SubscribeEvent
    public static void registresParticleFactory(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.SHIELD_PARTICLES.get(), ShieldParticle.Provider::new);
    }
}
