package net.chixozhmix.dnmmod.configs;

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
    private static Map<String, Supplier<Item>> cachedComponents = null;

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> spellEntries;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Spell component configuration")
                    .push("spell_components");

            spellEntries = builder
                    .comment("List of spell components in format: \"spell_class|item_id\"",
                            "Example: \"io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell|minecraft:rotten_flesh\"",
                            "You can change these values freely")
                    .defineList("entries", getDefaultEntries(),
                            obj -> obj instanceof String && ((String) obj).contains("|"));

            builder.pop();
        }

        private List<String> getDefaultEntries() {
            List<String> entries = new ArrayList<>();

            // Iron's Spells
            entries.add("io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell|minecraft:rotten_flesh");
            entries.add("io.redspace.ironsspellbooks.spells.holy.GreaterHealSpell|minecraft:glistering_melon_slice");
            entries.add("io.redspace.ironsspellbooks.spells.ender.SummonEnderChestSpell|minecraft:chest");
            entries.add("io.redspace.ironsspellbooks.spells.ender.BlackHoleSpell|minecraft:lodestone");
            entries.add("io.redspace.ironsspellbooks.spells.ender.RecallSpell|dnmmod:mirror");
            entries.add("io.redspace.ironsspellbooks.spells.fire.WallOfFireSpell|minecraft:lava_bucket");
            entries.add("io.redspace.ironsspellbooks.spells.fire.BurningDashSpell|dnmmod:burnt_sugar");
            entries.add("io.redspace.ironsspellbooks.spells.evocation.GustSpell|minecraft:feather");
            entries.add("io.redspace.ironsspellbooks.spells.evocation.InvisibilitySpell|minecraft:glass_bottle");
            entries.add("io.redspace.ironsspellbooks.spells.evocation.ArrowVolleySpell|minecraft:arrow");
            entries.add("io.redspace.ironsspellbooks.spells.ice.SummonPolarBearSpell|minecraft:cod");
            entries.add("io.redspace.ironsspellbooks.spells.lightning.BallLightningSpell|irons_spellbooks:lightning_bottle");
            entries.add("io.redspace.ironsspellbooks.spells.lightning.ChargeSpell|irons_spellbooks:energized_core");
            entries.add("io.redspace.ironsspellbooks.spells.lightning.LightningBoltSpell|minecraft:lightning_rod");
            entries.add("io.redspace.ironsspellbooks.spells.lightning.LightningLanceSpell|dnmmod:iron_trident");
            entries.add("io.redspace.ironsspellbooks.spells.lightning.ThunderstormSpell|dnmmod:thunderstorm_bottle");
            entries.add("io.redspace.ironsspellbooks.spells.lightning.ChainLightningSpell|irons_spellbooks:heavy_chain");
            entries.add("io.redspace.ironsspellbooks.spells.nature.EarthquakeSpell|minecraft:coarse_dirt");
            entries.add("io.redspace.ironsspellbooks.spells.blood.SacrificeSpell|dnmmod:ritual_dagger");


            // TravelOptics
            if (ModCapabilities.isTravelOpticsLoaded()) {
                entries.add("com.gametechbc.traveloptics.spells.blood.VigorSiphonSpell|minecraft:chain");
                entries.add("com.gametechbc.traveloptics.spells.fire.LavaBombSpell|minecraft:magma_block");
                entries.add("com.gametechbc.traveloptics.spells.ice.CursedRevenantsSpell|irons_spellbooks:frozen_bone_shard");
                entries.add("com.gametechbc.traveloptics.spells.fire.AnnihilationSpell|alexscaves:uranium_shard");
                entries.add("com.gametechbc.traveloptics.spells.ice.AxeOfTheDoomedSpell|cataclysm:cursium_ingot");
                entries.add("com.gametechbc.traveloptics.spells.blood.VigorSiphonSpell|minecraft:chain");
                entries.add("com.gametechbc.traveloptics.spells.fire.LavaBombSpell|minecraft:magma_block");
                entries.add("com.gametechbc.traveloptics.spells.ice.CursedRevenantsSpell|irons_spellbooks:frozen_bone_shard");
                entries.add("com.gametechbc.traveloptics.spells.fire.AnnihilationSpell|alexscaves:uranium_shard");
                entries.add("com.gametechbc.traveloptics.spells.ice.AxeOfTheDoomedSpell|cataclysm:cursium_ingot");
                entries.add("com.gametechbc.traveloptics.spells.aqua.HeraldOfAcropolisSpell|alexscaves:trilocaris_bucket");
                entries.add("com.gametechbc.traveloptics.spells.fire.IgnitedOnslaughtSpell|minecraft:blaze_rod");
                entries.add("com.gametechbc.traveloptics.spells.nature.PrimalPackSpell|alexscaves:dinosaur_nugget");
                entries.add("com.gametechbc.traveloptics.spells.aqua.RainfallSpell|minecraft:water_bucket");
                entries.add("com.gametechbc.traveloptics.spells.holy.SummonDesertDwellers|cataclysm:koboleton_bone");
                entries.add("com.gametechbc.traveloptics.spells.aqua.TidalGraspSpell|alexscaves:sea_glass_shards");
                entries.add("com.gametechbc.traveloptics.spells.aqua.VortexOfTheDeepSpell|minecraft:nautilus_shell");
                DnMmod.LOGGER.info("Added TravelOptics spell components to config");
            }

            // Alshanex Familiars
            if (ModCapabilities.isAlshanexFamiliarsLoaded()) {
                entries.add("net.alshanex.alshanex_familiars.spells.LullabySpell|minecraft:lily_of_the_valley");
                entries.add("net.alshanex.alshanex_familiars.spells.ExplosionMelodySpell|minecraft:jukebox");
                DnMmod.LOGGER.info("Added Alshanex Familiars spell components to config");
            }

            // Geomancy Plus
            if (ModCapabilities.isGeomancyPlusLoaded()) {
                entries.add("com.gametechbc.gtbcs_geomancy_plus.spells.geo.TremorSpikeSpell|minecraft:pointed_dripstone");
                entries.add("com.gametechbc.gtbcs_geomancy_plus.spells.geo.TremorStepSpell|minecraft:raw_iron_block");
                entries.add("com.gametechbc.gtbcs_geomancy_plus.spells.geo.ChunkerSpell|minecraft:dirt");
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

    public static Map<String, Supplier<Item>> getSpellComponents() {
        if (!needsReload && cachedComponents != null) {
            return cachedComponents;
        }

        Map<String, Supplier<Item>> result = new HashMap<>();

        // Безопасное получение значений из конфига
        List<? extends String> entries = COMMON.spellEntries.get();
        if (entries == null) {
            DnMmod.LOGGER.warn("Spell entries is null, returning empty map");
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

            String spellClass = parts[0];
            String itemId = parts[1];

            if (shouldSkipSpell(spellClass)) {
                continue;
            }

            result.put(spellClass, createItemSupplier(itemId));
        }

        cachedComponents = result;
        needsReload = false;
        return result;
    }

    private static boolean shouldSkipSpell(String spellClass) {
        if (spellClass.startsWith("com.gametechbc.traveloptics") && !ModCapabilities.isTravelOpticsLoaded()) {
            return true;
        }
        if (spellClass.startsWith("com.gametechbc.gtbcs_geomancy_plus") && !ModCapabilities.isGeomancyPlusLoaded()) {
            return true;
        }
        if (spellClass.startsWith("net.alshanex.alshanex_familiars") && !ModCapabilities.isAlshanexFamiliarsLoaded()) {
            return true;
        }
        if (spellClass.contains("alexscaves") && !ModCapabilities.isAlexsCavesLoaded()) {
            return true;
        }
        return false;
    }

    private static Supplier<Item> createItemSupplier(String itemId) {
        return () -> {
            ResourceLocation itemLoc = ResourceLocation.tryParse(itemId);
            if (itemLoc != null && ForgeRegistries.ITEMS.containsKey(itemLoc)) {
                return ForgeRegistries.ITEMS.getValue(itemLoc);
            }
            DnMmod.LOGGER.debug("Item not found for component: {}, using AIR", itemId);
            return Items.AIR;
        };
    }
}
