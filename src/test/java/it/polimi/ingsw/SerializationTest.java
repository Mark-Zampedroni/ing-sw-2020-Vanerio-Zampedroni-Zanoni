package it.polimi.ingsw;

import static org.junit.Assert.assertTrue;

import it.polimi.ingsw.constants.Height;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.rules.EventRules;
import it.polimi.ingsw.rules.GodRules;
import it.polimi.ingsw.rules.gods.HestiaRules;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;

/**
 * Unit test for simple App.
 */
public class SerializationTest
{
    @Test
    public void correctSerialization()
    {
        Player player1= new Player("Gianni");
        Player player2= new Player("Sandro");
        GodRules rules1 = Gods.HESTIA.createRules();
        GodRules rules2 = Gods.APOLLO.createRules();
        Session.getInstance().addPlayer(player1);
        Session.getInstance().addPlayer(player2);
        player1.getWorkers().get(0).setPosition(2,3);
        player1.getWorkers().get(1).setPosition(3,3);
        player2.getWorkers().get(0).setPosition(2,4);
        player2.getWorkers().get(1).setPosition(4,4);
        player1.setColor(Colors.WHITE);
        player2.setColor(Colors.BLUE);
        player1.setGod(Gods.HESTIA);
        player2.setGod(Gods.APOLLO);
        player1.setChallenger();
        player1.setRules(rules1);
        player2.setRules(rules2);
        Session.getInstance().getBoard().getTile(new Position(1,2)).increaseHeight();
        Session.getInstance().getBoard().getTile(new Position(1,2)).increaseHeight();
        Session.getInstance().getBoard().getTile(new Position(1,2)).increaseHeight();
        player1.getRules().executeBuild(new Position(1,2));
        Session.getInstance().getBoard().getTile(new Position(3,2)).increaseHeight();
        ((HestiaRules)player1.getRules()).setEvent(true);

        String filename = "serializedGame.ser";
        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream output = new ObjectOutputStream(file);

            output.writeObject(Session.getInstance());

            output.close();
            file.close();

            System.out.println("Session has been serialized");

        }
        catch(IOException ex) {
            System.out.println("IOException is caught");
        }

        Session serializedSession=null;
        try
        {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream input = new ObjectInputStream(file);

            serializedSession = (Session)input.readObject();

            input.close();
            file.close();

        }
        catch(IOException ex) {
            System.out.println("IOException is caught");
        }
        catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
        assertTrue(serializedSession.getPlayers().get(0).equals(player1));
        assertTrue(serializedSession.getPlayers().get(1).equals(player2));
        assertTrue(serializedSession.getPlayers().get(0).getUsername().equals("Gianni"));
        assertTrue(serializedSession.getPlayers().get(1).getUsername().equals("Sandro"));
        assertTrue(serializedSession.getPlayers().get(0).getRules().equals(rules1));
        assertTrue(serializedSession.getPlayers().get(1).getRules().equals(rules2));
        assertTrue(serializedSession.getPlayers().get(0).getGod().equals(Gods.HESTIA));
        assertTrue(serializedSession.getPlayers().get(1).getGod().equals(Gods.APOLLO));
        assertTrue(((HestiaRules)serializedSession.getPlayers().get(0).getRules()).getEvent());
        assertTrue(serializedSession.getPlayers().get(0).getRules().equals(rules1));
        assertTrue(serializedSession.getPlayers().get(1).getRules().equals(rules2));
        assertTrue(serializedSession.getPlayers().get(0).isChallenger());
        assertTrue(player1.getWorkers().get(0).getPosition().equals(serializedSession.getPlayers().get(0).getWorkers().get(0).getPosition()));
        assertTrue(player1.getWorkers().get(1).getPosition().equals(serializedSession.getPlayers().get(0).getWorkers().get(1).getPosition()));
        assertTrue(player2.getWorkers().get(0).getPosition().equals(serializedSession.getPlayers().get(1).getWorkers().get(0).getPosition()));
        assertTrue(player2.getWorkers().get(1).getPosition().equals(serializedSession.getPlayers().get(1).getWorkers().get(1).getPosition()));
        assertTrue(serializedSession.getBoard().getTile(new Position(1,2)).hasDome());
        assertTrue(serializedSession.getBoard().getTile(new Position(3,2)).getHeight()== Height.BOTTOM);
        System.out.println(serializedSession.getPlayers().get(0).toString()+" "+serializedSession.getPlayers().get(1).toString());
    }
}
