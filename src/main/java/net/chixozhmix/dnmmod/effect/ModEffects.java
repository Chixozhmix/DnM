package net.chixozhmix.dnmmod.effect;

import io.redspace.ironsspellbooks.effect.SummonTimer;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.effect.custom.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, DnMmod.MOD_ID);

    public static final RegistryObject<MobEffect> MAGE_ARMOR =
            EFFECTS.register("mage_armor", MageArmorEffect::new);
    public static final RegistryObject<MobEffect> ACID =
            EFFECTS.register("acid_effect", () -> new AcidEffect(MobEffectCategory.HARMFUL, 0x4CAF50, 1.0f));
    public static final RegistryObject<MobEffect> SUMMON_UNDEAD_SPIRIT =
            EFFECTS.register("summon_undead_spirit", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 12495141));
    public static final RegistryObject<MobEffect> SUMMON_FLAME_ATRONACH =
            EFFECTS.register("summon_flame_atronach", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 12495141));
    public static final RegistryObject<MobEffect> SUMMON_RAVEN =
            EFFECTS.register("summon_raven", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 12495141));
    public static final RegistryObject<MobEffect> PHANTOM_EFFECT =
            EFFECTS.register("phantom_effect", () -> new PhantomEffect(MobEffectCategory.BENEFICIAL, 0x78938c));
    public static final RegistryObject<MobEffect> SHRINK_EFFECT = EFFECTS.register("shrink_effect", () -> new ShrinkEffect());
    public static final RegistryObject<MobEffect> AGATHYS_ARMOR = EFFECTS.register("agathys_armor", AgathysArmor::new);
    public static final RegistryObject<MobEffect> THICK_OF_FIGHT = EFFECTS.register("thick_of_fight", ThickOfFight::new);
    public static final RegistryObject<MobEffect> CORPSE_POISON = EFFECTS.register("corpse_poison", () -> new CorpsePoison(1));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
