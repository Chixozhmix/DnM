package net.chixozhmix.dnmmod.entity.dragons;

public enum DragonTypes {
    BLUE ("BLUE"),
    RED ("RED"),
    GREEN ("GREEN"),
    WHITE ("WHITE");

    private final String type;

    DragonTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
