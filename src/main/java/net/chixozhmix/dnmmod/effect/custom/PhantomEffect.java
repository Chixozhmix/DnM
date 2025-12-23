package net.chixozhmix.dnmmod.effect.custom;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
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
            Level level = player.level();

            AttributeInstance creativeFlightAttr = player.getAttribute(ALObjects.Attributes.CREATIVE_FLIGHT.get());
            if (creativeFlightAttr != null) {
                creativeFlightAttr.setBaseValue(1.0);
            }

            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true;
            player.onUpdateAbilities();

            player.noPhysics = true;
            player.fallDistance = 0.0f;

            preventBedrockPassing(player);
        }
    }

    private void preventBedrockPassing(Player player) {
        Level level = player.level();
        AABB playerBounds = player.getBoundingBox();

        BlockPos.betweenClosed(
                BlockPos.containing(playerBounds.minX, playerBounds.minY, playerBounds.minZ),
                BlockPos.containing(playerBounds.maxX, playerBounds.maxY, playerBounds.maxZ)
        ).forEach(pos -> {
            BlockState blockState = level.getBlockState(pos);

            if (isForbiddenBlock(blockState)) {
                pushPlayerOutOfBlock(player, pos, blockState);
            }
        });
    }

    private void pushPlayerOutOfBlock(Player player, BlockPos blockPos, BlockState blockState) {
        AABB blockBounds = blockState.getShape(player.level(), blockPos).bounds().move(blockPos);
        AABB playerBounds = player.getBoundingBox();

        double pushX = 0;
        double pushY = 0;
        double pushZ = 0;

        if (playerBounds.intersects(blockBounds)) {
            double blockCenterX = blockPos.getX() + 0.5;
            double blockCenterY = blockPos.getY() + 0.5;
            double blockCenterZ = blockPos.getZ() + 0.5;

            double playerCenterX = player.getX();
            double playerCenterY = player.getY() + player.getEyeHeight();
            double playerCenterZ = player.getZ();

            pushX = playerCenterX - blockCenterX;
            pushY = playerCenterY - blockCenterY;
            pushZ = playerCenterZ - blockCenterZ;

            double length = Math.sqrt(pushX * pushX + pushY * pushY + pushZ * pushZ);
            if (length > 0) {
                double speed = 0.3;
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
            AttributeInstance creativeFlightAttr = player.getAttribute(ALObjects.Attributes.CREATIVE_FLIGHT.get());
            if (creativeFlightAttr != null) {
                creativeFlightAttr.setBaseValue(0.0);
            }
            if (!player.isCreative() && !player.isSpectator()) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }

            player.noPhysics = false;
            player.fallDistance = 0.0f;

            player.setDeltaMovement(player.getDeltaMovement().multiply(1, 0.5, 1));
        }
    }
}
