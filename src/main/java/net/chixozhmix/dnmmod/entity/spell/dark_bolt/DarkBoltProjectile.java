package net.chixozhmix.dnmmod.entity.spell.dark_bolt;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.registers.RegistrySpells;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class DarkBoltProjectile extends AbstractMagicProjectile {
    public DarkBoltProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public DarkBoltProjectile(EntityType<? extends DarkBoltProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        this.setOwner(shooter);
    }

    public DarkBoltProjectile(Level levelIn, LivingEntity shooter) {
        this(ModEntityType.DARK_BOLT.get(), levelIn, shooter);
    }

    @Override
    public void trailParticles() {
        Vec3 vec = this.getDeltaMovement();
        double length = vec.length();
        int count = (int)Math.min(20L, Math.round(length) * 3L) + 1;
        float f = (float)length / (float)count;

        for(int i = 0; i < count; ++i) {
            Vec3 random = Utils.getRandomVec3(0.02);
            Vec3 p = vec.scale((double)(f * (float)i));
            this.level().addParticle(ParticleHelper.BLOOD, this.getX() + random.x + p.x, this.getY() + random.y + p.y, this.getZ() + random.z + p.z, random.x, random.y, random.z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level(), ParticleHelper.BLOOD, x, y, z, 15, (double)0.0F, (double)0.0F, (double)0.0F, 0.18, true);
    }

    @Override
    public float getSpeed() {
        return 2.5f;
    }

    @Override
    public Optional<Supplier<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.discard();
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult pResult) {
        super.onHitEntity(pResult);
        DamageSources.applyDamage(pResult.getEntity(), this.damage, (RegistrySpells.DARK_BOLT_SPELL.get()).getDamageSource(this, this.getOwner()));
        this.consumeEntityImpact(pResult, true);
    }
}
