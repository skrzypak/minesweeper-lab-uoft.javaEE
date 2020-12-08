package pl.polsl.lab.saper.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class AppServlet extends HttpServlet {
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        String url = req.getParameter("address");
//
//        if ((url.length() == 0) || url.equals("http://")) {
//            getServletContext().getRequestDispatcher("/Error").forward(req, resp);
//        } else {
//            resp.sendRedirect(url);
//        }
    }
}
