package net.chixozhmix.dnmmod.spell.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.SwirlingParticleOptions;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class FalseLifeSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "false_life");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setMinRarity(SpellRarity.UNCOMMON)
            .setMaxLevel(10)
            .setCooldownSeconds(75)
            .build();

    public FalseLifeSpell() {
        this.baseManaCost = 40;
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 5;
    }

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
        return CastType.INSTANT;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.SELF_CAST_TWO_HANDS;
    }

    //Добавить звук
    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return super.getCastFinishSound();
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.duration", new Object[]{Utils.timeFromTicks(getDuration(spellLevel, caster), 1)}));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        int count = 0;
        AABB area = entity.getBoundingBox().inflate(10);

        for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, area)) {
            if(livingEntity == entity || !livingEntity.isAlive()) continue;
            count++;
        }

        float missingHealth = entity.getMaxHealth() - entity.getHealth();

        if (count > 0) {
            entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, getDuration(spellLevel, entity), Math.min(10, count), false, false, false));
            entity.setHealth(entity.getMaxHealth() - missingHealth);
        } else {
            entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, getDuration(spellLevel, entity), 1, false, false, false));
            entity.setHealth(entity.getMaxHealth() - missingHealth);
        }

        MagicManager.spawnParticles(level, ParticleHelper.BLOOD_GROUND, entity.getX(), entity.getY() + (double)1.0F, entity.getZ(), 50, 0.2, 0.2, 0.2, 0.1, false);
        MagicManager.spawnParticles(level, new SwirlingParticleOptions(ParticleHelper.BLOOD, new Vec3((double)0.0F, (double)1.0F, (double)0.0F), new Vec3((double)1.0F, (double)0.0F, (double)0.0F), new Vec3((double)0.75F, (double)0.75F, (double)12.0F), new Vec3(0.025, 0.025, -0.05)), entity.getX(), entity.getY() + (double)1.0F, entity.getZ(), 35, (double)0.0F, (double)0.5F, (double)0.0F, 0.01, false);


        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getDuration(int spelLevel, LivingEntity caster) {
        return (int) (this.getSpellPower(spelLevel, caster) * 20);
    }
}
