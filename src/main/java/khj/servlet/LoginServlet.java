package main.java.khj.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.java.khj.annotation.CustomAutowired;
import main.java.khj.annotation.CustomBean;
import main.java.khj.config.BeanConfig;
import main.java.khj.model.Member;
import main.java.khj.service.LoginService;

import java.io.IOException;

@CustomBean("loginServlet")
@WebServlet(urlPatterns = "/login-page")
public class LoginServlet extends HttpServlet {

    @CustomAutowired
    private LoginService loginService;

    @Override
    public void init() {


        // 의존성 주입 ---------------------------------------------------------------------------
        System.out.println("init start-------------------------");
        // BeanConfig 가져오기
        BeanConfig beanConfig = (BeanConfig) getServletContext().getAttribute("beanConfig");
        System.out.println("beanConfig = " + beanConfig);
        System.out.println("beans = " + beanConfig.getBeans());

        beanConfig.injectDependencies(this);
        System.out.println("init end-------------------------");
        // -------------------------------------------------------------------------------------
    }

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws IOException {
//
//        String id = req.getParameter("id");
//        String pw = req.getParameter("pw");
//
//
//        boolean loginState = loginService.login(id,pw, req);
//        if(loginState){
//            Member member = new Member(id, pw);
//            // 세션에 로그인 정보 저장
//            HttpSession session = req.getSession(true);
//            session.setAttribute("member", member);
//
//            resp.getWriter().write("LoginServlet is working! complite login! go home!!");
//        }
//        resp.getWriter().write("LoginServlet is working! but not login.");
//    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String viewPath = "/WEB-INF/views/LoginPage.jsp";

        RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
        dispatcher.forward(req, res);
    }
}
