package net.chixozhmix.dnmmod.api.registers;

import com.gametechbc.traveloptics.api.init.TravelopticsAttributes;
import com.gametechbc.traveloptics.util.TravelopticsDamageTypes;
import com.gametechbc.traveloptics.util.TravelopticsTags;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.chixozhmix.dnmmod.Util.ModTags;
import net.chixozhmix.dnmmod.sound.SoundsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class DnMSchools {
    public static final DeferredRegister<SchoolType> DNM_SCHOOLS;
    public static final ResourceLocation NECRO_RESOURCE;
    public static final RegistryObject<SchoolType> NECRO;

    public static RegistryObject<SchoolType> registerSchool(SchoolType schoolType) {
        return DNM_SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }

    static {
        DNM_SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, "dnmmod");
        NECRO_RESOURCE = new ResourceLocation("dnmmod", "necro");

        // Создаем LazyOptional для SoundEvent
        LazyOptional<SoundEvent> necroMagicSound = LazyOptional.of(() ->
                SoundsRegistry.NECRO_MAGIC.get() // Используем .get() для получения SoundEvent
        );

        ResourceLocation var10002 = NECRO_RESOURCE;
        TagKey var10003 = ModTags.NECRO_FOCUS;
        MutableComponent var10004 = Component.translatable("school.dnmmod.necro").withStyle((style) -> style.withColor(0x481265));
        RegistryObject var10005 = DnMAttributes.NECRO_SPELL_POWER;
        Objects.requireNonNull(var10005);
        LazyOptional var0 = LazyOptional.of(var10005::get);
        RegistryObject var10006 = DnMAttributes.NECRO_MAGIC_RESIST;
        Objects.requireNonNull(var10006);
        LazyOptional var1 = LazyOptional.of(var10006::get);
        NECRO = registerSchool(new SchoolType(var10002, var10003, var10004, var0, var1, necroMagicSound, DnMDamageType.NECRO_MAGIC));
    }
}
