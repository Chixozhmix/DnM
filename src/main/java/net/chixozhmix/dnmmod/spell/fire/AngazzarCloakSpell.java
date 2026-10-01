package net.chixozhmix.dnmmod.spell.fire;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.entity.spell.auras.AngazzarsAuraEntity;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class AngazzarCloakSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "angazzars_cloak");
    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setMaxLevel(10)
            .setSchoolResource(SchoolRegistry.FIRE_RESOURCE)
            .setCooldownSeconds(55)
            .build();

    public AngazzarCloakSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 5;
        this.castTime = 20;
        this.baseManaCost = 40;
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
        return List.of(Component.translatable("ui.irons_spellbooks.duration", new Object[]{Utils.timeFromTicks(this.getDuration(spellLevel, caster), 1)}),
                Component.translatable("ui.irons_spellbooks.radius", new Object[]{Utils.stringTruncation(4, 1)}));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        AngazzarsAuraEntity aura = new AngazzarsAuraEntity(ModEntityType.ANGAZZARS_AURA_ENTITY.get(), level);
        SpellUtils.addAura(aura, level, 4, this.getDuration(spellLevel, entity), entity);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getDuration(int spellLevel, LivingEntity entity) {
        return (int) (this.getSpellPower(spellLevel, entity) * 20);
    }
}
