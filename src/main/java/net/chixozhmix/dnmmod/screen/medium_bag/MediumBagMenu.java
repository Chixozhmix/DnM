package net.chixozhmix.dnmmod.screen.medium_bag;

import net.chixozhmix.dnmmod.items.custom.ComponentBag;
import net.chixozhmix.dnmmod.items.custom.MediumComponentBag;
import net.chixozhmix.dnmmod.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Optional;

public class MediumBagMenu extends AbstractContainerMenu {
    private final ItemStackHandler itemHandler;
    private final ItemStack bagItemStack;
    private final Player player;

    // Константы для слотов
    private static final int BAG_SLOT_COUNT = 15; // Количество слотов в сумке
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int BAG_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    public MediumBagMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, playerInventory.player.getMainHandItem());
    }

    public MediumBagMenu(int containerId, Inventory playerInventory, ItemStack bagItemStack) {
        super(ModMenuTypes.MEDIUM_COMPONENT_BAG_MENU.get(), containerId);

        this.player = playerInventory.player;
        this.bagItemStack = bagItemStack;

        // Получаем или создаем обработчик предметов для сумки
        Optional<IItemHandler> handlerOptional = bagItemStack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        if (handlerOptional.isPresent()) {
            this.itemHandler = (ItemStackHandler) handlerOptional.get();
        } else {
            // Fallback - создаем пустой обработчик
            this.itemHandler = new net.minecraftforge.items.ItemStackHandler(BAG_SLOT_COUNT);
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addBagSlots();
    }

    private void addBagSlots() {
        // Добавляем слоты сумки в виде сетки 5x3
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 5; ++col) {
                int x = 44 + col * 18;
                int y = 17 + row * 18;
                int slotIndex = col + row * 5;
                this.addSlot(new SlotItemHandler(itemHandler, slotIndex, x, y) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return !(stack.getItem() instanceof MediumComponentBag) ||
                                !(stack.getItem() instanceof ComponentBag);
                    }
                });
            }
        }
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = 84 + row * 18;
                int slotIndex = col + row * 9 + 9;
                this.addSlot(new Slot(playerInventory, slotIndex, x, y));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; ++col) {
            int x = 8 + col * 18;
            int y = 142;
            this.addSlot(new Slot(playerInventory, col, x, y));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Проверяем, является ли слот одним из слотов игрока
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // Слот игрока -> перемещаем в сумку

            // ПРОВЕРКА: запрещаем перемещение сумок в сумку через Shift+Click
            if (sourceStack.getItem() instanceof MediumComponentBag ||
                    sourceStack.getItem() instanceof ComponentBag) {
                return ItemStack.EMPTY; // Запрещаем перемещение сумок
            }

            if (!moveItemStackTo(sourceStack, BAG_FIRST_SLOT_INDEX, BAG_FIRST_SLOT_INDEX + BAG_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < BAG_FIRST_SLOT_INDEX + BAG_SLOT_COUNT) {
            // Слот сумки -> перемещаем в инвентарь игрока
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        // Если стек пуст, устанавливаем слот в пустой
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        // Проверяем, держит ли игрок сумку в руке
        return player.getMainHandItem().getItem() instanceof MediumComponentBag ||
                player.getOffhandItem().getItem() instanceof MediumComponentBag;
    }

    // Дополнительные методы для работы с сумкой
    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public ItemStack getBagItemStack() {
        return bagItemStack;
    }

    public int getBagSlotCount() {
        return BAG_SLOT_COUNT;
    }
}
