package net.chixozhmix.dnmmod.blocks.entity;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.chixozhmix.dnmmod.recipe.ScrollTableRecipe;
import net.chixozhmix.dnmmod.registers.ModBlockEntities;
import net.chixozhmix.dnmmod.screen.scroll_table.ScrolltableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
    private final ItemStackHandler itemHandler = new ItemStackHandler(7);

    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int INPUT_SLOT_3 = 2;
    private static final int INPUT_SLOT_4 = 3;
    private static final int INPUT_SLOT_5 = 4;
    private static final int INPUT_SLOT_6 = 5;
    private static final int OUTPUT_SLOT = 6;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 1;

    // Флаг, указывающий, что крафт завершен и предмет ожидает забора
    private boolean craftedItemWaiting = false;

    public ScrollTableEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SCROLL_TABLE_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> ScrollTableEntity.this.progress;
                    case 1 -> ScrollTableEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> ScrollTableEntity.this.progress = i1;
                    case 1 -> ScrollTableEntity.this.maxProgress = i1;
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
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
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

        for(int i = 0; i < itemHandler.getSlots(); i++)
        {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.dnmmod.scroll_table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ScrolltableMenu(i, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("scroll_table.progress", progress);
        pTag.putBoolean("scroll_table.crafted_waiting", craftedItemWaiting);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("scroll_table.progress");
        craftedItemWaiting = pTag.getBoolean("scroll_table.crafted_waiting");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        // Проверяем, был ли забран готовый предмет
        if (craftedItemWaiting && this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty()) {
            craftedItemWaiting = false;
            resetProgress();
        }

        // Начинаем новый крафт только если нет ожидающего предмета
        if (!craftedItemWaiting && hasRecipe()) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if (hasProgressFinished()) {
                craftItem();
                craftedItemWaiting = true; // Отмечаем, что предмет готов и ждет забора
                resetProgress();
            }
        } else if (!craftedItemWaiting) {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private boolean hasRecipe() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < 6; i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Level level = this.level;
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
        for (int i = 0; i < 6; i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Level level = this.level;
        if (level == null) return ItemStack.EMPTY;

        var recipe = level.getRecipeManager()
                .getRecipeFor(ScrollTableRecipe.Type.INSTANCE, inventory, level);

        if (recipe.isEmpty()) return ItemStack.EMPTY;

        return recipe.get().assemble(inventory, level.registryAccess());
    }

    private void craftItem() {
        ItemStack result = getCraftingResult();
        if (result.isEmpty()) return;

        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < 6; i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        // Получаем рецепт, чтобы узнать, какие предметы потреблять
        var recipe = level.getRecipeManager()
                .getRecipeFor(ScrollTableRecipe.Type.INSTANCE, inventory, level);

        if (recipe.isEmpty()) return;

        // Потребляем предметы согласно ингредиентам рецепта
        NonNullList<Ingredient> ingredients = recipe.get().getIngredients();
        for (int i = 0; i < ingredients.size() && i < 6; i++) {
            if (!ingredients.get(i).isEmpty()) {
                this.itemHandler.extractItem(i, 1, false);
            }
        }

        // Добавляем результат в выходной слот
        ItemStack currentOutput = this.itemHandler.getStackInSlot(OUTPUT_SLOT);

        if (currentOutput.isEmpty()) {
            this.itemHandler.setStackInSlot(OUTPUT_SLOT, result.copy());
        } else if (currentOutput.is(result.getItem())) {
            currentOutput.grow(result.getCount());
            this.itemHandler.setStackInSlot(OUTPUT_SLOT, currentOutput);
        }
    }

    private boolean canInsertItemIntoOutputSlot(net.minecraft.world.item.Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <=
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }
}