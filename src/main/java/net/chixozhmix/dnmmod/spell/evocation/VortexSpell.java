package net.chixozhmix.dnmmod.spell.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.spell.vortex.VortexAOE;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class VortexSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(DnMmod.MOD_ID, "vortex");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMaxLevel(5)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setCooldownSeconds(65)
            .setMinRarity(SpellRarity.UNCOMMON)
            .build();

    public VortexSpell() {
        this.baseManaCost = 65;
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
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
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_CONTINUOUS_OVERHEAD;
    }

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.radius", new Object[]{Utils.stringTruncation((double)this.getRadius(spellLevel, caster), 1)}),
                Component.translatable("ui.irons_spellbooks.duration", new Object[]{Utils.timeFromTicks((float)this.getDurationTicks(spellLevel, caster), 1)}),
                Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation((float) this.getDamage(spellLevel, caster), 1)}));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        int duration = this.getDurationTicks(spellLevel, entity);
        float radius = this.getRadius(spellLevel, entity);
        VortexAOE aoe = new VortexAOE(ModEntityType.VORTEX_AOE.get(), level);
        aoe.moveTo(entity.position());
        aoe.setOwner(entity);
        aoe.setStrengthKnockback(getKnockbackStrength(spellLevel, entity));
        aoe.setRadius(radius);
        aoe.setDuration(duration);
        aoe.setDamage(this.getDamage(spellLevel, entity));
        level.addFreshEntity(aoe);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getKnockbackStrength(int spellLevel, LivingEntity entity) {
        return (this.getSpellPower(spellLevel, entity)) * 0.15f;
    }

    private float getDamage(int spelLevel, LivingEntity entity) {
        return 2 + this.getSpellPower(spelLevel, entity);
    }

    private float getRadius(int spellLevel, LivingEntity caster) {
        return 4.0F * spellLevel;
    }

    private int getDurationTicks(int spellLevel, LivingEntity caster) {
        return (int)(20.0F * (10.0F + 1.5F * (float)spellLevel));
    }

}
