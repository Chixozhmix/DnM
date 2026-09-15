package net.chixozhmix.dnmmod.spell.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.SpellConfigHandler;
import net.chixozhmix.dnmmod.entity.spell.chromatic_orb.ChromaticOrb;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

@AutoSpellConfig
public class ChromaticOrbSpell extends AbstractSpell {

    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "chromatic_orb");

    public ChromaticOrbSpell() {
        this.baseManaCost = 45;
        this.baseSpellPower = 5;
        this.castTime = 15;
        this.manaCostPerLevel = 10;
        this.spellPowerPerLevel = 1;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMaxLevel(10)
            .setCooldownSeconds(20)
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
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
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        List<MutableComponent> baseInfo = List.of(
                Component.translatable("ui.dnmmod.bounces", new Object[]{Utils.stringTruncation((double)this.getBounces(spellLevel), 1)}),
                Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 2)}));

        return SpellConfigHandler.modifyGetUniqueInfo(spellLevel, caster, baseInfo,
                this);
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if(!SpellConfigHandler.checkPreCastConditions(level, spellLevel, entity, playerMagicData,
                this))
            return false;

        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        ChromaticOrb orb = new ChromaticOrb(level, entity);
        Vec3 spawn = entity.getEyePosition().add(entity.getForward().normalize());
        orb.setPos(spawn);
        Vec3 direction = entity.getLookAngle().normalize();
        orb.setDeltaMovement(direction.multiply(orb.getSpeed(), orb.getSpeed(), orb.getSpeed()));
        orb.setDamage(getDamage(spellLevel, entity));
        orb.setBounces(getBounces(spellLevel));
        level.addFreshEntity(orb);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 1.3F;
    }

    private int getBounces(int spellLevel) {
        return 2 + (spellLevel / 3);
    }
}
