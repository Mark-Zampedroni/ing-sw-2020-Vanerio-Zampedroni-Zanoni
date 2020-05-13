package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.cli.Ansi;
import it.polimi.ingsw.mvc.view.cli.Cli;
import it.polimi.ingsw.mvc.view.cli.CliScene;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

public class ClientApp {

    public static void main(String[] args) {
        new Cli("127.0.0.1", 7654);

        /*
        IntStream.range(0, 1000)
                .mapToObj(i -> "Char: " + i + " ==> " + new String(Character.toChars(i)))
                .forEach(System.out::println);*/

    }
}
