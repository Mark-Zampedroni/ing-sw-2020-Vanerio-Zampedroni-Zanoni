package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import it.polimi.ingsw.utility.enumerations.GuiWindow;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public abstract class GenericController {

    protected GuiWindow windowName;
    protected GuiManager gui;
    @FXML
    public Pane main;

    public GuiWindow getWindowName() {
        return windowName;
    }

    public void initialize(GenericController controller) {
        gui = GuiManager.getInstance();
        gui.setCurrentController(controller);
        windowName = GuiWindow.getInstanceName(controller.getClass());
    }

    public Scene getScene() {
        return main.getScene();
    }

    protected void hideNode(Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }

    protected void showNode(Node node) {
        node.setManaged(true);
        node.setVisible(true);
    }


}
