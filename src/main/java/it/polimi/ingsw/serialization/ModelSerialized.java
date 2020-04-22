package it.polimi.ingsw.serialization;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Worker;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Converts the model in a serialized model to store for persistence; it also create
 * a string to transmit with {@link it.polimi.ingsw.net.messages.Message message} to the client and back
 *
 */
public class ModelSerialized implements Serializable {

    private Session session;
    private String fileName;

    /**
     * Constructor
     *
     * @param session session serialized by the ModelSerialized
     */
    public ModelSerialized(Session session) {
        this.session=session;
    }

    /**
     * Getter for the {@link String string} filename
     *
     * @return the {@link String string} filename
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Converts a the session in a {@link File file} with a specific {@link String filename}
     */
    public void sessionSerializerFileType (){

        ObjectOutputStream output;
        fileName = "serializedSession.ser";
        try {
            FileOutputStream file = new FileOutputStream(fileName);
            output = new ObjectOutputStream(file);

            output.writeObject(session);

            output.close();
            file.close();
        }
        catch(IOException ex) {
            System.out.println("IOException is caught");
        }
    }

    /**
     * Converts the session in a serialized object contained in a {@link String string}
     *
     * @return the {@link String string} containing the session
     */
    public String sessionSerializerStringType() {
        return createSerialization(Session.getInstance());
    }

    /**
     * Converts the board in a serialized object contained in a {@link String string}
     *
     * @return the {@link String string} containing the board
     */
    public String boardSerialized () {
        Board board= Session.getInstance().getBoard();
        return createSerialization(board);
    }

    /**
     * Converts a {@link List list} containing the workers in a serialized object contained in a {@link String string}
     *
     * @param playerIndex index of the player that has the workers
     * @return the {@link String string} containing the {@link List list}
     */
    public String workersSerialized(int playerIndex) {
        ArrayList<Worker> workerArrayList = Session.getInstance().getPlayers().get(playerIndex).getWorkers();
        return createSerialization(workerArrayList);
    }

    private String createSerialization(Object object) {
        try{
            ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(bytesStream);
            output.writeObject(object);
            output.flush();
            return Base64.getEncoder().encodeToString(bytesStream.toByteArray());

        }
        catch (IOException e) {
            return "Error";
        }
    }
}
