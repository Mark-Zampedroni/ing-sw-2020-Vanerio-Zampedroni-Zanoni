package it.polimi.ingsw.serialization;

import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;

import java.io.*;

import java.util.Base64;

/**
 * Class that deserializes file in case of a internal shutdown or
 * deserializes message received by {@link it.polimi.ingsw.net.client.Client client} or {@link it.polimi.ingsw.net.server.Server server}
 */
public class ModelDeserialization {

    /**
     * Converts a serialized object contained in a {@link String string} in a object
     *
     * @param message identifies the {@link String string} with the object coded inside
     * @return the object deserialized
     */
    public Object deserialize(String message) {
        Object object=null;
        try
        {
            byte [] bytes = Base64.getDecoder().decode(message);
            ByteArrayInputStream bytesStream = new ByteArrayInputStream(bytes);
            ObjectInputStream inputStream = new ObjectInputStream(bytesStream);
            object = inputStream.readObject();

        }
        catch(IOException ex) {
            System.out.println("IOException is caught");
        }
        catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
        return object;
    }

    /**
     * Returns the Session converted back to a {@link File file}
     *
     * @return the session contained in the {@link File file}
     */
    public Session deserializeSessionFile(){
        String filename="serializedSession.ser";
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
        return serializedSession;
    }
}
