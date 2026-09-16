package net.chixozhmix.dnmmod.events;

import net.chixozhmix.dnmmod.Util.EventUtils;
import net.chixozhmix.dnmmod.entity.spell.JumpAOE;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class PlayerEvent {
    private static final String SPECIAL_JUMP = "SpecialJump";

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        if (!(target instanceof Player)) return;
        Entity source = event.getSource().getEntity();

        EventUtils.ManaShield(event, target);
        EventUtils.ProtectiveBracelet(event, target, source);
        EventUtils.DeathWisp(event, target, source);
    }

    @SubscribeEvent
    public static void onJump (LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();

        if(entity.hasEffect(ModEffects.JUMP.get())) {
            entity.getPersistentData().putBoolean(SPECIAL_JUMP, true);
            entity.hasImpulse = true;

            int amplifier = Objects.requireNonNull(entity.getEffect(ModEffects.JUMP.get())).getAmplifier();

            float multiplier = (10.0F * (amplifier + 1)) / 5;
            Vec3 forward = entity.getLookAngle();
            Vec3 vec = forward.multiply((double)3.0F, (double)1.0F, (double)3.0F).normalize().add((double)0.0F, (double)0.25F, (double)0.0F).scale((double)multiplier);
            entity.setDeltaMovement(new Vec3(Mth.lerp((double)0.75F, entity.getDeltaMovement().x, vec.x), Mth.lerp((double)0.75F, entity.getDeltaMovement().y, vec.y), Mth.lerp((double)0.75F, entity.getDeltaMovement().z, vec.z)));
            entity.invulnerableTime = 25;
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();

        if (!(entity instanceof Player player)) return;

        if (player.getPersistentData().getBoolean(SPECIAL_JUMP)) {
            event.setCanceled(true);

            if (!player.level().isClientSide) {

                JumpAOE jumpAOE = new JumpAOE(player.level());

                jumpAOE.setDuration(15);
                jumpAOE.setPos(player.getX(), player.getY() - player.getBbHeight(), player.getZ());
                player.level().addFreshEntity(jumpAOE);
            }

            player.getPersistentData().putBoolean(SPECIAL_JUMP, false);
        }
    }
}
