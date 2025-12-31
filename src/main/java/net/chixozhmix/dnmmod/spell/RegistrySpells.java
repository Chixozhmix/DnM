package net.chixozhmix.dnmmod.spell;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.spell.blood.RayOfEnfeeblementSpell;
import net.chixozhmix.dnmmod.spell.blood.SummonUndeadSpiritSpell;
import net.chixozhmix.dnmmod.spell.evocation.*;
import net.chixozhmix.dnmmod.spell.fire.SummonFlameAtronach;
import net.chixozhmix.dnmmod.spell.ice.AgathysArmorSpell;
import net.chixozhmix.dnmmod.spell.ice.IceDaggerSpell;
import net.chixozhmix.dnmmod.spell.lightning.ThunderwaveSpell;
import net.chixozhmix.dnmmod.spell.nature.SummonRavenSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class RegistrySpells {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY, DnMmod.MOD_ID);

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static final RegistryObject<AbstractSpell> MAGE_ARMOR = registerSpell(new MageArmorSpell());
    public static final RegistryObject<AbstractSpell> NIGHT_VISION = registerSpell(new NightVisionSpell());
    public static final RegistryObject<AbstractSpell> CLOUD_DAGGER = registerSpell(new CloudDaggerSpell());
    public static final RegistryObject<AbstractSpell> ICE_DAGGER = registerSpell(new IceDaggerSpell());
    public static final RegistryObject<AbstractSpell> THUNDERWAVE = registerSpell(new ThunderwaveSpell());
    public static final RegistryObject<AbstractSpell> CAUSTIC_BREW = registerSpell(new CausticBrewSpell());
    public static final RegistryObject<AbstractSpell> CHROMATIC_ORB = registerSpell(new ChromaticOrbSpell());
    public static final RegistryObject<AbstractSpell> SUMMON_UNDEAD_SPIRIT = registerSpell(new SummonUndeadSpiritSpell());
    public static final RegistryObject<AbstractSpell> SUMMON_RAVEN = registerSpell(new SummonRavenSpell());
    public static final RegistryObject<AbstractSpell> SHRINKING = registerSpell(new ShrinkingSpell());
    public static final RegistryObject<AbstractSpell> AGATHYS_ARMOR_SPELL = registerSpell(new AgathysArmorSpell());
    public static final RegistryObject<AbstractSpell> THICK_OF_FIGHT = registerSpell(new ThickOfFightSpell());
    public static final RegistryObject<AbstractSpell> RAY_OF_ENFEEBLEMENT = registerSpell(new RayOfEnfeeblementSpell());
    public static final RegistryObject<AbstractSpell> SUMMON_FLAME_ATRONACH = registerSpell(new SummonFlameAtronach());

    //Optional
    public static Optional<RegistryObject<AbstractSpell>> KNOCK = Optional.empty();

    public static void registerOptionalSpells(IEventBus eventBus) {
        if (ModList.get().isLoaded("locks")) {
            KNOCK = Optional.of(registerSpell(new Knock()));
        }
    }

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
        registerOptionalSpells(eventBus);
    }
}