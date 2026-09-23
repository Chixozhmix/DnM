package net.chixozhmix.dnmmod.goals.dragons;

import net.chixozhmix.dnmmod.entity.dragons.AbstractDragonEntity;
import net.chixozhmix.dnmmod.entity.dragons.client.AnimationsEnum;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

//Когда игрок оказывается в центре дракона - дракон не атакует. Нужно будет пофиксить
public class DragonMeleeAttackGoal extends Goal {

    private final AbstractDragonEntity dragon;

    private int attackTick;
    private int attackCooldown;
    private boolean attackPerformed;

    private static final double ATTACK_START_DISTANCE = 5.0D;

    public DragonMeleeAttackGoal(AbstractDragonEntity dragon) {
        this.dragon = dragon;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = dragon.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = dragon.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public void start() {
        attackTick = 0;
        attackCooldown = 0;
        attackPerformed = false;
    }

    @Override
    public void stop() {
        attackTick = 0;
        attackCooldown = 0;
        attackPerformed = false;

        dragon.setBitTick(0);
        dragon.setAnimState(AnimationsEnum.IDLE);
    }

    @Override
    public void tick() {
        LivingEntity target = dragon.getTarget();

        if (target == null || !target.isAlive())
            return;

        double headDistanceSqr = dragon.head.distanceToSqr(target);
        double torsoDistanceSqr = dragon.torso.distanceToSqr(target);

        boolean inAttackRange = headDistanceSqr <= ATTACK_START_DISTANCE * ATTACK_START_DISTANCE || torsoDistanceSqr <= ATTACK_START_DISTANCE * ATTACK_START_DISTANCE;

        // ------------------------------------------------
        // ИДЁМ К ЦЕЛИ
        // ------------------------------------------------

        if (attackTick <= 0 && !inAttackRange) {
            dragon.getNavigation().moveTo(target, dragon.getAttribute(Attributes.MOVEMENT_SPEED).getValue() + 0.85);

            dragon.getLookControl().setLookAt(target);
            return;
        }

        // ------------------------------------------------
        // АТАКА
        // ------------------------------------------------

        if (attackTick > 0) {
            dragon.getNavigation().stop();
            dragon.getLookControl().setLookAt(target);

            attackTick--;
            dragon.setBitTick(attackTick);

            // Момент попадания
            if (attackTick == 12 && !attackPerformed) {

                attackPerformed = true;

                //if(dragon.head.getBoundingBox().inflate(0.5).intersects(target.getBoundingBox()))
                  if(dragon.head.distanceTo(target) <= ATTACK_START_DISTANCE || dragon.torso.distanceTo(target) <= ATTACK_START_DISTANCE)
                    dragon.doHurtTarget(target);
            }

            if (attackTick == 0) {
                dragon.setAnimState(AnimationsEnum.IDLE);
                attackCooldown = 10;
                attackPerformed = false;
            }

            return;
        }

        // ------------------------------------------------
        // ПЕРЕЗАРЯДКА
        // ------------------------------------------------

        if (attackCooldown > 0) {
            attackCooldown--;
            dragon.getLookControl().setLookAt(target);
            return;
        }

        // ------------------------------------------------
        // НАЧИНАЕМ АТАКУ
        // ------------------------------------------------

        attackTick = 19;
        attackPerformed = false;

        dragon.setBitTick(19);
        dragon.setAnimState(AnimationsEnum.BIT);
    }
}
