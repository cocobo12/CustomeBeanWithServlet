package main.java.khj.container;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import main.java.khj.config.BeanConfig;


@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("cotextInit-----------------------------------------------------");
        String packageName = "main.java.khj";
        MyContainerInit myContainerInit = new MyContainerInit();
        myContainerInit.initAll(packageName, sce);
        System.out.println("init context comp!!!!!!!!!!!!!!!");

        // BeanConfig 생성 및 서블릿 컨텍스트에 등록
//        BeanConfig beanConfig = BeanConfig.getInstance();
//        System.out.println("beanConfig init : " + beanConfig);
//        System.out.println("beans : " + beanConfig.getBeans());
//        sce.getServletContext().setAttribute("beanConfig", beanConfig); // 서블릿 컨텍스트에 저장
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application is shutting down.");
    }
}
