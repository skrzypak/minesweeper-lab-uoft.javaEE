/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab.saper.servlets;

import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet presenting the use of cookies. Save last height and width board
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
@WebServlet(name = "CookiesServlet", urlPatterns = "/cookies")
public class CookiesServlet extends HttpServlet {

    private final Gson gson = new Gson(); // Gson object

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Cookie[] cookies = request.getCookies();
            Map<String,String> jsonMap = new HashMap<>();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("lastHeight")) {
                        jsonMap.put("lastHeight", cookie.getValue());
                    }
                    if (cookie.getName().equals("lastWidth")) {
                        jsonMap.put("lastWidth", cookie.getValue());
                    }
                }
            }
            response.setContentType("application/json;charset=UTF-8");
            out.print(this.gson.toJson(jsonMap));
            out.flush();
        }
    }

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
        response.setContentType("text/html;charset=UTF-8");
        Cookie c1 = new Cookie("lastHeight", request.getParameter("height"));
        Cookie c2 = new Cookie("lastWidth", request.getParameter("width"));
        response.addCookie(c1);
        response.addCookie(c2);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
