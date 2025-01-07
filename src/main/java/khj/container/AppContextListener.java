package main.java.khj.container;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import main.java.khj.config.BeanConfig;
import main.java.khj.config.MappingConfig;


@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("cotextInit-----------------------------------------------------");
        String packageName = "main.java.khj";
        // Bean 등 초기화
        MyContainerInit myContainerInit = new MyContainerInit();
        myContainerInit.initAll(packageName, sce);

        // 컴포넌트 초기화
//        MappingConfig mappingConfig = new MappingConfig();
//        mappingConfig.init(packageName, sce);

        System.out.println("init context comp!!!!!!!!!!!!!!!");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application is shutting down.");
    }
}
