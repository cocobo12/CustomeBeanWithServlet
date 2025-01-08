package khj.container;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("!!----------------context init start--------------------!!");
        String packageName = "khj";
        // Bean 등 초기화
        MyContainerInit myContainerInit = new MyContainerInit();
        myContainerInit.initAll(packageName, sce);

        System.out.println("!!----------------context init end----------------------!!");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application is shutting down.");
    }
}
