package khj.controller;

import jakarta.servlet.http.HttpServletRequest;
import khj.annotation.CustomRequstMapping;
import khj.model.ModelView;

import java.util.Map;

@CustomRequstMapping("/bro/join-form")
public class JoinFormController implements Controller {
    @Override
    public ModelView process(Map<String, String> paramMap, HttpServletRequest req) {
        ModelView mv = new ModelView("join-form");
        return mv;
    }
}
