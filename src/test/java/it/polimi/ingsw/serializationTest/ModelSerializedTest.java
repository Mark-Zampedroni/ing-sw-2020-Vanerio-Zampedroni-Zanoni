package it.polimi.ingsw.serializationTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ModelSerializedTest {

    /*
    //a seguito di questo test abbiamo una sessione serializzata sotto forma di file dentro ModelSerialized
    //la conversione è tramite funzione, la riconversione è locale
    Player player1;
    Player player2;
    GodRules rules1;
    GodRules rules2;

    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void clear() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player1);
        Setupper.removePlayer(player2);
        player1=null;
        player2=null;
        rules1=null;
        rules2=null;
    }

    @Test
    public void sessionSerializedTest() {

        Session.getInstance().setStarted(true);
        player1= new Player("Gianni", Colors.BLUE);
        player1.addWorker(new Position(2,3));
        player1.addWorker(new Position(3,3));
        player2= new Player("Sandro", Colors.WHITE);
        player2.addWorker(new Position(2,4));
        player2.addWorker(new Position(4,4));
        rules1 = Gods.HESTIA.createRules();
        rules2 = Gods.APOLLO.createRules();
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
        ModelSerialized modelSerialized= new ModelSerialized(Session.getInstance());
        modelSerialized.sessionSerializerFileType();

        Session serializedSession=null;
        try
        {
            FileInputStream file = new FileInputStream(modelSerialized.getFileName());
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
        assertTrue(serializedSession.getPlayers().get(0).isChallenger());
        assertTrue(player1.getWorkers().get(0).getPosition().equals(serializedSession.getPlayers().get(0).getWorkers().get(0).getPosition()));
        assertTrue(player1.getWorkers().get(1).getPosition().equals(serializedSession.getPlayers().get(0).getWorkers().get(1).getPosition()));
        assertTrue(player2.getWorkers().get(0).getPosition().equals(serializedSession.getPlayers().get(1).getWorkers().get(0).getPosition()));
        assertTrue(player2.getWorkers().get(1).getPosition().equals(serializedSession.getPlayers().get(1).getWorkers().get(1).getPosition()));
        assertTrue(serializedSession.getBoard().getTile(new Position(1,2)).hasDome());
        assertEquals(serializedSession.getBoard().getTile(new Position(3, 2)).getHeight(), Height.BOTTOM);
        assertEquals(modelSerialized.getFileName(), "serializedSession.ser");
        //assertEquals(serializedSession, modelSerialized.session);
        //non è stessa sessione ma ha stesso contenuto, problema??
    }*/

    /*@Test
    public void sessionSerializedTestString() {

        Session.getInstance().setStarted(true);
        player1= new Player("Gianni", Colors.BLUE);
        player1.addWorker(new Position(2,3));
        player1.addWorker(new Position(3,3));
        player2= new Player("Sandro", Colors.WHITE);
        player2.addWorker(new Position(2,4));
        player2.addWorker(new Position(4,4));
        rules1 = Gods.HESTIA.createRules();
        rules2 = Gods.APOLLO.createRules();
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
        ModelSerialized modelSerialized= new ModelSerialized(Session.getInstance());
        String message= modelSerialized.sessionSerializerStringType();
        Session serializedSession=null;
        try
        {
            byte [] bytes = Base64.getDecoder().decode(message);
            ByteArrayInputStream bytesStream = new ByteArrayInputStream(bytes);
            ObjectInputStream inputStream = new ObjectInputStream(bytesStream);
            serializedSession = (Session) inputStream.readObject();

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
        assertTrue(serializedSession.getPlayers().get(0).isChallenger());
        assertTrue(player1.getWorkers().get(0).getPosition().equals(serializedSession.getPlayers().get(0).getWorkers().get(0).getPosition()));
        assertTrue(player1.getWorkers().get(1).getPosition().equals(serializedSession.getPlayers().get(0).getWorkers().get(1).getPosition()));
        assertTrue(player2.getWorkers().get(0).getPosition().equals(serializedSession.getPlayers().get(1).getWorkers().get(0).getPosition()));
        assertTrue(player2.getWorkers().get(1).getPosition().equals(serializedSession.getPlayers().get(1).getWorkers().get(1).getPosition()));
        assertTrue(serializedSession.getBoard().getTile(new Position(1,2)).hasDome());
        assertEquals(serializedSession.getBoard().getTile(new Position(3, 2)).getHeight(), Height.BOTTOM);
    }

    @Test
    public void boardSerializedTest(){
        ModelSerialized modelSerialized= new ModelSerialized(Session.getInstance());
        Session.getInstance().setStarted(true);

        player1= new Player("Gianni", Colors.BLUE);
        player1.addWorker(new Position(2,3));
        player1.addWorker(new Position(3,3));
        player2= new Player("Sandro", Colors.WHITE);
        player2.addWorker(new Position(2,4));
        player2.addWorker(new Position(4,4));
        rules1 = Gods.HESTIA.createRules();
        rules2 = Gods.APOLLO.createRules();
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

        String message= modelSerialized.boardSerialized();
        Board serializedBoard=null;
        try
        {
            byte [] bytes = Base64.getDecoder().decode(message);
            ByteArrayInputStream bytesStream = new ByteArrayInputStream(bytes);
            ObjectInputStream inputStream = new ObjectInputStream(bytesStream);
            serializedBoard = (Board) inputStream.readObject();

        }
        catch(IOException ex) {
            System.out.println("IOException is caught");
        }
        catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }

        assertTrue(serializedBoard.getTile(new Position(1,2)).hasDome());
        assertEquals(serializedBoard.getTile(new Position(3, 2)).getHeight(), Height.BOTTOM);
    }

    @Test
    public void workerSerializedTest() {
        ModelSerialized modelSerialized= new ModelSerialized(Session.getInstance());
        Session.getInstance().setStarted(true);

        player1= new Player("Gianni", Colors.BLUE);
        player1.addWorker(new Position(2,3));
        player1.addWorker(new Position(3,3));
        player2= new Player("Sandro", Colors.WHITE);
        player2.addWorker(new Position(2,4));
        player2.addWorker(new Position(4,4));
        rules1 = Gods.HESTIA.createRules();
        rules2 = Gods.APOLLO.createRules();
        Session.getInstance().addPlayer(player1);
        Session.getInstance().addPlayer(player2);
        player1.setGod(Gods.HESTIA);
        player2.setGod(Gods.APOLLO);
        player1.setChallenger();
        player1.setRules(rules1);
        player2.setRules(rules2);
        ((HestiaRules)player1.getRules()).setEvent(true);

        String message= modelSerialized.workersSerialized(0);
        ArrayList<Worker> serializedWorkers=null;
        try
        {
            byte [] bytes = Base64.getDecoder().decode(message);
            ByteArrayInputStream bytesStream = new ByteArrayInputStream(bytes);
            ObjectInputStream inputStream = new ObjectInputStream(bytesStream);
            serializedWorkers = (ArrayList<Worker>) inputStream.readObject();

        }
        catch(IOException ex) {
            System.out.println("IOException is caught");
        }
        catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
        assertTrue(player1.getWorkers().get(0).getPosition().equals(serializedWorkers.get(0).getPosition()));
        assertTrue(player1.getWorkers().get(1).getPosition().equals(serializedWorkers.get(1).getPosition()));
    }*/
}
