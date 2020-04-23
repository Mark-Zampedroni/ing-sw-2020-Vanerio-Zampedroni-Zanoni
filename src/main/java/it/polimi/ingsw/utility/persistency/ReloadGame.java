package it.polimi.ingsw.utility.persistency;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReloadGame {

    public static void restartGame() {
        SavedDataClass savedGame = deserializeFile();
        //reinserisco i dati salvati all'interno del main(?) credo

    }


    static SavedDataClass deserializeFile() {
        String filename = "santorini.game";
        SavedDataClass savedData = null;
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream input = new ObjectInputStream(file);

            savedData = (SavedDataClass) input.readObject();

            input.close();
            file.close();
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
        return savedData;
    }
}