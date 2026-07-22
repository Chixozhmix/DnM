package net.chixozhmix.dnmmod;

import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.chixozhmix.dnmmod.Util.BrewingRecipe;
import net.chixozhmix.dnmmod.configs.SpellComponentConfig;
import net.chixozhmix.dnmmod.registers.*;
import net.chixozhmix.dnmmod.items.CreativeTabMod;
import net.chixozhmix.dnmmod.registers.ModStructures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DnMmod.MOD_ID)
public class DnMmod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "dnmmod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public DnMmod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueIMC);

        MinecraftForge.EVENT_BUS.register(this);

        //Config
        SpellComponentConfig.register();
        //Sounds
        SoundsRegistry.register(modEventBus);
        //Entity
        ModEntityType.register(modEventBus);
        //Items
        ModItems.register(modEventBus);
        ModPaintings.register(modEventBus);
        //Blocks
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        //Effects
        ModEffects.register(modEventBus);
        //Spells
        RegistrySpells.register(modEventBus);
        //CreativeTab
        CreativeTabMod.register(modEventBus);
        //Menus
        ModMenuTypes.register(modEventBus);
        //Recipes
        ModRecipes.register(modEventBus);
        //Necromancy
        //DnMAttributes.ATTRIBUTES.register(modEventBus);
//        DnMSchools.DNM_SCHOOLS.register(modEventBus);
        //Fluid
        Fluids.register(modEventBus);
        //Potions
        ModPotions.register(modEventBus);
        //Particles
        ParticleRegistry.register(modEventBus);
        //Structures
        ModStructures.register(modEventBus);

        modEventBus.addListener(this::addCreative);
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.HEAD.getMessageBuilder().build());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Potions.POISON, ModItems.GREEMON_FANG.get(), ModPotions.CORPSE_POISON.get()));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(ModPotions.RESISTANCE.get(), Items.ENDER_PEARL, ModPotions.ENDER_RESIST_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(ModPotions.RESISTANCE.get(), Items.BLAZE_ROD, ModPotions.FIRE_RESIST_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(ModPotions.RESISTANCE.get(), ItemRegistry.FROZEN_BONE_SHARD.get(), ModPotions.ICE_RESIST_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(ModPotions.RESISTANCE.get(), ItemRegistry.BLOOD_VIAL.get(), ModPotions.BLOOD_RESIST_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(ModPotions.RESISTANCE.get(), Items.EMERALD, ModPotions.EVOCATION_RESIST_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(ModPotions.RESISTANCE.get(), ItemRegistry.DIVINE_PEARL.get(), ModPotions.HOLY_RESIST_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(ModPotions.RESISTANCE.get(), Items.POISONOUS_POTATO, ModPotions.NATURE_RESIST_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(ModPotions.RESISTANCE.get(), ItemRegistry.LIGHTNING_BOTTLE.get(), ModPotions.LIGHTNING_RESIST_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(ModPotions.RESISTANCE.get(), Items.ECHO_SHARD, ModPotions.ELDRITCH_RESIST_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Potions.AWKWARD, Items.GOLDEN_APPLE, ModPotions.RESISTANCE.get()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath("dnmmod", path);
    }
}
