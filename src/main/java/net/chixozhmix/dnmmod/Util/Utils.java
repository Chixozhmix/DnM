package net.chixozhmix.dnmmod.Util;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class Utils {
    public static final Random RANDOM = new Random();

    //Рандомная выдача предмета или эффекта
    public static void randomGiver(LivingEntity entity, List<MobEffect> effectInstanceList, List<Item> itemList, @Nullable List<Item> uniqueItem) {
        float chance = RANDOM.nextFloat();

        if(chance < 0.7F) {
            Item item = randomItem(itemList);
            entity.spawnAtLocation(item.getDefaultInstance());
        } else if(chance < 0.90F) {
            MobEffectInstance effect = randomEffect(effectInstanceList);
            entity.addEffect(effect);
        } else {
            Item item;
            if(uniqueItem != null && !uniqueItem.isEmpty()) {
                item = randomItem(uniqueItem);
            } else {
                item = randomItem(itemList);
            }
            entity.spawnAtLocation(item.getDefaultInstance());
        }
    }

    public static MobEffectInstance randomEffect(List<MobEffect> effectInstanceList) {
        MobEffect effect = effectInstanceList.get(RANDOM.nextInt(effectInstanceList.size()));
        return new MobEffectInstance(effect, 12000, 0);
    }

    public static Item randomItem(List<Item> itemList) {
        return itemList.get(RANDOM.nextInt(itemList.size()));
    }

    //Проверка полного комплекта брони
    public static boolean hasFullSet(Player player) {
        ItemStack boots = player.getInventory().getArmor(0);
        ItemStack leggings = player.getInventory().getArmor(1);
        ItemStack chestplate = player.getInventory().getArmor(2);
        ItemStack helmet = player.getInventory().getArmor(3);

        return !helmet.isEmpty() && !chestplate.isEmpty() && !leggings.isEmpty() && !boots.isEmpty();
    }
}
