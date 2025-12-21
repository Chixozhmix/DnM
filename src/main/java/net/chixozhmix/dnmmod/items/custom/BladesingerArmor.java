package net.chixozhmix.dnmmod.items.custom;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import net.chixozhmix.dnmmod.items.UniqArmorMaterials;
import net.chixozhmix.dnmmod.items.client.BladesingerArmorModel;
import net.chixozhmix.dnmmod.spell.RegistrySpells;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BladesingerArmor extends ImbuableChestplateArmorItem {
    public BladesingerArmor(Type type, Properties settings) {
        super(UniqArmorMaterials.BLADESINGER_ARMOR, type, settings);
    }

    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer(new BladesingerArmorModel());
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack != null) {
            Item var3 = itemStack.getItem();
            if (var3 instanceof ArmorItem) {
                ArmorItem armorItem = (ArmorItem)var3;
                if (armorItem.getType() == Type.CHESTPLATE && !ISpellContainer.isSpellContainer(itemStack)) {
                    ISpellContainer spellContainer = ISpellContainer.create(2, true, true);
                    spellContainer.addSpell((AbstractSpell) RegistrySpells.MAGE_ARMOR.get(), 5, true, itemStack);
                    spellContainer.save(itemStack);
                }
            }
        }
    }
}
