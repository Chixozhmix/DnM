package net.chixozhmix.dnmmod.api.registers;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class DnMDamageType {
    public static final ResourceKey<DamageType> NECRO_MAGIC;

    static {
        NECRO_MAGIC = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("dnmmod:necro_magic"));
    }
}
