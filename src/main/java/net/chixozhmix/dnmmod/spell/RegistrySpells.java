package net.chixozhmix.dnmmod.spell;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.spell.evocation.CloudDaggerSpell;
import net.chixozhmix.dnmmod.spell.evocation.MageArmorSpell;
import net.chixozhmix.dnmmod.spell.evocation.NightVisionSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RegistrySpells {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY, DnMmod.MOD_ID);

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static final RegistryObject<AbstractSpell> MAGE_ARMOR = registerSpell(new MageArmorSpell());
    public static final RegistryObject<AbstractSpell> NIGHT_VISION = registerSpell(new NightVisionSpell());
    public static final RegistryObject<AbstractSpell> CLOUD_DAGGER = registerSpell(new CloudDaggerSpell());
    public static final RegistryObject<AbstractSpell> ICE_DAGGER = registerSpell(new IceDaggerSpell());


    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }
}