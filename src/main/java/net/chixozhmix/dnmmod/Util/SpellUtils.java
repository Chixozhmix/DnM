package net.chixozhmix.dnmmod.Util;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.chixozhmix.dnmmod.compat.Curios;
import net.chixozhmix.dnmmod.items.custom.ComponentBag;
import net.chixozhmix.dnmmod.items.custom.MediumComponentBag;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import top.theillusivec4.curios.api.CuriosApi;

public class SpellUtils {
    //Проверка компонентов для заклинания
    public static boolean checkSpellComponent(LivingEntity entity, Item item) {

        if(entity.getItemInHand(InteractionHand.MAIN_HAND).getItem() == item ||
                entity.getItemInHand(InteractionHand.OFF_HAND).getItem() == item || !(entity instanceof Player) || ((Player) entity).isCreative()) {
                return true;
        }

        // Если предмет не в руках, проверяем сумку компонентов
        if(checkComponentBag(entity, item)) {
            return true;
        }
        if(entity instanceof Player player) {
            player.displayClientMessage(Component.translatable("ui.dnmmod.component_loss"), true);
        } else {
            entity.sendSystemMessage(Component.translatable("ui.dnmmod.component_loss"));
        }

        return false;
    }

    private static boolean checkComponentBag(LivingEntity entity, Item item) {
        if (!(entity instanceof Player player)) {
            return false;
        }

        // Проверяем обычный инвентарь
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);

            if (stack.getItem() instanceof ComponentBag ||
                    stack.getItem() instanceof MediumComponentBag) {

                if (containsItemInBag(stack, item)) {
                    return true;
                }
            }
        }

        // Проверяем Curios
        return CuriosApi.getCuriosInventory(player)
                .map(curios ->
                        curios.getStacksHandler(Curios.BAG_SLOT)
                                .map(handler -> {
                                    ItemStack stack = handler.getStacks().getStackInSlot(0);
                                    return ((stack.getItem() instanceof ComponentBag) ||
                                            (stack.getItem() instanceof MediumComponentBag))
                                            && containsItemInBag(stack, item);
                                })
                                .orElse(false)
                )
                .orElse(false);
    }

    private static boolean containsItemInBag(ItemStack bagStack, Item item) {
        return bagStack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve()
                .map(handler -> {
                    // Проверяем все слоты сумки
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stackInSlot = handler.getStackInSlot(i);
                        if (!stackInSlot.isEmpty() && stackInSlot.getItem() == item) {
                            return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
    }

    //Получение названия предмета
    public static MutableComponent getComponentName(Item item) {
        return Component.translatable(item.getDescriptionId());
    }

    //Получение текущей маны игрока
    public static float getCurrentMana(Player player) {
        MagicData magicData = MagicData.getPlayerMagicData(player);
        return magicData.getMana();
    }

    //Скип спелов для миксина
    public static boolean shouldSkipSpell(String spellClass) {
        if (spellClass.startsWith("com.gametechbc.traveloptics") && !ModCapabilities.isTravelOpticsLoaded()) {
            return true;
        }
        if (spellClass.startsWith("com.gametechbc.gtbcs_geomancy_plus") && !ModCapabilities.isGeomancyPlusLoaded()) {
            return true;
        }
        if (spellClass.startsWith("net.alshanex.alshanex_familiars") && !ModCapabilities.isAlshanexFamiliarsLoaded()) {
            return true;
        }
        return false;
    }
}
