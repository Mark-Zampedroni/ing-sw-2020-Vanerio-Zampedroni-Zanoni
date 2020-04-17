package it.polimi.ingsw.enumerations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Colors that represent the {@link it.polimi.ingsw.model.player.Player player}'s
 * workers on the {@link it.polimi.ingsw.model.map.Board board}
 */
public enum Colors implements Serializable {
    BLUE, RED, GREEN, WHITE;

    public static boolean isValid(String color) {
        List<String> valid = new ArrayList<>(Arrays.asList("BLUE","RED","GREEN","WHITE"));
        return valid.contains(color);
    }

}
