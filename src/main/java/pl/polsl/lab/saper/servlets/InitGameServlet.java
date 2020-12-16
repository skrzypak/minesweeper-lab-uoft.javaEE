package pl.polsl.lab.saper.servlets;
import pl.polsl.lab.saper.DatabaseConfig;
import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.jdbc.CreateFnDb;
import pl.polsl.lab.saper.model.Dimensions;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.Gson;
import pl.polsl.lab.saper.model.Game;
import pl.polsl.lab.saper.model.Index;

/**
 * Class define servlet that create new game
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public class InitGameServlet extends HttpServlet {

    private final Gson gson = new Gson();   // Gson object

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        Map<String,String> jsonMap = new HashMap<>();

        if (session == null) {
            try {
                DatabaseConfig.close();
            } catch (SQLException throwables) {
                jsonMap.put("error", throwables.getMessage());
                throwables.printStackTrace();
            }
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            jsonMap.put("error", "No found active session");
            out.print(this.gson.toJson(jsonMap));
            out.flush();
            return;
        }

        try {

            Integer height = Integer.parseInt(request.getParameter("height"));
            Integer width = Integer.parseInt(request.getParameter("width"));

            Game gameModel = new Game(session.getId(), height, width);
            DatabaseConfig.set();
            randomMines(gameModel, height, width);

            CreateFnDb.insertNewGameToDb(gameModel);

            Dimensions dm = new Dimensions(gameModel.getBoardData().getNumOfRows(), gameModel.getBoardData().getNumOfCols());
            jsonMap.put("size", this.gson.toJson(dm));

            CookiesServlet ob = new CookiesServlet();
            ob.doPost(request, response);

        } catch (NumberFormatException | OutOfMemoryError | FieldException | SQLException e) {
            jsonMap.put("error", e.getMessage());
        } finally {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(this.gson.toJson(jsonMap));
            out.flush();
        }
    }

    /**
     * Method random mines and putting to game board
     * @param gameModel game model object
     * @param height board height without border (user input value)
     * @param width  board width without border (user input value)
     * @throws IllegalArgumentException if size is not correct
     */
    private void randomMines(Game gameModel, Integer height, Integer width) throws IllegalArgumentException {

        if (height == null || height <= 0) throw new IllegalArgumentException("Height null or less than 1");
        if (width == null || width <= 0) throw new IllegalArgumentException("Width null or less than 1");

        int boardElements = height * width;
        int numOfMine;

        if (boardElements > 3) {
            if (boardElements < 6) {
                numOfMine = ThreadLocalRandom.current().nextInt(1, boardElements / 2);
            } else {
                numOfMine = ThreadLocalRandom.current().nextInt(boardElements / 6, boardElements / 3);
            }
        } else {
            numOfMine = ThreadLocalRandom.current().nextInt(0, 1);
        }

        gameModel.setFreeFieldCounter(gameModel.getFreeFieldCounter() - numOfMine);

        int randRow;
        int randCol;

        for (int r = 0; r < numOfMine; r++) {

            do {

                if (height > 1)
                    randRow = ThreadLocalRandom.current().nextInt(1, height);
                else randRow = 1;

                if (width > 1)
                    randCol = ThreadLocalRandom.current().nextInt(1, width);
                else randCol = 1;

            } while (gameModel.getBoardData().get(new Index(randRow, randCol)).isMine());

            gameModel.getBoardData().get(new Index(randRow, randCol)).setAsMine();
        }

        countMines(gameModel);
    }

    /**
     * Function count mines around field and set value to proper filed
     * @param gameModel game model object
     */
    private void countMines(Game gameModel){

        for(int row = 1; row < gameModel.getBoardData().getNumOfRows() - 1; row++) {
            for(int col = 1; col < gameModel.getBoardData().getNumOfCols() - 1; col++) {
                int num = 0;
                LinkedList<Index> index = new LinkedList<>();
                index.add(new Index(row - 1, col - 1));
                index.add(new Index(row - 1, col));
                index.add(new Index(row - 1, col + 1));
                index.add(new Index(row, col - 1));
                index.add(new Index(row, col + 1));
                index.add(new Index(row + 1, col - 1));
                index.add(new Index(row + 1, col));
                index.add(new Index(row + 1, col + 1));
                for (Index i : index) {
                    try {
                        if (gameModel.getInfoAboutMine(i)) num++;
                    } catch (FieldException ignored) { }
                }
                try {
                    gameModel.setFiledAroundMines(num, new Index(row, col));
                } catch (FieldException ignored) { }
            }
        }
    }
}
