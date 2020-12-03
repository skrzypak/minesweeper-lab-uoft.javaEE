package pl.polsl.lab.saper.model;

/**
 * Class contain settings data
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public class Settings {

    private Integer lastBoardHeightInput;   // Contain last board height value sent from controller
    private Integer lastBoardWidthInput;    // Contain last board width value sent from controller

    /**
     * Class constructor.
     */
    public Settings() {
        this.lastBoardHeightInput = null;
        this.lastBoardWidthInput = null;
    }

    /**
     * Gets last board height input.
     *
     * @return last board height input
     */
    public Integer getLastBoardHeightInput() {
        return this.lastBoardHeightInput;
    }

    /**
     * Set last board height input from user
     *
     * @param num input value in Integer format
     */
    public void setLastBoardHeightInput(Integer num) {
        this.lastBoardHeightInput = num;
    }

    /**
     * Gets last board width input.
     *
     * @return last board width input
     */
    public Integer getLastBoardWidthInput() {
        return this.lastBoardWidthInput;
    }

    /**
     * Set last board width input from user
     *
     * @param num input value in Integer format
     */
    public void setLastBoardWidthInput(Integer num) {
        this.lastBoardWidthInput = num;
    }
}
