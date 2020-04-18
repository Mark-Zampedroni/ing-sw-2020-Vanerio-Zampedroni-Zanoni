package it.polimi.ingsw.model.serialization;

import it.polimi.ingsw.model.Session;

import java.io.*;

import java.util.Base64;

public class ModelDeserialization {

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
