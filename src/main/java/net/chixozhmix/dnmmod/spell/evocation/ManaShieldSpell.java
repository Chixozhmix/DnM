package net.chixozhmix.dnmmod.spell.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.SwirlingParticleOptions;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ManaShieldSpell extends AbstractSpell {

    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "mana_shield");

    public ManaShieldSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 200;
        this.spellPowerPerLevel = 30;
        this.castTime = 20;
        this.baseManaCost = 55;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(60)
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
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.SELF_CAST_TWO_HANDS;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.effect_length", new Object[]{Utils.timeFromTicks(this.getSpellPower(spellLevel, caster), 1)}));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(ModEffects.MANA_SHIELD.get(), (int) this.getSpellPower(spellLevel, entity), 0));
        MagicManager.spawnParticles(level, ParticleTypes.ENCHANT, entity.getX(), entity.getY() + (double)1.0F, entity.getZ(), 50, 0.3, 0.2, 0.2, 0.3, false);
        MagicManager.spawnParticles(level, new SwirlingParticleOptions(ParticleTypes.PORTAL, new Vec3((double)0.0F, (double)1.0F, (double)0.0F), new Vec3((double)1.0F, (double)0.0F, (double)0.0F), new Vec3((double)0.8F, (double)0.8F, (double)14.0F), new Vec3(0.025, 0.025, -0.05)), entity.getX(), entity.getY() + (double)1.0F, entity.getZ(), 35, (double)0.0F, (double)0.5F, (double)0.0F, 0.01, false);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
