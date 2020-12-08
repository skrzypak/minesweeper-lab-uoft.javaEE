package pl.polsl.lab.saper.servlets;

import pl.polsl.lab.saper.model.Settings;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class MenuServlet extends HttpServlet {

    private Settings settings = new Settings();

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
