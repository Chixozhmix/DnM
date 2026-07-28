package net.chixozhmix.dnmmod.items.custom.armor;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.chixozhmix.dnmmod.Util.Utils;
import net.chixozhmix.dnmmod.api.misc.ISetArmor;
import net.chixozhmix.dnmmod.items.UniqArmorMaterials;
import net.chixozhmix.dnmmod.items.client.VestmentsArchitectModel;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.List;
import java.util.UUID;

public class VestmentsArchitectArmor extends ImbuableChestplateArmorItem implements ISetArmor {
    private static final UUID SPELL_RESIST_BONUS_UUID = UUID.fromString("a1a1c3d1-e8f6-7190-abcd-ef1234347891");
    private static final UUID MAX_HEALTH_MODIFIER_UUID = UUID.fromString("a2c1a3d2-e8f6-7181-abcd-ef0334347891");

    private static final String AURA_COOLDOWN_KEY = "vestments_architect_aura_cd";
    private static final int AURA_COOLDOWN = 40;

    public VestmentsArchitectArmor(Type type, Properties properties, AttributeContainer... attributes) {
        super(UniqArmorMaterials.VESTMENTS_ARCHITECT_ARMOR, type, properties, attributes);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new VestmentsArchitectModel());
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if(!level.isClientSide()) {
            if(level.getGameTime() % 10 == 0) {
                if(Utils.hasFullSet(player))
                    armorSetBonus(player);
                else
                    removeAllBonuses(player);
            }
        }
    }

    @Override
    public void armorSetBonus(Player player) {
        AttributeInstance spellResist = player.getAttribute(AttributeRegistry.SPELL_RESIST.get());
        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);

        float currentHealth = player.getHealth();
        float maxHealth = player.getMaxHealth();
        float healthPercentage = currentHealth / maxHealth;

        Level level = player.level();
        int cooldown = player.getPersistentData().getInt(AURA_COOLDOWN_KEY);

        if (spellResist != null && spellResist.getModifier(SPELL_RESIST_BONUS_UUID) == null) {
            spellResist.addTransientModifier(new AttributeModifier(
                    SPELL_RESIST_BONUS_UUID,
                    "Vestments Architect Resist Bonus",
                    0.2,
                    AttributeModifier.Operation.MULTIPLY_BASE
            ));
        }

        if(health != null && health.getModifier(MAX_HEALTH_MODIFIER_UUID) == null) {
            health.addTransientModifier(new AttributeModifier(
                    MAX_HEALTH_MODIFIER_UUID,
                    "Vestments Architect Health Bonus",
                    6.0,
                    AttributeModifier.Operation.ADDITION
            ));
            player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));
        }

        if (cooldown > 0) {
            player.getPersistentData().putInt(
                    AURA_COOLDOWN_KEY,
                    Math.max(0, cooldown - 10)
            );
        }
        else if (healthPercentage <= 0.3) {

            AABB area = player.getBoundingBox().inflate(5);

            List<LivingEntity> entities = level.getEntitiesOfClass(
                    LivingEntity.class,
                    area
            );

            for (LivingEntity target : entities) {

                if (target == player)
                    continue;

                target.hurt(
                        level.damageSources().magic(),
                        1
                );
            }


            player.getPersistentData().putInt(
                    AURA_COOLDOWN_KEY,
                    AURA_COOLDOWN
            );
        }

        player.removeEffect(MobEffects.WITHER);
    }

    @Override
    public void removeAllBonuses(Player player) {
        AttributeInstance spellResist = player.getAttribute(AttributeRegistry.SPELL_RESIST.get());
        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);

        if(spellResist != null && spellResist.getModifier(SPELL_RESIST_BONUS_UUID) != null) {
            spellResist.removeModifier(SPELL_RESIST_BONUS_UUID);
        }

        if(health != null && health.getModifier(MAX_HEALTH_MODIFIER_UUID) != null) {
            health.removeModifier(MAX_HEALTH_MODIFIER_UUID);
            player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.dnmmod.vestments_architect"));

        if(Screen.hasShiftDown())
            pTooltipComponents.add(Component.translatable("tooltip.dnmmod.vestments_architect.shift").withStyle(ChatFormatting.DARK_AQUA));
        else pTooltipComponents.add(Component.translatable("tooltip.dnmmod.down_shift").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        super.initializeSpellContainer(itemStack);
    }
}
