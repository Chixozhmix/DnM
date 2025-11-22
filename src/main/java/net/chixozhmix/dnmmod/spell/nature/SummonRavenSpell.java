package net.chixozhmix.dnmmod.spell.nature;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.effect.ModEffects;
import net.chixozhmix.dnmmod.entity.summoned.SummonedRavenEntity;
import net.chixozhmix.dnmmod.items.ModItems;
import net.chixozhmix.dnmmod.sound.SoundsRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class SummonRavenSpell extends AbstractSpell {

    private static ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "summon_ravens");

    public SummonRavenSpell() {

        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 3;
        this.castTime = 30;
        this.baseManaCost = 60;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setMaxLevel(10)
            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
            .setCooldownSeconds(120)
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
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundsRegistry.SUMMON_RAVEN.get());
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.summon_count", new Object[]{spellLevel}),
                Component.translatable("ui.dnmmod.spell_component", new Object[]{SpellUtils.getComponentName(ModItems.RAVEN_FEATHER.get())})
        );
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return SpellUtils.checkSpellComponent(entity, ModItems.RAVEN_FEATHER.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        int summonTime = 12000;
        float radius = 1.5F + 0.185F * (float)spellLevel;

        for(int i = 0; i < spellLevel; ++i) {
            SummonedRavenEntity raven = new SummonedRavenEntity(level, entity, true);
            raven.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(raven.getOnPos()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
            raven.addEffect(new MobEffectInstance(ModEffects.SUMMON_RAVEN.get(), summonTime, 0, false, false, false));
            float yrot = 6.281F / (float)spellLevel * (float)i + entity.getYRot() * ((float)Math.PI / 180F);
            Vec3 spawn = Utils.moveToRelativeGroundLevel(level, entity.getEyePosition().add(new Vec3((double)(radius * Mth.cos(yrot)), (double)0.0F, (double)(radius * Mth.sin(yrot)))), 10);
            raven.setPos(spawn.x, spawn.y + 3.0f, spawn.z);
            raven.setYRot(entity.getYRot());
            raven.setOldPosAndRot();
            level.addFreshEntity(raven);
        }

        int effectAmplifier = spellLevel - 1;
        if (entity.hasEffect((MobEffect)ModEffects.SUMMON_RAVEN.get())) {
            effectAmplifier += entity.getEffect((MobEffect)ModEffects.SUMMON_RAVEN.get()).getAmplifier() + 1;
        }

        entity.addEffect(new MobEffectInstance((MobEffect)ModEffects.SUMMON_RAVEN.get(), summonTime, effectAmplifier, false, false, true));
    }
}
