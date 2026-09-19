package net.chixozhmix.dnmmod.api.entity.dragons;

import net.chixozhmix.dnmmod.entity.dragons.AbstractDragonEntity;
import net.chixozhmix.dnmmod.entity.dragons.DragonPartEntity;
import net.chixozhmix.dnmmod.entity.dragons.client.AnimationsEnum;
import net.minecraft.util.Mth;

import java.util.Objects;

public class HitboxController {

    public static void updateParts(AbstractDragonEntity dragon) {
        float bodyYaw = dragon.yBodyRot * Mth.DEG_TO_RAD;
        float sin = Mth.sin(bodyYaw);
        float cos = Mth.cos(bodyYaw);

        //Стоит
        if(Objects.equals(dragon.getAnimState(), AnimationsEnum.IDLE.getAnimId())) {
            updateSinglePart(dragon, dragon.head, 0.0, 2.5, -8.5, -sin, -cos);
            updateSinglePart(dragon, dragon.neck, 0.0, 2.5, -3.5, -sin, -cos);
            updateSinglePart(dragon, dragon.neck2, 0.0, 3.5, -6.0, -sin, -cos);

            updateSinglePart(dragon, dragon.tail1, 0, 1.5, 3.5, -sin, -cos);
            updateSinglePart(dragon, dragon.tail2, 0, 0.0, 7.0, -sin, -cos);
            updateSinglePart(dragon, dragon.tail3, 0, 0.0, 11.5, -sin, -cos);

            updateSinglePart(dragon, dragon.leftWing, 5.0, 1.0, 0.0, -sin, -cos);
            updateSinglePart(dragon, dragon.rightWing, -5.0, 1.0, 0.0, -sin, -cos);

            dragon.leftWing.setPartSize(5.0f, 5.0f);
            dragon.rightWing.setPartSize(5.0f, 5.0f);
        }
        //Завис в воздухе
        if(Objects.equals(dragon.getAnimState(), AnimationsEnum.FLY_IDLE.getAnimId())) {
            updateSinglePart(dragon, dragon.head, 0.0, 6.5, -5.0, -sin, -cos);
            updateSinglePart(dragon, dragon.neck, 0.0, 5.0, -1.5, -sin, -cos);
            updateSinglePart(dragon, dragon.neck2, 0.0, 6.5, -2.0, -sin, -cos);

            updateSinglePart(dragon, dragon.tail1, 0, -1.5, 2.5, -sin, -cos);
            updateSinglePart(dragon, dragon.tail2, 0, -3.0, 3.0, -sin, -cos);
            updateSinglePart(dragon, dragon.tail3, 0, -6.0, 3.5, -sin, -cos);

            updateSinglePart(dragon, dragon.leftWing, 8.0, 1.0, 0.0, -sin, -cos);
            updateSinglePart(dragon, dragon.rightWing, -8.0, 1.0, 0.0, -sin, -cos);

            dragon.leftWing.setPartSize(8.0f, 5.0f);
            dragon.rightWing.setPartSize(8.0f, 5.0f);
        }
        //Идет
        if(Objects.equals(dragon.getAnimState(), AnimationsEnum.WALK.getAnimId())) {
            updateSinglePart(dragon, dragon.head, 0.0, 2.5, -8.5, -sin, -cos);
            updateSinglePart(dragon, dragon.neck, 0.0, 2.5, -3.5, -sin, -cos);
            updateSinglePart(dragon, dragon.neck2, 0.0, 3.0, -6.0, -sin, -cos);

            updateSinglePart(dragon, dragon.tail1, 0, 1.5, 3.5, -sin, -cos);
            updateSinglePart(dragon, dragon.tail2, 0, 1.0, 7.0, -sin, -cos);
            updateSinglePart(dragon, dragon.tail3, 0, 1.0, 11.5, -sin, -cos);

            updateSinglePart(dragon, dragon.leftWing, 5.0, 1.0, 0.0, -sin, -cos);
            updateSinglePart(dragon, dragon.rightWing, -5.0, 1.0, 0.0, -sin, -cos);

            dragon.leftWing.setPartSize(5.0f, 5.0f);
            dragon.rightWing.setPartSize(5.0f, 5.0f);
        }
        //Летит
        if(Objects.equals(dragon.getAnimState(), AnimationsEnum.FLY.getAnimId())) {
            updateSinglePart(dragon, dragon.head, 0.0, 2.5, -8.5, -sin, -cos);
            updateSinglePart(dragon, dragon.neck, 0.0, 2.5, -3.5, -sin, -cos);
            updateSinglePart(dragon, dragon.neck2, 0.0, 3.0, -6.0, -sin, -cos);

            updateSinglePart(dragon, dragon.tail1, 0, 2.5, 3.5, -sin, -cos);
            updateSinglePart(dragon, dragon.tail2, 0, 2.0, 7.0, -sin, -cos);
            updateSinglePart(dragon, dragon.tail3, 0, 2.0, 11.5, -sin, -cos);

            updateSinglePart(dragon, dragon.leftWing, 8.0, 1.0, 0.0, -sin, -cos);
            updateSinglePart(dragon, dragon.rightWing, -8.0, 1.0, 0.0, -sin, -cos);

            dragon.leftWing.setPartSize(8.0f, 5.0f);
            dragon.rightWing.setPartSize(8.0f, 5.0f);
        }

        if(Objects.equals(dragon.getAnimState(), AnimationsEnum.BIT.getAnimId())) {
            updateSinglePart(dragon, dragon.head, 0.0, 0.5, -8.5, -sin, -cos);
            updateSinglePart(dragon, dragon.neck, 0.0, 2.5, -3.5, -sin, -cos);
            updateSinglePart(dragon, dragon.neck2, 0.0, 3.0, -6.0, -sin, -cos);

            updateSinglePart(dragon, dragon.tail1, 0, 1.5, 3.5, -sin, -cos);
            updateSinglePart(dragon, dragon.tail2, 0, 1.0, 7.0, -sin, -cos);
            updateSinglePart(dragon, dragon.tail3, 0, 1.0, 11.5, -sin, -cos);

            updateSinglePart(dragon, dragon.leftWing, 5.0, 1.0, 0.0, -sin, -cos);
            updateSinglePart(dragon, dragon.rightWing, -5.0, 1.0, 0.0, -sin, -cos);

            dragon.leftWing.setPartSize(5.0f, 5.0f);
            dragon.rightWing.setPartSize(5.0f, 5.0f);
        }
    }


    private static void updateSinglePart(AbstractDragonEntity dragon, DragonPartEntity part, double localX, double localY, double localZ, float sin, float cos) {
        double worldX = dragon.getX() + (localX * cos - localZ * sin);
        double worldY = dragon.getY() + localY;
        double worldZ = dragon.getZ() + (localX * sin + localZ * cos);

        part.xo = part.getX();
        part.yo = part.getY();
        part.zo = part.getZ();

        part.xOld = part.getX();
        part.yOld = part.getY();
        part.zOld = part.getZ();

        part.setPos(worldX, worldY, worldZ);
        part.setYRot(dragon.getYRot());
        part.setXRot(dragon.getXRot());
        part.yRotO = dragon.yRotO;
        part.xRotO = dragon.xRotO;
    }
}
