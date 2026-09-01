package net.chixozhmix.dnmmod;

import com.mojang.logging.LogUtils;
import net.chixozhmix.dnmmod.configs.SpellComponentConfig;
import net.chixozhmix.dnmmod.registers.*;
import net.chixozhmix.dnmmod.items.CreativeTabMod;
import net.chixozhmix.dnmmod.registers.ModStructures;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
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
                () -> new SlotTypeMessage.Builder("bracelet").size(2).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.HEAD.getMessageBuilder().build());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        DnMBrewingRegistry.registerRecipes();
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
