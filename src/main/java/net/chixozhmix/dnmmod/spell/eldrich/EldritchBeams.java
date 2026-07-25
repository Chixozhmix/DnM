package net.chixozhmix.dnmmod.spell.eldrich;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class EldritchBeams extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "eldritch_beams");

    public EldritchBeams() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
        this.baseManaCost = 60;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(200)
            .build();

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.CONTINUOUS;
    }

    @Override
    public boolean allowLooting() {
        return false;
    }

    @Override
    public boolean allowCrafting() {
        return false;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.WARDEN_SONIC_CHARGE);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.WARDEN_SONIC_BOOM);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_CONTINUOUS_OVERHEAD;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 1)}));

    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return super.checkPreCastConditions(level, spellLevel, entity, playerMagicData);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        if (level.isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) level;

        int beamCount = 8;
        float range = 25;
        float angleStep = 360f / beamCount;
        Vec3 beamStart = entity.position().add(0, entity.getBbHeight() / 2, 0);

        for (int i = 0; i < beamCount; i++) {
            double angle = Math.toRadians(angleStep * i);
            Vec3 beamDir = new Vec3(Math.cos(angle), 0, Math.sin(angle)).normalize();

            AABB checkArea = entity.getBoundingBox().inflate(range);
            for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, checkArea)) {
                if (target == entity || !target.isAlive()) continue;

                Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2, 0);
                Vec3 toTarget = targetPos.subtract(beamStart);
                double distanceAlongBeam = toTarget.dot(beamDir);

                if (distanceAlongBeam > 0 && distanceAlongBeam < range) {
                    Vec3 closestPointOnBeam = beamStart.add(beamDir.scale(distanceAlongBeam));
                    double distToBeam = targetPos.distanceTo(closestPointOnBeam);
                    if (distToBeam <= 2.0 + target.getBbWidth() / 2.0) {
                        SpellDamageSource source = this.getDamageSource(entity);
                        DamageSources.applyDamage(target, this.getDamage(spellLevel, entity), source);
                        DamageSources.ignoreNextKnockback(target);

                        serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT,
                                target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(),
                                8, 0.0, 0.0, 0.0, 0.0);
                    }
                }
            }
        }

        spawnBeamParticles(serverLevel, entity, beamCount, range);
    }

    private void spawnBeamParticles(ServerLevel serverLevel, LivingEntity entity, int beamCount, float range) {
        Vec3 startPos = entity.position().add(0, entity.getBbHeight() / 2, 0);
        float angleStep = 360f / beamCount;

        for (int i = 0; i < beamCount; i++) {
            double angle = Math.toRadians(angleStep * i);
            Vec3 beamDir = new Vec3(Math.cos(angle), 0, Math.sin(angle));

            for (double d = 0.5; d < range; d += 0.6) {
                Vec3 pos = startPos.add(beamDir.scale(d));

                serverLevel.sendParticles(
                        ParticleTypes.SONIC_BOOM,
                        pos.x, pos.y + 0.1, pos.z,
                        1,
                        0.0, 0.0, 0.0,
                        0.0
                );
            }
        }
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 1.7F;
    }
}
