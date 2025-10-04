package net.chixozhmix.dnmmod.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class Battleaxe extends AxeItem {
    public Battleaxe(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        // Проверяем, является ли атакующий игроком
        if (pAttacker instanceof Player player) {
            // Получаем значение "силы атаки" (attack strength)
            float attackStrength = player.getAttackStrengthScale(0.5F);

            // Если индикатор атаки не заполнен полностью (меньше 1.0), наносим 0 урона
            if (attackStrength < 1.0F) {

                pTarget.hurt(pAttacker.damageSources().mobAttack(pAttacker), 0);

                return false; // Возвращаем true, так как атака "произошла", но без урона
            }
        }

        // Если индикатор атаки заполнен полностью или атакующий не игрок - наносим нормальный урон
        Random random = new Random();
        float damage = random.nextFloat(8.0f) + 1; // Урон от 1 до 8

        if(pAttacker.hasEffect(MobEffects.DAMAGE_BOOST)) {
            MobEffectInstance strengthEffect = pAttacker.getEffect(MobEffects.DAMAGE_BOOST);

            if(strengthEffect != null) {
                int amplifier = strengthEffect.getAmplifier();

                float damageModifier = 1.0F + (amplifier + 1) * 0.5F;

                damage *= damageModifier;
            }
        }

        System.out.println("Урон: " + damage);

        // Применяем урон
        pTarget.hurt(pAttacker.damageSources().mobAttack(pAttacker), damage);



        // Повреждаем предмет
        pStack.hurtAndBreak(1, pAttacker, (entity) -> {
            entity.broadcastBreakEvent(pAttacker.getUsedItemHand());
        });

        return true;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        // Запрещаем зачарования, влияющие на урон
        if (enchantment == Enchantments.SHARPNESS ||
                enchantment == Enchantments.SMITE ||
                enchantment == Enchantments.BANE_OF_ARTHROPODS ||
                enchantment == Enchantments.SWEEPING_EDGE) {
            return false;
        }

        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        // Заголовок
        pTooltipComponents.add(Component.translatable("tooltip.dnmmod.weapon.mainhand.title")
                .withStyle(ChatFormatting.GRAY));

        // Урон (с динамическим значением)
        pTooltipComponents.add(Component.translatable("tooltip.dnmmod.battleaxe.damage")
                .withStyle(ChatFormatting.DARK_GREEN));

        // Скорость
        pTooltipComponents.add(Component.translatable("tooltip.dnmmod.battleaxe.speed")
                .withStyle(ChatFormatting.DARK_GREEN));

        // Пустая строка для разделения
        pTooltipComponents.add(Component.empty());
    }
}
