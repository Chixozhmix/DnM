package net.chixozhmix.dnmmod.spell.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.entity.spell.auras.TurnUndeadAuraEntity;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class TurnUndeadAuraSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "turn_undead");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMinRarity(SpellRarity.COMMON)
            .setMaxLevel(10)
            .setCooldownSeconds(80)
            .build();

    public TurnUndeadAuraSpell() {
        this.baseManaCost = 50;
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
        return List.of(Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 1)}),
                Component.translatable("ui.irons_spellbooks.radius", new Object[]{Utils.stringTruncation(this.getRadius(caster, spellLevel), 1)}),
                Component.translatable("ui.irons_spellbooks.duration", new Object[]{Utils.timeFromTicks(this.getDuration(spellLevel), 1)}));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        TurnUndeadAuraEntity turnUndeadAuraEntity = new TurnUndeadAuraEntity(ModEntityType.TURN_UNDEAD_ENTITY.get(), level);
        SpellUtils.addAura(turnUndeadAuraEntity, level, this.getRadius(entity, spellLevel),
                this.getDuration(spellLevel), this.getDamage(spellLevel, entity), entity);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getRadius(LivingEntity caster, int spellLevel) {
        return 5 + (this.getSpellPower(spellLevel, caster) * 2);
    }

    private int getDuration(int spellLevel) {
        return 200 + (spellLevel + 2) * 2;
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return 4 + this.getSpellPower(spellLevel, caster);
    }
}
