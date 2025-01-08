package khj.controller;

import jakarta.servlet.http.HttpServletRequest;
import khj.model.ModelView;

import java.util.Map;

public interface Controller {

    ModelView process(Map<String, String> paramMap, HttpServletRequest req);


}
