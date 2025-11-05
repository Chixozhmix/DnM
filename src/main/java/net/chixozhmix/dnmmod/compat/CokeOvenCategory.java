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
import net.chixozhmix.dnmmod.blocks.ModBlocks;
import net.chixozhmix.dnmmod.recipe.CokeOvenRecipes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CokeOvenCategory implements IRecipeCategory<CokeOvenRecipes> {
    public static final ResourceLocation UID = new ResourceLocation(DnMmod.MOD_ID, "coke_oven");
    public static final ResourceLocation TEXTURE = new ResourceLocation(DnMmod.MOD_ID, "textures/gui/coke_oven_gui.png");

    public static final RecipeType<CokeOvenRecipes> COKE_OVEN_RECIPES_RECIPE_TYPE = new RecipeType<>(UID, CokeOvenRecipes.class);

    private final IDrawable background;
    private final IDrawable icon;

    public CokeOvenCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0,0,176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.COKE_OVEN.get()));
    }

    @Override
    public RecipeType<CokeOvenRecipes> getRecipeType() {
        return COKE_OVEN_RECIPES_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.dnmmod.coke_oven");
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CokeOvenRecipes recipes, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 11).addIngredients(recipes.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 59).addItemStack(recipes.getResultItem(null));
    }
}
