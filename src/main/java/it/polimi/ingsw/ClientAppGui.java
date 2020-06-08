package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.gui.Gui;

public class ClientAppGui {
    public static void main(String[] args){
        Gui.getInstance().init((args.length == 0) ? "127.0.0.1" : args[0], 7654);
            }
}
