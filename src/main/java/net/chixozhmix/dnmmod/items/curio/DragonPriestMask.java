package net.chixozhmix.dnmmod.items.curio;

import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.chixozhmix.chilib.utils.items.PropertiesHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DragonPriestMask extends CurioBaseItem {
    public DragonPriestMask() {
        super(PropertiesHelper.stackItemProperties(1).rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        pTooltipComponents.add(Component.translatable("tooltip.dnmmod.dragon_priest_mask"));
    }
}
