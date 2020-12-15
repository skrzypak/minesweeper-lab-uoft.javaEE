package pl.polsl.lab.saper.jdbc;

import pl.polsl.lab.saper.DatabaseConfig;
import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.model.IEnumGame;
import pl.polsl.lab.saper.model.Index;

import java.sql.SQLException;
import java.sql.Statement;

public class Update {
    public static void setFieldAsMark(Integer id, Index inx) throws FieldException, SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("UPDATE FIELDS SET "
                + "MARKED=true "
                + "WHERE GAME_ID="+ id + " AND "
                + "ROW_INX="+inx.getRowIndex()+" AND "
                + "COL_INX="+inx.getColIndex()
                + ";");
    }

    public static void setFieldAsSelected(Integer id, Index inx) throws FieldException, SQLException {
        if(!Read.fieldSelected(id, inx)) {
            Statement statement = DatabaseConfig.getConn().createStatement();
            statement.executeUpdate("UPDATE FIELDS SET "
                    + "SELECTED=true "
                    + "WHERE GAME_ID="+ id + " AND "
                    + "ROW_INX="+inx.getRowIndex()+" AND "
                    + "COL_INX="+inx.getColIndex()
                    + ";");
            Update.decrementFreeFieldCounter(id);
        }
    }

    public static void decrementFreeFieldCounter(Integer id) throws SQLException {
        Integer counter = Read.getFreeFieldCounter(id);
        counter--;
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("UPDATE GAMES SET "
                + "FREE_FIELD_COUNTER=" + counter + " "
                + "WHERE ID="+ id
                + ";");
    }

    public static void setWin(Integer id) throws SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("UPDATE GAMES SET "
                + "RESULT=" + "'"  + IEnumGame.GameResult.WIN.toString() + "'"  + " "
                + "WHERE ID="+ id
                + ";");
    }

    public static void setLose(Integer id) throws SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("UPDATE GAMES SET "
                + "RESULT=" + "'"  + IEnumGame.GameResult.LOSE.toString() + "'"  + " "
                + "WHERE ID="+ id
                + ";");
    }
}
