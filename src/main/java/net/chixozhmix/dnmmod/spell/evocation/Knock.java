package net.chixozhmix.dnmmod.spell.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import melonslise.locks.common.init.LocksEnchantments;
import melonslise.locks.common.init.LocksSoundEvents;
import melonslise.locks.common.util.Lockable;
import melonslise.locks.common.util.LocksUtil;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.stream.Collectors;

@AutoSpellConfig
public class Knock extends AbstractSpell {
    private static final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "knock");

    public Knock() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 80;
        this.baseManaCost = 70;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setMaxLevel(1)
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        HitResult result = entity.pick(4.0f, 0.0f, false);

        if(result.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) result).getBlockPos();

            List<Lockable> match = LocksUtil.intersecting(level, pos).collect(Collectors.toList());

            if (!match.isEmpty()) {
                int openedLocks = 0;
                int blockedLocks = 0;
                boolean playedSound = false;

                for (Lockable lkb : match) {
                    int complexityLevel = getComplexityLevel(lkb);

                    if (complexityLevel > 0) {
                        blockedLocks++;

                        // Проигрываем другой звук для заблокированных замков
                        if (!level.isClientSide() && !playedSound) {
                            level.playSound(null, pos, LocksSoundEvents.LOCK_OPEN.get(),
                                    SoundSource.BLOCKS, 1.0f, 1.0f);
                            playedSound = true;
                        }
                        continue;
                    }
                    if (!level.isClientSide()) {
                        level.playSound(null, pos,
                                melonslise.locks.common.init.LocksSoundEvents.LOCK_OPEN.get(),
                                SoundSource.BLOCKS, 1f, 1f);
                        lkb.lock.setLocked(!lkb.lock.isLocked());
                        openedLocks++;
                    }
                }
                if (!level.isClientSide() && entity != null) {
                    if (blockedLocks > 0) {
                        entity.sendSystemMessage(
                                Component.translatable("ui.dnmmod.knock.complexity_blocked", blockedLocks)
                                        .withStyle(ChatFormatting.RED)
                        );
                    }
                    if (openedLocks > 0) {
                        entity.sendSystemMessage(
                                Component.translatable("ui.dnmmod.knock.opened", openedLocks)
                                        .withStyle(ChatFormatting.GREEN)
                        );
                    }
                    else if (openedLocks == 0 && blockedLocks > 0) {
                        entity.sendSystemMessage(
                                Component.translatable("ui.dnmmod.knock.all_blocked")
                                        .withStyle(ChatFormatting.DARK_RED)
                        );
                    }
                }
            }
        }
    }
    /**
     * Проверяет уровень зачарования Complexity на замке
     */
    private int getComplexityLevel(Lockable lockable) {
        try {
            return EnchantmentHelper.getItemEnchantmentLevel(
                    LocksEnchantments.COMPLEXITY.get(),
                    lockable.stack
            );
        } catch (Exception e) {
            DnMmod.LOGGER.debug("Failed to check complexity enchantment", e);
            return 0;
        }
    }
}
