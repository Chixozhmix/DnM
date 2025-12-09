package net.chixozhmix.dnmmod.entity;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.custom.UndeadSpiritEntity;
import net.chixozhmix.dnmmod.entity.ghost.GhostEntity;
import net.chixozhmix.dnmmod.entity.goblin_shaman.GoblinShamanEntity;
import net.chixozhmix.dnmmod.entity.goblin_warior.GoblinWariorEntity;
import net.chixozhmix.dnmmod.entity.greemon.GreemonEntity;
import net.chixozhmix.dnmmod.entity.green_hag.GreenHagEntity;
import net.chixozhmix.dnmmod.entity.leshy.LeshyEntity;
import net.chixozhmix.dnmmod.entity.raven.RavenEntity;
import net.chixozhmix.dnmmod.entity.spell.acid_projectile.AcidProjectile;
import net.chixozhmix.dnmmod.entity.spell.chromatic_orb.ChromaticOrb;
import net.chixozhmix.dnmmod.entity.spell.cloud_dagger.CloudDagger;
import net.chixozhmix.dnmmod.entity.spell.ice_dagger.IceDagger;
import net.chixozhmix.dnmmod.entity.summoned.SummonedRavenEntity;
import net.chixozhmix.dnmmod.entity.summoned.SummonedUndeadSpirit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DnMmod.MOD_ID);

    public static final RegistryObject<EntityType<CloudDagger>> MAGIC_DAGGER =
            ENTITY_TYPES.register("magic_dagger",
                    () -> EntityType.Builder.<CloudDagger>of(CloudDagger::new, MobCategory.MISC)
                            .sized(2f, 2f)
                            .clientTrackingRange(32)
                            .build("magic_dagger"));

    public static final RegistryObject<EntityType<IceDagger>> ICE_DAGGER =
            ENTITY_TYPES.register("ice_dagger",
                    () -> EntityType.Builder.<IceDagger>of(IceDagger::new, MobCategory.MISC)
                            .sized(2.0f, 2.0f)
                            .clientTrackingRange(64)
                            .build("ice_dagger"));

    public static final RegistryObject<EntityType<AcidProjectile>> ACID_PROJECTILE =
            ENTITY_TYPES.register("acid_projectile",
                    () -> EntityType.Builder.<AcidProjectile>of(AcidProjectile::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .clientTrackingRange(32)
                            .build("acid_projectile")
                    );

    public static final RegistryObject<EntityType<ChromaticOrb>> CHROMATIC_ORB =
            ENTITY_TYPES.register("chromatic_orb",
                    () -> EntityType.Builder.<ChromaticOrb>of(ChromaticOrb::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .clientTrackingRange(32)
                            .build("chromatic_orb")
            );

    //Mobs
    public static final RegistryObject<EntityType<UndeadSpiritEntity>> UNDEAD_SPIRIT =
            ENTITY_TYPES.register("undead_spirit", () -> EntityType.Builder.of(UndeadSpiritEntity::new, MobCategory.MONSTER)
                    .clientTrackingRange(8)
                    .sized(1.0f, 1.6f)
                    .build("undead_spirit"));
    public static final RegistryObject<EntityType<GoblinShamanEntity>> GOBLIN_SHAMAN =
            ENTITY_TYPES.register("goblin_shaman", () ->
                    EntityType.Builder.of(GoblinShamanEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(10)
                            .sized(0.6f, 0.8f)
                            .build("goblin_shaman"));
    public static final RegistryObject<EntityType<GoblinWariorEntity>> GOBLIN_WARRIOR =
            ENTITY_TYPES.register("goblin_warrior", () ->
                    EntityType.Builder.of(GoblinWariorEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(16)
                            .sized(0.6f, 0.6f)
                            .build("goblin_warrior"));
    public static final RegistryObject<EntityType<GreenHagEntity>> GREEN_HAG =
            ENTITY_TYPES.register("green_hag", () ->
                    EntityType.Builder.of(GreenHagEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(16)
                            .sized(1.0f, 2.3f)
                            .build("green_hag"));
    public static final RegistryObject<EntityType<RavenEntity>> RAVEN =
            ENTITY_TYPES.register("raven", () ->
                    EntityType.Builder.of(RavenEntity::new, MobCategory.CREATURE)
                            .clientTrackingRange(8)
                            .sized(0.375f, 0.5f)
                            .build("raven"));
    public static final RegistryObject<EntityType<LeshyEntity>> LESHY =
            ENTITY_TYPES.register("leshy", () ->
                    EntityType.Builder.of(LeshyEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(16)
                            .sized(1.2f, 2.7f)
                            .build("leshy"));
    public static final RegistryObject<EntityType<GhostEntity>> GHOST =
            ENTITY_TYPES.register("ghost", () ->
                    EntityType.Builder.of(GhostEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(10)
                            .sized(0.8f, 2.2f)
                            .build("ghost"));
    public static final RegistryObject<EntityType<GreemonEntity>> GREEMON =
            ENTITY_TYPES.register("greemon", () ->
                    EntityType.Builder.of(GreemonEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(10)
                            .sized(1.3f, 2.2f)
                            .build("greemon"));

    //SummonedMobs
    public static final RegistryObject<EntityType<SummonedUndeadSpirit>> SUMMONED_UNDEAD_SPIRIT =
            ENTITY_TYPES.register("summoned_undead_spirit", () ->
                    EntityType.Builder.<SummonedUndeadSpirit>of(SummonedUndeadSpirit::new, MobCategory.MONSTER)
                    .sized(1.0f, 1.6f)
                    .clientTrackingRange(32)
                    .build("summoned_undead_spirit"));
    public static final RegistryObject<EntityType<SummonedRavenEntity>> SUMMON_RAVEN =
            ENTITY_TYPES.register("summon_raven", () ->
                    EntityType.Builder.<SummonedRavenEntity>of(SummonedRavenEntity::new, MobCategory.MONSTER)
                            .clientTrackingRange(32)
                            .sized(0.375f, 0.5f)
                            .build("summon_raven"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
