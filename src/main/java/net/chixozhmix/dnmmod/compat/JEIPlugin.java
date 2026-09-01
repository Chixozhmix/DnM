package net.chixozhmix.dnmmod.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.recipe.ScrollTableRecipe;
import net.chixozhmix.dnmmod.screen.scroll_table.ScrollTableScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(DnMmod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ScrollTableCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        List<ScrollTableRecipe> recipes = manager.getAllRecipesFor(ScrollTableRecipe.Type.INSTANCE);
        registration.addRecipes(ScrollTableCategory.SCROLL_TABLE_TYPE, recipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ScrollTableScreen.class, 70, 35, 5, 5,
        ScrollTableCategory.SCROLL_TABLE_TYPE);
    }
}
