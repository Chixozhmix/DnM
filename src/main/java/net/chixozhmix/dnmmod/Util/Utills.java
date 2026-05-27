package net.chixozhmix.dnmmod.Util;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class Utills {
    public static final Random RANDOM = new Random();

    public static void randomGiver(LivingEntity entity, List<MobEffect> effectInstanceList, List<Item> itemList, @Nullable List<Item> uniqueItem) {
        float chance = RANDOM.nextFloat();

        if(chance < 0.7F) {
            Item item = randomItem(itemList);
            entity.spawnAtLocation(item.getDefaultInstance());
        } else if(chance < 0.95F) {
            MobEffectInstance effect = randomEffect(effectInstanceList);
            entity.addEffect(effect);
        } else {
            if(uniqueItem != null && !uniqueItem.isEmpty()) {
                Item item = randomItem(uniqueItem);
                entity.spawnAtLocation(item.getDefaultInstance());
            } else {
                Item item = randomItem(itemList);
                entity.spawnAtLocation(item.getDefaultInstance());
            }
        }
    }
    public static MobEffectInstance randomEffect(List<MobEffect> effectInstanceList) {
        MobEffect effect = effectInstanceList.get(RANDOM.nextInt(effectInstanceList.size()));
        return new MobEffectInstance(effect, 12000, 0);
    }
    public static Item randomItem(List<Item> itemList) {
        return itemList.get(RANDOM.nextInt(itemList.size()));
    }
}
