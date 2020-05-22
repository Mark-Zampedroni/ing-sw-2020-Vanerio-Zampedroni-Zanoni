package it.polimi.ingsw.mvc.view.gui;

import it.polimi.ingsw.mvc.view.gui.fxmlControllers.TitleController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class Gui extends Application {
    private static Gui instance = null;

    public static Gui getInstance() {
        if (instance == null)
            instance = new Gui();
        return instance;
    }

    @Override
    public void start(Stage stage) throws IOException {

        stage.setScene(new Scene(new Pane()));
        GuiManager.setLayout(stage.getScene(), "/fxmlFiles/Title.fxml");
        stage.show();
    }

    public void init(String ip, int port) {
        TitleController.setConnectionConfig(ip, port);
        Application.launch(Gui.class);
    }

    @Override
    public void stop() {
        GuiManager.getInstance().disconnectClient();
        System.exit(0);
    }
}
