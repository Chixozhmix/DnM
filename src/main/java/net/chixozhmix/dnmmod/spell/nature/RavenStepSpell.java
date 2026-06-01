package net.chixozhmix.dnmmod.spell.nature;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.registers.ParticleRegistry;
import net.chixozhmix.dnmmod.registers.SoundsRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class RavenStepSpell extends AbstractSpell {
    private static ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "raven_step");

    public RavenStepSpell() {

        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 3;
        this.castTime = 0;
        this.baseManaCost = 35;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setMaxLevel(5)
            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
            .setCooldownSeconds(15)
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
        return CastType.INSTANT;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.distance", new Object[]{Utils.stringTruncation((double)this.getDistance(spellLevel, caster), 1)}));
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent) SoundsRegistry.SUMMON_RAVEN.get());
    }

    @Override
    public void onClientPreCast(Level level, int spellLevel, LivingEntity entity, InteractionHand hand, @Nullable MagicData playerMagicData) {
        super.onClientPreCast(level, spellLevel, entity, hand, playerMagicData);

        for(int i = 0; i < 35; ++i) {
            double speed = 0.15 + Utils.random.nextDouble() * 0.2;
            double angle = Utils.random.nextDouble() * Math.PI * 2;

            double motionX = Math.cos(angle) * speed * 0.3;
            double motionZ = Math.sin(angle) * speed * 0.3;
            double motionY = Utils.random.nextDouble() * speed * 0.3;

            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    entity.getRandomX(0.4F),
                    entity.getRandomY(),
                    entity.getRandomZ(0.4F),
                    motionX, motionY, motionZ);
        }

        for(int i = 0; i < 35; ++i) {
            double speed = 0.15 + Utils.random.nextDouble() * 0.2;
            double angle = Utils.random.nextDouble() * Math.PI * 2;

            double motionX = Math.cos(angle) * speed * 2.5;
            double motionZ = Math.sin(angle) * speed * 2.5;
            double motionY = Utils.random.nextDouble() * speed * 2.3;

            level.addParticle(ParticleRegistry.RAVEN_PARTICLES.get(),
                    entity.getRandomX(0.4F),
                    entity.getRandomY(),
                    entity.getRandomZ(0.4F),
                    motionX, motionY, motionZ);
        }
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
        Vec3 dest = null;
        TeleportSpell.TeleportData teleportData = (TeleportSpell.TeleportData)playerMagicData.getAdditionalCastData();

        AABB area = new AABB(
                entity.getX() - 5.0F, entity.getY() - 5.0F, entity.getZ() - 5.0F,
                entity.getX() + 5.0F, entity.getY() + 5.0F, entity.getZ() + 5.0F);

        List<LivingEntity> entitiesInRange = level.getEntitiesOfClass(
                LivingEntity.class,
                area
        );

        for(LivingEntity entities : entitiesInRange) {
            if(entities != entity)
                entities.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
        }

        if (teleportData != null) {
            Vec3 potentialTarget = teleportData.getTeleportTargetPosition();
            if (potentialTarget != null) {
                dest = potentialTarget;
                Utils.handleSpellTeleport(this, entity, potentialTarget);
            }
        } else {
            label37: {
                HitResult hitResult = Utils.raycastForEntity(level, entity, this.getDistance(spellLevel, entity), true);
                if (entity.isPassenger()) {
                    entity.stopRiding();
                }

                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    Entity var10 = ((EntityHitResult)hitResult).getEntity();
                    if (var10 instanceof LivingEntity) {
                        LivingEntity target = (LivingEntity)var10;

                        for(int i = 0; i < 8; ++i) {
                            dest = target.position().subtract((new Vec3((double)0.0F, (double)0.0F, (double)1.5F)).yRot(-(target.getYRot() + (float)(i * 45)) * ((float)Math.PI / 180F)));
                            if (level.getBlockState(BlockPos.containing(dest).above()).isAir()) {
                                break;
                            }
                        }

                        Utils.handleSpellTeleport(this, entity, dest.add((double)0.0F, (double)1.0F, (double)0.0F));
                        entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition().subtract((double)0.0F, 0.15, (double)0.0F));
                        break label37;
                    }
                }

                dest = TeleportSpell.findTeleportLocation(level, entity, this.getDistance(spellLevel, entity));
                Utils.handleSpellTeleport(this, entity, dest);
            }
        }

        entity.resetFallDistance();
        level.playSound((Player)null, dest.x, dest.y, dest.z, (SoundEvent)this.getCastFinishSound().get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
         super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDistance(int spellLevel, LivingEntity sourceEntity) {
        return (float)(Utils.softCapFormula((double)this.getEntityPowerMultiplier(sourceEntity)) * (double)this.getSpellPower(spellLevel, (Entity)null));
    }
}
