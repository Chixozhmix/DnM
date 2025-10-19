package net.chixozhmix.dnmmod.spell.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.spell.cloud_dagger.CloudDagger;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

@AutoSpellConfig
public class CloudDaggerSpell extends AbstractSpell {

    private static final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "cloud_daggers");

    public CloudDaggerSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 30;
        this.baseManaCost = 40;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setMaxLevel(10)
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
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.15F, false);
        return true;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.distance", new Object[]{Utils.stringTruncation((double)this.getCylinderRadius(spellLevel, caster), 1)}),
                Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 2)})
                );
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        int daggerCount = this.getDaggerCount(spellLevel, entity);
        float cylinderRadius = this.getCylinderRadius(spellLevel, entity);
        float cylinderHeight = this.getCylinderHeight(spellLevel, entity);
        Vec3 center = null;

        ICastData castData = playerMagicData.getAdditionalCastData();
        if (castData instanceof TargetEntityCastData castTargetingData) {
            LivingEntity target = castTargetingData.getTarget((ServerLevel) level);
            if (target != null) {
                // ИСПРАВЛЕНИЕ: Берем позицию глаз цели или центр bounding box
                center = target.getEyePosition().subtract(0, 0.5, 0);
                // Альтернативно: center = target.getBoundingBox().getCenter();
            }
        }

        if (center == null) {
            center = Utils.raycastForEntity(level, entity, 32.0F, true, 0.15F).getLocation();
            center = Utils.moveToRelativeGroundLevel(level, center, 6);
        }

        // Создаем кинжалы в цилиндрической форме по границе
        createDoubleCylindricalDaggerCloud(level, spellLevel, entity, center, daggerCount, cylinderRadius, cylinderHeight);
    }

    private void createDoubleCylindricalDaggerCloud(Level level, int spellLevel, LivingEntity entity, Vec3 center, int daggerCount, float radius, float height) {
        int createdDaggers = 0;

        for (int i = 0; i < daggerCount; i++) {
            // Случайный угол и высота в цилиндре
            float angle = level.random.nextFloat() * 360.0F;
            double yOffset = (level.random.nextDouble() - 0.5) * height;

            // Позиция на цилиндре
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;

            Vec3 spawnPos = center.add(x, yOffset, z);

            // ИСПРАВЛЕНИЕ: Упрощенная проверка позиции
            if (isValidSpawnPosition(level, BlockPos.containing(spawnPos))) {
                CloudDagger dagger = new CloudDagger(level, entity, this.getDamage(spellLevel, entity));
                dagger.moveTo(spawnPos);

                // Направляем кинжалы наружу от центра
                dagger.setYRot(angle + 90.0F);
                dagger.setLifetime(100 + level.random.nextInt(40));

                level.addFreshEntity(dagger);
                createdDaggers++;
            }
        }
    }

    private boolean isValidSpawnPosition(Level level, BlockPos pos) {
        // ИСПРАВЛЕНИЕ: Разрешаем спавн в воздухе, но не внутри блоков
        return level.getBlockState(pos).isAir() &&
                level.getBlockState(pos.above()).isAir();
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity);
    }

    private int getDaggerCount(int spellLevel, LivingEntity entity) {
        return 14;
    }

    private float getCylinderRadius(int spellLevel, LivingEntity entity) {
        return 3;
    }

    private float getCylinderHeight(int spellLevel, LivingEntity entity) {
        return 2.5F + spellLevel * 0.3F;
    }
}
