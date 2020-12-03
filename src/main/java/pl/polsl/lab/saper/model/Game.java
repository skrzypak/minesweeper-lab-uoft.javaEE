package pl.polsl.lab.saper.model;

import pl.polsl.lab.saper.exception.FieldException;

/**
 * Class contain game data
 *
 * @author Konrad Skrzypczyk
 * @version 2.0
 */
public class Game {

    private GameBoard gameBoard;            // Contains game fields
    private Integer freeFieldCounter;       // Number of no selected field by player with no mine, if 0 player win
    private Boolean running;                // Game state (1) game running, otherwise 0
    private IEnumGame.GameResult gameResult; // Contain info about player result, used to display correct result

    /**
     * Class constructor.
     *
     * @param height new board height;
     * @param width  new board width;
     * @throws OutOfMemoryError when board is to big
     */
    public Game(Integer height, Integer width) {
        this.gameResult = IEnumGame.GameResult.NONE;
        this.gameBoard = new GameBoard(height, width);
        this.freeFieldCounter = height * width;
        try {
            createBoard(height, width);
        } catch (OutOfMemoryError e) {
            throw new OutOfMemoryError(e.getMessage());
        }
        this.running = true;
    }

    /**
     * Method initialize empty new board game
     *
     * @param height new board height;
     * @param width  new board width;
     */
    private void createBoard(Integer height, Integer width) {

        this.gameBoard = new GameBoard(height, width);

        // Fill all fields as empty
        for (int i = 0; i < this.gameBoard.getNumOfRows(); i++) {
            for (int j = 0; j < this.gameBoard.getNumOfCols(); j++) {
                try {
                    this.gameBoard.putEmptyField(new Index(i, j));
                } catch (FieldException ignored) {
                }
            }
        }
    }

    /**
     * Return game board
     *
     * @return game board array
     */
    public GameBoard getBoardData() {
        return this.gameBoard;
    }

    /**
     * Get game status
     *
     * @return Game end (0), Game running (1)
     */
    public Boolean getRunning() {
        return this.running;
    }

    /**
     * Method set filed as mark
     *
     * @param inx field index object
     * @throws FieldException if field value is game board border or out of range
     */
    public void setFieldAsMark(Index inx) throws FieldException {
        isCorrectField(inx);
        this.gameBoard.get(inx).setFieldAsMark();
    }

    /**
     * Method unmark filed
     *
     * @param inx field index object
     * @throws FieldException if field value is game board border or out of range
     */
    public void removeFieldMark(Index inx) throws FieldException {
        isCorrectField(inx);
        this.gameBoard.get(inx).removeFieldMark();
    }

    /**
     * Method set filed as selected by player
     *
     * @param num number of mine around field
     * @param inx field index object
     * @throws FieldException if field value is game board border or out of range
     */
    public void setFieldAsSelected(Integer num, Index inx) throws FieldException {

        if (num < 0 || num > 8) throw new FieldException("Invalid number of mine");
        isCorrectField(inx);

        if (this.gameBoard.get(inx).setFieldAsSelected(num))
            this.freeFieldCounter--;
    }

    /**
     * Method return number of game board rows
     *
     * @return number of rows
     */
    public Integer getNumOfRows() {
        return this.gameBoard.getNumOfRows();
    }

    /**
     * Method return number of game board columns
     *
     * @return number of columns
     */
    public Integer getNumOfCols() {
        return this.gameBoard.getNumOfCols();
    }

    /**
     * Method return info about is field a mine
     *
     * @param inx field index object
     * @return true if is mine, otherwise false
     * @throws FieldException if field value is game board border or out of range
     */
    public boolean getInfoAboutMine(Index inx) throws FieldException {
        isCorrectField(inx);
        return this.gameBoard.get(inx).isMine();
    }

    /**
     * Method return info about is field mark
     *
     * @param inx field index object
     * @return true if is selected, otherwise false
     * @throws FieldException if field value is game board border or out of range
     */
    public boolean getInfoAboutMark(Index inx) throws FieldException {
        isCorrectField(inx);
        return this.gameBoard.get(inx).isMarked();
    }

    /**
     * Method stop game
     */
    private void endGame() {
        this.running = false;
    }

    /**
     * Method set game as loses
     */
    public void setLose() {
        this.gameResult = IEnumGame.GameResult.LOSE;
        endGame();
    }

    /**
     * Method set game as winner
     */
    public void setWin() {
        this.gameResult = IEnumGame.GameResult.WIN;
        endGame();
    }

    /**
     * Method set game as canceled
     */
    public void setCancel() {
        this.gameResult = IEnumGame.GameResult.CANCELED;
        endGame();
    }

    /**
     * Method return game result
     *
     * @return LOSE (player lose) ,WIN (player wind), NONE (currently no result)
     */
    public IEnumGame.GameResult getGameResult() {
        return this.gameResult;
    }

    /**
     * Method return number of non selected fields by user
     *
     * @return number of non selected fields
     */
    public int getFreeFieldCounter() {
        return this.freeFieldCounter;
    }

    /**
     * Method set number of non selected fields by user
     *
     * @param num new number
     */
    public void setFreeFieldCounter(Integer num) {
        this.freeFieldCounter = num;
    }

    /**
     * Method return info about field was selected earlier by player
     *
     * @param inx field index object
     * @return true if was selected, otherwise false
     * @throws FieldException if field value is game board border or out of range
     */
    public boolean fieldSelected(Index inx) throws FieldException {
        isCorrectField(inx);
        return this.gameBoard.get(inx).isSelected();
    }

    /**
     * Method check that field is correct
     *
     * @param inx field index object
     * @throws FieldException if field value is game board border or out of range
     */
    public void isCorrectField(Index inx) throws FieldException {
        if (inx.getRowIndex() <= 0 || inx.getRowIndex() > getNumOfRows() - 2)
            throw new FieldException("Invalid row index");
        if (inx.getColIndex() <= 0 || inx.getColIndex() > getNumOfCols() - 2)
            throw new FieldException("Invalid column index");
    }
}

