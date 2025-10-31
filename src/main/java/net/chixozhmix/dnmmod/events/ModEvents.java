package net.chixozhmix.dnmmod.events;

import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.entity.ModEntityType;
import net.chixozhmix.dnmmod.entity.custom.UndeadSpiritEntity;
import net.chixozhmix.dnmmod.entity.goblin_shaman.GoblinShamanEntity;
import net.chixozhmix.dnmmod.entity.green_hag.GreenHagEntity;
import net.chixozhmix.dnmmod.entity.leshy.LeshyEntity;
import net.chixozhmix.dnmmod.entity.raven.RavenEntity;
import net.chixozhmix.dnmmod.entity.spell.cloud_dagger.CloudDagger;
import net.chixozhmix.dnmmod.entity.summoned.SummonedRavenEntity;
import net.chixozhmix.dnmmod.entity.summoned.SummonedUndeadSpirit;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DnMmod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityType.MAGIC_DAGGER.get(), CloudDagger.createLivingAttributes().build());
        event.put(ModEntityType.UNDEAD_SPIRIT.get(), UndeadSpiritEntity.createAttributes());
        event.put(ModEntityType.SUMMONED_UNDEAD_SPIRIT.get(), SummonedUndeadSpirit.createAttributes());
        event.put(ModEntityType.GOBLIN_SHAMAN.get(), GoblinShamanEntity.prepareAttributes().build());
        event.put(ModEntityType.GREEN_HAG.get(), GreenHagEntity.prepareAttributes().build());
        event.put(ModEntityType.RAVEN.get(), RavenEntity.createAttributes());
        event.put(ModEntityType.SUMMON_RAVEN.get(), SummonedRavenEntity.createAttributes());
        event.put(ModEntityType.LESHY.get(), LeshyEntity.prepareAttributes().build());
    }
}
