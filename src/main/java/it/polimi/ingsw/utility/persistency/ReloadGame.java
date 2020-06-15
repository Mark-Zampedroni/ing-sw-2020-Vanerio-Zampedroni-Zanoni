package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.ServerApp;
import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.dto.DtoSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ReloadGame {

    private static SavedData savedData;
    private static boolean isAlreadyLoaded;

    public static boolean isRestartable() {
        return (isAlreadyLoaded) || deserializeFile();
    }

    public static List<String> getInGamePlayersNames(){
        return savedData.getSession().getPlayers().stream()
                .filter(p -> !p.isLoser())
                .map(Player::getUsername).collect(Collectors.toList());
    }

    private static boolean deserializeFile() {
        if (ServerApp.isFeature()) {
            String filename = "santorini.game.ser";
            try {
                FileInputStream file = new FileInputStream(filename);
                ObjectInputStream input = new ObjectInputStream(file);
                ReloadGame.savedData = (SavedData) input.readObject();
                input.close();
                file.close();
                isAlreadyLoaded = true;
            } catch (IOException | ClassNotFoundException e) {
                return false;
            }
        }
        return true;
    }

    public static SavedData load() {
        return savedData;
    }

    public static void setFinishedLoad() {
        isAlreadyLoaded = false;
    }

    public static void reloadViews(SessionController controller, Map<String, ServerConnection> map, List<RemoteView> views, GameState state) {
        for (String name : map.keySet()) {
            map.get(name).putInLobby();
            RemoteView view = new RemoteView(map.get(name));
            view.register(name);
            views.add(view);
            if(state == GameState.GAME) { view.getFirstDTOSession(new DtoSession(savedData.getSession())); }
            view.addObserver(controller);
            savedData.getSession().getPlayers().stream()
                    .filter(p -> p.getRules() != null)
                    .forEach(p -> p.getRules().addObserver(view));
        }
    }

    public static SessionController reloadConnection(SessionController sessionController, Map<String,ServerConnection> reconnecting, ServerConnection connection, Logger LOG, Message message) {
        SessionController newController = null;
        if(!sessionController.isGameStarted() && ReloadGame.isRestartable()) {
            List<String> previousPlayers = ReloadGame.getInGamePlayersNames();
            if(previousPlayers.contains(message.getSender()) && !reconnecting.containsKey(message.getSender())) {
                newController = openIfFull(reconnecting,LOG,previousPlayers,connection,message);
            }
            else {
                connection.denyReconnection(message.getSender(),"You are not a player of the game being loaded");
                LOG.info("Unknown player "+message.getInfo()+" tried to reconnect to previous game");
            }
        }
        else {
            connection.denyReconnection(message.getSender(),"No game files that can be loaded exist");
            LOG.info("Player "+message.getInfo()+" tried to reconnect but no saved game exists");
        }
        return newController;
    }

    private static SessionController openIfFull(Map<String,ServerConnection> reconnecting, Logger LOG, List<String> previousPlayers, ServerConnection connection, Message message) {
        SessionController newController = null;
        LOG.info("Player "+message.getSender()+" asked to reconnect to the previous game");
        reconnecting.put(message.getSender(), connection);
        if(reconnecting.size() == previousPlayers.size()) {
            newController = new SessionController(LOG,ReloadGame.load(),reconnecting);
            ReloadGame.setFinishedLoad();
        }
        return newController;
    }

    public static boolean clearSavedFile() {
        File save = new File("santorini.game.ser");
        return save.delete();
    }

}
