package pl.polsl.lab.saper.servlets;
import pl.polsl.lab.saper.Content;
import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.model.Dimensions;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.Gson;
import pl.polsl.lab.saper.model.Field;
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

        Map<String,String> jsonMap = new HashMap<>();

        try {

            Integer height = Integer.parseInt(request.getParameter("height"));
            Integer width = Integer.parseInt(request.getParameter("width"));

            Content.clear();
            Content.set(height, width);
            randomMines(height, width);

            insertNewGameToDb(Content.get());

            Dimensions dm = new Dimensions(Content.get().getBoardData().getNumOfRows(), Content.get().getBoardData().getNumOfCols());
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
     * Insert new game data to database
     * @throws SQLException err syntax or connection
     */
    private static void insertNewGameToDb(Game gameModel) throws SQLException {
        Statement statement = Content.getConn().createStatement();

        statement.executeUpdate("INSERT INTO GAMES(ID, RESULT, FREE_FIELD_COUNTER ) VALUES ("
                +0+ ",'" +gameModel.getGameResult().toString() + "'" + "," + gameModel.getFreeFieldCounter() + ");");

        statement.executeUpdate("INSERT INTO GAMES_BOARD( GAME_ID, NUM_OF_ROWS, NUM_OF_COLS ) VALUES"
                + "(0," + gameModel.getBoardData().getNumOfRows() + "," + gameModel.getBoardData().getNumOfCols() + ");");

        for(Field f: gameModel.getBoardData().getFields()) {
            statement.executeUpdate("INSERT INTO FIELDS( GAME_ID, ROW_INX, COL_INX, MINE, MARKED, SELECTED, AROUND_MINES ) VALUES"
                    + "(0,"
                    + f.getRowIndex() + ","
                    + f.getColIndex() + ","
                    + f.isMine() + ","
                    + f.isMarked() + ","
                    + f.isSelected() + ","
                    + f.getNumOfMinesAroundField()
                    + ");");
        }

//        ResultSet rs = statement.executeQuery("SELECT * FROM GAMES;");
//        while(rs.next()){
//            System.out.println(rs.getString("FREE_FIELD_COUNTER"));
//        }
    }

    /**
     * Method random mines and putting to game board
     *
     * @param height board height without border (user input value)
     * @param width  board width without border (user input value)
     * @throws IllegalArgumentException if size is not correct
     */
    static private void randomMines(Integer height, Integer width) throws IllegalArgumentException {

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

        Content.get().setFreeFieldCounter(Content.get().getFreeFieldCounter() - numOfMine);

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

            } while (Content.get().getBoardData().get(new Index(randRow, randCol)).isMine());

            Content.get().getBoardData().get(new Index(randRow, randCol)).setAsMine();
        }

        countMines(Content.get().getBoardData().getNumOfRows(), Content.get().getBoardData().getNumOfCols());
    }

    /**
     * Function count mines around field and set value to proper filed
     * @param numOfRows board number of rows
     * @param numOfCols board number of columns
     */
    private static void countMines(Integer numOfRows, Integer numOfCols){

        for(int row = 1; row < numOfRows - 1; row++) {
            for(int col = 1; col < numOfCols - 1; col++) {
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
                        if (Content.get().getInfoAboutMine(i)) num++;
                    } catch (FieldException ignored) { }
                }
                try {
                    Content.get().setFiledAroundMines(num, new Index(row, col));
                } catch (FieldException ignored) { }
            }
        }
    }
}
