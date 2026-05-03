package net.chixozhmix.dnmmod.spell.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.SpellConfigHandler;
import net.chixozhmix.dnmmod.api.spell.DnMSpellAnimations;
import net.chixozhmix.dnmmod.entity.spell.tombstone.Tombstone;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

@AutoSpellConfig
public class BurialGroundSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(DnMmod.MOD_ID, "burial_ground");

    public BurialGroundSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 4;
        this.spellPowerPerLevel = 1;
        this.castTime = 30;
        this.baseManaCost = 40;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setMaxLevel(6)
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
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        List<MutableComponent> baseInfo = List.of(
                Component.translatable("ui.irons_spellbooks.damage",
                        Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 2))
        );
        return SpellConfigHandler.modifyGetUniqueInfo(spellLevel, caster, baseInfo,
                "net.chixozhmix.dnmmod.spell.blood.BurialGroundSpell");
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if (!SpellConfigHandler.checkPreCastConditions(level, spellLevel, entity, playerMagicData,
                "net.chixozhmix.dnmmod.spell.blood.BurialGroundSpell")) {
            return false;
        }
        return true;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return DnMSpellAnimations.RAISE_SOMETHING;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return DnMSpellAnimations.AOE_END;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        int totalCount = 8;
        int rings = 2;
        Vec3 center = null;

        HitResult raycast = Utils.raycastForEntity(level, entity, 32.0F, true, 0.15F);
        center = raycast.getLocation();

        if(raycast.getType() == HitResult.Type.BLOCK)
            center = center.add(0, 1, 0);

        center = Utils.moveToRelativeGroundLevel(level, center, 2);

        int spawned = 0;
        for(int ring = 0; ring < rings; ring++) {
            float ringRadius = 2.0F + (ring * 1.5F);
            int perRing = totalCount / rings;

            for(int i = 0; i < perRing && spawned < totalCount; i++) {
                double angle = (Math.PI * 2 / perRing) * i;

                float angleVariation = (Utils.random.nextFloat() - 0.5F) * (0.5F + ring * 0.3F);
                double finalAngle = angle + angleVariation;

                float radiusVariation = (Utils.random.nextFloat() - 0.5F) * 1.2F;
                float finalRadius = ringRadius + radiusVariation;

                double xOffset = Math.cos(finalAngle) * finalRadius;
                double zOffset = Math.sin(finalAngle) * finalRadius;

                Vec3 randomOffset = new Vec3(
                        (Utils.random.nextDouble() - 0.5) * 0.8,
                        (Utils.random.nextDouble() - 0.5) * 0.5,
                        (Utils.random.nextDouble() - 0.5) * 0.8
                );

                Vec3 spawn = center.add(xOffset, 0, zOffset).add(randomOffset);
                spawn = Utils.moveToRelativeGroundLevel(level, spawn, 4);

                if (!level.getBlockState(BlockPos.containing(spawn).below()).isAir()) {
                    Tombstone tombstone = new Tombstone(level, entity, this.getDamage(spellLevel, entity));
                    tombstone.moveTo(spawn);
                    tombstone.setYRot((float)Utils.random.nextInt(360));
                    level.addFreshEntity(tombstone);
                    spawned++;
                }
            }
        }
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 1.2F;
    }


    @Override
    public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setLifestealPercent(0.10f);
    }
}
