package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.cli.Ansi;
import it.polimi.ingsw.mvc.view.cli.Cli;
import it.polimi.ingsw.mvc.view.cli.CliScene;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ClientApp {

    public static void main(String[] args) {
        new Cli("127.0.0.1", 7654);
        //PrintStream out = new PrintStream(System.out, true, "UTF-8");
        //out.println( "┤")
        //System.out.println(System.getProperty("file.encoding"));



        /*
        out.println("│ ┤ ╣\t║\t╗\t╝\t╜\t╛\t┐\n" +
                "└\t┴\t┬\t├\t─\t┼\t╞\t╟\t╚\t╔\t╩\t╦\t╠\t═\t╬\t╧\n" +
                "╨\t╤\t╥\t╙\t╘\t╒\t╓\t╫\t╪\t┘\t┌");*/
        /*System.out.println("\u03f4");
        System.out.println("\u2502");
        System.out.println( "\\u" + Integer.toHexString('│' | 0x10000).substring(1) );*/
        //System.out.println(Ansi.addBackgroundColor("CYAN",true) + CliScene.TITLE + Ansi.RESET);
    }
}
