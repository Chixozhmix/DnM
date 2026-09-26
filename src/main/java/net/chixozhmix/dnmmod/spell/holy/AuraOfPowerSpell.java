package net.chixozhmix.dnmmod.spell.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.chilib.particles.ParticleDirection;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.ParticleSpawnHelper;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.entity.spell.auras.PowerAuraEntity;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoSpellConfig
public class AuraOfPowerSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "aura_power");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMinRarity(SpellRarity.UNCOMMON)
            .setCooldownSeconds(95)
            .setMaxLevel(5)
            .build();

    public AuraOfPowerSpell() {
        this.baseManaCost = 70;
        this.baseSpellPower = 2;
        this.manaCostPerLevel = 10;
        this.spellPowerPerLevel = 1;
        this.castTime = 25;
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
        return List.of(Component.translatable("ui.irons_spellbooks.radius", new Object[]{Utils.stringTruncation(this.getRadius(caster, spellLevel), 1)}),
                Component.translatable("ui.irons_spellbooks.duration", new Object[]{Utils.timeFromTicks(this.getDuration(spellLevel), 1)}));
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        ParticleSpawnHelper.spawnParticlesCelindr(level, entity, 6, ParticleHelper.WISP, ParticleDirection.INWARD, 2.0F, 1.0F, 0.0F);
        super.onServerCastTick(level, spellLevel, entity, playerMagicData);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        PowerAuraEntity powerAuraEntity = new PowerAuraEntity(ModEntityType.POWER_AURA.get(), level);
        SpellUtils.addAura(powerAuraEntity, level, this.getRadius(entity, spellLevel), this.getDuration(spellLevel), entity);

        MagicManager.spawnParticles(level, ParticleHelper.WISP, entity.getX(), entity.getY() + 1, entity.getZ(), 30, 0.5, 0.5, 0.5, 0.4, true);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getRadius(LivingEntity caster, int spellLevel) {
        return 5 + (this.getSpellPower(spellLevel, caster) * 2);
    }

    private int getDuration(int spellLevel) {
        return 300 + ((spellLevel + 2) * 20);
    }
}
