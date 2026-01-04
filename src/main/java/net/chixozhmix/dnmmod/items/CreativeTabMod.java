package net.chixozhmix.dnmmod.items;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.PropertiesHelper;
import net.chixozhmix.dnmmod.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabMod {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DnMmod.MOD_ID);

    //Weapons
    public static final RegistryObject<CreativeModeTab> WEAPONS_TAB = CREATIVE_MOD_TABS.register("weapons",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.IRON_DAGGER.get()))
                    .title(Component.translatable("creativetab.dnmmod.weapons"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.IRON_DAGGER.get());
                        output.accept(ModItems.IRON_SPEAR.get());
                        output.accept(ModItems.IRON_GREATAXE.get());
                        output.accept(ModItems.IRON_HALBERD.get());
                        output.accept(ModItems.IRON_BATTLEAXE.get());
                        output.accept(ModItems.IRON_GREATSWORD.get());
                        output.accept(ModItems.IRON_MACE.get());
                        output.accept(ModItems.IRON_SCIMITAR.get());
                        output.accept(ModItems.IRON_GLAIVE.get());
                        output.accept(ModItems.IRON_SICKLE.get());
                        output.accept(ModItems.IRON_KLEVETS.get());
                        output.accept(ModItems.IRON_TRIDENT.get());
                        output.accept(ModItems.IRON_KATANA.get());
                        output.accept(ModItems.DIAMOND_DAGGER.get());
                        output.accept(ModItems.DIAMOND_SPEAR.get());
                        output.accept(ModItems.DIAMOND_GREATAXE.get());
                        output.accept(ModItems.DIAMOND_HALBERD.get());
                        output.accept(ModItems.DIAMOND_BATTLEAXE.get());
                        output.accept(ModItems.DIAMOND_GREATSWORD.get());
                        output.accept(ModItems.DIAMOND_MACE.get());
                        output.accept(ModItems.DIAMOND_SCIMITAR.get());
                        output.accept(ModItems.DIAMOND_GLAIVE.get());
                        output.accept(ModItems.DIAMOND_SICKLE.get());
                        output.accept(ModItems.DIAMOND_KLEVETS.get());
                        output.accept(ModItems.DIAMOND_TRIDENT.get());
                        output.accept(ModItems.DIAMOND_KATANA.get());
                        output.accept(ModItems.RITUAL_DAGGER.get());
                        output.accept(ModItems.BLADESINGER_SWORD.get());
                    })
                    .build());
    //Staffs
    public static final RegistryObject<CreativeModeTab> STAFFS_TAB = CREATIVE_MOD_TABS.register("staffs_and_wand",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WOODEN_WAND.get()))
                    .title(Component.translatable("creativetab.dnmmod.staffs"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.WOODEN_WAND.get());
                        output.accept(ModItems.ELECTROMANCER_WAND.get());
                        output.accept(ModItems.CRYOMANCER_WAND.get());
                        output.accept(ModItems.DRUID_WAND.get());
                        output.accept(ModItems.BLOOD_WAND.get());
                        output.accept(ModItems.EVOKER_WAND.get());
                        output.accept(ModItems.PYROMANCER_WAND.get());
                        output.accept(ModItems.ENDER_WAND.get());
                        output.accept(ModItems.SACRED_SYMBOL.get());

                        PropertiesHelper.addOptionalItem(output, ModItems.AQUA_WAND);
                        PropertiesHelper.addOptionalItem(output, ModItems.KAPELLMEISTER_WAND);
//                        PropertiesHelper.addOptionalItem(output, ModItems.GEOMANCY_WAND);

                        output.accept(ModItems.MAGICAL_GRIMOIRE.get());
                    })
                    .build());
    //Items
    public static final RegistryObject<CreativeModeTab> ITEMS_TAB = CREATIVE_MOD_TABS.register("items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.RAVEN_FEATHER.get()))
                    .title(Component.translatable("creativetab.dnmmod.items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.ECTOPLASM.get());
                        output.accept(ModItems.PHANTOM_POTION.get());
                        output.accept(ModItems.THUNDERSTORM_BOTTLE.get());
                        output.accept(ModItems.RAVEN_FEATHER.get());
                        output.accept(ModItems.MAID_DRESS.get());
                        output.accept(ModItems.MAID_CAP.get());
                        output.accept(ModItems.MIRROR.get());
                        output.accept(ModItems.BURNT_SUGAR.get());
                        output.accept(ModItems.DICE20.get());
                        output.accept(ModItems.ARCHER_ARMOR_BOOTS.get());
                        output.accept(ModItems.ARCHER_ARMOR_LEGGINGS.get());
                        output.accept(ModItems.ARCHER_ARMOR_CHESTPLATE.get());
                        output.accept(ModItems.ARCHER_ARMOR_HELMET.get());
                        output.accept(ModItems.IMPROVED_ARCHER_BOOTS.get());
                        output.accept(ModItems.IMPROVED_ARCHER_LEGGINGS.get());
                        output.accept(ModItems.IMPROVED_ARCHER_CHESTPLATE.get());
                        output.accept(ModItems.IMPROVED_ARCHER_HELMET.get());
                        output.accept(ModItems.SHADOW_ARCHER_BOOTS.get());
                        output.accept(ModItems.SHADOW_ARCHER_LEGGINGS.get());
                        output.accept(ModItems.SHADOW_ARCHER_CHESTPLATE.get());
                        output.accept(ModItems.SHADOW_ARCHER_HELMET.get());
                        output.accept(ModItems.BLADESINGER_BOOTS.get());
                        output.accept(ModItems.BLADESINGER_LEGGINGS.get());
                        output.accept(ModItems.BLADESINGER_CHESTPLATE.get());
                        output.accept(ModItems.BLADESINGER_HELMET.get());
                    })
                    .build());
    //Magic Items
    public static final RegistryObject<CreativeModeTab> MAGIC_ITEMS_TAB = CREATIVE_MOD_TABS.register("magic_items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.HAG_EYE.get()))
                    .title(Component.translatable("creativetab.dnmmod.magic_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.HAG_EYE.get());
                        output.accept(ModItems.FOREST_HEART.get());
                        output.accept(ModItems.WAND_CORE.get());
                        output.accept(ModItems.CRYOMANCER_WAND_CORE.get());
                        output.accept(ModItems.PYROMANCER_WAND_CORE.get());
                        output.accept(ModItems.ELECTROMANCER_WAND_CORE.get());
                        output.accept(ModItems.DRUID_WAND_CORE.get());
                        output.accept(ModItems.BLOOD_WAND_CORE.get());
                        output.accept(ModItems.EVOKER_WAND_CORE.get());
                        output.accept(ModItems.ENDER_WAND_CORE.get());

                        PropertiesHelper.addOptionalItem(output, ModItems.AQUA_WAND_CORE);
                        PropertiesHelper.addOptionalItem(output, ModItems.KAPELLMEISTER_WAND_CORE);
//                        PropertiesHelper.addOptionalItem(output, ModItems.GEOMANCY_WAND_CORE);

                        output.accept(ModItems.BLADE_RUNE.get());
                        output.accept(ModItems.PROTECTION_RING.get());
                        output.accept(ModItems.COMPONENT_BAG.get());
                        output.accept(ModItems.MEDIUM_COMPONENT_BAG.get());
                    })
                    .build());
    //Blocks
    public static final RegistryObject<CreativeModeTab> BLOCKS_TAB = CREATIVE_MOD_TABS.register("blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CLAY_SHALE.get()))
                    .title(Component.translatable("creativetab.dnmmod.blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.LESHY_ALTAR.get());
                    })
                    .build());
    //Eggs
    public static final RegistryObject<CreativeModeTab> EGGS = CREATIVE_MOD_TABS.register("eggs",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.GHOST_SPAWN_EGG.get()))
                    .title(Component.translatable("creativetab.dnmmod.eggs"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.GHOST_SPAWN_EGG.get());
                        output.accept(ModItems.UNDEAD_SPIRIT_SPAWN_EGG.get());
                        output.accept(ModItems.RAVEN_SPAWN_EGG.get());
                        output.accept(ModItems.GOBLIN_SHAMAN_SPAWN_EGG.get());
                        output.accept(ModItems.GOBLIN_WARRIOR_SPAWN_EGG.get());
                        output.accept(ModItems.LESHY_SPAWN_EGG.get());
                        output.accept(ModItems.GREEMON_SPAWN_EGG.get());
                        output.accept(ModItems.GREEN_HAG_SPAWN_EGG.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);

    }
}
