package main.java.khj.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import main.java.khj.annotation.CustomBean;

import java.io.IOException;

@CustomBean()
@WebServlet(urlPatterns = "/board")
public class BoardServlet extends HttpServlet {
    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp)
            throws IOException {
        resp.getWriter().write("BoardServlet is working!");
    }
}
