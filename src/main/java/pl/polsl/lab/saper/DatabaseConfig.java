package pl.polsl.lab.saper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class define global game model
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public class DatabaseConfig {

    static private Connection conn = null; // Database connections handler

    /**
     * Class constructor
     */
    private DatabaseConfig() { }

    /**
     * Create new gameModel instance if not exists
     * @throws SQLException when detect connection err to databse
     * */
    static public void set() throws SQLException {
        boolean next = conn != null;
        DriverManager.registerDriver(new org.h2.Driver());
        String url = "jdbc:h2:mem:db";
        conn = DriverManager.getConnection(url);
        if (next) {
            truncateTables();
        } else {
            createTables();
        }
    }

    /**
     * Create tables in database
     * @throws SQLException err syntax or connection
     */
    private static void createTables() throws SQLException {
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

    /**
     * Drops tables in database
     * @throws SQLException err syntax or connection
     */
    static private void dropsTables() throws SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("DROP TABLE FIELDS;");
        statement.executeUpdate("DROP TABLE GAMES_BOARD;");
        statement.executeUpdate("DROP TABLE GAMES;");
    }

    /**
     * Truncate tables in database
     * @throws SQLException err syntax or connection
     */
    static private void truncateTables() throws SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("TRUNCATE TABLE FIELDS;");
        statement.executeUpdate("TRUNCATE TABLE GAMES_BOARD;");
        statement.executeUpdate("TRUNCATE TABLE GAMES;");
    }

    /**
     * Get connection to database
     * @return connection to database
     * */
    static public Connection getConn() {
        return conn;
    }

    /**
     * Remove game model object (equals stop game) and close connection to database
     * */
    static public void close() throws SQLException {
        if (conn != null) {
            dropsTables();
            conn.close();
        }
    }
}
