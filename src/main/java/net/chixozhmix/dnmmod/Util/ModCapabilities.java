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
            travelOpticsLoaded = ModList.get().isLoaded("traveloptics");
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
            geomancyLoaded = ModList.get().isLoaded("gtbcs_geomancy_plus");
        }
        return geomancyLoaded;
    }

    public static boolean isAlshanexFamiliarsLoaded() {
        if (alshanexFamiliarsLoaded == null) {
            alshanexFamiliarsLoaded = ModList.get().isLoaded("alshanex_familiars");
        }
        return alshanexFamiliarsLoaded;
    }

    public static boolean isAlexsCavesLoaded() {
        if (alexsCavesLoaded == null) {
            alexsCavesLoaded = ModList.get().isLoaded("alexscaves");
        }
        return alexsCavesLoaded;
    }
}