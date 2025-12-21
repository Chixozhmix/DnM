package net.chixozhmix.dnmmod.spell.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.api.registers.DnMSchools;
import net.chixozhmix.dnmmod.effect.ModEffects;
import net.chixozhmix.dnmmod.entity.summoned.SummonedUndeadSpirit;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class SummonUndeadSpiritSpell extends AbstractSpell {

    private static ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "summon_undead_spirit");

    public SummonUndeadSpiritSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 3;
        this.castTime = 30;
        this.baseManaCost = 60;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setMaxLevel(3)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
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
        return Optional.of(SoundsRegistry.SUMMON_UNDEAD_SPIRIT.get());
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.summon_count", new Object[]{spellLevel}),
                Component.translatable("ui.dnmmod.spell_component", new Object[]{SpellUtils.getComponentName(Items.SKELETON_SKULL)}));
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return SpellUtils.checkSpellComponent(entity, Items.SKELETON_SKULL);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        int summonTime = 12000;
        float radius = 1.5F + 0.185F * (float)spellLevel;

        for(int i = 0; i < spellLevel; ++i) {
            SummonedUndeadSpirit undead =   new SummonedUndeadSpirit(level, entity, true);
            undead.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(undead.getOnPos()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
            undead.addEffect(new MobEffectInstance(ModEffects.SUMMON_UNDEAD_SPIRIT.get(), summonTime, 0, false, false, false));
            float yrot = 6.281F / (float)spellLevel * (float)i + entity.getYRot() * ((float)Math.PI / 180F);
            Vec3 spawn = Utils.moveToRelativeGroundLevel(level, entity.getEyePosition().add(new Vec3((double)(radius * Mth.cos(yrot)), (double)0.0F, (double)(radius * Mth.sin(yrot)))), 10);
            undead.setPos(spawn.x, spawn.y, spawn.z);
            undead.setYRot(entity.getYRot());
            undead.setOldPosAndRot();
            level.addFreshEntity(undead);
        }

        int effectAmplifier = spellLevel - 1;
        if (entity.hasEffect((MobEffect)ModEffects.SUMMON_UNDEAD_SPIRIT.get())) {
            effectAmplifier += entity.getEffect((MobEffect)ModEffects.SUMMON_UNDEAD_SPIRIT.get()).getAmplifier() + 1;
        }

        entity.addEffect(new MobEffectInstance((MobEffect)ModEffects.SUMMON_UNDEAD_SPIRIT.get(), summonTime, effectAmplifier, false, false, true));

    }
}
