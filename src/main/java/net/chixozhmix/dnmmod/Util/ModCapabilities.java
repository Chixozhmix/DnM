package net.chixozhmix.dnmmod.Util;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraftforge.fml.ModList;

public class ModCapabilities {
    private static Boolean travelOpticsLoaded = null;
    private static Boolean geomancyLoaded = null;
    private static Boolean alshanexFamiliarsLoaded = null;
    private static Boolean alexsCavesLoaded = null;

    public static boolean isTravelOpticsLoaded() {
        if (travelOpticsLoaded == null) {
            travelOpticsLoaded = ModList.get().isLoaded("traveloptics") ||
                    isClassPresent("com.gametechbc.traveloptics.TravelOpticsMod");
            if (travelOpticsLoaded) {
                DnMmod.LOGGER.info("TravelOptics mod detected");
            } else {
                DnMmod.LOGGER.debug("TravelOptics mod not present");
            }
        }
        return travelOpticsLoaded;
    }

    public static boolean isGeomancyPlusLoaded() {
        if (geomancyLoaded == null) {
            geomancyLoaded = ModList.get().isLoaded("gtbcs_geomancy_plus") ||
                    isClassPresent("com.gametechbc.gtbcs_geomancy_plus.GeomancyPlus");
        }
        return geomancyLoaded;
    }

    public static boolean isAlshanexFamiliarsLoaded() {
        if (alshanexFamiliarsLoaded == null) {
            alshanexFamiliarsLoaded = ModList.get().isLoaded("alshanex_familiars") ||
                    isClassPresent("net.alshanex.alshanex_familiars.AlshanexFamiliars");
        }
        return alshanexFamiliarsLoaded;
    }

    public static boolean isAlexsCavesLoaded() {
        if (alexsCavesLoaded == null) {
            alexsCavesLoaded = ModList.get().isLoaded("alexscaves") ||
                    isClassPresent("com.github.alexmodguy.alexscaves.AlexsCaves");
        }
        return alexsCavesLoaded;
    }

    private static boolean isClassPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
