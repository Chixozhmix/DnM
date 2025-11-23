package net.chixozhmix.dnmmod.datagen;

import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DnMmod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Weapons
        handheldItem(ModItems.IRON_DAGGER);
        handheldItem(ModItems.IRON_BATTLEAXE);
        handheldItem(ModItems.IRON_MACE);
        handheldItem(ModItems.IRON_SCIMITAR);
        handheldItem(ModItems.IRON_SICKLE);
        handheldItem(ModItems.IRON_KLEVETS);
        handheldItem(ModItems.DIAMOND_DAGGER);
        handheldItem(ModItems.DIAMOND_BATTLEAXE);
        handheldItem(ModItems.DIAMOND_MACE);
        handheldItem(ModItems.DIAMOND_SCIMITAR);
        handheldItem(ModItems.DIAMOND_SICKLE);
        handheldItem(ModItems.DIAMOND_KLEVETS);
        handheldItem(ModItems.PHANTOM_POTION);
        curioItem(ModItems.PROTECTION_RING);
        simpleItem(ModItems.COMPONENT_BAG);
        simpleItem(ModItems.MEDIUM_COMPONENT_BAG);
        handheldItem(ModItems.RITUAL_DAGGER);
        //Items
        simpleItem(ModItems.COKE_COAL);
        simpleItem(ModItems.FLUX);
        simpleItem(ModItems.RAW_STEEL);
        simpleItem(ModItems.STEEL_INGOT);
        simpleItem(ModItems.STEEL_NUGGET);
        simpleItem(ModItems.HAG_EYE);
        simpleItem(ModItems.WAND_CORE);
        simpleItem(ModItems.DRUID_WAND_CORE);
        simpleItem(ModItems.PYROMANCER_WAND_CORE);
        simpleItem(ModItems.CRYOMANCER_WAND_CORE);
        simpleItem(ModItems.ELECTROMANCER_WAND_CORE);
        simpleItem(ModItems.EVOKER_WAND_CORE);
        simpleItem(ModItems.BLOOD_WAND_CORE);
        simpleItem(ModItems.ENDER_WAND_CORE);
        simpleItem(ModItems.ECTOPLASM);
        simpleItem(ModItems.FOREST_HEART);
        simpleItem(ModItems.RAVEN_FEATHER);
        simpleItem(ModItems.THUNDERSTORM_BOTTLE);
        simpleItem(ModItems.BURNT_SUGAR);
        simpleItem(ModItems.MIRROR);
        //Staff and wands
        handheldItem(ModItems.WOODEN_WAND);
        handheldItem(ModItems.CRYOMANCER_WAND);
        handheldItem(ModItems.ELECTROMANCER_WAND);
        handheldItem(ModItems.DRUID_WAND);
        handheldItem(ModItems.PYROMANCER_WAND);
        handheldItem(ModItems.BLOOD_WAND);
        handheldItem(ModItems.EVOKER_WAND);
        handheldItem(ModItems.ENDER_WAND);
        handheldItem(ModItems.SACRED_SYMBOL);
        //Projectiles
        simpleItem(ModItems.FIREBALT);
        //eggs
        spawnEggItem(ModItems.GHOST_SPAWN_EGG);
        spawnEggItem(ModItems.UNDEAD_SPIRIT_SPAWN_EGG);
        spawnEggItem(ModItems.RAVEN_SPAWN_EGG);
        spawnEggItem(ModItems.LESHY_SPAWN_EGG);
        spawnEggItem(ModItems.GREEMON_SPAWN_EGG);
        spawnEggItem(ModItems.GREEN_HAG_SPAWN_EGG);
        spawnEggItem(ModItems.GOBLIN_SHAMAN_SPAWN_EGG);
        spawnEggItem(ModItems.GOBLIN_WARRIOR_SPAWN_EGG);
        //Armor
        simpleItem(ModItems.MAID_DRESS);
        simpleItem(ModItems.MAID_CAP);


    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(DnMmod.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder curioItem(RegistryObject<CurioBaseItem> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(DnMmod.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem (RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(DnMmod.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder saplingItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(DnMmod.MOD_ID, "block/" + item.getId().getPath()));
    }

    private ItemModelBuilder spawnEggItem (RegistryObject<ForgeSpawnEggItem> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/template_spawn_egg"));
    }
}
