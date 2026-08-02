package net.chixozhmix.dnmmod.spell.eldrich;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.events.SpellTickHelper;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class PsychicScreamSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "psychic_scream");

    public PsychicScreamSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 1;
        this.castTime = 40;
        this.baseManaCost = 120;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(220)
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
        return CastType.LONG;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(this.getDamage(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(this.getRadius(spellLevel, caster), 2)),
                Component.translatable("ui.irons_spellbooks.effect_length", "10s")
        );
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.WARDEN_SONIC_CHARGE);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.SCULK_SHRIEKER_SHRIEK);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        if (level.isClientSide()) {
            return;
        }

        applySpellEffects(level, spellLevel, entity);
        spawnBlastwave(level, spellLevel, entity);
        spawnInwardSoulParticles(level, spellLevel, entity);

        addRunLater(level, spellLevel, entity, 5);
        addRunLater(level, spellLevel, entity, 10);
    }

    private void addRunLater(Level level, int spellLevel, LivingEntity entity, int delayTicks) {
        SpellTickHelper.runLater(level, delayTicks, () -> {
            if (entity.isAlive()) spawnBlastwave(level, spellLevel, entity);
        });
    }

    private void applySpellEffects(Level level, int spellLevel, LivingEntity entity) {
        AABB area = new AABB(entity.blockPosition()).inflate(getRadius(spellLevel, entity));
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);

        for (LivingEntity target : entities) {
            if (target == entity) continue;

            DamageSources.applyDamage(target, getDamage(spellLevel, entity), getDamageSource(entity));
            target.addEffect(new MobEffectInstance(ModEffects.MIND_CONTROL.get(), 200));
        }
    }

    private void spawnBlastwave(Level level, int spellLevel, LivingEntity entity) {
        MagicManager.spawnParticles(level,
                new BlastwaveParticleOptions(((SchoolType) SchoolRegistry.ELDRITCH.get()).getTargetingColor(), getRadius(spellLevel, entity)),
                entity.getX(), entity.getBoundingBox().getCenter().y, entity.getZ(),
                2, 0.0D, 0.0D, 0.0D, 0.0D, true);
    }

    private void spawnInwardSoulParticles(Level level, int spellLevel, LivingEntity caster) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        double radius = getRadius(spellLevel, caster);

        double cx = caster.getX();
        double cy = caster.getY() + caster.getBbHeight() * 0.5;
        double cz = caster.getZ();

        int particleCount = (int) (radius * 8);

        for (int i = 0; i < particleCount; i++) {
            double angle = Math.PI * 2 * i / particleCount;

            double x = cx + Math.cos(angle) * radius;
            double z = cz + Math.sin(angle) * radius;

            double dx = cx - x;
            double dz = cz - z;

            double length = Math.sqrt(dx * dx + dz * dz);

            dx /= length;
            dz /= length;

            serverLevel.sendParticles(ParticleTypes.SOUL, x, cy, z, 1, dx * 0.18, 0.02, dz * 0.18, 0);
        }
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 2.5F;
    }

    private float getRadius(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 2.0F;
    }
}