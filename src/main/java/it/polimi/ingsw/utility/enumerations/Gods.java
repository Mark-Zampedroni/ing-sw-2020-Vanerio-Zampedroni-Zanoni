package it.polimi.ingsw.utility.enumerations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Gods implemented in the game
 */
public enum Gods implements Serializable {
    APOLLO("Your Move: Your Worker may move into an opponent Worker's space by forcing their Worker to the space yours just vacated."),
    ARTEMIS("Your Move: Your Worker may move one additional time, but not back to its initial space."),
    ATHENA("Opponent's Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn."),
    ATLAS("Your Build: Your Worker may build a dome at any level."),
    DEMETER("Your Build: Your Worker may build one additional time, but not on the same space."),
    HEPHAESTUS("Your Build: Your Worker may build one additional block (not dome) on top of your first block."),
    MINOTAUR("Your Move: Your Worker may move into an opponent Worker's space, if their Worker can be forced one space straight backwards to an unoccupied space at any level."),
    PAN("Win Condition: You also win if your Worker moves down two or more levels."),
    PROMETHEUS("Your Turn: If your Worker does not move up, it may build both before and after moving."),
    ZEUS("Your Build: Your Worker may build a block under itself."),
    TRITON("Your Move: Each time your Worker moves into a perimeter space, it may immediately move again."),
    POSEIDON("End of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."),
    HESTIA("Your Build: Your Worker may build one additional time, but this cannot be on a perimeter space."),
    HERA("Opponent's Turn: An opponent cannot win by moving into a perimeter space.");

    private final String description;

    /**
     * Constructor for each of the classes in the enum; it's private
     *
     * @param description the description of the god's power
     */
    Gods(String description) {
        this.description = description;
    }

    /**
     * Returns a list with all the names of the gods
     *
     * @return the list containing all the names of the gods
     */
    public static List<String> getGodsStringList() {
        return new ArrayList<>(Arrays.asList("APOLLO", "ARTEMIS", "ATHENA", "ATLAS", "DEMETER", "HEPHAESTUS", "MINOTAUR",
                "PAN", "PROMETHEUS", "ZEUS", "TRITON", "POSEIDON", "HESTIA", "HERA"));
    }

    /**
     * Returns if a given string is the name of a god
     *
     * @param god the name of the god
     * @return {@code true} if the given god can is one of the selectable ones
     */
    public static boolean isValid(String god) {
        List<String> valid = getGodsStringList();
        return valid.contains(god.toUpperCase());
    }

    /**
     * Getter for the description of a god
     * @return the description of the god
     */
    public String getDescription() {
        return description;
    }
}
