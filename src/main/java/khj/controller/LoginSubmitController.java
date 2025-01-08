package khj.controller;

import jakarta.servlet.http.HttpServletRequest;
import khj.annotation.CustomAutowired;
import khj.annotation.CustomRequstMapping;
import khj.model.Member;
import khj.model.ModelView;
import khj.service.LoginService;

import java.util.HashMap;
import java.util.Map;


@CustomRequstMapping("/bro/login")
public class LoginSubmitController implements Controller {
    @CustomAutowired
    private LoginService loginService;

    @Override
    public ModelView process(Map<String, String> paramMap, HttpServletRequest req) {
        String id = paramMap.get("id");
        String pw = paramMap.get("pw");
        System.out.println("loginService : " + loginService);
        Member member = new Member(id, pw);
        Member member1 = loginService.login(req, member);
        // 로그인한 Member객체 -> ModelView
        ModelView mv = new ModelView("home-form");
        if (member1 == null) {
            return mv;
        }
        Map<String, Object> model = new HashMap<>();
        model.put("member", member1);
        mv.setModel(model);

        return mv;
    }
}
