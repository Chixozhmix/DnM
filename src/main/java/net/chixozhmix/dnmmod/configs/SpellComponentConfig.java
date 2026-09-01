package net.chixozhmix.dnmmod.configs;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.Util.ModCapabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SpellComponentConfig {
    private static boolean needsReload = true;
    private static Map<ResourceLocation, Supplier<Item>> cachedComponents = null;

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> spellEntries;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Spell component configuration")
                    .push("spell_components");

            spellEntries = builder
                    .comment("List of spell components in format: \"modid:spell_name|item_id\"",
                            "Example: \"irons_spellbooks:raise_dead|minecraft:rotten_flesh\"",
                            "You can change these values freely")
                    .defineList("entries", getDefaultEntries(),
                            obj -> obj instanceof String && ((String) obj).contains("|"));

            builder.pop();
        }

        private List<String> getDefaultEntries() {
            List<String> entries = new ArrayList<>();

            // Iron's Spells
            entries.add("irons_spellbooks:raise_dead|minecraft:rotten_flesh");
            entries.add("irons_spellbooks:greater_heal|minecraft:glistering_melon_slice");
            entries.add("irons_spellbooks:summon_ender_chest|minecraft:chest");
            entries.add("irons_spellbooks:black_hole|minecraft:lodestone");
            entries.add("irons_spellbooks:recall|dnmmod:mirror");
            entries.add("irons_spellbooks:wall_of_fire|minecraft:lava_bucket");
            entries.add("irons_spellbooks:burning_dash|dnmmod:burnt_sugar");
            entries.add("irons_spellbooks:gust|minecraft:feather");
            entries.add("irons_spellbooks:invisibility|minecraft:glass_bottle");
            entries.add("irons_spellbooks:arrow_volley|minecraft:arrow");
            entries.add("irons_spellbooks:summon_polar_bear|minecraft:cod");
            entries.add("irons_spellbooks:ball_lightning|irons_spellbooks:lightning_bottle");
            entries.add("irons_spellbooks:charge|irons_spellbooks:energized_core");
            entries.add("irons_spellbooks:lightning_bolt|minecraft:lightning_rod");
            //entries.add("irons_spellbooks:lightning_lance|dnmmod:iron_trident"); заменить компонент или убрать
            entries.add("irons_spellbooks:thunderstorm|dnmmod:crystallized_lightning");
            entries.add("irons_spellbooks:chain_lightning|irons_spellbooks:heavy_chain_necklace");
            entries.add("irons_spellbooks:earthquake|minecraft:coarse_dirt");
            entries.add("irons_spellbooks:sacrifice|dnmmod:ritual_dagger");

            // DnM
            entries.add("dnmmod:burial_ground|minecraft:soul_sand");
            entries.add("dnmmod:summon_undead_spirit|minecraft:skeleton_skull");
            entries.add("dnmmod:contagion|dnmmod:greemon_fang");
            entries.add("dnmmod:cloud_daggers|minecraft:glass");
            entries.add("dnmmod:chromatic_orb|minecraft:diamond");
            entries.add("dnmmod:summon_raven|dnmmod:raven_feather");

            // TravelOptics
            if (ModCapabilities.isTravelOpticsLoaded()) {
                entries.add("traveloptics:vigor_siphon|minecraft:chain");
                entries.add("traveloptics:lava_bomb|minecraft:magma_block");
                entries.add("traveloptics:cursed_revenants|irons_spellbooks:frozen_bone_shard");
                entries.add("traveloptics:annihilation|alexscaves:uranium_shard");
                entries.add("traveloptics:axe_of_the_doomed|cataclysm:cursium_ingot");
                entries.add("traveloptics:herald_of_acropolis|alexscaves:trilocaris_bucket");
                entries.add("traveloptics:ignited_onslaught|minecraft:blaze_rod");
                entries.add("traveloptics:primal_pack|alexscaves:dinosaur_nugget");
                entries.add("traveloptics:rainfall|minecraft:water_bucket");
                entries.add("traveloptics:summon_desert_dwellers|cataclysm:koboleton_bone");
                entries.add("traveloptics:tidal_grasp|alexscaves:sea_glass_shards");
                entries.add("traveloptics:vortex_of_the_deep|minecraft:nautilus_shell");
                DnMmod.LOGGER.info("Added TravelOptics spell components to config");
            }

            // Alshanex Familiars
            if (ModCapabilities.isAlshanexFamiliarsLoaded()) {
                entries.add("alshanex_familiars:lullaby|minecraft:lily_of_the_valley");
                entries.add("alshanex_familiars:explosion_melody|minecraft:jukebox");
                DnMmod.LOGGER.info("Added Alshanex Familiars spell components to config");
            }

            // Geomancy Plus
            if (ModCapabilities.isGeomancyPlusLoaded()) {
                entries.add("gtbcs_geomancy_plus:tremor_spike|minecraft:pointed_dripstone");
                entries.add("gtbcs_geomancy_plus:tremor_step|minecraft:raw_iron_block");
                entries.add("gtbcs_geomancy_plus:chunker|minecraft:dirt");
                DnMmod.LOGGER.info("Added Geomancy Plus spell components to config");
            }

            return entries;
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, "dnmmod-spell-components.toml");
    }

    public static void reload() {
        cachedComponents = null;
        needsReload = true;
        DnMmod.LOGGER.info("Spell components cache cleared");
    }

    public static Map<ResourceLocation, Supplier<Item>> getSpellComponents() {
        if (!needsReload && cachedComponents != null) {
            return cachedComponents;
        }

        Map<ResourceLocation, Supplier<Item>> result = new HashMap<>();

        List<? extends String> entries = COMMON.spellEntries.get();
        if (entries == null) {
            return result;
        }

        for (String entry : entries) {
            if (entry == null || !entry.contains("|")) {
                continue;
            }

            String[] parts = entry.split("\\|", 2);
            if (parts.length != 2) {
                continue;
            }

            ResourceLocation spellLoc = ResourceLocation.tryParse(parts[0]);
            String itemId = parts[1];

            if (spellLoc == null) {
                continue;
            }

            // Проверка модов (если мод не установлен, пропускаем)
            if (shouldSkipMod(spellLoc.getNamespace())) {
                continue;
            }

            result.put(spellLoc, createItemSupplier(itemId));
        }

        cachedComponents = result;
        needsReload = false;
        return result;
    }

    private static boolean shouldSkipMod(String modId) {
        if (modId.equals("traveloptics") && !ModCapabilities.isTravelOpticsLoaded()) return true;
        if (modId.equals("gtbcs_geomancy_plus") && !ModCapabilities.isGeomancyPlusLoaded()) return true;
        if (modId.equals("alshanex_familiars") && !ModCapabilities.isAlshanexFamiliarsLoaded()) return true;
        return false;
    }

    private static Supplier<Item> typeItemSupplier(String itemId) { // оставлено для совместимости, но лучше использовать createItemSupplier
        return createItemSupplier(itemId);
    }

    private static Supplier<Item> createItemSupplier(String itemId) {
        return () -> {
            ResourceLocation itemLoc = ResourceLocation.tryParse(itemId);
            if (itemLoc != null && ForgeRegistries.ITEMS.containsKey(itemLoc)) {
                return ForgeRegistries.ITEMS.getValue(itemLoc);
            }
            return Items.AIR;
        };
    }
}