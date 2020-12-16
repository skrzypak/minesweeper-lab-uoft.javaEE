package pl.polsl.lab.saper.jdbc;
import java.sql.SQLException;

/**
 * Class contains method that define delete database operation
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public class DeleteFnDb {

    /**
     * Drops tables in database
     * @throws SQLException err syntax or connection
     */
    public static void dropsTables() throws SQLException {
        JDBC.executeUpdate("DROP TABLE FIELDS;");
        JDBC.executeUpdate("DROP TABLE GAMES_BOARD;");
        JDBC.executeUpdate("DROP TABLE GAMES;");
    }

    /**
     * Truncate tables in database
     * @throws SQLException err syntax or connection
     */
    public static void truncateTables() throws SQLException {
        JDBC.executeUpdate("TRUNCATE TABLE FIELDS;");
        JDBC.executeUpdate("TRUNCATE TABLE GAMES_BOARD;");
        JDBC.executeUpdate("TRUNCATE TABLE GAMES;");
    }
}
