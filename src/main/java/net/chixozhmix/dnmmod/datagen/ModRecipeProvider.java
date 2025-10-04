package net.chixozhmix.dnmmod.datagen;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.blocks.ModBlocks;
import net.chixozhmix.dnmmod.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> STEEL_SMELTABLES = List.of(ModItems.RAW_STEEL.get());

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        //Dagger
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CUMMON_DAGGER.get())
                .pattern("   ")
                .pattern(" I ")
                .pattern("S  ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);

        //Spear
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CUMMON_SPEAR.get())
                .pattern(" I ")
                .pattern(" S ")
                .pattern(" S ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);

        //Clay shale
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CLAY_SHALE.get(), 4)
                .pattern("CCC")
                .pattern("CDC")
                .pattern("CCC")
                .define('D', Items.COBBLED_DEEPSLATE)
                .define('C', Items.CLAY_BALL)
                .unlockedBy("has_clay", has(Items.CLAY_BALL))
                .save(consumer);

        //Coke oven
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COKE_OVEN.get(), 1)
                .pattern("B B")
                .pattern("BCB")
                .pattern("BTB")
                .define('T', Items.IRON_TRAPDOOR)
                .define('B', Items.BRICKS)
                .define('C', Items.COAL_BLOCK)
                .unlockedBy("has_bricks", has(Items.BRICKS))
                .save(consumer);

        //Raw Steel
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RAW_STEEL.get(), 3)
                .pattern("CCC")
                .pattern("III")
                .pattern("FFF")
                .define('C', ModItems.COKE_COAL.get())
                .define('I', Items.IRON_INGOT)
                .define('F', ModItems.FLUX.get())
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);

        //Flux
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FLUX.get(), 4)
                .requires(ModBlocks.CLAY_SHALE.get())
                .unlockedBy(getHasName(ModBlocks.CLAY_SHALE.get()), has(ModBlocks.CLAY_SHALE.get()))
                .save(consumer);

        //Steel Ingot
        oreBlasting(consumer, STEEL_SMELTABLES, RecipeCategory.MISC, ModItems.STEEL_INGOT.get(), 0.25f, 200, "steel");
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory,
                                      ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme,
                pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory,
                                      ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience,
                pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe>
            pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime,
                                     String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime,
                    pCookingSerializer).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike)).save(pFinishedRecipeConsumer,
                    DnMmod.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }

    }
}
