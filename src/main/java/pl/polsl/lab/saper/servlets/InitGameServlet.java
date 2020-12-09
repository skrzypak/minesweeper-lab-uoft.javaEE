package pl.polsl.lab.saper.servlets;
import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.model.Dimensions;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.Gson;
import pl.polsl.lab.saper.model.Index;

public class InitGameServlet extends HttpServlet {

    private final Gson gson = new Gson();

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        Integer height = Integer.parseInt(request.getParameter("height"));
        Integer width = Integer.parseInt(request.getParameter("width"));

        TODO.clear();
        TODO.set(height, width);
        randomMines(height, width);

        Dimensions dm = new Dimensions(TODO.get().getBoardData().getNumOfRows(), TODO.get().getBoardData().getNumOfCols());
        String dmJsonString = this.gson.toJson(dm);
        response.setContentType("application/json;charset=UTF-8");
        out.print(dmJsonString);
        out.flush();
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

        TODO.get().setFreeFieldCounter(TODO.get().getFreeFieldCounter() - numOfMine);

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

            } while (TODO.get().getBoardData().get(new Index(randRow, randCol)).isMine());

            TODO.get().getBoardData().get(new Index(randRow, randCol)).setAsMine();
        }

        countMines(TODO.get().getBoardData().getNumOfRows(), TODO.get().getBoardData().getNumOfCols());
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
                        if (TODO.get().getInfoAboutMine(i)) num++;
                    } catch (FieldException ignored) { }
                }
                try {
                    TODO.get().setFiledAroundMines(num, new Index(row, col));
                } catch (FieldException ignored) { }
            }
        }
    }
}
