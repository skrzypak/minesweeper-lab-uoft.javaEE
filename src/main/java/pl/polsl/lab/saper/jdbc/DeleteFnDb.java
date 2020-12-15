package pl.polsl.lab.saper.jdbc;

import pl.polsl.lab.saper.DatabaseConfig;
import java.sql.SQLException;
import java.sql.Statement;

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
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("DROP TABLE FIELDS;");
        statement.executeUpdate("DROP TABLE GAMES_BOARD;");
        statement.executeUpdate("DROP TABLE GAMES;");
    }

    /**
     * Truncate tables in database
     * @throws SQLException err syntax or connection
     */
    public static void truncateTables() throws SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("TRUNCATE TABLE FIELDS;");
        statement.executeUpdate("TRUNCATE TABLE GAMES_BOARD;");
        statement.executeUpdate("TRUNCATE TABLE GAMES;");
    }
}
