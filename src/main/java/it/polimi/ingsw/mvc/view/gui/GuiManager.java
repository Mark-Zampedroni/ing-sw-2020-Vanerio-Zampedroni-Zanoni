package it.polimi.ingsw.mvc.view.gui;

import it.polimi.ingsw.mvc.view.gui.fxmlControllers.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GuiManager extends Client {

    private static GuiManager instance = null;
    private static Logger GUI_LOG;

    private static double width;

    private GenericController currentController;

    private GuiManager() {
        super();
        GUI_LOG = LOG;
    }

    public static GuiManager getInstance() {
        if (instance == null)
            instance = new GuiManager();
        return instance;
    }

    public Stage getStage() {
        return Gui.getStage();
    }

    public double getDefaultWidth() {
        return width;
    }

    public void setDefaultWidth(double w) {
        width = w;
    }

    public void setCurrentController(GenericController currentController) {
        this.currentController = currentController;
    }

    @Override
    protected void disconnectClient() {
        super.disconnectClient();
    }

    /*SET A SCENE*/

    public static void setLayout(Scene scene, String path) {
        try {
            Pane pane = loadFxmlPane(path);
            scene.setRoot(pane);
        } catch(IOException e) {
            GUI_LOG.severe("Can't load "+path);
        }

    }

    /*CREATE A DIALOG */
     public static void showDialog(Stage window, String title, String text) throws IOException {
        String path = "/fxmlFiles/Dialog.fxml";
        Pane dialogPane = loadFxmlPane(path);
        Scene dialogScene = new Scene(dialogPane, 600, 300);

        Stage dialog = new Stage();
        dialog.setScene(dialogScene);
        dialog.initOwner(window);
        dialog.initStyle(StageStyle.DECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setAlwaysOnTop(true);

        dialogScene.lookup("#okButton").addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());

        ((Label) dialogScene.lookup("#dialogTitle")).setText(title);
        ((Label) dialogScene.lookup("#dialogText")).setText(text);

        dialog.showAndWait();
    }

    private static Pane loadFxmlPane(String path) throws IOException {
         try{
            return FXMLLoader.load(GuiManager.class.getResource(path));
         } catch (final IOException e) {
             String errorText = "Resource at "+path+" couldn't be loaded";
             GUI_LOG.severe(errorText);
             throw new IOException(errorText);
         }
    }

    public Map<String,Colors> getPlayers(){ return players;}

    public String getNumberOfPlayers() {
         return (players == null) ? String.valueOf(0) : Integer.toString(players.size());
    }

    public String getUsername() { return username; }

    public static String getFxmlPath(Class<?> c) {
        List<String> s = Arrays.asList(c.toString().split("\\."));
        String name = s.get(s.size()-1);
        return "/fxmlFiles/"+name.substring(0,name.length()-10)+".fxml";
    }

    private void runUpdate(Class<?> c, Runnable request) {
        if(!(currentController.getWindowName() == c)) {
            Platform.runLater(() -> setLayout(currentController.getScene(), GuiManager.getFxmlPath(c)));
        }
        Platform.runLater(request);
    }

    @Override
    public void showInfo(String text) {
        runUpdate(TitleController.class, () -> ((TitleController)currentController).showInfo(text));
    }

    @Override
    public void requestNumberOfPlayers() {
        runUpdate(TitleController.class, () -> ((TitleController)currentController).requestNumberOfPlayers());
    }

    @Override
    public void showLobby(List<Colors> availableColors) {
        runUpdate(LobbyController.class, () -> ((LobbyController)currentController).showLobby(availableColors));
    }

    @Override
    public void updateChallengerGodSelection(List<String> chosenGods) {
        runUpdate(ChallengerSelectionController.class, () ->((ChallengerSelectionController)currentController).updateChallengerGodSelection(chosenGods));
    }

    @Override
    public void requestChallengerGod(List<String> chosenGods) {
        runUpdate(ChallengerSelectionController.class, () ->((ChallengerSelectionController)currentController).requestChallengerGod(chosenGods));
    }


    @Override
    public void updatePlayerGodSelection(String turnOwner, Map<String, String> choices, List<String> chosenGods) {
        runUpdate(GodSelectionController.class, () ->((GodSelectionController)currentController).updatePlayerGodSelection(turnOwner,choices,chosenGods));
    }

    @Override
    public void requestPlayerGod(List<String> chosenGods, Map<String, String> choices) {
        runUpdate(GodSelectionController.class, () ->((GodSelectionController)currentController).requestPlayerGod(chosenGods,choices));
    }

    @Override
    public void updateStarterPlayerSelection(Map<String, String> choices) {
        runUpdate(GodSelectionController.class, () ->((GodSelectionController)currentController).updateStarterPlayerSelection(choices));
    }

    @Override
    public void requestStarterPlayer(Map<String, String> choices) {
        runUpdate(GodSelectionController.class, () ->((GodSelectionController)currentController).requestStarterPlayer(choices));
    }

    @Override
    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods, boolean specialPower) {
        runUpdate(BoardController.class, () ->((BoardController)currentController).requestTurnAction(possibleActions,session,colors,gods));
    }

    @Override
    public void showBoard(DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        runUpdate(BoardController.class, () ->((BoardController)currentController).showBoard(session,colors,gods));
    }

    @Override
    public void showReconnection(boolean isReconnecting) {
        Platform.runLater(() -> currentController.showReconnection(isReconnecting));
    }

    @Override
    public void showDisconnected(String info) {
        runUpdate(DisconnectionController.class, () -> ((DisconnectionController)currentController).showDisconnected(info));
    }

    @Override
    public void requestLogin() { /* Logic already implemented within showLobby */ }
}
