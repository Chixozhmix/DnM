package net.chixozhmix.dnmmod.items.custom.spellbooks;

import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.item.UniqueSpellBook;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class TaintedSpellbook extends UniqueSpellBook {

    public TaintedSpellbook() {
        super(SpellRarity.LEGENDARY, SpellDataRegistryHolder.of(new SpellDataRegistryHolder[]{new SpellDataRegistryHolder(SpellRegistry.MAGIC_MISSILE_SPELL, 10),
                new SpellDataRegistryHolder(SpellRegistry.STARFALL_SPELL, 7),
                new SpellDataRegistryHolder(SpellRegistry.ELDRITCH_BLAST_SPELL, 3)}), 9, () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(UUID.fromString("b450acae-1cb8-45c1-9a08-e9ae0fdda6db"), "Spellbook modifier", 200.0F, AttributeModifier.Operation.ADDITION));
            builder.put(AttributeRegistry.ELDRITCH_MAGIC_RESIST.get(), new AttributeModifier(UUID.fromString("b450acae-1cb8-45c1-9a08-e9ae0fdda6db"), "Spellbook modifier", 0.1F, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
        });
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, Level context, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, context, lines, flag);
        AffinityData affinityData = AffinityData.getAffinityData(itemStack);
        if (!affinityData.affinityData().isEmpty()) {
            int i = TooltipsUtils.indexOfComponent(lines, "tooltip.irons_spellbooks.spellbook_spell_count");
            lines.addAll(i < 0 ? lines.size() : i + 1, affinityData.getDescriptionComponent());
        }
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack != null) {
            super.initializeSpellContainer(itemStack);
            AffinityData.setAffinityData(itemStack, RegistrySpells.HUNGER_OF_HADAR.get(), 1);
        }
    }
}
