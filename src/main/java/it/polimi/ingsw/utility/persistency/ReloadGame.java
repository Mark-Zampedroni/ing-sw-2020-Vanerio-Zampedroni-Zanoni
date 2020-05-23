package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.ServerApp;
import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.game.ActionMessage;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ReloadGame {

    private static SavedDataClass savedData;
    private static boolean isAlreadyLoaded;

    public static boolean isRestartable() {
        return (isAlreadyLoaded) || deserializeFile();
    }

    public static List<String> getPlayersNames(){
        System.out.println("Querying for players, reply: "+savedData.getSession().getPlayers()); // TEST <<------
        return savedData.getSession().getPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
    }

    private static boolean deserializeFile() {
        if (ServerApp.isFeature()) {
            System.out.println("Reloading in ReloadGame");  // TEST <<-------
            String filename = "santorini.game.ser";
            try {
                FileInputStream file = new FileInputStream(filename);
                ObjectInputStream input = new ObjectInputStream(file);
                ReloadGame.savedData = (SavedDataClass) input.readObject();
                input.close();
                file.close();
                isAlreadyLoaded = true;
                System.out.println("Done with reload");  // TEST <<-------
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Exception is caught in ReloadGame : deserializeFile");
                return false;
            }
        }
        return true;
    }

    public static SavedDataClass load() {
        return savedData;
    }

    public static void setFinishedLoad() {
        isAlreadyLoaded = false;
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
