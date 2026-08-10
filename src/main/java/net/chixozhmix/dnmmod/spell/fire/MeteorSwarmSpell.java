package net.chixozhmix.dnmmod.spell.fire;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.ParticleSpawnHelper;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.api.spell.DnMSpellAnimations;
import net.chixozhmix.dnmmod.entity.spell.meteor.MeteorEntity;
import net.chixozhmix.dnmmod.events.SpellTickHelper;
import net.chixozhmix.dnmmod.particle.ParticleDirection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoSpellConfig
public class MeteorSwarmSpell extends AbstractSpell {
    private static ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "meteor_swarm");

    public MeteorSwarmSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 3;
        this.castTime = 200;
        this.baseManaCost = 400;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setMaxLevel(1)
            .setSchoolResource(SchoolRegistry.FIRE_RESOURCE)
            .setCooldownSeconds(300)
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
    public AnimationHolder getCastStartAnimation() {
        return DnMSpellAnimations.METEOR_SWARM_START;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return DnMSpellAnimations.METEOR_SWARM_END;
    }

    @Override
    public boolean allowCrafting() {
        return false;
    }

    @Override
    public boolean allowLooting() {
        return false;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 2)}),
                Component.translatable("ui.irons_spellbooks.radius", new Object[]{this.getRadius()}));
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        ParticleSpawnHelper.spawnParticlesCelindr(level, entity, 6, ParticleHelper.FIRE, ParticleDirection.INWARD, (double)3.0F, (double)2.0F, (double)2.0F);
        if(playerMagicData != null && playerMagicData.getCastDurationRemaining() <= 180)
            SpellUtils.applyHovering(entity, 2.0F, 0.2, 0.3, true);

        super.onServerCastTick(level, spellLevel, entity, playerMagicData);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 eyePos = entity.getEyePosition();
        Vec3 look = entity.getLookAngle();

        HitResult hit = level.clip(new ClipContext(
                eyePos,
                eyePos.add(look.scale(64)),
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                entity
        ));

        Vec3 targetPos;

        if (hit.getType() == HitResult.Type.MISS) {
            targetPos = eyePos.add(look.scale(64));
        } else {
            targetPos = hit.getLocation();
        }

        for (int i = 0; i < 4; i++) {

            final int index = i;

            SpellTickHelper.runLater(level, index * 20, () -> {

                RandomSource random = level.random;

                double angle = random.nextDouble() * Math.PI * 2;
                double radius = Math.sqrt(random.nextDouble()) * 20;

                double offsetX = Math.cos(angle) * radius;
                double offsetZ = Math.sin(angle) * radius;

                double spawnAngle = random.nextDouble() * Math.PI * 2;
                double spawnRadius = 30 + random.nextDouble() * 20;

                Vec3 impactPos = targetPos.add(offsetX, 0, offsetZ);

                Vec3 spawnPos = impactPos.add(
                        Math.cos(spawnAngle) * spawnRadius,
                        45,
                        Math.sin(spawnAngle) * spawnRadius
                );

                MeteorEntity meteor = new MeteorEntity(level, entity);
                meteor.setDamage(getDamage(spellLevel, entity));
                meteor.setExplosionRadius(getRadius());

                meteor.setPos(spawnPos);

                Vec3 direction = impactPos.subtract(spawnPos).normalize();

                meteor.shoot(
                        direction.x,
                        direction.y,
                        direction.z,
                        meteor.getSpeed(),
                        0
                );

                level.addFreshEntity(meteor);
            });
        }

        CameraShakeManager.addCameraShake(new CameraShakeData(50, entity.position(), 15.0F));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return 10.0F + 5.0F * this.getSpellPower(spellLevel, caster);
    }

    public int getRadius() {
        return 20;
    }
}
