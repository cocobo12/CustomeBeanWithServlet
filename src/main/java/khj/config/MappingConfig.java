package main.java.khj.config;

import jakarta.servlet.ServletContextEvent;
import main.java.khj.annotation.*;
import main.java.khj.config.handler.CustomInvocationHandler;
import main.java.khj.config.handler.CustomInvocationHandlerCglib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static main.java.khj.container.ClassPath.findClassesWithAnnotation;

@CustomConfiguration(order = 2)
public class MappingConfig implements InitApp {


    // 서블릿 컨테이너 내에서 돌아가는 모든 객체는 컴포넌츠로 주입.
    private final Map<String, Object> components = new HashMap<>();

    private final Map<String, Object> requestMapping = new HashMap<>();

    private static final MappingConfig instance = new MappingConfig();

//    public static MappingConfig getInstance(){
//        return instance;
//    }

    @Override
    public void init(String packageName, ServletContextEvent sce) {
        BeanConfig beanConfig = (BeanConfig) sce.getServletContext().getAttribute("beanConfig");
        try {
            // CustomComponent 애노테이션이 있는 클래스 검색
            Set<Class<?>> compClasses = findClassesWithAnnotation(CustomComponent.class, packageName);

            for (Class<?> compClass : compClasses) {
                CustomComponent customComponent = compClass.getAnnotation(CustomComponent.class);
                String compName = customComponent.value().isEmpty() ? compClass.getSimpleName() : customComponent.value();


                // 클래스의 인스턴스 생성
                Object instance = compClass.getDeclaredConstructor().newInstance();
                //Object reactInstance = getRealObject(instance);

                // components에 저장
                components.put(compName, instance);

                System.out.println("Component initialized: Name = " + compName + ", Instance = " + instance);
            }
            System.out.println("Components: " + components);

            // 의존성 주입
            beanConfig.injectDependenciesComps(components);


            // CustomRequstMapping 애노테이션 처리
            Set<Class<?>> maps = findClassesWithAnnotation(CustomRequstMapping.class, packageName);
            System.out.println("maps : " + maps);
            for (Class<?> map : maps) {
                CustomRequstMapping customRequstMapping = map.getAnnotation(CustomRequstMapping.class);
                String url = customRequstMapping.value();
                System.out.println("components2 : " + components);
                System.out.println("url : " + url);
                System.out.println("map : " + map);
                System.out.println("map name : " + map.getSimpleName());
                System.out.println("map class : " + map.getDeclaredConstructor());
                System.out.println("map class new : " + map.getDeclaredConstructor().newInstance());

                requestMapping.put(url, map.getDeclaredConstructor().newInstance());
            }

            // 서블릿 컨텍스트에 저장
            sce.getServletContext().setAttribute("components", components);
            sce.getServletContext().setAttribute("requestMapping", requestMapping);

            System.out.println("Mapping initialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to Initialize components", e);
        }
    }

    // Proxy에서 실제 객체 추출
    private Object getRealObject(Object proxy) {
        if (Proxy.isProxyClass(proxy.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(proxy);
            if (handler instanceof CustomInvocationHandler) {
                return ((CustomInvocationHandler) handler).getTarget();
            }
        } else if (proxy instanceof net.sf.cglib.proxy.Factory) {
            // CGLIB 프록시에서 실제 객체 추출
            net.sf.cglib.proxy.Factory factory = (net.sf.cglib.proxy.Factory) proxy;
            net.sf.cglib.proxy.Callback callback = factory.getCallback(0);
            if (callback instanceof CustomInvocationHandlerCglib) {
                return ((CustomInvocationHandlerCglib) callback).getTarget();
            }
        }
        return proxy; // 프록시가 아닌 경우 원래 객체 반환
    }
}
