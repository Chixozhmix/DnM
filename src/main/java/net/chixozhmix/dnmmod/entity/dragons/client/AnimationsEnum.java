package net.chixozhmix.dnmmod.entity.dragons.client;

public enum AnimationsEnum {
    IDLE ("IDLE"),
    WALK ("WALK"),
    FLY ("FLY"),
    SLEEP ("SLEEP"),

    BIT ("BIT"),
    TAIL_ATTACK ("TAIL_ATTACK"),
    WING_ATTACK ("WING_ATTACK");

    private final String animId;

    AnimationsEnum(String animId) {
        this.animId = animId;
    }

    public String getAnimId() {
        return animId;
    }


}
