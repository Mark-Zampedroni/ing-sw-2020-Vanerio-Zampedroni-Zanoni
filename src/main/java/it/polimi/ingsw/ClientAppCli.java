package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.cli.Cli;
import it.polimi.ingsw.mvc.view.cli.CliScene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientAppCli {

    public static void main(String[] args) {
        new Cli((args.length == 0) ? "127.0.0.1" : args[0], 7654); //79.50.167.55:7654
    }
}
