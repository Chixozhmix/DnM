package net.chixozhmix.dnmmod.items.custom;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.chixozhmix.dnmmod.items.UniqArmorMaterials;
import net.chixozhmix.dnmmod.items.client.VoileOfDeathModel;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.List;

public class VoileOfDeathArmor extends ExtendedArmorItem {
    public VoileOfDeathArmor(Type type, Properties properties) {
        super(UniqArmorMaterials.VOILE_OF_DEATH, type, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        super.onArmorTick(stack, level, player);

        if(level.isClientSide)
            return;

        float currentHP = player.getHealth();
        float maxHP = player.getMaxHealth();

        if(player.getItemBySlot(EquipmentSlot.CHEST).getItem() != this)
            return;

        if(currentHP <= maxHP/2) {
            if(!player.hasEffect(ModEffects.PHANTOM_EFFECT.get()) && !player.hasEffect(MobEffectRegistry.TRUE_INVISIBILITY.get()))
            {
                player.addEffect(new MobEffectInstance(
                        ModEffects.PHANTOM_EFFECT.get(),
                        200,
                        0,
                        false,
                        false
                ));

                player.addEffect(new MobEffectInstance(
                        MobEffectRegistry.TRUE_INVISIBILITY.get(),
                        200,
                        0,
                        false,
                        false
                ));
            }
        }
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        pTooltipComponents.add(Component.translatable("tooltip.dnmmod.voile_of_death"));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer(new VoileOfDeathModel());
    }
}
