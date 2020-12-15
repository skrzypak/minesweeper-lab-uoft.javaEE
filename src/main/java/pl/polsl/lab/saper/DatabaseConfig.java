package pl.polsl.lab.saper;
import pl.polsl.lab.saper.jdbc.Create;
import pl.polsl.lab.saper.jdbc.Delete;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            Delete.truncateTables();
        } else {
            Create.createTables();
        }
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
            Delete.dropsTables();
            conn.close();
        }
    }
}
