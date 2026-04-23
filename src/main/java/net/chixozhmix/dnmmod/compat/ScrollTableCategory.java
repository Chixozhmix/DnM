package net.chixozhmix.dnmmod.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.recipe.ScrollTableRecipe;
import net.chixozhmix.dnmmod.registers.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ScrollTableCategory implements IRecipeCategory<ScrollTableRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(DnMmod.MOD_ID, "scroll_table");
    public static final ResourceLocation TEXTURE = new ResourceLocation(DnMmod.MOD_ID, "textures/gui/scroll_table_gui.png");

    public static final RecipeType<ScrollTableRecipe> SCROLL_TABLE_TYPE = new RecipeType<>(UID, ScrollTableRecipe.class);

    private final IDrawable bg;
    private final IDrawable icon;

    public ScrollTableCategory(IGuiHelper helper) {
        this.bg = helper.createDrawable(TEXTURE, 0,0,176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SCROLL_TABLE.get()));
    }

    @Override
    public RecipeType<ScrollTableRecipe> getRecipeType() {
        return SCROLL_TABLE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.dnmmod.scroll_table");
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return this.bg;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, ScrollTableRecipe scrollTableRecipe, IFocusGroup iFocusGroup) {
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 30, 12).addIngredients(scrollTableRecipe.getIngredients().get(0));

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 130, 12).addIngredients(scrollTableRecipe.getIngredients().get(1));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 21, 37).addIngredients(scrollTableRecipe.getIngredients().get(2));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 138, 37).addIngredients(scrollTableRecipe.getIngredients().get(3));

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 30, 59).addIngredients(scrollTableRecipe.getIngredients().get(4));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 130, 59).addIngredients(scrollTableRecipe.getIngredients().get(5));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 80, 17).addIngredients(scrollTableRecipe.getIngredients().get(6));

// OUTPUT
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 80, 54)
                .addItemStack(scrollTableRecipe.getResultItem(null));
    }
}
