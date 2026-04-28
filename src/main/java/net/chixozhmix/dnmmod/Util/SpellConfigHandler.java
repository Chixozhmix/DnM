package net.chixozhmix.dnmmod.Util;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.chixozhmix.dnmmod.configs.SpellComponentConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpellConfigHandler {
    public static boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity,
                                                 MagicData playerMagicData, String spellClassName) {
        Supplier<Item> componentSupplier = SpellComponentConfig.getSpellComponents().get(spellClassName);

        if (componentSupplier != null) {
            Item requiredComponent = componentSupplier.get();
            if (requiredComponent != Items.AIR && !SpellUtils.checkSpellComponent(entity, requiredComponent)) {
                return false;
            }
        }
        return true;
    }

    public static List<MutableComponent> modifyGetUniqueInfo(int spellLevel, LivingEntity caster,
                                                             List<MutableComponent> originalInfo,
                                                             String spellClassName) {
        Supplier<Item> componentSupplier = SpellComponentConfig.getSpellComponents().get(spellClassName);

        if (componentSupplier != null) {
            Item requiredComponent = componentSupplier.get();
            if (requiredComponent != Items.AIR) {
                List<MutableComponent> modified = new ArrayList<>(originalInfo);
                modified.add(Component.translatable("ui.dnmmod.spell_component",
                        SpellUtils.getComponentName(requiredComponent)));
                return modified;
            }
        }
        return originalInfo;
    }
}
