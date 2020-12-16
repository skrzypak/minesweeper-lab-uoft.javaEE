package pl.polsl.lab.saper.jdbc;

import pl.polsl.lab.saper.model.IEnumGame;
import pl.polsl.lab.saper.model.Index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class contains method that define read database operation
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public class ReadFnDb {

    /**
     * Method return info about is field mark
     * @param id game id
     * @param inx field index object
     * @return true if is selected, otherwise false
     * @throws SQLException err syntax or connection
     */
    public static boolean getInfoAboutMark(String id, Index inx) throws SQLException {
        ResultSet rs = JDBC.executeQuery("SELECT FIELDS.MARKED "
                + "FROM ((GAMES JOIN GAMES_BOARD ON GAMES.GAME_BOARD_ID = GAMES_BOARD.ID) "
                + "JOIN FIELDS ON GAMES_BOARD.ID = FIELDS.GAME_BOARD_ID) "
                + "WHERE GAMES.ID="+ "'" + id + "'" + " AND "
                + "FIELDS.ROW_INX="+inx.getRowIndex()+" AND "
                + "FIELDS.COL_INX="+inx.getColIndex()
                + ";");

        if (rs.next()) {
            return rs.getBoolean("FIELDS.MARKED");
        }

        throw new SQLException("Not found MARKED param in FIELDS database INDEX: " + inx.toString());
    }

    /**
     * Get info about field select state
     * @param id game id
     * @param inx field index object
     * @throws SQLException err syntax or connection
     * @return true if field was selected otherwise false
     */
    public static boolean fieldSelected(String id, Index inx) throws SQLException {
        ResultSet rs = JDBC.executeQuery("SELECT FIELDS.SELECTED "
                + "FROM ((GAMES JOIN GAMES_BOARD ON GAMES.GAME_BOARD_ID = GAMES_BOARD.ID) "
                + "JOIN FIELDS ON GAMES_BOARD.ID = FIELDS.GAME_BOARD_ID) "
                + "WHERE GAMES.ID="+ "'" + id + "'" + " AND "
                + "FIELDS.ROW_INX="+inx.getRowIndex()+" AND "
                + "FIELDS.COL_INX="+inx.getColIndex()
                + ";");

        if (rs.next()) {
            return rs.getBoolean("FIELDS.SELECTED");
        }

        throw new SQLException("Not found SELECTED param in FIELDS database INDEX: " + inx.toString());
    }

    /**
     * Get info about field contain mine
     * @param id game id
     * @param inx field index object
     * @throws SQLException err syntax or connection
     * @return true if field is mine otherwise false
     */
    public static boolean getInfoAboutMine(String id, Index inx) throws SQLException {
        ResultSet rs = JDBC.executeQuery("SELECT FIELDS.MINE  "
                + "FROM ((GAMES JOIN GAMES_BOARD ON GAMES.GAME_BOARD_ID = GAMES_BOARD.ID) "
                + "JOIN FIELDS ON GAMES_BOARD.ID = FIELDS.GAME_BOARD_ID) "
                + "WHERE GAMES.ID="+ "'" + id + "'" + " AND "
                + "FIELDS.ROW_INX="+inx.getRowIndex()+" AND "
                + "FIELDS.COL_INX="+inx.getColIndex()
                + ";");

        if (rs.next()) {
            return rs.getBoolean("FIELDS.MINE");
        }

        throw new SQLException("Not found MINE param in FIELDS database INDEX: " + inx.toString());
    }

    /**
     * Get info about number of mines around field
     * @param id game id
     * @param inx field index object
     * @throws SQLException err syntax or connection
     * @return number of mines from database
     */
    public static Integer getNumOfMinesAroundField(String id, Index inx) throws SQLException {
        ResultSet rs = JDBC.executeQuery("SELECT FIELDS.AROUND_MINES "
                + "FROM ((GAMES JOIN GAMES_BOARD ON GAMES.GAME_BOARD_ID = GAMES_BOARD.ID) "
                + "JOIN FIELDS ON GAMES_BOARD.ID = FIELDS.GAME_BOARD_ID) "
                + "WHERE GAMES.ID="+ "'" + id + "'" + " AND "
                + "FIELDS.ROW_INX="+inx.getRowIndex()+" AND "
                + "FIELDS.COL_INX="+inx.getColIndex()
                + ";");

        if (rs.next()) {
            return rs.getInt("FIELDS.AROUND_MINES");
        }
        throw new SQLException("Not found AROUND_MINES param in FIELDS database INDEX: " + inx.toString());
    }

    /**
     * Get number of fields that not contains mine and user no selected it
     * @param id game id
     * @throws SQLException err syntax or connection
     * @return number from database
     */
    public static Integer getFreeFieldCounter(String id) throws SQLException {
        ResultSet rs = JDBC.executeQuery("SELECT FREE_FIELD_COUNTER FROM GAMES "
                + "WHERE ID="+ "'" + id + "'"
                + ";");

        if (rs.next()) {
            System.out.println();
            return rs.getInt("FREE_FIELD_COUNTER");
        }

        throw new SQLException("Not found FREE_FIELD_COUNTER param in GAMES ID: " + id);
    }

    /**
     * Get number of board columns
     * @param id game id
     * @throws SQLException err syntax or connection
     * @return numbers of columns from database
     */
    public static int getNumOfCols(String id) throws SQLException {
        ResultSet rs = JDBC.executeQuery("SELECT GAMES_BOARD.NUM_OF_COLS  "
                + "FROM (GAMES JOIN GAMES_BOARD ON GAMES.GAME_BOARD_ID = GAMES_BOARD.ID) "
                + "WHERE GAMES.ID="+ "'" + id + "'"
                + ";");

        if (rs.next()) {
            return rs.getInt("GAMES_BOARD.NUM_OF_COLS");
        }

        throw new SQLException("Not found NUM_OF_COLS param in GAMES_BOARDS GAME_ID: " + id);
    }

    /**
     * Get number of board rows
     * @param id game id
     * @throws SQLException err syntax or connection
     * @return number of board rows from database
     */
    public static int getNumOfRows(String id) throws SQLException {
        ResultSet rs = JDBC.executeQuery("SELECT GAMES_BOARD.NUM_OF_ROWS  "
                + "FROM (GAMES JOIN GAMES_BOARD ON GAMES.GAME_BOARD_ID = GAMES_BOARD.ID) "
                + "WHERE GAMES.ID="+ "'" + id + "'"
                + ";");

        if (rs.next()) {
            return rs.getInt("GAMES_BOARD.NUM_OF_ROWS");
        }

        throw new SQLException("Not found NUM_OF_ROWS param in GAMES_BOARDS GAME_ID: " + id);
    }

    /**
     * Get game state result
     * @param id game id
     * @throws SQLException err syntax or connection
     * @return game state result form database
     */
    public static IEnumGame.GameResult getGameResult(String id) throws SQLException  {
        ResultSet rs = JDBC.executeQuery("SELECT RESULT FROM GAMES "
                + "WHERE ID="+ "'" + id + "'"
                + ";");

        if (rs.next()) {
            if(rs.getString("RESULT").equals("NONE"))
                return IEnumGame.GameResult.NONE;

            if(rs.getString("RESULT").equals("LOSE"))
                return IEnumGame.GameResult.LOSE;

            if(rs.getString("RESULT").equals("WIN"))
                return IEnumGame.GameResult.WIN;

            if(rs.getString("RESULT").equals("CANCELED"))
                return IEnumGame.GameResult.CANCELED;
        }

        throw new SQLException("Not found RESULT param in GAMES ID: " + id);
    }

    /**
     * Get all fields index that contains mines
     * @param id game key id
     * @return array list
     * @throws SQLException err syntax or connection
     */
    public static ArrayList<Index> getMinesIndex(String id) throws SQLException {
        ArrayList<Index> minesIndex = new ArrayList<>();
        ResultSet rs = JDBC.executeQuery("SELECT FIELDS.ROW_INX, FIELDS.COL_INX  "
                + "FROM ((GAMES JOIN GAMES_BOARD ON GAMES.GAME_BOARD_ID = GAMES_BOARD.ID) "
                + "JOIN FIELDS ON GAMES_BOARD.ID = FIELDS.GAME_BOARD_ID) "
                + "WHERE GAMES.ID="+ "'" + id + "'" + " AND "
                + "MINE=true "
                + "GROUP BY FIELDS.ID;");

        while (rs.next()) {
            Index inx = new Index(rs.getInt("ROW_INX"), rs.getInt("COL_INX"));
            minesIndex.add(inx);
        }

        return minesIndex;
    }
}
