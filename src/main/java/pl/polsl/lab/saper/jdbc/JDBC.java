package pl.polsl.lab.saper.jdbc;

import pl.polsl.lab.saper.DatabaseConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class contains method that define connection operation to database
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public class JDBC {

    private static Statement statement; // Statement handler

    /**
     * @param sql sgl query
     * @throws SQLException err syntax or connection
     */
    public static void executeUpdate(String sql) throws SQLException {
        statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate(sql);
    }

    /**
     * @param sql sgl query
     * @return records from database
     * @throws SQLException err syntax or connection
     */
    public static ResultSet executeQuery(String sql) throws SQLException {
        statement = DatabaseConfig.getConn().createStatement();
        return statement.executeQuery(sql);
    }
}
