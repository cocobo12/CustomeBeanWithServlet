package khj.controller;

import jakarta.servlet.http.HttpServletRequest;
import khj.annotation.CustomAutowired;
import khj.annotation.CustomRequstMapping;
import khj.model.Member;
import khj.model.ModelView;
import khj.service.JoinService;

import java.util.HashMap;
import java.util.Map;

@CustomRequstMapping("/bro/join")
public class JoinSubmitController implements Controller {
    @CustomAutowired
    private JoinService joinService;

    @Override
    public ModelView process(Map<String, String> paramMap, HttpServletRequest req) {
        String id = paramMap.get("id");
        String pw = paramMap.get("pw");
        System.out.println("loginService : " + joinService);
        Member member = new Member(id, pw);
        joinService.join(req, member);
        // 로그인한 Member객체 -> ModelView
        ModelView mv = new ModelView("home-form");
        return mv;
    }
}
