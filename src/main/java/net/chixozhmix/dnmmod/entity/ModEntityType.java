package net.chixozhmix.dnmmod.entity;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.custom.UndeadSpiritEntity;
import net.chixozhmix.dnmmod.entity.goblin_shaman.GoblinShamanEntity;
import net.chixozhmix.dnmmod.entity.green_hag.GreenHagEntity;
import net.chixozhmix.dnmmod.entity.projectiles.custom.FIrebolt;
import net.chixozhmix.dnmmod.entity.spell.acid_projectile.AcidProjectile;
import net.chixozhmix.dnmmod.entity.spell.chromatic_orb.ChromaticOrb;
import net.chixozhmix.dnmmod.entity.spell.cloud_dagger.CloudDagger;
import net.chixozhmix.dnmmod.entity.spell.ice_dagger.IceDagger;
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

    public static final RegistryObject<EntityType<FIrebolt>> FIREBALT =
            ENTITY_TYPES.register("fire_balt",
                    () -> EntityType.Builder.<FIrebolt>of(FIrebolt::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("fire_balt"));

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
                    .sized(1.0f, 1.6f)
                    .build("undead_spirit"));

    public static final RegistryObject<EntityType<GoblinShamanEntity>> GOBLIN_SHAMAN =
            ENTITY_TYPES.register("goblin_shaman", () ->
                    EntityType.Builder.of(GoblinShamanEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 0.8f)
                            .build("goblin_shaman"));

    public static final RegistryObject<EntityType<GreenHagEntity>> GREEN_HAG =
            ENTITY_TYPES.register("green_hag", () ->
                    EntityType.Builder.of(GreenHagEntity::new, MobCategory.MONSTER)
                            .sized(1.0f, 2.3f)
                            .build("green_hag"));

    //SummonedMobs
    public static final RegistryObject<EntityType<SummonedUndeadSpirit>> SUMMONED_UNDEAD_SPIRIT =
            ENTITY_TYPES.register("summoned_undead_spirit", () ->
                    EntityType.Builder.<SummonedUndeadSpirit>of(SummonedUndeadSpirit::new, MobCategory.MONSTER)
                    .sized(1.0f, 1.6f)
                    .clientTrackingRange(64)
                    .build("summoned_undead_spirit"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
