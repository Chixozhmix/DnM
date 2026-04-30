package net.chixozhmix.dnmmod.compat;

import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.List;

public class JEIBrewingRecipes implements IJeiBrewingRecipe {
    private final Potion input;
    private final Item ingredient;
    private final Potion output;

    public JEIBrewingRecipes(Potion input, Item ingredient, Potion output) {
        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public List<ItemStack> getPotionInputs() {
        return List.of(PotionUtils.setPotion(new ItemStack(Items.POTION), input));
    }

    @Override
    public List<ItemStack> getIngredients() {
        return List.of(new ItemStack(ingredient));
    }

    @Override
    public ItemStack getPotionOutput() {
        return PotionUtils.setPotion(new ItemStack(Items.POTION), output);
    }

    @Override
    public int getBrewingSteps() {
        return 0;
    }
}
