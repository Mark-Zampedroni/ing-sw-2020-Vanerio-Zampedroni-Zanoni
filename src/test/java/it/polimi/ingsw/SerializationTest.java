package it.polimi.ingsw;

import static org.junit.Assert.assertEquals;
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
        Player player1= new Player("Gianni", Colors.BLUE);
        player1.addWorker(new Position(2,3));
        player1.addWorker(new Position(3,3));
        Player player2= new Player("Sandro", Colors.WHITE);
        player2.addWorker(new Position(2,4));
        player2.addWorker(new Position(4,4));
        GodRules rules1 = Gods.HESTIA.createRules();
        GodRules rules2 = Gods.APOLLO.createRules();
        Session.getInstance().addPlayer(player1);
        Session.getInstance().addPlayer(player2);
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
        assertEquals(serializedSession.getPlayers().get(0), player1);
        assertEquals(serializedSession.getPlayers().get(1), player2);
        assertEquals("Gianni", serializedSession.getPlayers().get(0).getUsername());
        assertEquals("Sandro", serializedSession.getPlayers().get(1).getUsername());
        assertEquals(serializedSession.getPlayers().get(0).getRules(), rules1);
        assertEquals(serializedSession.getPlayers().get(1).getRules(), rules2);
        assertEquals(serializedSession.getPlayers().get(0).getGod(), Gods.HESTIA);
        assertEquals(serializedSession.getPlayers().get(1).getGod(), Gods.APOLLO);
        assertTrue(((HestiaRules)serializedSession.getPlayers().get(0).getRules()).getEvent());
        assertEquals(serializedSession.getPlayers().get(0).getRules(), rules1);
        assertEquals(serializedSession.getPlayers().get(1).getRules(), rules2);
        assertTrue(serializedSession.getPlayers().get(0).isChallenger());
        assertTrue(player1.getWorkers().get(0).getPosition().equals(serializedSession.getPlayers().get(0).getWorkers().get(0).getPosition()));
        assertTrue(player1.getWorkers().get(1).getPosition().equals(serializedSession.getPlayers().get(0).getWorkers().get(1).getPosition()));
        assertTrue(player2.getWorkers().get(0).getPosition().equals(serializedSession.getPlayers().get(1).getWorkers().get(0).getPosition()));
        assertTrue(player2.getWorkers().get(1).getPosition().equals(serializedSession.getPlayers().get(1).getWorkers().get(1).getPosition()));
        assertTrue(serializedSession.getBoard().getTile(new Position(1,2)).hasDome());
        assertEquals(serializedSession.getBoard().getTile(new Position(3, 2)).getHeight(), Height.BOTTOM);
    }
}
