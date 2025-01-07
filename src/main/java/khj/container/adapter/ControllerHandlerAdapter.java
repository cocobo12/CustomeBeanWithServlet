package main.java.khj.container.adapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.khj.controller.Controller;
import main.java.khj.model.ModelView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerHandlerAdapter implements MyHandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object con) throws ServletException, IOException {
        System.out.println("access controller adapter!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Controller controller = (Controller) con;

        Map<String, String> paramMap = createParamMap(request);
        System.out.println("paramMap : " + paramMap);

        HashMap<String, Object> model = new HashMap<>();

        String viewName = controller.process(paramMap, model, request);
        System.out.println("viewName : " + viewName);

        ModelView mv = new ModelView(viewName);
        mv.setModel(model);

        return mv;
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
