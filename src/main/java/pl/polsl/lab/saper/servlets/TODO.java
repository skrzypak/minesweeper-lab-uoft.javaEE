package pl.polsl.lab.saper.servlets;

import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.model.Game;

public class TODO {

    static private Game gameModel = null;

    private TODO() { }

    static public void set(Integer height, Integer width) throws FieldException {
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
