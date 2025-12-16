package net.chixozhmix.dnmmod.items.custom;

import net.chixozhmix.dnmmod.screen.medium_bag.MediumBagMenu;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class MediumComponentBag extends Item {

    public MediumComponentBag(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide() && pPlayer instanceof ServerPlayer serverPlayer) {
            // Открываем интерфейс сумки
            NetworkHooks.openScreen(serverPlayer,
                    new SimpleMenuProvider(
                            (windowId, playerInventory, player) ->
                                    new MediumBagMenu(windowId, playerInventory, stack),
                            stack.getHoverName()
                    )
            );
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
                if (cap == ForgeCapabilities.ITEM_HANDLER) {
                    return LazyOptional.of(() -> new MediumComponentBag.ComponentBagItemStackHandler(stack, 15)).cast();
                }
                return LazyOptional.empty();
            }
        };
    }


    private static class ComponentBagItemStackHandler extends ItemStackHandler {
        private final ItemStack bagStack;

        public ComponentBagItemStackHandler(ItemStack stack, int size) {
            super(size);
            this.bagStack = stack;
            // Загрузка данных из NBT
            if (stack.hasTag() && stack.getTag().contains("Inventory")) {
                deserializeNBT(stack.getTag().getCompound("Inventory"));
            }
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            // Сохранение данных в NBT предмета
            if (!bagStack.hasTag()) {
                bagStack.setTag(new CompoundTag());
            }
            bagStack.getTag().put("Inventory", serializeNBT());
        }
    }
}
