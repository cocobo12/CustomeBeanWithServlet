package khj.container;

import jakarta.servlet.ServletContextEvent;
import khj.annotation.CustomConfiguration;
import khj.config.InitApp;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static khj.container.ClassPath.findClassesWithAnnotation;


public class MyContainerInit {

    public void initAll(String packageName, ServletContextEvent sce) {
        // CustomConfiguration 애노테이션이 있는 클래스 검색
        Set<Class<?>> configClasses = findClassesWithAnnotation(CustomConfiguration.class, packageName);

        // order 속성 기준으로 정렬
        List<Class<?>> sortedClasses = new ArrayList<>(configClasses);
        sortedClasses.sort((c1, c2) -> {
            int order1 = c1.getAnnotation(CustomConfiguration.class).order();
            int order2 = c2.getAnnotation(CustomConfiguration.class).order();
            return Integer.compare(order1, order2);
        });

        // 정렬된 순서대로 초기화 실행
        System.out.println("Sorted configuration classes: " + sortedClasses);
        for (Class<?> clazz : sortedClasses) {
            try {
                InitApp appInit = (InitApp) clazz.getDeclaredConstructor().newInstance();
                System.out.println("InitApp: " + appInit);

                appInit.init(packageName, sce);

            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize configuration: " + clazz.getName(), e);
            }
        }
    }

}
