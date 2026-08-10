package net.chixozhmix.dnmmod.events;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.entity.reaper.ReaperEntity;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEvent {
    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();

        if (!(target instanceof Player)) return;

        Entity source = event.getSource().getEntity();

        //Щит маны
        if(target.hasEffect(ModEffects.MANA_SHIELD.get())) {
            float damage = event.getAmount();

            MagicData magicData = MagicData.getPlayerMagicData(target);

            float mana = SpellUtils.getCurrentMana((Player) target);

            if(mana <= 0)
                return;

            float manaCost = (float) (damage + (target.getAttributeValue(AttributeRegistry.MAX_MANA.get()) * 0.1f));

            if(mana >= manaCost) {
                magicData.setMana(mana - manaCost);
                System.out.println(manaCost);
                event.setCanceled(true);
            } else {
                magicData.setMana(0);

                event.setAmount(damage - mana);
            }
        }

        //эффект Шепота Смерти
        if (!(source instanceof LivingEntity attacker)) return;

        if (attacker.getMobType() == MobType.UNDEAD
                && attacker.getClass() != ReaperEntity.class && !target.hasEffect(ModEffects.REAPER_EFFECT.get())) {

            RandomSource random = target.getRandom();

            if (random.nextFloat() <= 0.01F) {
                target.addEffect(new MobEffectInstance(
                        ModEffects.REAPER_EFFECT.get(),
                        600,
                        0
                ));
            }
        }
    }
}
