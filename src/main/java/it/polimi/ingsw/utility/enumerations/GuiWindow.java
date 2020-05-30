package it.polimi.ingsw.utility.enumerations;

import it.polimi.ingsw.mvc.view.gui.fxmlControllers.*;

import java.util.Arrays;
import java.util.List;

public enum GuiWindow {
    TITLE, LOBBY, CHALLENGER_SELECTION, GOD_SELECTION, BOARD;

    public static GuiWindow getInstanceName(Class c) {
        if(c == TitleController.class) { return TITLE; }
        else if(c == LobbyController.class) { return LOBBY; }
        else if(c == ChallengerSelectionController.class) { return CHALLENGER_SELECTION; }
        else if(c == GodSelectionController.class) { return GOD_SELECTION; }
        else if(c == BoardController.class) { return BOARD; }
        else { return null; }
    }

    public static String getFxmlPath(Class c) {
        List<String> s = Arrays.asList(c.toString().split("\\."));
        String name = s.get(s.size()-1);
        return "/fxmlFiles/"+name.substring(0,name.length()-10)+".fxml";
    }

}
