package pl.polsl.lab.saper.model;

/**
 * Interface that define game enum type used in game model
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public interface IEnumGame {

    /**
     * Enum type define game result:
     * NONE no special game result
     * LOSE player lose game
     * WIN player win game
     * CANCELED player canceled game
     */
    enum GameResult {
        /**
         * None game result.
         */
        NONE,
        /**
         * Lose game result.
         */
        LOSE,
        /**
         * Win game result.
         */
        WIN,
        /**
         * Canceled game result.
         */
        CANCELED
    }
}
