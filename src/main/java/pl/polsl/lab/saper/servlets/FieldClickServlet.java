package pl.polsl.lab.saper.servlets;

import com.google.gson.Gson;
import pl.polsl.lab.saper.exception.FieldException;
import pl.polsl.lab.saper.model.Index;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "FieldClickServlet", urlPatterns = "/fieldClick")
public class FieldClickServlet extends HttpServlet {

    private final Gson gson = new Gson();

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

        Integer row = Integer.parseInt(req.getParameter("row"));
        Integer col = Integer.parseInt(req.getParameter("col"));
        String type = req.getParameter("btn");
        Index inx = new Index(row, col);
        String respJsonString;

        if(type.equals("left")) {
            // Set as selected
            try {
                TODO.get().setFieldAsSelected(inx);
                // TODO game status object
                respJsonString = this.gson.toJson(TODO.get().getNumOfMinesAroundField(inx));
            } catch (FieldException e) {
                respJsonString = this.gson.toJson(e);
            }

        } else if(type.equals("right")) {
            // Set as mark
            try {
                TODO.get().setFieldAsMark(inx);
                respJsonString = this.gson.toJson(true);
            } catch (FieldException e) {
                respJsonString = this.gson.toJson(e);
            }

        } else {
            respJsonString = this.gson.toJson(false);
        }

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(respJsonString);
        out.flush();
    }
}