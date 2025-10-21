package net.chixozhmix.dnmmod.spell.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class PuritySpell extends AbstractSpell {

    private static ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "purity");

    public PuritySpell() {
        this.manaCostPerLevel = 15;
        this.baseManaCost = 130;
        this.baseSpellPower = 1;
        this.castTime = 30;
        this.spellPowerPerLevel = 1;
    }

    private static final List<MobEffect> NEGATIVE_EFFECTS = List.of(
            MobEffects.POISON,
            MobEffects.WITHER,
            MobEffects.WEAKNESS,
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.DIG_SLOWDOWN,
            MobEffects.BLINDNESS,
            MobEffects.HUNGER,
            MobEffects.UNLUCK,
            MobEffects.BAD_OMEN,
            MobEffects.CONFUSION,
            MobEffects.LEVITATION,
            MobEffects.GLOWING
    );

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(1)
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.BEACON_ACTIVATE);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.BEACON_DEACTIVATE);
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.SELF_CAST_TWO_HANDS;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        // Снимаем все негативные эффекты
        int effectsRemoved = removeNegativeEffects(entity);

        // Визуальные и звуковые эффекты
        if (effectsRemoved > 0) {
            // Воспроизводим звук очищения
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.BEACON_POWER_SELECT, entity.getSoundSource(), 1.0f, 1.0f);
        }
    }

    private int removeNegativeEffects(LivingEntity entity) {
        int effectsRemoved = 0;
        List<MobEffect> toRemove = new ArrayList<>();

        // Проверяем все активные эффекты
        for (MobEffectInstance effectInstance : entity.getActiveEffects()) {
            MobEffect effect = effectInstance.getEffect();

            // Проверяем, является ли эффект негативным
            if (isNegativeEffect(effect)) {
                toRemove.add(effect);
            }
        }

        // Удаляем эффекты
        for (MobEffect effect : toRemove) {
            entity.removeEffect(effect);
            effectsRemoved++;
        }

        return effectsRemoved;
    }

    /**
     * Проверяет, является ли эффект негативным
     */
    private boolean isNegativeEffect(MobEffect effect) {
        // Проверяем по списку известных негативных эффектов
        if (NEGATIVE_EFFECTS.contains(effect)) {
            return true;
        }

        // Дополнительная проверка: эффекты, которые не являются полезными
        return !effect.isBeneficial();
    }
}
