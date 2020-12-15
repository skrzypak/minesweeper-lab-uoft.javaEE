package pl.polsl.lab.saper;

import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.model.Game;

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
public class Content {

    static private Game gameModel = null;   // Game model object
    static private Connection conn = null;

    /**
     * Class constructor
     */
    private Content() { }

    /**
     * Create new gameModel instance if not exists
     * @param height board height
     * @param width board width
     * @throws FieldException if dimensions are wrong
     * @throws SQLException when detect connection err to databse
     * */
    static public void set(Integer height, Integer width) throws FieldException, SQLException {
        if (Content.gameModel == null) {
            gameModel = new Game(height, width);
            DriverManager.registerDriver(new org.h2.Driver());
            String url = "jdbc:h2:mem:db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to H2 has been established.");
            createTables();
        }
    }

    /**
     * Create tables in database
     * @throws SQLException err syntax or connection
     */
    private static void createTables() throws SQLException {
        Statement statement = Content.getConn().createStatement();

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
        Statement statement = Content.getConn().createStatement();
        statement.executeUpdate("DROP TABLE FIELDS;");
        statement.executeUpdate("DROP TABLE GAMES_BOARD;");
        statement.executeUpdate("DROP TABLE GAMES;");
    }

    /**
     * Get game model object
     * @return game model object
     * */
    static public Game get() {
        return gameModel;
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
    static public void clear() throws SQLException {
        gameModel = null;
        if (conn != null) {
            dropsTables();
            conn.close();
        }
    }
}
