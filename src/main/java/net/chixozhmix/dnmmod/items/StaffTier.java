package net.chixozhmix.dnmmod.items;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;

public class StaffTier extends io.redspace.ironsspellbooks.item.weapons.StaffTier {
    public static StaffTier TAINTED_STAFF;

    static final float damage_modifier = 2.0f;
    static final float speed_modifier = -3.0f;

    public StaffTier(float damage, float speed, AttributeContainer... attributes) {
        super(damage, speed, attributes);
    }

    static {
        TAINTED_STAFF = new StaffTier(damage_modifier, speed_modifier, new AttributeContainer[]{
                new AttributeContainer(AttributeRegistry.MANA_REGEN, 0.20F, MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.15F, MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.ENDER_SPELL_POWER, 0.10F, MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.05F, MULTIPLY_BASE)
        });
    }
}
