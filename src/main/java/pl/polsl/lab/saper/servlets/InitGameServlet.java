package pl.polsl.lab.saper.servlets;

import pl.polsl.lab.saper.model.Game;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class InitGameServlet extends HttpServlet {

    private Game gameModel;

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

        Integer height = Integer.parseInt(request.getParameter("height"));
        Integer width = Integer.parseInt(request.getParameter("width"));
        gameModel = new Game(height, width);

        PrintWriter out = response.getWriter();
        for (Integer i = 1; i < gameModel.getBoardData().getNumOfRows() - 1; i++) {
            out.println("<div class=\"columns\">");
            for (Integer j = 1; j < gameModel.getBoardData().getNumOfCols() - 1; j++) {
                out.println("<div " + "id="+i+"|"+j + " class=\"column field no-click no-select \"></div>");
            }
            out.println("</div>");
        }

    }
}
