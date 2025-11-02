package net.chixozhmix.dnmmod.effect.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class PhantomEffect extends MobEffect {
    public PhantomEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);

        if(pLivingEntity instanceof Player player) {
            // Применяем эффект призрака
            player.noPhysics = true;
            player.setNoGravity(true);
            player.fallDistance = 0.0f;

            preventBedrockPassing(player);

            // Движение вниз при нажатии Shift
            if (player.isShiftKeyDown()) {
                player.setDeltaMovement(player.getDeltaMovement().add(0, -0.2, 0));
            }
        }
    }

    private void preventBedrockPassing(Player player) {
        Level level = player.level();
        AABB playerBounds = player.getBoundingBox();

        // Проверяем все блоки, с которыми пересекается игрок
        BlockPos.betweenClosed(
                BlockPos.containing(playerBounds.minX, playerBounds.minY, playerBounds.minZ),
                BlockPos.containing(playerBounds.maxX, playerBounds.maxY, playerBounds.maxZ)
        ).forEach(pos -> {
            BlockState blockState = level.getBlockState(pos);

            // Если блок - бедрок или другие запрещенные блоки
            if (isForbiddenBlock(blockState)) {
                // Выталкиваем игрока из блока
                pushPlayerOutOfBlock(player, pos, blockState);
            }
        });
    }

    private void pushPlayerOutOfBlock(Player player, BlockPos blockPos, BlockState blockState) {
        AABB blockBounds = blockState.getShape(player.level(), blockPos).bounds().move(blockPos);
        AABB playerBounds = player.getBoundingBox();

        // Вычисляем направление для выталкивания
        double pushX = 0;
        double pushY = 0;
        double pushZ = 0;

        // Определяем, с какой стороны игрок вошел в блок
        if (playerBounds.intersects(blockBounds)) {
            // Выталкиваем в направлении, противоположном центру блока
            double blockCenterX = blockPos.getX() + 0.5;
            double blockCenterY = blockPos.getY() + 0.5;
            double blockCenterZ = blockPos.getZ() + 0.5;

            double playerCenterX = player.getX();
            double playerCenterY = player.getY() + player.getEyeHeight();
            double playerCenterZ = player.getZ();

            pushX = playerCenterX - blockCenterX;
            pushY = playerCenterY - blockCenterY;
            pushZ = playerCenterZ - blockCenterZ;

            // Нормализуем вектор и устанавливаем скорость выталкивания
            double length = Math.sqrt(pushX * pushX + pushY * pushY + pushZ * pushZ);
            if (length > 0) {
                double speed = 0.3; // Скорость выталкивания
                pushX = pushX / length * speed;
                pushY = pushY / length * speed;
                pushZ = pushZ / length * speed;

                player.setDeltaMovement(pushX, pushY, pushZ);
            }
        }
    }

    private boolean isForbiddenBlock(BlockState blockState) {
        return blockState.getBlock() == Blocks.BEDROCK ||
                blockState.getBlock() == Blocks.BARRIER ||
                blockState.getBlock() == Blocks.COMMAND_BLOCK ||
                blockState.getBlock() == Blocks.STRUCTURE_BLOCK ||
                blockState.getBlock() == Blocks.JIGSAW ||
                blockState.getBlock() == Blocks.OBSIDIAN ||
                blockState.getBlock() == Blocks.CRYING_OBSIDIAN;
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Mod.EventBusSubscriber
    public static class PhantomEffectHandler {

        @SubscribeEvent
        public static void onEffectExpired(MobEffectEvent.Expired event) {
            handleEffectRemoval(event.getEntity(), event.getEffectInstance());
        }

        @SubscribeEvent
        public static void onEffectRemoved(MobEffectEvent.Remove event) {
            handleEffectRemoval(event.getEntity(), event.getEffectInstance());
        }

        private static void handleEffectRemoval(LivingEntity entity, MobEffectInstance effectInstance) {
            if (effectInstance != null && effectInstance.getEffect() instanceof PhantomEffect) {
                if (entity instanceof Player player) {
                    resetPlayerPhysics(player);
                }
            }
        }

        private static void resetPlayerPhysics(Player player) {
            player.noPhysics = false;
            player.setNoGravity(false);
            player.fallDistance = 0.0f;

            player.setDeltaMovement(player.getDeltaMovement().multiply(1, 0.5, 1));
        }
    }
}
