package pl.polsl.lab.saper.servlets;

import com.google.gson.Gson;
import pl.polsl.lab.saper.Content;
import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.model.Field;
import pl.polsl.lab.saper.model.Game;
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

        if(!isGameRunning()) {
            jsonMap.put("error", "Game is not running");
        } else {

            Integer row = Integer.parseInt(req.getParameter("row"));
            Integer col = Integer.parseInt(req.getParameter("col"));
            String type = req.getParameter("btn");
            Index inx = new Index(row, col);

            if (type.equals("left")) {
                // Set as selected
                try {

                    if(Content.get().getNumOfMinesAroundField(inx) == 0) {
                        Map<String,Integer> tmp = new HashMap<>();
                        findUntilNoZeroField(tmp, inx);
                        jsonMap.put("zero", this.gson.toJson(tmp));
                    }

                    Content.get().setFieldAsSelected(inx);
                    updateFieldDb(Content.get().getBoardData().get(inx));
                    updateGameStatus(inx);

                    jsonMap.put("numOfMines", Content.get().getNumOfMinesAroundField(inx).toString());

                    jsonMap.put("mine", String.valueOf(Content.get().getInfoAboutMine(inx)));

                    updateGameDb(Content.get());

                } catch (FieldException | SQLException e) {
                    jsonMap.put("error", e.getMessage());
                }

                if(Content.get().getGameResult() == IEnumGame.GameResult.LOSE) {
                    ArrayList<Index> arr = getMinesIndex();
                    jsonMap.put("mines", this.gson.toJson(arr));
                }

            } else if (type.equals("right")) {
                // Set as mark
                try {
                    if(!Content.get().fieldSelected(inx)) {
                        if(Content.get().getInfoAboutMark(inx)) {
                            Content.get().removeFieldMark(inx);
                        } else {
                            Content.get().setFieldAsMark(inx);
                        }
                        updateFieldDb(Content.get().getBoardData().get(inx));
                        jsonMap.put("mark", String.valueOf(Content.get().getInfoAboutMark(inx)));

                        updateGameDb(Content.get());

                    } else {
                        jsonMap.put("error", "Field is selected");
                    }
                } catch (FieldException | SQLException e) {
                    jsonMap.put("error", e.getMessage());
                }

            } else {
                jsonMap.put("error", "Invalid button type");
            }
        }

        resp.setContentType("application/json;charset=UTF-8");
        jsonMap.put("gameStatus", Content.get().getGameResult().toString());
        PrintWriter out = resp.getWriter();
        out.print(this.gson.toJson(jsonMap));
        out.flush();
    }

    /**
     * Method update field data in database
     * @param f field object
     * @throws SQLException err syntax or connection
     * */
    private void updateFieldDb(Field f) throws SQLException {
        Statement statement = Content.getConn().createStatement();
        statement.executeUpdate("UPDATE FIELDS SET "
                + "MINE=" + f.isMine() + ","
                + "MARKED=" +f.isMarked() + ","
                + "SELECTED=" +f.isSelected()+ " "
                + "WHERE GAME_ID="+ 0 + " AND "
                + "ROW_INX="+f.getRowIndex()+" AND "
                + "COL_INX="+f.getColIndex()
                + ";");
    }

    /**
     * Functions update game status
     * If user selected filed with mine, function set game status as lose in model
     * If user selected last empty field, function set game status as win in model
     * @throws SQLException err syntax or connection
     * @param inx field index object
     */
    private void updateGameStatus(Index inx) throws SQLException {
        try {
            if (Content.get().getInfoAboutMine(inx)) {
                Content.get().setLose();
            }
            if (Content.get().getFreeFieldCounter() <= 0) {
                Content.get().setWin();
            }
        } catch (FieldException ignored) { }
    }

    /**
     * Method update game status in database
     * @param gameModel model
     * @throws SQLException err syntax or connection
     * */
    private void updateGameDb(Game gameModel) throws SQLException {
        Statement statement = Content.getConn().createStatement();
        statement.executeUpdate("UPDATE GAMES SET "
                + "RESULT=" + "'" + gameModel.getGameResult() + "'" + ","
                + "FREE_FIELD_COUNTER=" + gameModel.getFreeFieldCounter() + " "
                + "WHERE ID="+ 0 + ";");
    }


    /**
     * Check if game is running
     *
     * @return TRUE if game is running or FALSE if end
     */
    private boolean isGameRunning() {
        return Content.get().getRunning();
    }

    /**
     * Function calling to renderGameResult in GameView class
     * @return array of index which contain mines
     */
    private ArrayList<Index> getMinesIndex() {
        ArrayList<Index> minesIndex = new ArrayList<>();
        for (int i = 1; i < Content.get().getNumOfRows() - 1; i++) {
            for (int j = 1; j < Content.get().getNumOfCols() - 1; j++) {
                try {
                    Index inx = new Index(i, j);
                    if (Content.get().getInfoAboutMine(inx))
                        minesIndex.add(inx);
                } catch (FieldException ignored) { }
            }
        }
        return minesIndex;
    }

    /**
     * Discover recursive all zero mines fields
     * @param tmp ref to map of index
     * @param inx field index object
     */
    private void findUntilNoZeroField(Map<String,Integer> tmp, Index inx) {

        try {
            if (Content.get().getInfoAboutMine(inx) || Content.get().fieldSelected(inx)) {
                //Field has mine or was selected earlier so end recursive
                return;
            }
        } catch (FieldException e) {
            // Field is boarder so end recursive
            return;
        }

        Integer mines = 0;

        try {
            mines = Content.get().getNumOfMinesAroundField(inx);
        } catch (FieldException ignored) { }

        if (mines > 0) {
            // Field is no mine but around have samo mine, so set field as selected and end recursive
            try {
                Content.get().setFieldAsSelected(inx);
                tmp.put(this.gson.toJson(inx), mines);
            } catch (FieldException ignored) {
            }
            return;
        }

        try {
            // Field has no mine so set as 0, next call recursive to next fields
            Content.get().setFieldAsSelected(inx);
            tmp.put(this.gson.toJson(inx), 0);
        } catch (FieldException ignored) { }

        int row = inx.getRowIndex();
        int col = inx.getColIndex();

        findUntilNoZeroField(tmp, new Index(row + 1, col - 1));
        findUntilNoZeroField(tmp, new Index(row + 1, col + 1));
        findUntilNoZeroField(tmp, new Index(row - 1, col - 1));
        findUntilNoZeroField(tmp, new Index(row - 1, col + 1));
        findUntilNoZeroField(tmp, new Index(row + 1, col));
        findUntilNoZeroField(tmp, new Index(row, col + 1));
        findUntilNoZeroField(tmp, new Index(row, col - 1));
        findUntilNoZeroField(tmp, new Index(row - 1, col));

    }

}