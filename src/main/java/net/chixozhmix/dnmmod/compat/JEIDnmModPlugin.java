package net.chixozhmix.dnmmod.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.recipe.CokeOvenRecipes;
import net.chixozhmix.dnmmod.screen.CokeOvenScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEIDnmModPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(DnMmod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CokeOvenCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<CokeOvenRecipes> cokeOvenRecipes = recipeManager.getAllRecipesFor(CokeOvenRecipes.Type.INSTANCE);
        registration.addRecipes(CokeOvenCategory.COKE_OVEN_RECIPES_RECIPE_TYPE, cokeOvenRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CokeOvenScreen.class, 70, 30, 20, 30,
                CokeOvenCategory.COKE_OVEN_RECIPES_RECIPE_TYPE);
    }
}
