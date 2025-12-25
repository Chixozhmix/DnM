package net.chixozhmix.dnmmod.items.custom;

import melonslise.locks.common.init.LocksEnchantments;
import melonslise.locks.common.init.LocksSoundEvents;
import melonslise.locks.common.util.Lockable;
import melonslise.locks.common.util.LocksUtil;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Collectors;

public class Key extends Item {
    public Key(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResult useOn(UseOnContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        List<Lockable> match = (List) LocksUtil.intersecting(world, pos).collect(Collectors.toList());

        if (match.isEmpty()) {
            return InteractionResult.PASS;
        }

        ItemStack keyStack = ctx.getItemInHand();

        Lockable targetLock = match.get(0);

        int lockComplexity = getComplexityLevel(targetLock);

        if (lockComplexity > 0) {
            if (!world.isClientSide) {
                world.playSound(ctx.getPlayer(), pos,
                        (SoundEvent) LocksSoundEvents.LOCK_RATTLE.get(),
                        SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.FAIL;
        }

        world.playSound(ctx.getPlayer(), pos,
                (SoundEvent) LocksSoundEvents.LOCK_OPEN.get(),
                SoundSource.BLOCKS, 1.0F, 1.0F);

        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!ctx.getPlayer().isCreative()) {
            keyStack.hurtAndBreak(1, ctx.getPlayer(),
                    (player) -> player.broadcastBreakEvent(ctx.getHand()));
        }

        for(Lockable lkb : match) {
            lkb.lock.setLocked(!lkb.lock.isLocked());
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

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
