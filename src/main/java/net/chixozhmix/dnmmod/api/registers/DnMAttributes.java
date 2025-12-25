//package net.chixozhmix.dnmmod.api.registers;
//
//
//import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
//import net.chixozhmix.dnmmod.DnMmod;
//import net.minecraft.world.entity.ai.attributes.Attribute;
//import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;
//
//@Mod.EventBusSubscriber(
//        modid = DnMmod.MOD_ID,
//        bus = Mod.EventBusSubscriber.Bus.MOD
//)
//public class DnMAttributes {
//    public static final DeferredRegister<Attribute> ATTRIBUTES;
//    public static final RegistryObject<Attribute> NECRO_MAGIC_RESIST;
//    public static final RegistryObject<Attribute> NECRO_SPELL_POWER;
//
//    public static RegistryObject<Attribute> necroResistanceAttribute(String id) {
//        return ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicRangedAttribute("attribute.dnmmod." + id + "_magic_resist", (double)1.0F, (double)-100.0F, (double)100.0F)).setSyncable(true));
//    }
//
//    public static RegistryObject<Attribute> necroPowerAttribute(String id) {
//        return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicRangedAttribute("attribute.dnmmod." + id + "_spell_power", (double)1.0F, (double)-100.0F, (double)100.0F)).setSyncable(true));
//    }
//
//    @SubscribeEvent
//    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
//        event.getTypes().forEach((entity) -> {
//            event.add(entity, (Attribute)NECRO_SPELL_POWER.get());
//            event.add(entity, (Attribute)NECRO_MAGIC_RESIST.get());
//        });
//    }
//
//    static {
//        ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, "dnmmod");
//        NECRO_MAGIC_RESIST = necroResistanceAttribute("necro");
//        NECRO_SPELL_POWER = necroPowerAttribute("necro");
//    }
//}
