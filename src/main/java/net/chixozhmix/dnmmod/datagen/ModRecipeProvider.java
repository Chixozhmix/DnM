package net.chixozhmix.dnmmod.datagen;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_DAGGER.get())
                .pattern("   ")
                .pattern(" I ")
                .pattern("S  ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_KLEVETS.get())
                .pattern(" IS")
                .pattern(" SI")
                .pattern("S  ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_SICKLE.get())
                .pattern(" I ")
                .pattern("  I")
                .pattern(" S ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_SPEAR.get())
                .pattern(" I ")
                .pattern(" S ")
                .pattern(" S ")
                .define('I', Items.IRON_NUGGET)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_GLAIVE.get())
                .pattern(" IN")
                .pattern(" S ")
                .pattern(" S ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .define('N', Items.IRON_NUGGET)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_GREATAXE.get())
                .pattern("III")
                .pattern("ISI")
                .pattern(" S ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_HALBERD.get())
                .pattern(" II")
                .pattern("ISI")
                .pattern(" S ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_BATTLEAXE.get())
                .pattern("ISI")
                .pattern(" SI")
                .pattern(" S ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_GREATSWORD.get())
                .pattern(" I ")
                .pattern("III")
                .pattern(" S ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_SCIMITAR.get())
                .pattern("  I")
                .pattern(" I ")
                .pattern("S  ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_MACE.get())
                .pattern("NIN")
                .pattern(" S ")
                .pattern(" S ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .define('N', Items.IRON_NUGGET)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_TRIDENT.get())
                .pattern(" NN")
                .pattern(" SN")
                .pattern("S  ")
                .define('S', Items.STICK)
                .define('N', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_KATANA.get())
                .pattern(" N ")
                .pattern(" N ")
                .pattern("GSG")
                .define('S', Items.STICK)
                .define('N', Items.IRON_INGOT)
                .define('G', Items.STRING)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_DAGGER.get())
                .pattern("   ")
                .pattern(" I ")
                .pattern("S  ")
                .define('I', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_KLEVETS.get())
                .pattern(" IS")
                .pattern(" SI")
                .pattern("S  ")
                .define('I', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_SICKLE.get())
                .pattern(" I ")
                .pattern("  I")
                .pattern(" S ")
                .define('I', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_SPEAR.get())
                .pattern("  I")
                .pattern(" S ")
                .pattern("S  ")
                .define('I', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_GLAIVE.get())
                .pattern(" IN")
                .pattern(" S ")
                .pattern(" S ")
                .define('I', Items.DIAMOND)
                .define('S', Items.STICK)
                .define('N', Items.IRON_NUGGET)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_GREATAXE.get())
                .pattern("III")
                .pattern("ISI")
                .pattern(" S ")
                .define('I', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_HALBERD.get())
                .pattern(" II")
                .pattern("ISI")
                .pattern(" S ")
                .define('I', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_BATTLEAXE.get())
                .pattern("ISI")
                .pattern(" SI")
                .pattern(" S ")
                .define('I', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_GREATSWORD.get())
                .pattern(" I ")
                .pattern("III")
                .pattern(" S ")
                .define('I', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_SCIMITAR.get())
                .pattern("  I")
                .pattern(" I ")
                .pattern("S  ")
                .define('I', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_MACE.get())
                .pattern("NIN")
                .pattern(" S ")
                .pattern(" S ")
                .define('I', Items.DIAMOND)
                .define('S', Items.STICK)
                .define('N', Items.IRON_NUGGET)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_TRIDENT.get())
                .pattern(" NN")
                .pattern(" SN")
                .pattern("S  ")
                .define('S', Items.STICK)
                .define('N', Items.DIAMOND)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_KATANA.get())
                .pattern(" N ")
                .pattern(" N ")
                .pattern("GSG")
                .define('S', Items.STICK)
                .define('N', Items.DIAMOND)
                .define('G', Items.STRING)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RITUAL_DAGGER.get())
                .pattern("   ")
                .pattern(" O ")
                .pattern("B  ")
                .define('B', Items.BONE)
                .define('O', Items.OBSIDIAN)
                .unlockedBy("has_obsidian", has(Items.OBSIDIAN))
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
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ENDER_WAND_CORE.get(), 1)
                .requires(ModItems.WAND_CORE.get())
                .requires(Items.ENDER_PEARL)
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
                .pattern("  C")
                .pattern(" W ")
                .pattern("C  ")
                .define('W', ModItems.ELECTROMANCER_WAND_CORE.get())
                .define('C', Items.COPPER_INGOT)
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
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ENDER_WAND.get(), 1)
                .pattern("  S")
                .pattern(" W ")
                .pattern("S  ")
                .define('W', ModItems.ENDER_WAND_CORE.get())
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
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MAID_DRESS.get(), 1)
                .pattern("W W")
                .pattern("WWW")
                .pattern("WWW")
                .define('W', Items.WHITE_WOOL)
                .unlockedBy("has_wool", has(Items.WHITE_WOOL))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MAID_CAP.get(), 1)
                .pattern("   ")
                .pattern("WFW")
                .pattern("   ")
                .define('W', Items.WHITE_WOOL)
                .define('F', Items.OXEYE_DAISY)
                .unlockedBy("has_wool", has(Items.WHITE_WOOL))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IMPROVED_ARCHER_HELMET.get(), 1)
                .pattern("III")
                .pattern("IAI")
                .pattern(" B ")
                .define('A', ModItems.ARCHER_ARMOR_HELMET.get())
                .define('I', Items.IRON_INGOT)
                .define('B', Items.BLACK_DYE)
                .unlockedBy("has_archer", has(ModItems.ARCHER_ARMOR_HELMET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IMPROVED_ARCHER_CHESTPLATE.get(), 1)
                .pattern("IAI")
                .pattern("IBI")
                .pattern("III")
                .define('A', ModItems.ARCHER_ARMOR_CHESTPLATE.get())
                .define('I', Items.IRON_INGOT)
                .define('B', Items.BLACK_DYE)
                .unlockedBy("has_archer", has(ModItems.ARCHER_ARMOR_CHESTPLATE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IMPROVED_ARCHER_LEGGINGS.get(), 1)
                .pattern("III")
                .pattern("IBI")
                .pattern("IAI")
                .define('A', ModItems.ARCHER_ARMOR_LEGGINGS.get())
                .define('I', Items.IRON_INGOT)
                .define('B', Items.BLACK_DYE)
                .unlockedBy("has_archer", has(ModItems.ARCHER_ARMOR_LEGGINGS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IMPROVED_ARCHER_BOOTS.get(), 1)
                .pattern("   ")
                .pattern("IBI")
                .pattern("IAI")
                .define('A', ModItems.ARCHER_ARMOR_BOOTS.get())
                .define('I', Items.IRON_INGOT)
                .define('B', Items.BLACK_DYE)
                .unlockedBy("has_archer", has(ModItems.ARCHER_ARMOR_BOOTS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SHADOW_ARCHER_BOOTS.get(), 1)
                .pattern(" R ")
                .pattern("IBI")
                .pattern("IAI")
                .define('A', ModItems.IMPROVED_ARCHER_BOOTS.get())
                .define('I', Items.DIAMOND)
                .define('B', Items.NETHERITE_INGOT)
                .define('R', ModItems.RAVEN_FEATHER.get())
                .unlockedBy("has_archer", has(ModItems.IMPROVED_ARCHER_BOOTS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SHADOW_ARCHER_LEGGINGS.get(), 1)
                .pattern("IRI")
                .pattern("IBI")
                .pattern("IAI")
                .define('A', ModItems.IMPROVED_ARCHER_LEGGINGS.get())
                .define('I', Items.DIAMOND)
                .define('B', Items.NETHERITE_INGOT)
                .define('R', ModItems.RAVEN_FEATHER.get())
                .unlockedBy("has_archer", has(ModItems.IMPROVED_ARCHER_LEGGINGS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SHADOW_ARCHER_CHESTPLATE.get(), 1)
                .pattern("RAR")
                .pattern("IBI")
                .pattern("III")
                .define('A', ModItems.IMPROVED_ARCHER_CHESTPLATE.get())
                .define('I', Items.DIAMOND)
                .define('B', Items.NETHERITE_INGOT)
                .define('R', ModItems.RAVEN_FEATHER.get())
                .unlockedBy("has_archer", has(ModItems.IMPROVED_ARCHER_CHESTPLATE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SHADOW_ARCHER_HELMET.get(), 1)
                .pattern("RIR")
                .pattern("IAI")
                .pattern(" B ")
                .define('A', ModItems.IMPROVED_ARCHER_HELMET.get())
                .define('I', Items.DIAMOND)
                .define('B', Items.NETHERITE_INGOT)
                .define('R', ModItems.RAVEN_FEATHER.get())
                .unlockedBy("has_archer", has(ModItems.IMPROVED_ARCHER_HELMET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MAGICAL_GRIMOIRE.get(), 1)
                .pattern("BCB")
                .pattern("LEL")
                .pattern("ASA")
                .define('S', ItemRegistry.IRON_SPELL_BOOK.get())
                .define('A', ItemRegistry.ARCANE_ESSENCE.get())
                .define('E', Items.AMETHYST_SHARD)
                .define('L', Items.LEATHER)
                .define('B', Items.BONE)
                .define('C', Items.BLUE_DYE)
                .unlockedBy("has_spell_book", has(ItemRegistry.IRON_SPELL_BOOK.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COMPONENT_BAG.get(), 1)
                .pattern(" S ")
                .pattern("LAL")
                .pattern(" L ")
                .define('L', Items.LEATHER)
                .define('S', Items.STRING)
                .define('A', ItemRegistry.ARCANE_ESSENCE.get())
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MEDIUM_COMPONENT_BAG.get(), 1)
                .pattern(" S ")
                .pattern("ALA")
                .pattern(" A ")
                .define('L', Items.LEATHER)
                .define('S', Items.STRING)
                .define('A', ItemRegistry.MAGIC_CLOTH.get())
                .unlockedBy("has_arcane_ingot", has(ItemRegistry.ARCANE_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PROTECTION_RING.get(), 1)
                .pattern("SG ")
                .pattern("GAG")
                .pattern(" G ")
                .define('A', ItemRegistry.MITHRIL_SCRAP.get())
                .define('S', Items.SHIELD)
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_mithril_scrap", has(ItemRegistry.MITHRIL_SCRAP.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MIRROR.get(), 1)
                .pattern(" I ")
                .pattern("IGI")
                .pattern(" I ")
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GLASS)
                .unlockedBy("has_glass", has(Items.GLASS))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLADE_RUNE.get(), 1)
                .pattern("DDD")
                .pattern("DRD")
                .pattern("DDD")
                .define('D', ModItems.DIAMOND_DAGGER.get())
                .define('R', ItemRegistry.BLANK_RUNE.get())
                .unlockedBy("has_rune", has(ItemRegistry.BLANK_RUNE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLADESINGER_BOOTS.get(), 1)
                .requires(ModItems.BLADE_RUNE.get())
                .requires(ItemRegistry.WIZARD_BOOTS.get())
                .requires(ItemRegistry.ARCANE_ESSENCE.get())
                .unlockedBy("has_blade_rune", has(ModItems.BLADE_RUNE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLADESINGER_LEGGINGS.get(), 1)
                .requires(ModItems.BLADE_RUNE.get())
                .requires(ItemRegistry.WIZARD_LEGGINGS.get())
                .requires(ItemRegistry.ARCANE_ESSENCE.get())
                .unlockedBy("has_blade_rune", has(ModItems.BLADE_RUNE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLADESINGER_CHESTPLATE.get(), 1)
                .requires(ModItems.BLADE_RUNE.get())
                .requires(ItemRegistry.WIZARD_CHESTPLATE.get())
                .requires(ItemRegistry.ARCANE_ESSENCE.get())
                .unlockedBy("has_blade_rune", has(ModItems.BLADE_RUNE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLADESINGER_HELMET.get(), 1)
                .requires(ModItems.BLADE_RUNE.get())
                .requires(ItemRegistry.WIZARD_HELMET.get())
                .requires(ItemRegistry.ARCANE_ESSENCE.get())
                .unlockedBy("has_blade_rune", has(ModItems.BLADE_RUNE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BLADESINGER_SWORD.get(), 1)
                .pattern("  I")
                .pattern("RS ")
                .pattern("I  ")
                .define('R', ModItems.BLADE_RUNE.get())
                .define('I', Items.IRON_SWORD)
                .define('S', Items.STICK)
                .unlockedBy("has_blade_rune", has(ModItems.BLADE_RUNE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.THUNDERSTORM_BOTTLE.get(), 1)
                .requires(ItemRegistry.LIGHTNING_BOTTLE.get())
                .requires(createPotionIngredient(Potions.WATER))
                .unlockedBy(getHasName(ItemRegistry.LIGHTNING_BOTTLE.get()), has(ItemRegistry.LIGHTNING_BOTTLE.get()))
                .save(consumer);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.SUGAR),
                        RecipeCategory.MISC, ModItems.BURNT_SUGAR.get(), 0.7f, 40)
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .save(consumer, DnMmod.MOD_ID + ":burnt_sugar_from_smelting_sugar");
    }

    private static Ingredient createPotionIngredient(Potion potions) {
        ItemStack waterPotion = new ItemStack(Items.POTION);
        PotionUtils.setPotion(waterPotion, potions);
        return StrictNBTIngredient.of(waterPotion);
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
