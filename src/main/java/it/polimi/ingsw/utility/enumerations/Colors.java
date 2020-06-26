package it.polimi.ingsw.utility.enumerations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Available colors; each {@link it.polimi.ingsw.mvc.model.player.Player player} can choose its own,
 * at a given Session a color can't be assigned to more than one player
 */
public enum Colors implements Serializable {
    BLUE, BROWN, WHITE;

    /**
     * Returns if a string is the name of a valid color
     *
     * @param color the name of the color to check
     * @return {@code true} if the name given is one of the colors in the game
     */
    public static boolean isValid(String color) {
        List<String> valid = new ArrayList<>(Arrays.asList("BLUE", "BROWN", "WHITE"));
        return valid.contains(color);
    }

}
