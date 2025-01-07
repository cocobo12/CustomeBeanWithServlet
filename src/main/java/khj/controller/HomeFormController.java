package main.java.khj.controller;

import jakarta.servlet.http.HttpServletRequest;
import main.java.khj.annotation.CustomController;
import main.java.khj.annotation.CustomRequstMapping;

import java.util.Map;


@CustomRequstMapping("/home")
@CustomController
public class HomeFormController implements Controller {

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model, HttpServletRequest req) {
        return "home-form";
    }
}
