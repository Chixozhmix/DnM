package net.chixozhmix.dnmmod.Util;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.chixozhmix.dnmmod.entity.evil.reaper.ReaperEntity;
import net.chixozhmix.dnmmod.items.curio.ProtectiveBrasletItem;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.UUID;

public class EventUtils {
    //Щит маны
    public static void ManaShield(LivingHurtEvent event, LivingEntity target) {
        if(target.hasEffect(ModEffects.MANA_SHIELD.get())) {
            float damage = event.getAmount();
            MagicData magicData = MagicData.getPlayerMagicData(target);
            float mana = SpellUtils.getCurrentMana((Player) target);

            if(mana <= 0) return;

            float manaCost = (float) (damage + (target.getAttributeValue(AttributeRegistry.MAX_MANA.get()) * 0.1f));

            if(mana >= manaCost) {
                magicData.setMana(mana - manaCost);
                event.setCanceled(true);
            } else {
                magicData.setMana(0);

                event.setAmount(damage - mana);
            }
        }
    }

    //Браслет Защиты
    public static void ProtectiveBracelet(LivingHurtEvent event, LivingEntity target, Entity source) {
        if (source == null) return;

        CuriosApi.getCuriosHelper()
                .findCurios(target, ModItems.PROTECTIVE_BRASLET.get())
                .forEach(slotResult -> {
                    ItemStack bracelet = slotResult.stack();
                    UUID owner = ProtectiveBrasletItem.getOwner(bracelet);
                    if (owner != null && source.getUUID().equals(owner)) {
                        event.setCanceled(true);
                    }
                });
    }

    //Шепот смерти
    public static void DeathWisp(LivingHurtEvent event, LivingEntity target, Entity source) {
        if (!(source instanceof LivingEntity attacker)) return;

        if (attacker.getMobType() == MobType.UNDEAD && attacker.getClass() != ReaperEntity.class && !target.hasEffect(ModEffects.REAPER_EFFECT.get())) {
            RandomSource random = target.getRandom();

            if (random.nextFloat() <= 0.01F)
                target.addEffect(new MobEffectInstance(ModEffects.REAPER_EFFECT.get(), 600, 0));
        }
    }
}
