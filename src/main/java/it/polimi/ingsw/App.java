package it.polimi.ingsw;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.rules.EsempioDueRules;
import it.polimi.ingsw.rules.EsempioUnoRules;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {

        Player player1 = new Player("Duskwood");
        Player player2 = new Player("Zanons");
        // Qui non serve aggiungere a Session ma è un esempio per l'utilizzo dei metodi statici
        Session.addPlayer(player1);
        Session.addPlayer(player2);
        Board.resetBoard();

        player1.setRules(new EsempioUnoRules()); //Primo set di regole: può costruire fino a MIDDLE
        player2.setRules(new EsempioDueRules()); //Secondo set di regole: può costruire fino a DOME

        // I player costruiscono entrambi nella cella 0,0
        player1.buildAction(0,0); // Regole1 : costruisce BOTTOM
        player2.buildAction(0,0); // Regole2 : costruisce MIDDLE
        player1.buildAction(0,0); // Regole1 : azione bloccata
        player2.buildAction(0,0); // Regole2 : costruisce TOP
        player1.buildAction(0,0); // Regole1 : azione bloccata
        player2.buildAction(0,0); // Regole2 : costruisce DOME
        player1.buildAction(0,0); // Regole1 : azione bloccata
        player2.buildAction(0,0); // Regole2 : azione bloccata

    }
}
