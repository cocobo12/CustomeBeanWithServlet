package main.java.khj.container;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.http.HttpServletRequest;
import main.java.khj.annotation.CustomConfiguration;
import main.java.khj.config.InitApp;

import java.util.Set;

import static main.java.khj.container.ClassPath.findClassesWithAnnotation;


public class MyContainerInit {

    public void initAll(String packageName, ServletContextEvent sce) {
        // CustomWebServlet 애노테이션이 있는 클래스 검색
        Set<Class<?>> inits = findClassesWithAnnotation(CustomConfiguration.class, packageName);
        // 초기화 실행
        System.out.println("inits :" + inits);
        for(Class<?> clazz : inits) {
            try {
                InitApp appInit = (InitApp) clazz.getDeclaredConstructor().newInstance();
                System.out.println("InitApp : " + appInit);

                appInit.init(packageName, sce);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
