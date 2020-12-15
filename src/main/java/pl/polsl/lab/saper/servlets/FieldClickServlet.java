package pl.polsl.lab.saper.servlets;

import com.google.gson.Gson;
import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.jdbc.ReadFnDb;
import pl.polsl.lab.saper.jdbc.UpdateFnDb;
import pl.polsl.lab.saper.model.IEnumGame;
import pl.polsl.lab.saper.model.Index;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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

                        if(ReadFnDb.getNumOfMinesAroundField(id, inx) == 0) {
                            Map<String,Integer> tmp = new HashMap<>();
                            findUntilNoZeroField(tmp, inx, id);
                            jsonMap.put("zero", this.gson.toJson(tmp));
                        }

                        UpdateFnDb.setFieldAsSelected(id, inx);
                        updateGameStatus(id, inx);

                        jsonMap.put("numOfMines", ReadFnDb.getNumOfMinesAroundField(id, inx).toString());

                        jsonMap.put("mine", String.valueOf(ReadFnDb.getInfoAboutMine(id, inx)));

                    } catch (SQLException e) {
                        jsonMap.put("error", e.getMessage());
                        e.printStackTrace();
                    }

                    try {
                        if(ReadFnDb.getGameResult(id) == IEnumGame.GameResult.LOSE) {
                            ArrayList<Index> arr = ReadFnDb.getMinesIndex(id);
                            jsonMap.put("mines", this.gson.toJson(arr));
                        }
                    } catch (SQLException throwables) {
                        jsonMap.put("error", throwables.getMessage());
                        throwables.printStackTrace();
                    }

                } else if (type.equals("right")) {
                    // Set as mark
                    try {
                        if(!ReadFnDb.fieldSelected(id, inx)) {
                            if(ReadFnDb.getInfoAboutMark(id, inx)) {
                                UpdateFnDb.removeFieldMark(id, inx);
                            } else {
                                UpdateFnDb.setFieldAsMark(id, inx);
                            }
                            jsonMap.put("mark", String.valueOf(ReadFnDb.getInfoAboutMark(id, inx)));
                        } else {
                            jsonMap.put("error", "Field is selected");
                        }
                    } catch (SQLException e) {
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
            jsonMap.put("gameStatus", ReadFnDb.getGameResult(id).toString());
        } catch (SQLException throwables) {
            jsonMap.put("error", throwables.getMessage());
            throwables.printStackTrace();
        }
        PrintWriter out = resp.getWriter();
        out.print(this.gson.toJson(jsonMap));
        out.flush();
    }

    /**
     * Check if game is running
     * @param id game id
     * @throws SQLException err syntax or connection
     * @return TRUE if game is running or FALSE if end
     */
    private boolean isGameRunning(Integer id) throws SQLException {
        IEnumGame.GameResult r = ReadFnDb.getGameResult(id);
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
        if (ReadFnDb.getInfoAboutMine(id, inx)) {
            UpdateFnDb.setLose(id);
        }
        if (ReadFnDb.getFreeFieldCounter(id) <= 0) {
            UpdateFnDb.setWin(id);
        }
    }

    /**
     * Discover recursive all zero mines fields
     * @param tmp ref to map of index
     * @param inx field index object
     * @param id game id
     */
    private void findUntilNoZeroField(Map<String,Integer> tmp, Index inx, Integer id) {

        try {
            isCorrectField(id, inx);
            if (ReadFnDb.getInfoAboutMine(id, inx) || ReadFnDb.fieldSelected(id, inx)) {
                //Field has mine or was selected earlier so end recursive
                return;
            }
        } catch (FieldException | SQLException e) {
            // Field is boarder so end recursive
            return;
        }

        int mines = 0;

        try {
            isCorrectField(id, inx);
            mines = ReadFnDb.getNumOfMinesAroundField(id, inx);
        } catch (FieldException | SQLException ignore) { }

        if (mines > 0) {
            // Field is no mine but around have samo mine, so set field as selected and end recursive
            try {
                isCorrectField(id, inx);
                UpdateFnDb.setFieldAsSelected(id, inx);
            } catch (FieldException | SQLException ignore) { }
            tmp.put(this.gson.toJson(inx), mines);
            return;
        }

        // Field has no mine so set as 0, next call recursive to next fields
        try {
            isCorrectField(id, inx);
            UpdateFnDb.setFieldAsSelected(id, inx);
        } catch (FieldException | SQLException ignore) { }
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

    /**
     * Check whether field is board or out of range
     * @param id game id
     * @param inx field index object
     * @throws SQLException err syntax or connection
     * @throws FieldException if field index is out of range or game board border
     */
    public void isCorrectField(Integer id, Index inx) throws FieldException, SQLException {
        if (inx.getRowIndex() <= 0 || inx.getRowIndex() > ReadFnDb.getNumOfRows(id) - 2)
            throw new FieldException("Invalid row index");
        if (inx.getColIndex() <= 0 || inx.getColIndex() > ReadFnDb.getNumOfCols(id) - 2)
            throw new FieldException("Invalid column index");
    }

}