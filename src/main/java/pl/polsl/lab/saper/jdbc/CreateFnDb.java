package pl.polsl.lab.saper.jdbc;

import pl.polsl.lab.saper.model.Field;
import pl.polsl.lab.saper.model.Game;

import java.sql.SQLException;

/**
 * Class contains method that define create database operation
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public class CreateFnDb {

    /**
     * Create main tables in database (GAMES, GAMES_BOARD, FIELDS)
     * @throws SQLException err syntax or connection
     */
    public static void createTables() throws SQLException {

        JDBC.executeUpdate("CREATE TABLE GAMES"
                + "(ID VARCHAR(100) PRIMARY KEY, RESULT VARCHAR(10), "
                + "FREE_FIELD_COUNTER INTEGER, GAME_BOARD_ID INTEGER);");

        JDBC.executeUpdate("CREATE TABLE GAMES_BOARD"
                + "(ID INTEGER PRIMARY KEY, NUM_OF_ROWS INTEGER, "
                + "NUM_OF_COLS INTEGER);");

        JDBC.executeUpdate("CREATE TABLE FIELDS"
                + "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "GAME_BOARD_ID INTEGER, ROW_INX INTEGER, COL_INX INTEGER, MINE BOOLEAN, MARKED BOOLEAN, "
                + "SELECTED BOOLEAN, AROUND_MINES INTEGER);");
    }

    /**
     * Insert new game data to database
     * @param gameModel game object
     * @throws SQLException err syntax or connection
     */
    public static void insertNewGameToDb(Game gameModel) throws SQLException {

        JDBC.executeUpdate("INSERT INTO GAMES(ID, RESULT, FREE_FIELD_COUNTER, GAME_BOARD_ID) " +
                "VALUES ("
                + "'" + gameModel.getId() + "'"
                +",'" + gameModel.getGameResult().toString() + "'"
                + "," + gameModel.getFreeFieldCounter()
                + "," + gameModel.getBoardData().getId()
                + ");");

        JDBC.executeUpdate("INSERT INTO GAMES_BOARD( ID, NUM_OF_ROWS, NUM_OF_COLS ) VALUES"
                + "("
                + gameModel.getBoardData().getId()+","
                + gameModel.getBoardData().getNumOfRows() + ","
                + gameModel.getBoardData().getNumOfCols() + ");");

        for(Field f: gameModel.getBoardData().getFields()) {
            JDBC.executeUpdate("INSERT INTO " +
                    "FIELDS( GAME_BOARD_ID, ROW_INX, COL_INX, MINE, MARKED, SELECTED, AROUND_MINES ) VALUES"
                    + "("
                    + gameModel.getBoardData().getId()+","
                    + f.getRowIndex() + ","
                    + f.getColIndex() + ","
                    + f.isMine() + ","
                    + f.isMarked() + ","
                    + f.isSelected() + ","
                    + f.getNumOfMinesAroundField()
                    + ");");
        }
    }
}
