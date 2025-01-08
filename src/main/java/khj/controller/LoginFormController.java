package khj.controller;

import jakarta.servlet.http.HttpServletRequest;
import khj.annotation.CustomRequstMapping;
import khj.model.ModelView;


import java.util.Map;


@CustomRequstMapping("/bro/login-form")
public class LoginFormController implements Controller {

    @Override
    public ModelView process(Map<String, String> paramMap, HttpServletRequest req) {
        ModelView mv = new ModelView("login-form");
        return mv;
    }

}
