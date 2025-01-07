package main.java.khj.controller;

import jakarta.servlet.http.HttpServletRequest;
import main.java.khj.annotation.CustomAutowired;
import main.java.khj.annotation.CustomController;
import main.java.khj.annotation.CustomRequstMapping;
import main.java.khj.model.Member;

import main.java.khj.service.LoginService;

import java.util.Map;


@CustomRequstMapping("/login")
@CustomController
public class LoginFormController implements Controller {

    @CustomAutowired
    private LoginService loginServiceImp;

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model, HttpServletRequest req) {
        String id = paramMap.get("id");
        String pw = paramMap.get("pw");

        Member member = new Member(id, pw);
        loginServiceImp.login(req, member);

        return null;
    }
}
