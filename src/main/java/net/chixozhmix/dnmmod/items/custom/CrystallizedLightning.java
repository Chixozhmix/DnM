package net.chixozhmix.dnmmod.items.custom;

import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrystallizedLightning extends Item {
    public CrystallizedLightning(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        HitResult result = Utils.raycastForEntity(pLevel, pPlayer, 16.0F, true, 1.0F);
        Vec3 pos = result.getLocation();

        if(!pLevel.isClientSide) {
            LightningBolt lightningBolt = (LightningBolt) EntityType.LIGHTNING_BOLT.create(pLevel);
            lightningBolt.setVisualOnly(false);
            lightningBolt.setDamage(10.0F);
            lightningBolt.moveTo(pos);
            pLevel.addFreshEntity(lightningBolt);
        }

        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        itemStack.shrink(1);

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        pTooltipComponents.add(Component.translatable("tooltip.dnmmod.crystallized_lightning"));
    }
}
