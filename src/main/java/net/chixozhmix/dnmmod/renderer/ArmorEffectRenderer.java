package net.chixozhmix.dnmmod.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class ArmorEffectRenderer {
    private static final Map<UUID, Float> armorAnimState = new HashMap<>();
    private static final Map<UUID, Float> rotationStates = new HashMap<>();
    private static MobEffect TRIGGER_EFFECT = null;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (event.phase == TickEvent.Phase.END  && minecraft.level != null) {
            if (TRIGGER_EFFECT == null) {
                TRIGGER_EFFECT = ModEffects.AGATHYS_ARMOR.get();
            }

            // Обновляем анимации для всех игроков с эффектом
            for (Player player : minecraft.level.players()) {
                UUID playerId = player.getUUID();

                // Обновляем вращение щитов
                float currentRotation = rotationStates.getOrDefault(playerId, 0f);
                rotationStates.put(playerId, currentRotation + 2.0f); // Скорость вращения

                if (player.hasEffect(TRIGGER_EFFECT)) {
                    float currentState = armorAnimState.getOrDefault(playerId, 0f);

                    // Плавное появление
                    if (currentState < 1.0f) {
                        armorAnimState.put(playerId, Math.min(currentState + 0.05f, 1.0f));
                    }
                } else {
                    // Плавное исчезновение
                    if (armorAnimState.containsKey(playerId)) {
                        float currentState = armorAnimState.get(playerId);
                        if (currentState > 0f) {
                            armorAnimState.put(playerId, Math.max(currentState - 0.05f, 0f));
                        } else {
                            armorAnimState.remove(playerId);
                            rotationStates.remove(playerId);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderLivingEvent.Post<Player, HumanoidModel<Player>> event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (TRIGGER_EFFECT == null) {
            TRIGGER_EFFECT = ModEffects.AGATHYS_ARMOR.get();
        }

        if (!player.hasEffect(TRIGGER_EFFECT) && !armorAnimState.containsKey(player.getUUID())) {
            return;
        }

        float animationState = armorAnimState.getOrDefault(player.getUUID(), 0f);
        if (animationState <= 0f) return;

        renderRotatingShields(player, event.getPoseStack(), event.getMultiBufferSource(),
                event.getPartialTick(), animationState);
    }

    private static void renderRotatingShields(Player player, PoseStack poseStack,
                                              MultiBufferSource bufferSource, float partialTicks,
                                              float alpha) {
        poseStack.pushPose();

        // Получаем текущее вращение для игрока
        float rotation = rotationStates.getOrDefault(player.getUUID(), 0f);

        // Интерполируем вращение для плавности
        float prevRotation = rotation - 2.0f;
        float interpolatedRotation = prevRotation + (rotation - prevRotation) * partialTicks;

        // Рендерим два вращающихся щита
        renderShieldPair(player, poseStack, bufferSource, partialTicks, alpha, interpolatedRotation);

        poseStack.popPose();
    }

    private static void renderShieldPair(Player player, PoseStack poseStack,
                                         MultiBufferSource bufferSource, float partialTicks,
                                         float alpha, float rotation) {

        // Радиус вращения вокруг игрока
        float radius = 1.2f;

        // Высота щитов относительно игрока
        float yOffset = 1.2f;

        // Рендерим первый щит
        renderSingleShield(player, poseStack, bufferSource, partialTicks, alpha,
                rotation, radius, yOffset, 0);

        // Рендерим второй щит
        renderSingleShield(player, poseStack, bufferSource, partialTicks, alpha,
                rotation + 180f, radius, yOffset, 1);
    }

    private static void renderSingleShield(Player player, PoseStack poseStack,
                                           MultiBufferSource bufferSource, float partialTicks,
                                           float alpha, float angle, float radius,
                                           float yOffset, int shieldIndex) {
        poseStack.pushPose();

        // Позиционируем щит по кругу вокруг игрока
        float radAngle = (float) Math.toRadians(angle);
        float x = (float) Math.cos(radAngle) * radius;
        float z = (float) Math.sin(radAngle) * radius;

        poseStack.translate(x, yOffset, z);

        // Ориентируем щит лицом наружу от игрока
        poseStack.mulPose(Axis.YP.rotationDegrees(-angle + 45));

        // Наклон щита для лучшего обзора
        poseStack.mulPose(Axis.XP.rotationDegrees(15));

        // Масштабируем щит
        float scale = 0.8f;
        poseStack.scale(scale, scale, scale);

        // Легкая пульсация
        float pulse = (float) (Math.sin(player.tickCount * 0.2f) * 0.1f + 1.0f);
        poseStack.scale(pulse, pulse, pulse);

        // Рендерим модель щита
        renderShieldModel(poseStack, bufferSource, alpha);

        poseStack.popPose();
    }

    private static void renderShieldModel(PoseStack poseStack, MultiBufferSource bufferSource,
                                          float alpha) {
        Minecraft minecraft = Minecraft.getInstance();

        // Загружаем модель щита
        ShieldModel shieldModel = new ShieldModel(
                minecraft.getEntityModels().bakeLayer(ModelLayers.SHIELD)
        );

        ResourceLocation shieldTexture = new ResourceLocation(DnMmod.MOD_ID, "textures/entity/agahys_shield.png");


        shieldModel.plate().render(poseStack,
                bufferSource.getBuffer(shieldModel.renderType(shieldTexture)),
                15728880, OverlayTexture.NO_OVERLAY,
                1.0f, 1.0f, 1.0f, alpha * 0.9f);
    }
}