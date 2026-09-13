package net.chixozhmix.dnmmod.spell.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class VortexSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(DnMmod.MOD_ID, "votrex");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMaxLevel(5)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setCooldownSeconds(75)
            .setMinRarity(SpellRarity.RARE)
            .build();

    public VortexSpell() {
        this.baseManaCost = 85;
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 2;
        this.castTime = 60;
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
        return CastType.LONG;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return super.getUniqueInfo(spellLevel, caster);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        //Создавать зону пылевого вихря, следующего за кастером и отталкивающего врагов

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getKnockbackStrength(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) + spellLevel;
    }

    private int getDurationTicks(int spellLevel, LivingEntity caster) {
        return (int)(20.0F * (10.0F + 1.5F * (float)spellLevel));
    }

}
