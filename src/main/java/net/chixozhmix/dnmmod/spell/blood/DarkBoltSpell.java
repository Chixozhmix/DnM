package net.chixozhmix.dnmmod.spell.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.entity.spell.dark_bolt.DarkBoltProjectile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class DarkBoltSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(DnMmod.MOD_ID, "dark_bolt");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setMaxLevel(10)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setCooldownSeconds(2)
            .build();

    public DarkBoltSpell () {
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 10;
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

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 2)}));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        DarkBoltProjectile darkBolt = new DarkBoltProjectile(level, entity);
        SpellUtils.shootProjectile(darkBolt, level, entity, this.getDamage(spellLevel, entity));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) / 2;
    }

    @Override
    public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setLifestealPercent(0.05f);
    }
}
