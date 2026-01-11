package net.chixozhmix.dnmmod.items.curio;

import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DragonPriestMask extends CurioBaseItem {
    public DragonPriestMask(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, tooltip, pIsAdvanced);

        tooltip.add(Component.translatable("tooltip.dnmmod.dragon_priest_mask"));
    }
}
