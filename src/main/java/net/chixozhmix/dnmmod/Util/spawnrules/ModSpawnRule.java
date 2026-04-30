package net.chixozhmix.dnmmod.Util.spawnrules;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;

public class ModSpawnRule {
    public static boolean ThunderSpawnRule(
            EntityType<? extends Mob> entityType,
            ServerLevelAccessor level,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random) {

        boolean canSpawnOnGround = Monster.checkMonsterSpawnRules(
                (EntityType<? extends Monster>) entityType,
                level,
                spawnType,
                pos,
                random);

        if (!canSpawnOnGround) {
            return false;
        }

        boolean isThundering = level.getLevel().isThundering();

        return isThundering;
    }

    public static boolean CastingMonsterSpawnRule(
            EntityType<? extends Mob> entityType,
            ServerLevelAccessor level,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random) {

        return Monster.checkMonsterSpawnRules(
                (EntityType<? extends Monster>) entityType,
                level,
                spawnType,
                pos,
                random);
    }
}
