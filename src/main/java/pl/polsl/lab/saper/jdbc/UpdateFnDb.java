package pl.polsl.lab.saper.jdbc;

import pl.polsl.lab.saper.model.IEnumGame;
import pl.polsl.lab.saper.model.Index;

import java.sql.SQLException;

/**
 * Class contains method that define update database operation
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public class UpdateFnDb {

    /**
     * Method set filed as mark
     * @param id game id
     * @param inx field index object
     * @throws SQLException err syntax or connection
     */
    public static void setFieldAsMark(String id, Index inx) throws SQLException {

        JDBC.executeUpdate("UPDATE FIELDS SET "
                + "FIELDS.MARKED=true "
                + "WHERE GAME_BOARD_ID=("
                + "SELECT GAMES_BOARD.ID FROM (GAMES JOIN GAMES_BOARD ON GAMES.GAME_BOARD_ID = GAMES_BOARD.ID) " +
                "WHERE GAMES.ID = " + "'" + id + "'" + " "
                + ") AND "
                + "FIELDS.ROW_INX="+inx.getRowIndex()+" AND "
                + "FIELDS.COL_INX="+inx.getColIndex()
                + ";");
    }

    /**
     * Method set filed as selected by player
     * @param id game id
     * @param inx field index object
     * @throws SQLException err syntax or connection
     */
    public static void setFieldAsSelected(String id, Index inx) throws SQLException {
        if(!ReadFnDb.fieldSelected(id, inx)) {

            JDBC.executeUpdate("UPDATE FIELDS SET "
                    + "SELECTED=true "
                    + "WHERE GAME_BOARD_ID=("
                    + "SELECT GAMES_BOARD.ID FROM (GAMES JOIN GAMES_BOARD ON GAMES.GAME_BOARD_ID = GAMES_BOARD.ID) " +
                    "WHERE GAMES.ID = " + "'" + id + "'" + " "
                    + ") AND "
                    + "FIELDS.ROW_INX="+inx.getRowIndex()+" AND "
                    + "FIELDS.COL_INX="+inx.getColIndex()
                    + ";");
            UpdateFnDb.decrementFreeFieldCounter(id);
        }
    }

    /**
     * Method decrement free field counter (number of non selected no-mine fields)
     * @param id game id
     * @throws SQLException err syntax or connection
     */
    public static void decrementFreeFieldCounter(String id) throws SQLException {
        Integer counter = ReadFnDb.getFreeFieldCounter(id);
        counter--;
        JDBC.executeUpdate("UPDATE GAMES SET "
                + "FREE_FIELD_COUNTER=" + counter + " "
                + "WHERE ID="+ "'" + id + "'"
                + ";");
    }

    /**
     * Method set game as win
     * @param id game id
     * @throws SQLException err syntax or connection
     */
    public static void setWin(String id) throws SQLException {
        JDBC.executeUpdate("UPDATE GAMES SET "
                + "RESULT=" + "'"  + IEnumGame.GameResult.WIN.toString() + "'"  + " "
                + "WHERE ID="+ "'" + id + "'"
                + ";");
    }

    /**
     * Method set game as loses
     * @param id game id
     * @throws SQLException err syntax or connection
     */
    public static void setLose(String id) throws SQLException {
        JDBC.executeUpdate("UPDATE GAMES SET "
                + "RESULT=" + "'"  + IEnumGame.GameResult.LOSE.toString() + "'"  + " "
                + "WHERE ID="+ "'" + id + "'"
                + ";");
    }

    /**
     * Method unmark filed
     * @param id game id
     * @param inx field index object
     * @throws SQLException err syntax or connection
     */
    public static void removeFieldMark(String id, Index inx) throws SQLException {

        JDBC.executeUpdate("UPDATE FIELDS SET "
                + "MARKED=false "
                + "WHERE GAME_BOARD_ID=("
                + "SELECT GAMES_BOARD.ID FROM (GAMES JOIN GAMES_BOARD ON GAMES.GAME_BOARD_ID = GAMES_BOARD.ID) " +
                "WHERE GAMES.ID = " + "'" + id + "'" + " "
                + ") AND "
                + "FIELDS.ROW_INX="+inx.getRowIndex()+" AND "
                + "FIELDS.COL_INX="+inx.getColIndex()
                + ";");
    }
}
