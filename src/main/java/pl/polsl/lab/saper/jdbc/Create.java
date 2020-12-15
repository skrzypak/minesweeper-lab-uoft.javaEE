package pl.polsl.lab.saper.jdbc;

import pl.polsl.lab.saper.DatabaseConfig;

import java.sql.SQLException;
import java.sql.Statement;

public class Create {
    /**
     * Create tables in database
     * @throws SQLException err syntax or connection
     */
    public static void createTables() throws SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();

        statement.executeUpdate("CREATE TABLE GAMES"
                + "(ID INTEGER PRIMARY KEY, RESULT VARCHAR(10), "
                + "FREE_FIELD_COUNTER INTEGER);");
        statement.executeUpdate("CREATE TABLE GAMES_BOARD"
                + "(GAME_ID INTEGER PRIMARY KEY, NUM_OF_ROWS INTEGER, "
                + "NUM_OF_COLS INTEGER);");
        statement.executeUpdate("CREATE TABLE FIELDS"
                + "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, GAME_ID INTEGER, ROW_INX INTEGER, COL_INX INTEGER, MINE BOOLEAN, MARKED BOOLEAN, "
                + "SELECTED BOOLEAN, AROUND_MINES INTEGER);");
    }
}
