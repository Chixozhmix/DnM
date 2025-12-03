package net.chixozhmix.dnmmod.items.custom;


import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import net.chixozhmix.dnmmod.items.UniqArmorMaterials;
import net.chixozhmix.dnmmod.items.client.ShadowArcherModel;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ShadowArcher extends ImbuableChestplateArmorItem {
    public ShadowArcher(Type type, Properties settings) {
        super(UniqArmorMaterials.SHADOW_ARCHER, type, settings);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer(new ShadowArcherModel());
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack != null) {
            Item var3 = itemStack.getItem();
            if (var3 instanceof ArmorItem) {
                ArmorItem armorItem = (ArmorItem)var3;
                if (armorItem.getType() == Type.CHESTPLATE && !ISpellContainer.isSpellContainer(itemStack)) {
                    ISpellContainer spellContainer = ISpellContainer.create(2, true, true);
                    spellContainer.addSpell((AbstractSpell) SpellRegistry.BLOOD_STEP_SPELL.get(), 4, true, itemStack);
                    spellContainer.addSpell((AbstractSpell) SpellRegistry.INVISIBILITY_SPELL.get(), 1, true, itemStack);
                    spellContainer.save(itemStack);
                }
            }

        }
    }
}
