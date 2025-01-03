package main.java.khj.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import main.java.khj.annotation.CustomBean;


import java.io.IOException;

@CustomBean
@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp)
            throws IOException {
        System.out.println("test comp!!!!!!!!!!!!!!!!!!!!!!!!!!");
        resp.getWriter().write("TestServlet is working!");
    }

}
