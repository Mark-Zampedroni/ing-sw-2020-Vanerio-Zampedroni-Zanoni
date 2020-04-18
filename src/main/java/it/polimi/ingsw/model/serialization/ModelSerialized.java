package it.polimi.ingsw.model.serialization;

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

    public ModelSerialized(Session session) {
        this.session=session;
    }

    public String getFileName() {
        return fileName;
    }

    //converte la sessione in un outputstream
    public ObjectOutputStream sessionSerializerFileType (){

        //for local behaviours
        ObjectOutputStream output=null;
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
        return output;
    }

    //sessione in versione stringa, unico con doppia versione
    public String sessionSerializerStringType() {
        String string="";
        try {
            ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(bytesStream);
            output.writeObject(session);
            output.flush();
            string = Base64.getEncoder().encodeToString(bytesStream.toByteArray());
            return string;
        }
        catch (IOException e) {
            return "Error";
        }
    }

    public String boardSerialized () {
        Board board= Session.getInstance().getBoard();
        String string="";

        try {
            ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(bytesStream);
            output.writeObject(board);
            output.flush();
            string = Base64.getEncoder().encodeToString(bytesStream.toByteArray());
            return string;
        }
        catch (IOException e) {
            return "Error";
        }
    }

    //dei worker serializzati non conosco i master
    public String workersSerialized(int playerIndex) {
        ArrayList<Worker> workerArrayList = Session.getInstance().getPlayers().get(playerIndex).getWorkers();
        String string="";

        try {
            ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(bytesStream);
            output.writeObject(workerArrayList);
            output.flush();
            string = Base64.getEncoder().encodeToString(bytesStream.toByteArray());
            return string;
        }
        catch (IOException e) {
            return "Error";
        }
    }

}
