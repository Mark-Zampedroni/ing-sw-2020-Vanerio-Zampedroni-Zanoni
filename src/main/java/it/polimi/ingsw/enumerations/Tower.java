package it.polimi.ingsw.enumerations;

public enum Tower {

    GROUND,
    BOTTOM,
    MIDDLE,
    TOP,
    DOME;

    public static Tower increase(Tower height) {
            return toTowerHeight(toNumericalValue(height)+1);
        }

    public static int toNumericalValue(Tower height) {
        switch(height) {
            case GROUND: return 0;
            case BOTTOM: return 1;
            case MIDDLE: return 2;
            case TOP: return 3;
            case DOME: return 4;
            default: return 0;
        }
    }

    public static Tower toTowerHeight(int value) {
        switch(value) {
            case 0: return GROUND;
            case 1: return BOTTOM;
            case 2: return MIDDLE;
            case 3: return TOP;
            case 4: return DOME;
            default: return null;
        }
    }

}
