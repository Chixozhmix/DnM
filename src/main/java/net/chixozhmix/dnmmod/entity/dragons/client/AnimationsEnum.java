package net.chixozhmix.dnmmod.entity.dragons.client;

public enum AnimationsEnum {
    //Movements
    IDLE ("IDLE"),
    WALK ("WALK"),
    FLY ("FLY"),
    FLY_IDLE ("FLY_IDLE"),

    //Attacks
    BIT ("BIT"),
    TAIL_ATTACK ("TAIL_ATTACK"),
    WING_ATTACK ("WING_ATTACK"),
    BREATH_ATTACK ("BREATH_ATTACK");

    //Unique Skills


    private final String animId;

    AnimationsEnum(String animId) {
        this.animId = animId;
    }

    public String getAnimId() {
        return animId;
    }


}
