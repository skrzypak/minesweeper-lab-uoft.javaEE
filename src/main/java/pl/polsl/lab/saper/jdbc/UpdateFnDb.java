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
    public static void setFieldAsMark(Integer id, Index inx) throws SQLException {
        JDBC.executeUpdate("UPDATE FIELDS SET "
                + "MARKED=true "
                + "WHERE GAME_ID="+ id + " AND "
                + "ROW_INX="+inx.getRowIndex()+" AND "
                + "COL_INX="+inx.getColIndex()
                + ";");
    }

    /**
     * Method set filed as selected by player
     * @param id game id
     * @param inx field index object
     * @throws SQLException err syntax or connection
     */
    public static void setFieldAsSelected(Integer id, Index inx) throws SQLException {
        if(!ReadFnDb.fieldSelected(id, inx)) {
            JDBC.executeUpdate("UPDATE FIELDS SET "
                    + "SELECTED=true "
                    + "WHERE GAME_ID="+ id + " AND "
                    + "ROW_INX="+inx.getRowIndex()+" AND "
                    + "COL_INX="+inx.getColIndex()
                    + ";");
            UpdateFnDb.decrementFreeFieldCounter(id);
        }
    }

    /**
     * Method decrement free field counter (number of non selected no-mine fields)
     * @param id game id
     * @throws SQLException err syntax or connection
     */
    public static void decrementFreeFieldCounter(Integer id) throws SQLException {
        Integer counter = ReadFnDb.getFreeFieldCounter(id);
        counter--;
        JDBC.executeUpdate("UPDATE GAMES SET "
                + "FREE_FIELD_COUNTER=" + counter + " "
                + "WHERE ID="+ id
                + ";");
    }

    /**
     * Method set game as win
     * @param id game id
     * @throws SQLException err syntax or connection
     */
    public static void setWin(Integer id) throws SQLException {
        JDBC.executeUpdate("UPDATE GAMES SET "
                + "RESULT=" + "'"  + IEnumGame.GameResult.WIN.toString() + "'"  + " "
                + "WHERE ID="+ id
                + ";");
    }

    /**
     * Method set game as loses
     * @param id game id
     * @throws SQLException err syntax or connection
     */
    public static void setLose(Integer id) throws SQLException {
        JDBC.executeUpdate("UPDATE GAMES SET "
                + "RESULT=" + "'"  + IEnumGame.GameResult.LOSE.toString() + "'"  + " "
                + "WHERE ID="+ id
                + ";");
    }

    /**
     * Method unmark filed
     * @param id game id
     * @param inx field index object
     * @throws SQLException err syntax or connection
     */
    public static void removeFieldMark(Integer id, Index inx) throws SQLException {
        JDBC.executeUpdate("UPDATE FIELDS SET "
                + "MARKED=false "
                + "WHERE GAME_ID="+ id + " AND "
                + "ROW_INX="+inx.getRowIndex()+" AND "
                + "COL_INX="+inx.getColIndex()
                + ";");
    }
}
