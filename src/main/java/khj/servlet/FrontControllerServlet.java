package main.java.khj.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.khj.container.adapter.ControllerHandlerAdapter;
import main.java.khj.container.adapter.MyHandlerAdapter;
import main.java.khj.model.ModelView;
import main.java.khj.view.MyView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServlet", urlPatterns = "/*")
public class FrontControllerServlet extends HttpServlet {


    private Map<String, Object> handlerMappingMap;
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServlet() {
        initHandlerAdapters();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.handlerMappingMap = (Map<String, Object>) getServletContext().getAttribute("requestMapping");

    }


    private void initHandlerAdapters() {
        System.out.println("init adapter!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        handlerAdapters.add(new ControllerHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Mapping list : " + handlerMappingMap);
        System.out.println("param : " + request.getRequestURI());
        Object handler = getHandler(request);
        System.out.println("handler : " + handler);
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = getHandlerAdapter(handler);
        System.out.println("adapter@ : " + adapter);
        ModelView mv = adapter.handle(request, response, handler);

        String viewName = mv.getViewName();
        System.out.println("frontCon viewName : " + viewName);
        MyView view = viewResolver(viewName);
        System.out.println("MyView : " + view);
        view.render(mv.getModel(), request, response);

    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        System.out.println("getHandlerAdapter------------------------------------------");
        System.out.println("handler : " + handler);
        System.out.println("handler class : " + handler.getClass());
        System.out.println("-----------------------------------------------------------");
        //MemberFormController
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }

    private MyView viewResolver(String viewName) {
        // main/webapp/WEB-INF/views/home-form.jsp
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}