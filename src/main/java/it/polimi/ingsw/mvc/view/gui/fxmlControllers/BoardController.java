package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.serialization.dto.DtoPosition;
import it.polimi.ingsw.utility.serialization.dto.DtoSession;

import java.util.List;
import java.util.Map;

public class BoardController extends GenericController {

    public void initialize() {
        super.initialize(this);
    }

    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        // update da view che richiede di fare un azione tra quelle elencate in una delle posizione fornite per ognuna
    }

    public void showBoard(DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        // update da view che richiede di aggiornare la board a quella contenuta in session
        // il 3D è pesante da generare, va modificata solo la differenza tra la board vecchia (va salvata) e quella nuova
    }

}
