package net.chixozhmix.dnmmod.entity.projectiles.custom;

import net.chixozhmix.dnmmod.api.CustomMagicProjectile;
import net.chixozhmix.dnmmod.entity.ModEntityType;
import net.chixozhmix.dnmmod.items.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;


public class FIrebolt extends CustomMagicProjectile {

    // Конструктор для сущности
    public FIrebolt(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level,
                1.0F, 10.0F,
                () -> ParticleTypes.FLAME,
                120,
                0.0F,
                SoundEvents.PLAYER_HURT_ON_FIRE,
                SoundEvents.FIRE_EXTINGUISH
        );
    }

    // Конструктор для стрелка
    public FIrebolt(Level level, LivingEntity shooter) {
        super(ModEntityType.FIREBALT.get(), level, shooter,
                1.0F, 10.0F,
                () -> ParticleTypes.FLAME,
                120,
                0.0F,
                SoundEvents.PLAYER_HURT_ON_FIRE,
                SoundEvents.FIRE_EXTINGUISH
        );
    }

    // Конструктор для позиции (опционально)
    public FIrebolt(Level level, double x, double y, double z) {
        super(ModEntityType.FIREBALT.get(), level, x, y, z,
                1.0F, 10.0F,
                () -> ParticleTypes.FLAME,
                120,
                0.0F,
                SoundEvents.PLAYER_HURT_ON_FIRE,
                SoundEvents.FIRE_EXTINGUISH
        );
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.FIREBALT.get();
    }
}
