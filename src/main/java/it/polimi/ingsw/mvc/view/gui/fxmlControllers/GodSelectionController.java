package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import java.util.List;
import java.util.Map;

public class GodSelectionController extends GenericController {

    public void initialize() {
        super.initialize(this);
    }

    public void updatePlayerGodSelection(String turnOwner, Map<String, String> choices, List<String> chosenGods) {
        // update da view quando è il turno di un altro client
    }

    public void requestPlayerGod(List<String> chosenGods, Map<String, String> choices) {
        // update da view quando tocca a questo client
    }

    public void updateStarterPlayerSelection(Map<String, String> choices) {
        // update da view a tutti i giocatori notificando che il challenger sta scegliendo lo starter player
    }

    public void requestStarterPlayer(Map<String, String> choices) {
        // update da view al challenger avvertendolo che deve scegliere lo starter player
    }

}
