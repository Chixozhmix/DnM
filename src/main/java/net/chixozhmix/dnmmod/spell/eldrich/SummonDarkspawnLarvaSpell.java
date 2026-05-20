package net.chixozhmix.dnmmod.spell.eldrich;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.darkspawn_larva.summon.SummonDarkspawnLarva;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

@AutoSpellConfig
public class SummonDarkspawnLarvaSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "summon_darkspawn_larva");

    public SummonDarkspawnLarvaSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 45;
        this.baseManaCost = 95;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(160)
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
    public boolean allowLooting() {
        return false;
    }

    @Override
    public boolean allowCrafting() {
        return false;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.summon_count", new Object[]{this.getSummonCount(spellLevel, caster)}));
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (SummonManager.recastFinishedHelper(serverPlayer, recastInstance, recastResult, castDataSerializable)) {
            super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
        }
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new SummonedEntitiesCastData();
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        PlayerRecasts recasts = playerMagicData.getPlayerRecasts();
        if (!recasts.hasRecastForSpell(this)) {
            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();
            int summonTime = 12000;
            float radius = 1.5F + 0.185F * (float)spellLevel;
            int count = this.getSummonCount(spellLevel, entity);

            for (int i = 0; i < count; i++) {
                SummonDarkspawnLarva larva = new SummonDarkspawnLarva(level, entity, true);
                larva.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(larva.getOnPos()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
                float yrot = 6.281F / (float)spellLevel * (float)i + entity.getYRot() * ((float)Math.PI / 180F);
                Vec3 spawn = Utils.moveToRelativeGroundLevel(level, entity.getEyePosition().add(new Vec3((double)(radius * Mth.cos(yrot)), (double)0.0F, (double)(radius * Mth.sin(yrot)))), 10);
                larva.setPos(spawn.x, spawn.y, spawn.z);
                larva.setYRot(entity.getYRot());
                larva.setOldPosAndRot();
                level.addFreshEntity(larva);
                SummonManager.initSummon(entity, larva, summonTime, summonedEntitiesCastData);
            }
            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, getRecastCount(spellLevel, entity), summonTime, castSource, summonedEntitiesCastData);
            recasts.addRecast(recastInstance, playerMagicData);
        }
    }

    public int getSummonCount(int spellLevel, LivingEntity caster) {
        return spellLevel + 1;
    }
}
