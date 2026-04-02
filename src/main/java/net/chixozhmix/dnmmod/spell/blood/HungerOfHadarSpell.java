package net.chixozhmix.dnmmod.spell.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.spell.hunger_of_hadar.HungerOfHadar;
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

public class HungerOfHadarSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "hunger_of_hadar");

    public HungerOfHadarSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 25;
        this.baseManaCost = 60;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(120)
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
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.FINISH_ANIMATION;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation
                ((double)this.getDamage(spellLevel, caster), 2)}));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        int count = 4;
        Vec3 center = null;

        HitResult raycast = Utils.raycastForEntity(level, entity, 32.0F, true, 0.15F);
        center = raycast.getLocation();

        if(raycast.getType() == HitResult.Type.BLOCK)
            center = center.add(0, 1, 0); // Поднимаем над землей

        center = Utils.moveToRelativeGroundLevel(level, center, 2);

        if (!level.isClientSide) {
            HungerOfHadar hungerOfHadar = new HungerOfHadar(level, entity, center);

            float radius = 6.0F;
            float damage = getDamage(spellLevel, entity);

            hungerOfHadar.setRadius(radius);
            hungerOfHadar.setDamage(damage);
            hungerOfHadar.setDuration(280);

            level.addFreshEntity(hungerOfHadar);

            for(int r = 0; r < count; ++r) {
                float tentacles = (float)(count);

                for(int i = 0; (float)i < tentacles; ++i) {
                    Vec3 random = new Vec3(Utils.getRandomScaled((double)0.5F), Utils.getRandomScaled((double)0.5F), Utils.getRandomScaled((double)0.5F));
                    Vec3 spawn = center.add((new Vec3((double)0.0F, (double)0.0F, 1.2F + (double)(r * 0.05F))).yRot(6.281F / tentacles * (float)i)).add(random);
                    spawn = Utils.moveToRelativeGroundLevel(level, spawn, 4);
                    if (!level.getBlockState(BlockPos.containing(spawn).below()).isAir()) {
                        VoidTentacle tentacle = new VoidTentacle(level, entity, this.getDamage(spellLevel, entity));
                        tentacle.setDamage(0);
                        tentacle.moveTo(spawn);
                        tentacle.setYRot((float)Utils.random.nextInt(360));
                        tentacle.setBoundingBox(tentacle.getBoundingBox().inflate(-0.3F));
                        level.addFreshEntity(tentacle);
                    }
                }
            }
        }
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 2F;
    }

    @Override
    public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setLifestealPercent(0.10F);
    }
}
