package net.chixozhmix.dnmmod.Util;


import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModTags {
    public static final TagKey<Item> NECRO_FOCUS;
    public static final TagKey<Item> COMPONENT;

    public static final TagKey<Block> NOT_PASSABLE;
    public static final TagKey<Block> ANTIBUILDER_IGNORES;
    public static final TagKey<Block> NO_DRAGON_BREAK;

    public static final TagKey<Structure> TAINTED_COMPASS_LOCATOR;

    static {
        NECRO_FOCUS = TagKey.create(Registries.ITEM, new ResourceLocation(DnMmod.MOD_ID, "necro_focus"));
        COMPONENT = TagKey.create(Registries.ITEM, new ResourceLocation(DnMmod.MOD_ID, "component"));
        NOT_PASSABLE = TagKey.create(Registries.BLOCK, new ResourceLocation(DnMmod.MOD_ID, "not_passable"));
        ANTIBUILDER_IGNORES = TagKey.create(Registries.BLOCK, new ResourceLocation(DnMmod.MOD_ID, "antibuilder_ignore"));
        NO_DRAGON_BREAK = TagKey.create(Registries.BLOCK, new ResourceLocation(DnMmod.MOD_ID, "no_dragon_break"));
        TAINTED_COMPASS_LOCATOR = TagKey.create(Registries.STRUCTURE, new ResourceLocation(DnMmod.MOD_ID, "tainted_compass_locator"));
    }
}
