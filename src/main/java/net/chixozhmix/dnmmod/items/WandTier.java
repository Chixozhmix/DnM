package net.chixozhmix.dnmmod.items;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class WandTier implements IronsWeaponTier {
    public static WandTier WOODEN_WAND;
    public static WandTier DRUID_WAND;
    public static WandTier ELECTROMANCER_WAND;
    public static WandTier CRYOMANCER_WAND;
    public static WandTier PYROMANCER_WAND;
    public static WandTier BLOOD_WAND;
    public static WandTier SACRED_SYMBOL;
    public static WandTier ENDER_WAND;
    public static WandTier EVOKER_WAND;
    float damage;
    float speed;
    AttributeContainer[] attributes;

    static final float damage_modifier = 1.0f;
    static final float speed_modifier = -2.0f;

    public WandTier(float damage, float speed, AttributeContainer... attributes) {
        this.damage = damage;
        this.speed = speed;
        this.attributes = attributes;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public AttributeContainer[] getAdditionalAttributes() {
        return this.attributes;
    }

    static {
        WOODEN_WAND = new WandTier(damage_modifier, speed_modifier, new AttributeContainer[]{
                new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.1, AttributeModifier.Operation.MULTIPLY_BASE)});
        DRUID_WAND = new WandTier(damage_modifier, speed_modifier, new AttributeContainer[]{
                new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.NATURE_SPELL_POWER, 0.15, AttributeModifier.Operation.MULTIPLY_BASE)});
        ELECTROMANCER_WAND = new WandTier(damage_modifier, speed_modifier, new AttributeContainer[]{
                new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, 0.15, AttributeModifier.Operation.MULTIPLY_BASE)});
        PYROMANCER_WAND = new WandTier(damage_modifier, speed_modifier, new AttributeContainer[]{
                new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.FIRE_SPELL_POWER, 0.15, AttributeModifier.Operation.MULTIPLY_BASE)});
        BLOOD_WAND = new WandTier(damage_modifier, speed_modifier, new AttributeContainer[]{
                new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.15, AttributeModifier.Operation.MULTIPLY_BASE)});
        CRYOMANCER_WAND = new WandTier(damage_modifier, speed_modifier, new AttributeContainer[]{
                new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.ICE_SPELL_POWER, 0.15, AttributeModifier.Operation.MULTIPLY_BASE)});
        ENDER_WAND = new WandTier(damage_modifier, speed_modifier, new AttributeContainer[]{
                new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.ENDER_SPELL_POWER, 0.15, AttributeModifier.Operation.MULTIPLY_BASE)});
        SACRED_SYMBOL = new WandTier(damage_modifier, speed_modifier, new AttributeContainer[]{
                new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.HOLY_SPELL_POWER, 0.15, AttributeModifier.Operation.MULTIPLY_BASE)});
        EVOKER_WAND = new WandTier(damage_modifier, speed_modifier, new AttributeContainer[]{
                new AttributeContainer(AttributeRegistry.MANA_REGEN, (double)0.25F, AttributeModifier.Operation.MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.NATURE_SPELL_POWER, 0.15, AttributeModifier.Operation.MULTIPLY_BASE),
                new AttributeContainer(AttributeRegistry.SUMMON_DAMAGE, 0.10f, AttributeModifier.Operation.MULTIPLY_BASE)});

    }
}
