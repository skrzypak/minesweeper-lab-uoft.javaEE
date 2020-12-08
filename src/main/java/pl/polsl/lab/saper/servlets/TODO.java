package pl.polsl.lab.saper.servlets;

import pl.polsl.lab.saper.model.Game;
import pl.polsl.lab.saper.model.Index;

import java.util.concurrent.ThreadLocalRandom;

public class TODO {

    static private Game gameModel = null;

    private TODO() { }

    static public void set(Integer height, Integer width) {
        if (TODO.gameModel == null) {
            gameModel = new Game(height, width);
        }
    }

    static public Game get() {
        return gameModel;
    }

    static public void clear() {
        gameModel = null;
    }
}
