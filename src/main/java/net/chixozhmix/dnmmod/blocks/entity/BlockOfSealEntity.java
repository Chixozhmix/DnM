package net.chixozhmix.dnmmod.blocks.entity;

import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.chixozhmix.dnmmod.blocks.custom.BlockOfSeal;
import net.chixozhmix.dnmmod.entity.modeus.ModeusBoss;
import net.chixozhmix.dnmmod.registers.ModBlockEntities;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockOfSealEntity extends BlockEntity {
    private int summonTimer = 60;
    private int cooldownTimer = 0;
    private final RandomSource rnd = RandomSource.create();

    public BlockOfSealEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BLOCK_OF_SEAL_ENTITY.get(), pPos, pBlockState);
    }

    public static void ticker(Level level, BlockPos pos, BlockState blockState, BlockOfSealEntity entity) {
        if (!level.hasNearbyAlivePlayer(pos.getX(), pos.getY(), pos.getZ(), 32)) {
            return;
        }

        if(blockState.getBlock() instanceof BlockOfSeal) {
            boolean isLit = blockState.getValue(BlockOfSeal.LIT);
            boolean isLocked = blockState.getValue(BlockOfSeal.LOCKED);

            if(isLocked) {
                entity.cooldownTimer++;
                if(entity.cooldownTimer >= 1200) { // 60 секунд
                    level.setBlock(pos, blockState.setValue(BlockOfSeal.LOCKED, false), 3);
                    entity.cooldownTimer = 0;
                    entity.setChanged();
                }
            } else {
                entity.cooldownTimer = 0;
            }

            if(isLit) {
                entity.summonTimer--;

                double d0 = pos.getX();
                double d1 = pos.getY();
                double d2 = pos.getZ();

                if(level instanceof ServerLevel serverLevel) {
                    double centerX = d0 + 0.5;
                    double centerY = d1 + 0.5;
                    double centerZ = d2 + 0.5;

                    // Частицы, разлетающиеся во все стороны
                    for(int p = 0; p < 8; p++) {
                        double theta = entity.rnd.nextDouble() * Math.PI * 2;
                        double phi = Math.acos(2.0 * entity.rnd.nextDouble() - 1.0);

                        double dx = Math.sin(phi) * Math.cos(theta);
                        double dy = Math.sin(phi) * Math.sin(theta);
                        double dz = Math.cos(phi);

                        serverLevel.sendParticles(
                                ParticleTypes.SCULK_SOUL,
                                centerX, centerY, centerZ,
                                2,
                                dx * 0.2,
                                dy * 0.2,
                                dz * 0.2,
                                0.1
                        );
                    }

                    // Облако частиц вокруг центра
                    for(int p = 0; p < 3; p++) {
                        double offsetX = (entity.rnd.nextDouble() - 0.5) * 1.0;
                        double offsetY = (entity.rnd.nextDouble() - 0.5) * 1.0;
                        double offsetZ = (entity.rnd.nextDouble() - 0.5) * 1.0;

                        serverLevel.sendParticles(
                                ParticleTypes.SCULK_SOUL,
                                centerX + offsetX, centerY + offsetY, centerZ + offsetZ,
                                2,
                                0, 0, 0,
                                0.01
                        );
                    }
                }

                if(entity.summonTimer <= 0) {
                    //Добавить звук появления
                    entity.spawnCreature((ServerLevel) level, pos);
                    entity.summonTimer = 60;
                    BlockState newState = blockState.setValue(BlockOfSeal.LIT, false)
                            .setValue(BlockOfSeal.LOCKED, true);
                    level.setBlock(pos, newState, 3);
                    entity.setChanged();
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("summonTimer", summonTimer);
        pTag.putInt("cooldownTimer", cooldownTimer);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.summonTimer = pTag.getInt("summonTimer");
        this.cooldownTimer = pTag.getInt("cooldownTimer");
    }

    private void spawnCreature(ServerLevel level, BlockPos pos) {
        Player player = level.getNearestPlayer(
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                32.0,
                false
        );

        if (player == null) {
            return;
        }

        Direction direction = Direction.getNearest(
                player.getX() - (pos.getX() + 0.5),
                0,
                player.getZ() - (pos.getZ() + 0.5)
        );

        double spawnX = pos.getX() + 0.5 + direction.getStepX() * 2.5;
        double spawnY = pos.getY() + 0.5;
        double spawnZ = pos.getZ() + 0.5 + direction.getStepZ() * 2.5;

        ModeusBoss modeusBoss = ModEntityType.MODEUS.get().create(level);

        if (modeusBoss != null) {

            double dx = player.getX() - spawnX;
            double dz = player.getZ() - spawnZ;
            float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0F);

            modeusBoss.moveTo(
                    spawnX,
                    spawnY,
                    spawnZ,
                    yaw,
                    0.0F
            );

            modeusBoss.setYRot(yaw);
            modeusBoss.setYHeadRot(yaw);
            modeusBoss.setYBodyRot(yaw);

            modeusBoss.finalizeSpawn(
                    level,
                    level.getCurrentDifficultyAt(pos),
                    MobSpawnType.SPAWNER,
                    null,
                    null
            );

            level.addFreshEntity(modeusBoss);
        }
    }
}