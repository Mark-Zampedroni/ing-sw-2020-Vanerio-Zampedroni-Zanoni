package it.polimi.ingsw.view.CLI;

import java.util.ArrayList;
import java.util.List;

public class Screen {

    private final List<String> layout;

    public Screen() {
        layout = new ArrayList<>();
        clear();
    }

    public void clear() {
        synchronized(layout) {
            layout.clear();
            layout.add("\n------------------------------------------------------");
        }
    }

    public String getLayout() {
        synchronized(layout) {
            return String.join("", layout);
        }
    }

    public void addLine(String string) {
        synchronized(layout) {
            layout.add("\n" + string);
        }
    }

    public void removeLastLine() {
        synchronized(layout) {
            if (layout.size() != 0) {
                layout.remove(layout.size() - 1);
            }
        }
    }

}
