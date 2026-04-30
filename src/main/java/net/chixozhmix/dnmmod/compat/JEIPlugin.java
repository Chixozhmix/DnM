package net.chixozhmix.dnmmod.compat;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.recipe.ScrollTableRecipe;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.chixozhmix.dnmmod.registers.ModPotions;
import net.chixozhmix.dnmmod.screen.scroll_table.ScrollTableScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
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

        //Potions
        registration.addRecipes(
                RecipeTypes.BREWING,
                List.of(new JEIBrewingRecipes(
                        Potions.POISON,
                        ModItems.GREEMON_FANG.get(),
                        ModPotions.CORPSE_POISON.get()
                ))
        );
        registration.addRecipes(
                RecipeTypes.BREWING,
                List.of(new JEIBrewingRecipes(ModPotions.RESISTANCE.get(), Items.ENDER_PEARL, ModPotions.ENDER_RESIST_POTION.get()
                ))
        );
        registration.addRecipes(
                RecipeTypes.BREWING,
                List.of(new JEIBrewingRecipes(ModPotions.RESISTANCE.get(), Items.BLAZE_ROD, ModPotions.FIRE_RESIST_POTION.get()
                ))
        );
        registration.addRecipes(
                RecipeTypes.BREWING,
                List.of(new JEIBrewingRecipes(ModPotions.RESISTANCE.get(), ItemRegistry.FROZEN_BONE_SHARD.get(), ModPotions.ICE_RESIST_POTION.get()
                ))
        );
        registration.addRecipes(
                RecipeTypes.BREWING,
                List.of(new JEIBrewingRecipes(ModPotions.RESISTANCE.get(), ItemRegistry.BLOOD_VIAL.get(), ModPotions.BLOOD_RESIST_POTION.get()
                ))
        );
        registration.addRecipes(
                RecipeTypes.BREWING,
                List.of(new JEIBrewingRecipes(ModPotions.RESISTANCE.get(), Items.EMERALD, ModPotions.EVOCATION_RESIST_POTION.get()
                ))
        );
        registration.addRecipes(
                RecipeTypes.BREWING,
                List.of(new JEIBrewingRecipes(ModPotions.RESISTANCE.get(), ItemRegistry.DIVINE_PEARL.get(), ModPotions.HOLY_RESIST_POTION.get()
                ))
        );
        registration.addRecipes(
                RecipeTypes.BREWING,
                List.of(new JEIBrewingRecipes(ModPotions.RESISTANCE.get(), Items.POISONOUS_POTATO, ModPotions.NATURE_RESIST_POTION.get()
                ))
        );
        registration.addRecipes(
                RecipeTypes.BREWING,
                List.of(new JEIBrewingRecipes(ModPotions.RESISTANCE.get(), ItemRegistry.LIGHTNING_BOTTLE.get(), ModPotions.LIGHTNING_RESIST_POTION.get()
                ))
        );
        registration.addRecipes(
                RecipeTypes.BREWING,
                List.of(new JEIBrewingRecipes(ModPotions.RESISTANCE.get(), Items.ECHO_SHARD, ModPotions.ELDRITCH_RESIST_POTION.get()
                ))
        );
        registration.addRecipes(
                RecipeTypes.BREWING,
                List.of(new JEIBrewingRecipes(Potions.AWKWARD, Items.GOLDEN_APPLE, ModPotions.RESISTANCE.get()
                ))
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ScrollTableScreen.class, 70, 35, 5, 5,
        ScrollTableCategory.SCROLL_TABLE_TYPE);
    }
}
