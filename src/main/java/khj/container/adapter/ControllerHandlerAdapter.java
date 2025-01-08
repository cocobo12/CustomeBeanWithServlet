package khj.container.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import khj.controller.Controller;
import khj.model.ModelView;

import java.util.HashMap;
import java.util.Map;

public class ControllerHandlerAdapter implements MyHandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object con) {
        //System.out.println("access controller adapter----------------------");
        Controller controller = (Controller) con;

        Map<String, String> paramMap = createParamMap(request);
        //System.out.println("paramMap : " + paramMap);


        ModelView mv = controller.process(paramMap, request);
        System.out.println("viewName : " + mv.getViewName());
        //System.out.println("end controller adapter------------------------");
        return mv;
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
