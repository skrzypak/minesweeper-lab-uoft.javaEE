package pl.polsl.lab.saper.servlets;

import com.google.gson.Gson;
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

                    if(TODO.get().getNumOfMinesAroundField(inx) == 0) {
                        Map<String,Integer> tmp = new HashMap<>();
                        findUntilNoZeroField(tmp, inx);
                        jsonMap.put("zero", this.gson.toJson(tmp));
                    }

                    TODO.get().setFieldAsSelected(inx);
                    updateGameStatus(inx);

                    jsonMap.put("numOfMines", TODO.get().getNumOfMinesAroundField(inx).toString());

                    jsonMap.put("mine", String.valueOf(TODO.get().getInfoAboutMine(inx)));

                } catch (FieldException e) {
                    jsonMap.put("error", e.getMessage());
                }

                if(TODO.get().getGameResult() == IEnumGame.GameResult.LOSE) {
                    ArrayList<Index> arr = getMinesIndex();
                    jsonMap.put("mines", this.gson.toJson(arr));
                }

            } else if (type.equals("right")) {
                // Set as mark
                try {
                    if(!TODO.get().fieldSelected(inx)) {
                        if(TODO.get().getInfoAboutMark(inx)) {
                            TODO.get().removeFieldMark(inx);
                        } else {
                            TODO.get().setFieldAsMark(inx);
                        }
                        jsonMap.put("mark", String.valueOf(TODO.get().getInfoAboutMark(inx)));
                    } else {
                        jsonMap.put("error", "Field is selected");
                    }
                } catch (FieldException e) {
                    jsonMap.put("error", e.getMessage());
                }

            } else {
                jsonMap.put("error", "Invalid button type");
            }
        }

        resp.setContentType("application/json;charset=UTF-8");
        jsonMap.put("gameStatus", TODO.get().getGameResult().toString());
        PrintWriter out = resp.getWriter();
        out.print(this.gson.toJson(jsonMap));
        out.flush();
    }

    /**
     * Functions update game status
     * If user selected filed with mine, function set game status as lose in model
     * If user selected last empty field, function set game status as win in model
     *
     * @param inx field index object
     */
    private void updateGameStatus(Index inx) {
        try {
            if (TODO.get().getInfoAboutMine(inx)) TODO.get().setLose();
        } catch (FieldException ignored) { }

        if (TODO.get().getFreeFieldCounter() <= 0) TODO.get().setWin();
    }

    /**
     * Check if game is running
     *
     * @return TRUE if game is running or FALSE if end
     */
    private boolean isGameRunning() {
        return TODO.get().getRunning();
    }

    /**
     * Function calling to renderGameResult in GameView class
     * @return array of index which contain mines
     */
    private ArrayList<Index> getMinesIndex() {
        ArrayList<Index> minesIndex = new ArrayList<>();
        for (int i = 1; i < TODO.get().getNumOfRows() - 1; i++) {
            for (int j = 1; j < TODO.get().getNumOfCols() - 1; j++) {
                try {
                    Index inx = new Index(i, j);
                    if (TODO.get().getInfoAboutMine(inx))
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
            if (TODO.get().getInfoAboutMine(inx) || TODO.get().fieldSelected(inx)) {
                //Field has mine or was selected earlier so end recursive
                return;
            }
        } catch (FieldException e) {
            // Field is boarder so end recursive
            return;
        }

        Integer mines = 0;

        try {
            mines = TODO.get().getNumOfMinesAroundField(inx);
        } catch (FieldException ignored) { }

        if (mines > 0) {
            // Field is no mine but around have samo mine, so set field as selected and end recursive
            try {
                TODO.get().setFieldAsSelected(inx);
                tmp.put(this.gson.toJson(inx), mines);
            } catch (FieldException ignored) {
            }
            return;
        }

        try {
            // Field has no mine so set as 0, next call recursive to next fields
            TODO.get().setFieldAsSelected(inx);
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