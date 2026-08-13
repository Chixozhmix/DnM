package net.chixozhmix.dnmmod.items.curio;

import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ProtectiveBrasletItem extends CurioBaseItem {

    public ProtectiveBrasletItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        pTooltipComponents.add(Component.translatable("tooltip.dnmmod.protective_braslet"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        CompoundTag tag = stack.getTag();

        if (tag == null || !tag.hasUUID("OwnerUUID")) {
            setOwner(stack, pPlayer);
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static void setOwner(ItemStack stack, Player player) {
        stack.getOrCreateTag().putUUID("OwnerUUID", player.getUUID());
    }

    public static UUID getOwner(ItemStack stack) {
        CompoundTag tag = stack.getTag();

        if (tag != null && tag.hasUUID("OwnerUUID")) {
            return tag.getUUID("OwnerUUID");
        }

        return null;
    }

    public static boolean isOwner(ItemStack stack, Player player) {
        UUID owner = getOwner(stack);
        return owner != null && owner.equals(player.getUUID());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return getOwner(stack) != null;
    }
}
