package khj.controller;

import jakarta.servlet.http.HttpServletRequest;
import khj.annotation.CustomRequstMapping;
import khj.model.ModelView;


import java.util.Map;


@CustomRequstMapping("/bro/home")
public class HomeFormController implements Controller {

    @Override
    public ModelView process(Map<String, String> paramMap, HttpServletRequest req) {
        ModelView mv = new ModelView("home-form");
        return mv;
    }
}
