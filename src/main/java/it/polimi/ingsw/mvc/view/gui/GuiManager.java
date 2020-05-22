package it.polimi.ingsw.mvc.view.gui;

import it.polimi.ingsw.mvc.view.gui.fxmlControllers.NumberOfPlayersController;
import it.polimi.ingsw.mvc.view.gui.fxmlControllers.TitleController;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.serialization.dto.DtoPosition;
import it.polimi.ingsw.utility.serialization.dto.DtoSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GuiManager extends Client {
    private static GuiManager instance = null;
    private String ip;
    private int port;

    private TitleController titleController;
    private NumberOfPlayersController numberOfPlayersController;


    public static GuiManager getInstance() {
        if (instance == null)
            instance = new GuiManager();
        return instance;
    }

    /*SET A SCENE*/

    public static void setLayout(Scene scene, String path) throws IOException {
        try {
        Pane pane =  FXMLLoader.load(GuiManager.class.getResource(path));
        scene.setRoot(pane);
        } catch (IOException e) {
           System.out.println("error"); //LOGGER
        }
    }

    /*CREATE A DIALOG */
     public static void showDialog(Stage window, String title, String text) {
        FXMLLoader loader = new FXMLLoader(GuiManager.class.getClassLoader().getResource("fxmlFiles/Dialog.fxml"));

        Scene dialogScene = null;
        try {
            dialogScene = new Scene(loader.load(), 600, 300);
        } catch (IOException e) {
            //Logger
        }

        Stage dialog = new Stage();
        dialog.setScene(dialogScene);
        dialog.initOwner(window);
        dialog.initStyle(StageStyle.DECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setAlwaysOnTop(true);

         assert dialogScene != null;
         dialogScene.lookup("#okButton").addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());

        ((Label) dialogScene.lookup("#dialogTitle")).setText(title);
        ((Label) dialogScene.lookup("#dialogText")).setText(text);

        dialog.showAndWait();
    }


    void setTitleController(TitleController titleController){
         this.titleController = titleController;
    }

    void setNumberOfPlayersController(NumberOfPlayersController numberOfPlayersController){
         this.numberOfPlayersController = numberOfPlayersController;
    }


    @Override
    public void showInfo(String text) {

    }

    @Override
    public void requestNumberOfPlayers() {

    }

    @Override
    public void showLobby(List<Colors> availableColors) {

    }

    @Override
    public void requestLogin() {

    }

    @Override
    public void updateChallengerGodSelection(List<String> chosenGods) {

    }

    @Override
    public void requestChallengerGod(List<String> chosenGods) {

    }

    @Override
    public void updatePlayerGodSelection(String turnOwner, Map<String, String> choices, List<String> chosenGods) {

    }

    @Override
    public void requestPlayerGod(List<String> chosenGods, Map<String, String> choices) {

    }

    @Override
    public void updateStarterPlayerSelection(Map<String, String> choices) {

    }

    @Override
    public void requestStarterPlayer(Map<String, String> choices) {

    }

    @Override
    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {

    }

    @Override
    public void showBoard(DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {

    }
}
