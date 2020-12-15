package pl.polsl.lab.saper.jdbc;

import pl.polsl.lab.saper.DatabaseConfig;
import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.model.Index;

import java.sql.SQLException;
import java.sql.Statement;

public class Delete {
    public static void removeFieldMark(Integer id, Index inx) throws FieldException, SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("UPDATE FIELDS SET "
                + "MARKED=false "
                + "WHERE GAME_ID="+ id + " AND "
                + "ROW_INX="+inx.getRowIndex()+" AND "
                + "COL_INX="+inx.getColIndex()
                + ";");
    }

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
