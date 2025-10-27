package net.chixozhmix.dnmmod.items.custom;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HagEye extends Item implements IPresetSpellContainer {
    public HagEye(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        pTooltipComponents.add(Component.translatable("tooltip.dnmmod.hag_eye"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        pPlayer.addEffect(
                new MobEffectInstance(
                        MobEffects.NIGHT_VISION,
                        1200,
                        0,
                        false,
                        true,
                        true
                ));

        AABB area = new AABB(pPlayer.blockPosition()).inflate(15);

        List<LivingEntity> entities = pLevel.getEntitiesOfClass(LivingEntity.class, area);

        for(LivingEntity target : entities) {
            if (target == pPlayer) continue;

            target.addEffect(
                    new MobEffectInstance(
                            MobEffects.GLOWING,
                            1200,
                            0,
                            false,
                            true,
                            true
                    ));

            // Устанавливаем кулдаун
            pPlayer.getCooldowns().addCooldown(this, 2400);
            pPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack != null) {
            if (!ISpellContainer.isSpellContainer(itemStack)) {
                ISpellContainer spellContainer = ISpellContainer.create(1, true, false);
                spellContainer.addSpell((AbstractSpell) SpellRegistry.POISON_ARROW_SPELL.get(), 4, true, itemStack);
                spellContainer.save(itemStack);
            }

        }
    }
}
