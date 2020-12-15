package pl.polsl.lab.saper.servlets;

import com.google.gson.Gson;
import pl.polsl.lab.saper.DatabaseConfig;
import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.model.IEnumGame;
import pl.polsl.lab.saper.model.Index;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class define servlet that update model after click fieald
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
@WebServlet(name = "FieldClickServlet", urlPatterns = "/fieldClick")
public class FieldClickServlet extends HttpServlet {

    private final Gson gson = new Gson();  // Gson object

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param req servlet request. Must contain 'row' and 'col'
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,String> jsonMap = new HashMap<>();

        Integer id = 0;

        try {
            if(!isGameRunning(id)) {
                jsonMap.put("error", "Game is not running");
            } else {
                Integer row = Integer.parseInt(req.getParameter("row"));
                Integer col = Integer.parseInt(req.getParameter("col"));
                String type = req.getParameter("btn");
                Index inx = new Index(row, col);

                if (type.equals("left")) {
                    // Set as selected
                    try {

                        if(getNumOfMinesAroundField(id, inx) == 0) {
                            Map<String,Integer> tmp = new HashMap<>();
                            //findUntilNoZeroField(tmp, inx, id);
                            jsonMap.put("zero", this.gson.toJson(tmp));
                        }

                        setFieldAsSelected(id, inx);
                        updateGameStatus(id, inx);

                        jsonMap.put("numOfMines", getNumOfMinesAroundField(id, inx).toString());

                        jsonMap.put("mine", String.valueOf(getInfoAboutMine(id, inx)));

                    } catch (SQLException | FieldException e) {
                        jsonMap.put("error", e.getMessage());
                        e.printStackTrace();
                    }

                    try {
                        if(getGameResult(id) == IEnumGame.GameResult.LOSE) {
                            ArrayList<Index> arr = getMinesIndex(0);
                            jsonMap.put("mines", this.gson.toJson(arr));
                        }
                    } catch (SQLException throwables) {
                        jsonMap.put("error", throwables.getMessage());
                        throwables.printStackTrace();
                    }

                } else if (type.equals("right")) {
                    // Set as mark
                    try {
                        if(!fieldSelected(id, inx)) {
                            if(getInfoAboutMark(id, inx)) {
                                removeFieldMark(id, inx);
                            } else {
                                setFieldAsMark(id, inx);
                            }
                            jsonMap.put("mark", String.valueOf(getInfoAboutMark(id, inx)));
                        } else {
                            jsonMap.put("error", "Field is selected");
                        }
                    } catch (FieldException | SQLException e) {
                        jsonMap.put("error", e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    jsonMap.put("error", "Invalid button type");
                }
            }
        } catch (SQLException throwables) {
            jsonMap.put("error", "Invalid button type");
            throwables.printStackTrace();
        }

        resp.setContentType("application/json;charset=UTF-8");
        try {
            jsonMap.put("gameStatus", getGameResult(id).toString());
        } catch (SQLException throwables) {
            jsonMap.put("error", throwables.getMessage());
            throwables.printStackTrace();
        }
        PrintWriter out = resp.getWriter();
        out.print(this.gson.toJson(jsonMap));
        out.flush();
    }

    private void setFieldAsMark(Integer id, Index inx) throws FieldException, SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("UPDATE FIELDS SET "
                + "MARKED=true "
                + "WHERE GAME_ID="+ id + " AND "
                + "ROW_INX="+inx.getRowIndex()+" AND "
                + "COL_INX="+inx.getColIndex()
                + ";");
    }

    private void setFieldAsSelected(Integer id, Index inx) throws FieldException, SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("UPDATE FIELDS SET "
                + "SELECTED=true "
                + "WHERE GAME_ID="+ id + " AND "
                + "ROW_INX="+inx.getRowIndex()+" AND "
                + "COL_INX="+inx.getColIndex()
                + ";");
    }

    private void removeFieldMark(Integer id, Index inx) throws FieldException, SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("UPDATE FIELDS SET "
                + "MARKED=false "
                + "WHERE GAME_ID="+ id + " AND "
                + "ROW_INX="+inx.getRowIndex()+" AND "
                + "COL_INX="+inx.getColIndex()
                + ";");
    }

    private boolean getInfoAboutMark(Integer id, Index inx) throws FieldException, SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        ResultSet rs = statement.executeQuery("SELECT MARKED FROM FIELDS "
                + "WHERE GAME_ID="+ id + " AND "
                + "ROW_INX="+inx.getRowIndex()+" AND "
                + "COL_INX="+inx.getColIndex()
                + ";");

        if (rs.next()) {
            return rs.getBoolean("MARKED");
        }

        throw new SQLException("Not found MARKED param in FIELDS database INDEX: " + inx.toString());
    }

    private boolean fieldSelected(Integer id, Index inx) throws FieldException, SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        ResultSet rs = statement.executeQuery("SELECT SELECTED FROM FIELDS "
                + "WHERE GAME_ID="+ id + " AND "
                + "ROW_INX="+inx.getRowIndex()+" AND "
                + "COL_INX="+inx.getColIndex()
                + ";");

        if (rs.next()) {
            return rs.getBoolean("SELECTED");
        }

        throw new SQLException("Not found SELECTED param in FIELDS database INDEX: " + inx.toString());
    }

    private boolean getInfoAboutMine(Integer id, Index inx) throws FieldException, SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        ResultSet rs = statement.executeQuery("SELECT MINE FROM FIELDS "
                + "WHERE GAME_ID="+ id + " AND "
                + "ROW_INX="+inx.getRowIndex()+" AND "
                + "COL_INX="+inx.getColIndex()
                + ";");

        if (rs.next()) {
            return rs.getBoolean("MINE");
        }

        throw new SQLException("Not found MINE param in FIELDS database INDEX: " + inx.toString());
    }

    private Integer getNumOfMinesAroundField(Integer id, Index inx) throws FieldException, SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        ResultSet rs = statement.executeQuery("SELECT AROUND_MINES FROM FIELDS "
                + "WHERE GAME_ID="+ id + " AND "
                + "ROW_INX="+inx.getRowIndex()+" AND "
                + "COL_INX="+inx.getColIndex()
                + ";");

        if (rs.next()) {
            return rs.getInt("AROUND_MINES");
        }

        throw new SQLException("Not found AROUND_MINES param in FIELDS database INDEX: " + inx.toString());
    }

    private void decrementFreeFieldCounter(Integer id) throws SQLException {
        Integer counter = getFreeFieldCounter(id);
        counter--;
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("UPDATE GAMES SET "
                + "FREE_FIELD_COUNTER=" + counter + " "
                + "WHERE ID="+ id
                + ";");
    }

    private Integer getFreeFieldCounter(Integer id) throws SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        ResultSet rs = statement.executeQuery("SELECT FREE_FIELD_COUNTER FROM GAMES "
                + "WHERE ID="+ id
                + ";");

        if (rs.next()) {
            System.out.println();
            return rs.getInt("FREE_FIELD_COUNTER");
        }

        throw new SQLException("Not found FREE_FIELD_COUNTER param in GAMES ID: " + id);
    }

    private int getNumOfCols(Integer id) throws SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        ResultSet rs = statement.executeQuery("SELECT NUM_OF_COLS FROM GAMES_BOARD "
                + "WHERE GAME_ID="+ id
                + ";");

        if (rs.next()) {
            return rs.getInt("NUM_OF_COLS");
        }

        throw new SQLException("Not found NUM_OF_COLS param in GAMES_BOARDS GAME_ID: " + id);
    }

    private int getNumOfRows(Integer id) throws SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        ResultSet rs = statement.executeQuery("SELECT NUM_OF_ROWS FROM GAMES_BOARD "
                + "WHERE GAME_ID="+ id
                + ";");

        if (rs.next()) {
            return rs.getInt("NUM_OF_ROWS");
        }

        throw new SQLException("Not found NUM_OF_ROWS param in GAMES_BOARDS GAME_ID: " + id);
    }

    private void setWin(Integer id) throws SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("UPDATE GAMES SET "
                + "RESULT=" + "'"  + IEnumGame.GameResult.WIN.toString() + "'"  + " "
                + "WHERE ID="+ id
                + ";");
    }

    private void setLose(Integer id) throws SQLException {
        Statement statement = DatabaseConfig.getConn().createStatement();
        statement.executeUpdate("UPDATE GAMES SET "
                + "RESULT=" + "'"  + IEnumGame.GameResult.LOSE.toString() + "'"  + " "
                + "WHERE ID="+ id
                + ";");
    }

    private IEnumGame.GameResult getGameResult(Integer id) throws SQLException  {
        Statement statement = DatabaseConfig.getConn().createStatement();
        ResultSet rs = statement.executeQuery("SELECT RESULT FROM GAMES "
                + "WHERE ID="+ id
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
     * Check if game is running
     *
     * @return TRUE if game is running or FALSE if end
     */
    private boolean isGameRunning(Integer id) throws SQLException {
        IEnumGame.GameResult r = getGameResult(id);
        return r == IEnumGame.GameResult.NONE;
    }

    /**
     * Functions update game status
     * If user selected filed with mine, function set game status as lose in model
     * If user selected last empty field, function set game status as win in model
     * @throws SQLException err syntax or connection
     * @param id game id
     * @param inx field index object
     */
    private void updateGameStatus(Integer id, Index inx) throws SQLException {
        try {
            if (getInfoAboutMine(id, inx)) {
                setLose(id);
            }

            decrementFreeFieldCounter(id);

            if (getFreeFieldCounter(id) <= 0) {
                setWin(id);
            }
        } catch (FieldException ignored) { }
    }

    /**
     * Function calling to renderGameResult in GameView class
     * @param id game key id
     * @return array of index which contain mines
     */
    private ArrayList<Index> getMinesIndex(Integer id) throws SQLException {
        ArrayList<Index> minesIndex = new ArrayList<>();
        for (int i = 1; i < getNumOfRows(id) - 1; i++) {
            for (int j = 1; j < getNumOfCols(id) - 1; j++) {
                try {
                    Index inx = new Index(i, j);
                    if (getInfoAboutMine(id, inx))
                        minesIndex.add(inx);
                } catch (FieldException ignore) { }
            }
        }
        return minesIndex;
    }

    /**
     * Discover recursive all zero mines fields
     * @param tmp ref to map of index
     * @param inx field index object
     * @param id game id
     */
    private void findUntilNoZeroField(Map<String,Integer> tmp, Index inx, Integer id) throws SQLException {

        try {
            if (getInfoAboutMine(id, inx) || fieldSelected(id, inx)) {
                //Field has mine or was selected earlier so end recursive
                return;
            }
        } catch (FieldException | SQLException e) {
            // Field is boarder so end recursive
            return;
        }

        int mines = 0;

        try {
            mines = getNumOfMinesAroundField(id, inx);
        } catch (FieldException ignore) { }

        if (mines > 0) {
            // Field is no mine but around have samo mine, so set field as selected and end recursive
            try {
                setFieldAsSelected(id, inx);
            } catch (FieldException ignore) { }
            tmp.put(this.gson.toJson(inx), mines);
            return;
        }

        // Field has no mine so set as 0, next call recursive to next fields
        try {
            setFieldAsSelected(id, inx);
        } catch (FieldException ignore) { }
        tmp.put(this.gson.toJson(inx), 0);

        int row = inx.getRowIndex();
        int col = inx.getColIndex();

        findUntilNoZeroField(tmp, new Index(row + 1, col - 1), id);
        findUntilNoZeroField(tmp, new Index(row + 1, col + 1), id);
        findUntilNoZeroField(tmp, new Index(row - 1, col - 1), id);
        findUntilNoZeroField(tmp, new Index(row - 1, col + 1), id);
        findUntilNoZeroField(tmp, new Index(row + 1, col), id);
        findUntilNoZeroField(tmp, new Index(row, col + 1), id);
        findUntilNoZeroField(tmp, new Index(row, col - 1), id);
        findUntilNoZeroField(tmp, new Index(row - 1, col), id);

    }

}