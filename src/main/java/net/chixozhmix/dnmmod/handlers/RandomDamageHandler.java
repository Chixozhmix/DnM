package net.chixozhmix.dnmmod.handlers;

import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = DnMmod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RandomDamageHandler {
    private static final Random RANDOM = new Random();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            ItemStack itemStack = player.getMainHandItem();

            if (!itemStack.isEmpty() &&
                    (itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof AxeItem)) {

                float randomDamage = 0.0F;

                if (itemStack.getItem() instanceof SwordItem) {
                    randomDamage = RANDOM.nextFloat(1.0F, 8.0F);

                    if(player.hasEffect(MobEffects.DAMAGE_BOOST)) {
                        MobEffectInstance strengthEffect = player.getEffect(MobEffects.DAMAGE_BOOST);

                        if(strengthEffect != null) {
                            int amplifier = strengthEffect.getAmplifier();

                            float damageModifier = 1.0F + (amplifier + 1) * 0.5F;

                            randomDamage *= damageModifier;

                            System.out.println("Strength effect detected! Level: " + (amplifier + 1) + ", Damage multiplier: " + damageModifier);
                        }
                    }

                } else if (itemStack.getItem() instanceof AxeItem) {
                    randomDamage = RANDOM.nextFloat(1.0F, 8.0F);

                    if(player.hasEffect(MobEffects.DAMAGE_BOOST)) {
                        MobEffectInstance strengthEffect = player.getEffect(MobEffects.DAMAGE_BOOST);

                        if(strengthEffect != null) {
                            int amplifier = strengthEffect.getAmplifier();

                            float damageModifier = 1.0F + (amplifier + 1) * 0.5F;

                            randomDamage *= damageModifier;

                            System.out.println("Strength effect detected! Level: " + (amplifier + 1) + ", Damage multiplier: " + damageModifier);
                        }
                    }

                }

                float attackStrength = player.getAttackStrengthScale(0.5F);

                // Если индикатор атаки не заполнен полностью (меньше 1.0), наносим 0 урона
                if (attackStrength < 1.0F) {
                    randomDamage = 0.0F;
                }



                System.out.println("Overriding damage to: " + randomDamage + " (was: " + event.getAmount() + ")");
                event.setAmount(randomDamage);
            }
        }
    }
}
