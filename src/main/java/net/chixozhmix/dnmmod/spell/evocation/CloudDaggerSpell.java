package net.chixozhmix.dnmmod.spell.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.entity.spell.cloud_dagger.CloudDagger;
import net.chixozhmix.dnmmod.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
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
        return SpellUtils.ckeckSpellComponent(entity, ModItems.IRON_DAGGER.get());
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.distance", new Object[]{Utils.stringTruncation((double)this.getCylinderRadius(), 1)}),
                Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 2)}),
                Component.translatable("ui.dnmmod.spell_component", new Object[]{SpellUtils.getComponentName(ModItems.IRON_DAGGER.get())})
                );
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        Vec3 center = null;

        ICastData castData = playerMagicData.getAdditionalCastData();
        if (castData instanceof TargetEntityCastData castTargetingData) {
            LivingEntity target = castTargetingData.getTarget((ServerLevel) level);
            if (target != null) {
                center = target.getEyePosition().subtract(0, 0, 0);
            }
        }

        if (center == null) {
            HitResult raycast = Utils.raycastForEntity(level, entity, 32.0F, true, 0.15F);
            center = raycast.getLocation();

            if(raycast.getType() == HitResult.Type.BLOCK)
                center = center.add(0, 4, 0);

            center = Utils.moveToRelativeGroundLevel(level, center, 2);
        }

        // Создаем кинжалы в цилиндрической форме по границе
        createDoubleCylindricalDaggerCloud(level, spellLevel, entity, center, getDaggerCount(), getCylinderRadius(), getCylinderHeight(spellLevel, entity));
    }

    private void createDoubleCylindricalDaggerCloud(Level level, int spellLevel, LivingEntity entity, Vec3 center, int daggerCount, float radius, float height) {
        int layers = Math.max(2, (int) Math.ceil(height / 1.5f)); // Количество слоев по высоте
        int daggersPerLayer = daggerCount / layers; // Кинжалов на слой

        // Если общее количество не делится равномерно, добавляем остаток к последнему слою
        int remainingDaggers = daggerCount - (daggersPerLayer * layers);

        int createdDaggers = 0;

        for (int layer = 0; layer < layers; layer++) {
            int daggersInThisLayer = daggersPerLayer + (layer == layers - 1 ? remainingDaggers : 0);
            float layerHeight = (float)layer / (layers - 1); // От 0 до 1

            // Высота текущего слоя (от нижней до верхней границы цилиндра)
            double yOffset = (layerHeight - 0.5f) * height;

            for (int i = 0; i < daggersInThisLayer; i++) {
                // Равномерное распределение по окружности
                float angle = (float)i / daggersInThisLayer * 360.0F;

                // Позиция на цилиндре
                double x = Math.cos(Math.toRadians(angle)) * radius;
                double z = Math.sin(Math.toRadians(angle)) * radius;

                Vec3 spawnPos = center.add(x, yOffset, z);

                if (isValidSpawnPosition(level, BlockPos.containing(spawnPos))) {
                    CloudDagger dagger = new CloudDagger(level, entity, this.getDamage(spellLevel, entity));
                    dagger.moveTo(spawnPos);

                    dagger.setYRot(angle + 90.0F);
                    dagger.setLifetime(100 + level.random.nextInt(40));

                    level.addFreshEntity(dagger);
                    createdDaggers++;
                }
            }
        }
    }

    private boolean isValidSpawnPosition(Level level, BlockPos pos) {
        return level.getBlockState(pos).isAir() &&
                level.getBlockState(pos.above()).isAir();
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity);
    }

    private int getDaggerCount() {
        return 14;
    }

    private float getCylinderRadius() {
        return 2;
    }

    private float getCylinderHeight(int spellLevel, LivingEntity entity) {
        return 0.5F + spellLevel * 0.5F;
    }
}
