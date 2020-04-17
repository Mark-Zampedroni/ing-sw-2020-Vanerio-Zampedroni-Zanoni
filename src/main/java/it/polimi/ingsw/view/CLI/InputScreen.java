package it.polimi.ingsw.view.CLI;

import java.util.ArrayList;
import java.util.List;

public class InputScreen implements Screen {

    private final List<String> layout;

    public InputScreen() {
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

    public void removeLastLine() {
        if(layout.size()  != 0) {
            layout.remove(layout.size() - 1);
        }
    }

}
