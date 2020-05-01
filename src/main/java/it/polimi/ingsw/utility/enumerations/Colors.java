package it.polimi.ingsw.utility.enumerations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Colors that represent the {@link it.polimi.ingsw.MVC.model.player.Player player}'s
 * workers on the {@link it.polimi.ingsw.MVC.model.map.Board board}
 */
public enum Colors implements Serializable {
    BLUE, BROWN, WHITE;

    public static boolean isValid(String color) {
        List<String> valid = new ArrayList<>(Arrays.asList("BLUE","BROWN","WHITE"));
        return valid.contains(color);
    }

}
