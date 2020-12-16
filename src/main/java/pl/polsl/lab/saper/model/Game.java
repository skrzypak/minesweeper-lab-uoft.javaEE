package pl.polsl.lab.saper.model;

import pl.polsl.lab.saper.exception.FieldException;

/**
 * Class contain game data
 *
 * @author Konrad Skrzypczyk
 * @version 2.0
 */
public class Game {

    private String id;                       // Session id
    private GameBoard gameBoard;             // Contains game fields
    private Integer freeFieldCounter;        // Number of no selected field by player with no mine, if 0 player win
    private Boolean running;                 // Game state (1) game running, otherwise 0
    private IEnumGame.GameResult gameResult; // Contain info about player result, used to display correct result

    /**
     * Class constructor.
     * @param id session id
     * @param height new board height;
     * @param width  new board width;
     * @throws OutOfMemoryError when board is to big
     * @throws FieldException invalid baord size
     */
    public Game(String id, Integer height, Integer width) throws FieldException {

        if (height <= 0 || width <= 0) throw new FieldException("Invalid height or width size");
        this.gameResult = IEnumGame.GameResult.NONE;
        this.gameBoard = new GameBoard(height, width);
        this.freeFieldCounter = height * width;
        try {
            createBoard(height, width);
        } catch (OutOfMemoryError e) {
            throw new OutOfMemoryError(e.getMessage());
        }
        this.id = id;
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
     * Method set filed as selected by player
     *
     * @param num number of mine around field
     * @param inx field index object
     * @throws FieldException if field value is game board border or out of range
     */
    public void setFiledAroundMines(Integer num, Index inx) throws FieldException {
        this.gameBoard.get(inx).setAroundMines(num);
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

    /** Get session id
     * @return sesion id
     * */
    public String getId() {
        return this.id;
    }

}

