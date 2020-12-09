package pl.polsl.lab.saper;

import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.model.Game;

/**
 * Class define global game model
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public class Content {

    static private Game gameModel = null;   // Game model object

    /**
     * Class constructor
     */
    private Content() { }

    /**
     * Create new gameModel instance if not exists
     * @param height board height
     * @param width board width
     * @throws FieldException if dimensions are wrong
     * */
    static public void set(Integer height, Integer width) throws FieldException {
        if (Content.gameModel == null) {
            gameModel = new Game(height, width);
        }
    }

    /**
     * Get game model object
     * @return game model object
     * */
    static public Game get() {
        return gameModel;
    }

    /**
     * Remove game model object (equals stop game)
     * */
    static public void clear() {
        gameModel = null;
    }
}
