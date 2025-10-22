package net.chixozhmix.dnmmod.spell.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.spell.chromatic_orb.ChromaticOrb;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

@AutoSpellConfig
public class ChromaticOrbSpell extends AbstractSpell {

    private static ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "chromatic_orb");

    public ChromaticOrbSpell() {
        this.baseManaCost = 70;
        this.baseSpellPower = 6;
        this.castTime = 35;
        this.manaCostPerLevel = 10;
        this.spellPowerPerLevel = 1;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMaxLevel(10)
            .setCooldownSeconds(30)
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
        return List.of(
                Component.translatable("ui.dnmmod.bounces", new Object[]{Utils.stringTruncation((double)this.getBounces(spellLevel), 1)}),
                Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 2)})
        );
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        // Создаем снаряд
        ChromaticOrb orb = new ChromaticOrb(level, entity);

        // Устанавливаем позицию запуска
        Vec3 spawn = entity.getEyePosition().add(entity.getForward().normalize());
        orb.setPos(spawn);

        Vec3 direction = entity.getLookAngle().normalize();
        orb.setDeltaMovement(direction.multiply(orb.getSpeed(), orb.getSpeed(), orb.getSpeed()));
        orb.setDamage(getDamage(spellLevel, entity));
        orb.setBounces(getBounces(spellLevel));
        level.addFreshEntity(orb);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 1.7F;
    }

    private int getBounces(int spellLevel) {
        // Количество перескакиваний увеличивается с уровнем заклинания
        return 2 + (spellLevel / 3); // 2 на 1 уровне, до 5 на 10 уровне
    }
}
