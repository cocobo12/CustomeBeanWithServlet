package main.java.khj.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import main.java.khj.annotation.CustomBean;
import main.java.khj.model.Member;

import java.io.IOException;

@CustomBean
@WebServlet(urlPatterns = "/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp)
            throws IOException {
        // 세션 가져오기
        HttpSession session = req.getSession(false);
        if (session != null) {
            // 세션에서 member 객체 가져오기
            Member member = (Member) session.getAttribute("member");

            if (member != null) {
                // member 객체 정보 출력
                resp.getWriter().write("Welcome, " + member.getId() + "! You are logged in.");
            } else {
                resp.getWriter().write("No member found in session. Please log in.");
            }
        } else {
            resp.getWriter().write("No active session. Please log in.");
        }
    }
}
