package net.chixozhmix.dnmmod.entity.custom;

import io.redspace.ironsspellbooks.entity.spells.ice_spike.IceSpikeEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class IceArrowEntity extends AbstractArrow {
    public IceArrowEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public IceArrowEntity(Level level, LivingEntity shooter) {
        super(ModEntityType.ICE_ARROW_ENTITY.get(), shooter, level);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.ICE_ARROW.get());
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);

        if (level().isClientSide)
            return;

        spawnCrystal(pResult.getLocation());
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);

        if (level().isClientSide)
            return;

        spawnCrystal(pResult.getLocation());
    }

    private void addParticle() {
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.SNOWFLAKE,
                    getX(),
                    getY(),
                    getZ(),
                    60,
                    1,
                    1,
                    1,
                    0.05
            );
        }
    }

    private void spawnCrystal(Vec3 center) {
        addParticle();

        for (int i = 0; i < 5; i++) {

            double angle = i * Math.PI * 2 / 5;
            double radius = 2;

            double x = center.x + Math.cos(angle) * radius;
            double z = center.z + Math.sin(angle) * radius;

            BlockPos pos = BlockPos.containing(x, center.y, z);

            while (level().isEmptyBlock(pos) && pos.getY() > level().getMinBuildHeight()) {
                pos = pos.below();
            }

            IceSpikeEntity spike = new IceSpikeEntity(level(), (LivingEntity) getOwner());

            spike.setDamage(0);
            spike.setWaitTime(0);
            spike.setSpikeSize(2);

            float yaw = (float)Math.toDegrees(angle) + 90F;
            float pitch = -30F;

            spike.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, yaw, pitch);

            spike.setYRot(yaw);
            spike.setXRot(pitch);
            spike.yRotO = yaw;
            spike.xRotO = pitch;

            level().addFreshEntity(spike);
        }

        level().playSound(null, getX(), getY(), getZ(), SoundRegistry.ICE_SPIKE_EMERGE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        AABB cristalArea = new AABB(
                this.getX() - 5.0, this.getY() - 5.0, this.getZ() - 5.0,
                this.getX() + 5.0, this.getY() + 5.0, this.getZ() + 5.0
        );

        List<LivingEntity> entityInRange = this.level().getEntitiesOfClass(
                LivingEntity.class,
                cristalArea
        );

        for (LivingEntity entities : entityInRange) {
            entities.hurt(this.damageSources().indirectMagic(this, getOwner()), 4);
            entities.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));
        }

        discard();
    }
}
