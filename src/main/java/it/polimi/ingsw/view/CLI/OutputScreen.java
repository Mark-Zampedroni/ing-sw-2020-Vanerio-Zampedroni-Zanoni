package it.polimi.ingsw.view.CLI;

import java.util.ArrayList;
import java.util.List;

public class OutputScreen implements Screen {

    private final List<String> layout;

    public OutputScreen() {
        layout = new ArrayList<>();
        clear();
    }

    public void clear() {
        layout.clear();
        layout.add("");
    }

    public String getLayout() {
        synchronized(layout) {
            return String.join("", layout);
        }
    }

    public void addLine(String string) {
        layout.add("\n"+string);
    }

}
