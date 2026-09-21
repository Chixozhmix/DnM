package net.chixozhmix.dnmmod.goals.dragons;

import net.chixozhmix.dnmmod.entity.dragons.AbstractDragonEntity;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DragonPatrolLocationGoal extends WaterAvoidingRandomStrollGoal {

    private final double radiusSqr;
    private final AbstractDragonEntity dragon;

    public DragonPatrolLocationGoal(AbstractDragonEntity dragon, double radius, double speedModifier) {
        super(dragon, speedModifier);

        this.dragon = Objects.requireNonNull(dragon);
        this.radiusSqr = radius * radius;
    }

    @Override
    protected @Nullable Vec3 getPosition() {
        Vec3 origin = dragon.getSpawnPos();

        if (origin == null)
            return super.getPosition();

        double distanceSqr = dragon.position().subtract(origin).horizontalDistanceSqr();

        if (distanceSqr > radiusSqr)
            return LandRandomPos.getPosTowards(dragon, 8, 4, origin);

        return super.getPosition();
    }
}
