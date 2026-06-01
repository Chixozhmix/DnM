package net.chixozhmix.dnmmod.items.custom.armor;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.chixozhmix.dnmmod.Util.Utils;
import net.chixozhmix.dnmmod.items.UniqArmorMaterials;
import net.chixozhmix.dnmmod.items.client.WaningMoonArmorModel;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.List;
import java.util.UUID;

public class WaningMoonArmor extends ImbuableChestplateArmorItem {
    private static final UUID MANA_REGEN_BONUS_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    private static final UUID COOLDOWN_BONUS_UUID = UUID.fromString("a1b2c3d1-e5f6-7890-abcd-ef1234647891");

    public WaningMoonArmor(Type pType, Properties pProperties, AttributeContainer... attributes) {
        super(UniqArmorMaterials.WANING_MOON_ARMOR, pType, pProperties, attributes);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if(!level.isClientSide()) {
            if(Utils.hasFullSet(player)) {
                armorSetBonus(player);
            } else {
                removeAllBonuses(player);
            }
        }
    }

    private void armorSetBonus(Player player) {
        float currentHealth = player.getHealth();
        float maxHealth = player.getMaxHealth();
        float healthPercentage = currentHealth / maxHealth;

        float currentMana = SpellUtils.getCurrentMana(player);
        double maxMana = player.getAttribute(AttributeRegistry.MAX_MANA.get()).getValue();
        float manaPercentage = (float) (currentMana / maxMana);

        AttributeInstance cooldownReduction = player.getAttribute(AttributeRegistry.COOLDOWN_REDUCTION.get());
        AttributeInstance manaRegen = player.getAttribute(AttributeRegistry.MANA_REGEN.get());

        if(cooldownReduction != null && manaRegen != null) {
            boolean shouldHaveCooldownBonus = healthPercentage < 0.3f;
            boolean hasCooldownBonus = cooldownReduction.getModifier(COOLDOWN_BONUS_UUID) != null;

            if(shouldHaveCooldownBonus && !hasCooldownBonus) {
                AttributeModifier cooldownModifier = new AttributeModifier(
                        COOLDOWN_BONUS_UUID,
                        "Waning Moon Cooldown Bonus",
                        0.2,
                        AttributeModifier.Operation.MULTIPLY_BASE
                );
                cooldownReduction.addTransientModifier(cooldownModifier);
            } else if(!shouldHaveCooldownBonus && hasCooldownBonus) {
                cooldownReduction.removeModifier(COOLDOWN_BONUS_UUID);
            }

            boolean shouldHaveManaBonus = manaPercentage < 0.25f;
            boolean hasManaBonus = manaRegen.getModifier(MANA_REGEN_BONUS_UUID) != null;

            if(shouldHaveManaBonus && !hasManaBonus) {
                AttributeModifier manaModifier = new AttributeModifier(
                        MANA_REGEN_BONUS_UUID,
                        "Waning Moon Mana Bonus",
                        0.3,
                        AttributeModifier.Operation.MULTIPLY_BASE
                );
                manaRegen.addTransientModifier(manaModifier);
            } else if(!shouldHaveManaBonus && hasManaBonus) {
                manaRegen.removeModifier(MANA_REGEN_BONUS_UUID);
            }
        }
    }

    private void removeAllBonuses(Player player) {
        AttributeInstance cooldownReduction = player.getAttribute(AttributeRegistry.COOLDOWN_REDUCTION.get());
        AttributeInstance manaRegen = player.getAttribute(AttributeRegistry.MANA_REGEN.get());

        if(cooldownReduction != null && cooldownReduction.getModifier(COOLDOWN_BONUS_UUID) != null) {
            cooldownReduction.removeModifier(COOLDOWN_BONUS_UUID);
        }

        if(manaRegen != null && manaRegen.getModifier(MANA_REGEN_BONUS_UUID) != null) {
            manaRegen.removeModifier(MANA_REGEN_BONUS_UUID);
        }
    }

    /**Проверить, работает ли через утилиты**/

//    private boolean hasFullSet(Player player) {
//        ItemStack boots = player.getInventory().getArmor(0);
//        ItemStack leggings = player.getInventory().getArmor(1);
//        ItemStack chestplate = player.getInventory().getArmor(2);
//        ItemStack helmet = player.getInventory().getArmor(3);
//
//        return !helmet.isEmpty() && !chestplate.isEmpty() && !leggings.isEmpty() && !boots.isEmpty();
//    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.dnmmod.waning_moon_armor"));

        if(Screen.hasShiftDown())
            pTooltipComponents.add(Component.translatable("tooltip.dnmmod.waning_moon_armor.shift").withStyle(ChatFormatting.AQUA));
        else pTooltipComponents.add(Component.translatable("tooltip.dnmmod.down_shift").withStyle(ChatFormatting.GRAY));

    }

    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer(new WaningMoonArmorModel());
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        super.initializeSpellContainer(itemStack);
    }
}
