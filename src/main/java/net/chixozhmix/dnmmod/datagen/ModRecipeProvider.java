package net.chixozhmix.dnmmod.datagen;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
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
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CUMMON_DAGGER.get())
                .pattern("   ")
                .pattern(" I ")
                .pattern("S  ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COMMON_KLEVETS.get())
                .pattern(" IS")
                .pattern(" SI")
                .pattern("S  ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COMMON_SICKLE.get())
                .pattern(" I ")
                .pattern("  I")
                .pattern(" S ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CUMMON_SPEAR.get())
                .pattern(" I ")
                .pattern(" S ")
                .pattern(" S ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COMMON_GLAIVE.get())
                .pattern(" IN")
                .pattern(" S ")
                .pattern(" S ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .define('N', ModItems.STEEL_NUGGET.get())
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CUMMON_GREATAXE.get())
                .pattern("III")
                .pattern("ISI")
                .pattern(" S ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COMMON_HALBERD.get())
                .pattern(" II")
                .pattern("ISI")
                .pattern(" S ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COMMON_BATTLEAXE.get())
                .pattern(" II")
                .pattern(" SI")
                .pattern(" S ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COMMON_GREATSWORD.get())
                .pattern(" I ")
                .pattern("III")
                .pattern(" S ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COMMON_SCIMITAR.get())
                .pattern("  I")
                .pattern(" I ")
                .pattern("S  ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COMMON_MACE.get())
                .pattern("NIN")
                .pattern(" S ")
                .pattern(" S ")
                .define('I', ModItems.STEEL_INGOT.get())
                .define('S', Items.STICK)
                .define('N', ModItems.STEEL_NUGGET.get())
                .unlockedBy("has_steel_ingot", has(ModItems.STEEL_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CLAY_SHALE.get(), 4)
                .pattern("CCC")
                .pattern("CDC")
                .pattern("CCC")
                .define('D', Items.COBBLED_DEEPSLATE)
                .define('C', Items.CLAY_BALL)
                .unlockedBy("has_clay", has(Items.CLAY_BALL))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COKE_OVEN.get(), 1)
                .pattern("B B")
                .pattern("BCB")
                .pattern("BTB")
                .define('T', Items.IRON_TRAPDOOR)
                .define('B', Items.BRICKS)
                .define('C', Items.COAL_BLOCK)
                .unlockedBy("has_bricks", has(Items.BRICKS))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RAW_STEEL.get(), 3)
                .pattern("CCC")
                .pattern("III")
                .pattern("FFF")
                .define('C', ModItems.COKE_COAL.get())
                .define('I', Items.IRON_INGOT)
                .define('F', ModItems.FLUX.get())
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FLUX.get(), 4)
                .requires(ModBlocks.CLAY_SHALE.get())
                .unlockedBy(getHasName(ModBlocks.CLAY_SHALE.get()), has(ModBlocks.CLAY_SHALE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.STEEL_NUGGET.get(), 9)
                .requires(ModItems.STEEL_INGOT.get())
                .unlockedBy(getHasName(ModItems.STEEL_INGOT.get()), has(ModItems.STEEL_INGOT.get()))
                .save(consumer);
        oreBlasting(consumer, STEEL_SMELTABLES, RecipeCategory.MISC, ModItems.STEEL_INGOT.get(), 0.25f, 200, "steel");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STEEL_INGOT.get(), 1)
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ModItems.STEEL_NUGGET.get())
                .unlockedBy("has_steel_nugget", has(ModItems.STEEL_NUGGET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WAND_CORE.get(), 1)
                .pattern("  A")
                .pattern(" M ")
                .pattern("   ")
                .define('M', ItemRegistry.ARCANE_ESSENCE.get())
                .define('A', Items.AMETHYST_SHARD)
                .unlockedBy("has_arcane_essence", has(ItemRegistry.ARCANE_ESSENCE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DRUID_WAND_CORE.get(), 1)
                .requires(ModItems.WAND_CORE.get())
                .requires(Items.POISONOUS_POTATO)
                .unlockedBy(getHasName(ModItems.WAND_CORE.get()), has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ELECTROMANCER_WAND_CORE.get(), 1)
                .requires(ModItems.WAND_CORE.get())
                .requires(ItemRegistry.LIGHTNING_BOTTLE.get())
                .unlockedBy(getHasName(ModItems.WAND_CORE.get()), has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PYROMANCER_WAND_CORE.get(), 1)
                .requires(ModItems.WAND_CORE.get())
                .requires(Items.BLAZE_ROD)
                .unlockedBy(getHasName(ModItems.WAND_CORE.get()), has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CRYOMANCER_WAND_CORE.get(), 1)
                .requires(ModItems.WAND_CORE.get())
                .requires(ItemRegistry.FROZEN_BONE_SHARD.get())
                .unlockedBy(getHasName(ModItems.WAND_CORE.get()), has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EVOKER_WAND_CORE.get(), 1)
                .requires(ModItems.WAND_CORE.get())
                .requires(Items.EMERALD)
                .unlockedBy(getHasName(ModItems.WAND_CORE.get()), has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLOOD_WAND_CORE.get(), 1)
                .requires(ModItems.WAND_CORE.get())
                .requires(ItemRegistry.BLOOD_VIAL.get())
                .unlockedBy(getHasName(ModItems.WAND_CORE.get()), has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WOODEN_WAND.get(), 1)
                .pattern("  S")
                .pattern(" W ")
                .pattern("S  ")
                .define('W', ModItems.WAND_CORE.get())
                .define('S', Items.STICK)
                .unlockedBy("has_wand_core", has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ELECTROMANCER_WAND.get(), 1)
                .pattern("  S")
                .pattern(" W ")
                .pattern("S  ")
                .define('W', ModItems.ELECTROMANCER_WAND_CORE.get())
                .define('S', Items.STICK)
                .unlockedBy("has_wand_core", has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PYROMANCER_WAND.get(), 1)
                .pattern("  S")
                .pattern(" W ")
                .pattern("S  ")
                .define('W', ModItems.PYROMANCER_WAND_CORE.get())
                .define('S', Items.STICK)
                .unlockedBy("has_wand_core", has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CRYOMANCER_WAND.get(), 1)
                .pattern("  S")
                .pattern(" W ")
                .pattern("S  ")
                .define('W', ModItems.CRYOMANCER_WAND_CORE.get())
                .define('S', Items.STICK)
                .unlockedBy("has_wand_core", has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLOOD_WAND.get(), 1)
                .pattern("  B")
                .pattern(" W ")
                .pattern("B  ")
                .define('W', ModItems.BLOOD_WAND_CORE.get())
                .define('B', Items.BONE)
                .unlockedBy("has_wand_core", has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EVOKER_WAND.get(), 1)
                .pattern("  S")
                .pattern(" W ")
                .pattern("S  ")
                .define('W', ModItems.EVOKER_WAND_CORE.get())
                .define('S', Items.STICK)
                .unlockedBy("has_wand_core", has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DRUID_WAND.get(), 1)
                .pattern("  S")
                .pattern(" W ")
                .pattern("S  ")
                .define('W', ModItems.DRUID_WAND_CORE.get())
                .define('S', Items.STICK)
                .unlockedBy("has_wand_core", has(ModItems.WAND_CORE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SACRED_SYMBOL.get(), 1)
                .pattern(" GG")
                .pattern(" DG")
                .pattern("G  ")
                .define('D', ItemRegistry.DIVINE_PEARL.get())
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_wand_core", has(ModItems.WAND_CORE.get()))
                .save(consumer);
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
