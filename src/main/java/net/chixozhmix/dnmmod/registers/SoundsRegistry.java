package net.chixozhmix.dnmmod.registers;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundsRegistry{
    public static final DeferredRegister<SoundEvent> SOUND_EVENT =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DnMmod.MOD_ID);

    public static final RegistryObject<SoundEvent> GHOST_AMBIENT = registerSoundEvent("ghost_ambient");
    public static final RegistryObject<SoundEvent> SUMMON_UNDEAD_SPIRIT = registerSoundEvent("summon_undead_spirit");
    public static final RegistryObject<SoundEvent> UNDEAD_SPIRIT = registerSoundEvent("undead_spirit");
    public static final RegistryObject<SoundEvent> RAVEN_AMBIENT = registerSoundEvent("raven_ambient");
    public static final RegistryObject<SoundEvent> SUMMON_RAVEN = registerSoundEvent("summon_raven");
    public static final RegistryObject<SoundEvent> GOBLIN_AMBIENT = registerSoundEvent("goblin_ambient");
    public static final RegistryObject<SoundEvent> GOBLIN_HURT = registerSoundEvent("goblin_hurt");
    public static final RegistryObject<SoundEvent> LESHY_AMBIENT = registerSoundEvent("leshy_ambient");
    public static final RegistryObject<SoundEvent> GREEMON_AMBIENT = registerSoundEvent("greemon_ambient");
    public static final RegistryObject<SoundEvent> NECRO_MAGIC = registerSoundEvent("necro_magic");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENT.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(DnMmod.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENT.register(eventBus);
    }
}
