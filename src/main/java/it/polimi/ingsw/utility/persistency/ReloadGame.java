package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.ServerApp;
import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.dto.DtoSession;

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

    public static List<String> getPlayersNames(){
        return savedData.getSession().getPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
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
            List<String> previousPlayers = ReloadGame.getPlayersNames();
            if(previousPlayers.contains(message.getSender()) && !reconnecting.containsKey(message.getSender())) {
                newController = openGame(reconnecting,LOG,previousPlayers,connection,message);
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

    private static SessionController openGame(Map<String,ServerConnection> reconnecting, Logger LOG, List<String> previousPlayers, ServerConnection connection, Message message) {
        SessionController newController = null;
        LOG.info("Player "+message.getSender()+" asked to reconnect to the previous game");
        reconnecting.put(message.getSender(), connection);
        if(reconnecting.size() == previousPlayers.size()) {
            newController = new SessionController(LOG,ReloadGame.load(),reconnecting);
            ReloadGame.setFinishedLoad();
        }
        return newController;
    }

}


    //non posso avere lista delle connessioni quindi:
    //metodo che riceve da client dei ping con il loro nome in game, quando li riceve li associa creando delle nuove view
    //e associando la socket una volta create le nuove view allora ristabilisce il sessioncontroller e lo statecontroller relativo
    //per poi mandare in broadcast l'ultimo messaggio di cui non aveva ricevuto risposta, mi sono salvato ultimo messaggio ricevuto
    //
    // SCHEMATIZZATO:
    //
    // 1° SALVATAGGIO:
    // ->Salvo ogni volta che mi arriva un messaggio (Azioni non ancora svolta, 1° caso)
    // ->Salvo ogni volta che invio una risposta (Azione svolta, 2° caso)
    //
    // 2° RICARICA:
    // Elementi: dati sessioncontroller (gamestate, turnowner, gamecapacity, session, statecontroller, lista dei nomi),
    //           statecontroller, ultimo messaggio (se risposta allora avevo svolto ultima azione)
    // Mancanti: lista delle view (ho solo lista dei nomi dei player), LOGGER, viewLock (che si ricrea)
    // -> (A) Se sono in lobby controller, nuovo gioco e segnalo di tornare al primo passo del client
    // -> (B) Da selection in poi invece ripristino il game
    //
    // (A) Quando avviene la ricarica, controllo lo stato di gioco, se è lobby allora creo un nuovo Server da zero
    //      a quelli di cui mi arrivano i ping rispondo con "restarta game" e loro tornano a prima schermata (lato client)
    //
    // (B) Da selection in poi, recupero tutti i dati,
    //      1) A questo punto gli faccio riconnettere tutti i client che stanno pingando verso il server {il loro nome,
    //         non necessario perchè mando tutto in broadcast, va bene generico ping}, controllando la lista
    //         dei nomi che è memorizzata, creo delle nuove remote-view
    //      2) Creo un nuovo session controller, gli associo lo state controller relativo (salvato)
    //         dopo gli assegno i dati relativi al sessioncontroller appena creato (coprendo le variabili non salvabili)
    //      3) Controllo se l'ultimo messaggio è di risposta dal server allora sono in attesa di messaggi quindi
    //         mando un messaggio con l'ultima risposta e il tutto torna tranquillamente in esecuzione.
    //         se invece l'ultimo messaggio è esegui allora faccio l'elaborazione legata a quel messaggio poi vado avanti
    //         e il tutto torna in esecuzione
    //      4) Fine del ripristino, i salvataggi si svolgono tranquillamente come prima
    //      5) A fine della partita CANCELLARE il file dei salvataggi
    //
