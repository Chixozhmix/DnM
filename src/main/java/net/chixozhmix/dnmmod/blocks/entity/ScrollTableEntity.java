package net.chixozhmix.dnmmod.blocks.entity;

import net.chixozhmix.dnmmod.recipe.ScrollTableRecipe;
import net.chixozhmix.dnmmod.registers.ModBlockEntities;
import net.chixozhmix.dnmmod.screen.scroll_table.ScrolltableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScrollTableEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(8);

    private static final int OUTPUT_SLOT = 7;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 1;

    private boolean craftedItemWaiting = false;

    public ScrollTableEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SCROLL_TABLE_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> progress = value;
                    case 1 -> maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.dnmmod.scroll_table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ScrolltableMenu(id, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("progress", progress);
        tag.putBoolean("crafted_waiting", craftedItemWaiting);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("progress");
        craftedItemWaiting = tag.getBoolean("crafted_waiting");
    }

    private boolean stillHasValidRecipe() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());

        for (int i = 0; i < 7; i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        if (level == null) return false;

        return level.getRecipeManager()
                .getRecipeFor(ScrollTableRecipe.Type.INSTANCE, inventory, level)
                .isPresent();
    }

    public void tick(Level level, BlockPos pos, BlockState state) {

        if (craftedItemWaiting && !stillHasValidRecipe()) {
            itemHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
            craftedItemWaiting = false;
            resetProgress();
        }

        // Если игрок забрал результат — тратим ингредиенты
        if (craftedItemWaiting && itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty()) {
            consumeIngredients();
            craftedItemWaiting = false;
            resetProgress();
        }

        // Пока результат не забрали — новый крафт НЕ запускаем
        if (craftedItemWaiting) return;

        if (hasRecipe()) {
            progress++;
            setChanged(level, pos, state);

            if (progress >= maxProgress) {
                craftItem();
                craftedItemWaiting = true;
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private boolean hasRecipe() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());

        for (int i = 0; i < 7; i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        if (level == null) return false;

        var recipe = level.getRecipeManager()
                .getRecipeFor(ScrollTableRecipe.Type.INSTANCE, inventory, level);

        if (recipe.isEmpty()) return false;

        ItemStack result = recipe.get().getResultItem(null);
        if (result.isEmpty()) return false;

        return canInsertAmountIntoOutputSlot(result.getCount()) &&
                canInsertItemIntoOutputSlot(result.getItem());
    }

    private ItemStack getCraftingResult() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());

        for (int i = 0; i < 7; i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        if (level == null) return ItemStack.EMPTY;

        var recipe = level.getRecipeManager()
                .getRecipeFor(ScrollTableRecipe.Type.INSTANCE, inventory, level);

        return recipe.map(r -> r.assemble(inventory, level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }

    // Только создаём результат — БЕЗ удаления ингредиентов
    private void craftItem() {
        ItemStack result = getCraftingResult();
        if (result.isEmpty()) return;

        ItemStack currentOutput = itemHandler.getStackInSlot(OUTPUT_SLOT);

        if (currentOutput.isEmpty()) {
            itemHandler.setStackInSlot(OUTPUT_SLOT, result.copy());
        } else if (currentOutput.is(result.getItem())) {
            currentOutput.grow(result.getCount());
            itemHandler.setStackInSlot(OUTPUT_SLOT, currentOutput);
        }
    }

    // Удаляем ингредиенты ТОЛЬКО после забора результата
    private void consumeIngredients() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());

        for (int i = 0; i < 7; i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        var recipe = level.getRecipeManager()
                .getRecipeFor(ScrollTableRecipe.Type.INSTANCE, inventory, level);

        if (recipe.isEmpty()) return;

        NonNullList<Ingredient> ingredients = recipe.get().getIngredients();

        for (int i = 0; i < ingredients.size() && i < 7; i++) {
            if (!ingredients.get(i).isEmpty()) {
                itemHandler.extractItem(i, 1, false);
            }
        }
    }

    private boolean canInsertItemIntoOutputSlot(net.minecraft.world.item.Item item) {
        ItemStack output = itemHandler.getStackInSlot(OUTPUT_SLOT);
        return output.isEmpty() || output.is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        ItemStack output = itemHandler.getStackInSlot(OUTPUT_SLOT);
        return output.getCount() + count <= output.getMaxStackSize();
    }
}