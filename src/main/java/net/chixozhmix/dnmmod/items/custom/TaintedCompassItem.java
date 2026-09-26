package net.chixozhmix.dnmmod.items.custom;

import net.chixozhmix.dnmmod.Util.ModTags;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class TaintedCompassItem extends Item {
    public TaintedCompassItem(Properties pProperties) {
        super(pProperties);
    }

    public static GlobalPos getTempleLocation(Entity entity, CompoundTag compoundTag) {
        if(!(entity.level().dimension() == Level.OVERWORLD && compoundTag.contains("TemplePos")))
            return null;

        return GlobalPos.of(entity.level().dimension(), NbtUtils.readBlockPos(compoundTag.getCompound("TemplePos")));
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(!pLevel.isClientSide) {
            var tag = pStack.getOrCreateTag();
            if(!tag.contains("isInInventory")) {
                tag.putBoolean("isInInventory", true);
            }
        }
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        findTemple(pStack, pLevel, pPlayer);
    }

    private void findTemple(ItemStack pStack, Level pLevel, Player pPlayer) {
        if(pLevel instanceof ServerLevel serverLevel) {
            BlockPos blockPos = serverLevel.findNearestMapStructure(ModTags.TAINTED_COMPASS_LOCATOR, pPlayer.blockPosition(), 100, false);
            if(blockPos != null) {
                var tag = pStack.getOrCreateTag();
                tag.put("TemplePos", NbtUtils.writeBlockPos(blockPos));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (missingWarning(itemStack)) {
            findTemple(itemStack, pLevel, pPlayer);
            pPlayer.getCooldowns().addCooldown(ModItems.TAINTED_COMPASS.get(), 200);
            return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public boolean missingWarning(ItemStack itemStack) {
        return itemStack.getTag() != null && itemStack.getTag().contains("isInInventory") && !itemStack.getTag().contains("CatacombsPos");
    }
}
