package main.java.khj.config;

import jakarta.servlet.ServletContextEvent;
import main.java.khj.annotation.CustomAutowired;
import main.java.khj.annotation.CustomBean;
import main.java.khj.annotation.CustomConfiguration;
import main.java.khj.config.handler.CustomInvocationHandler;
import main.java.khj.config.handler.CustomInvocationHandlerCglib;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static main.java.khj.container.ClassPath.findClassesWithAnnotation;
import static main.java.khj.container.ClassPath.findClassesWithFieldAnnotation;

@CustomConfiguration(order = 1)
public class BeanConfig implements InitApp {

    public BeanConfig() {
    }

    // Singleton Instance
    private static final BeanConfig instance = new BeanConfig();

    // 객체를 저장할 맵
    private final Map<String, Object> beans = new HashMap<>();

    // Private Constructor (Singleton)

    // Get Singleton Instance
    public static BeanConfig getInstance() {
        return instance;
    }

    public Map<String, Object> getBeans() {
        return beans;
    }

    @Override
    public void init(String packageName, ServletContextEvent sce) {
        try {
            // CustomBean 애노테이션이 있는 클래스 검색
            Set<Class<?>> beanClasses = findClassesWithFieldAnnotation(CustomBean.class, packageName);
            System.out.println("beanClasses : " + beanClasses);

            // 객체 생성 및 저장
            for (Class<?> beanClass : beanClasses) {
                Object classInstance = beanClass.getDeclaredConstructor().newInstance(); // 클래스 자체 인스턴스 생성
                for (Field field : beanClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(CustomBean.class)) {
                        // 접근 권한 설정
                        field.setAccessible(true);

                        // 필드가 이미 초기화된 상태인지 확인
                        Object fieldInstance = field.get(classInstance);
                        if (fieldInstance == null) {
                            // @CustomBean이 달린 필드의 객체 생성
                            fieldInstance = field.getType().getDeclaredConstructor().newInstance();
                            field.set(classInstance, fieldInstance); // 필드에 인스턴스 주입
                        }

                        // 프록시 생성 (필요시)
                        Object proxy = createProxy(fieldInstance);

                        // Bean 이름 설정
                        CustomBean customBean = field.getAnnotation(CustomBean.class);
                        String beanName = customBean.value().isEmpty() ? field.getName() : customBean.value();

                        // 싱글톤으로 관리
                        if (!beans.containsKey(beanName)) {
                            beans.put(beanName, proxy);
                            System.out.println("Bean registered: " + beanName + " -> " + proxy);
                        } else {
                            System.out.println("Bean already registered: " + beanName);
                        }
                    }
                }
            }

            // @CustomAutowired가 달린 필드에 의존성 주입
            for (Object bean : beans.values()) {
                injectDependencies(bean);
            }

            System.out.println("beans : " + beans);
            // 서블릿 컨텍스트에 저장
            sce.getServletContext().setAttribute("beanConfig", this);
            System.out.println("Bean initialization completed!");

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize beans", e);
        }
    }

//    @Override
//    public void init(String packageName, ServletContextEvent sce) {
//        try {
//            // CustomBean 애노테이션이 있는 클래스 검색
//            Set<Class<?>> beanClasses = findClassesWithFieldAnnotation(CustomBean.class, packageName);
//            System.out.println("beanClasses : " + beanClasses);
//            // 객체 생성 및 저장
//            for (Class<?> beanClass : beanClasses) {
//                for (Field field : beanClass.getDeclaredFields()) {
//                    if (field.isAnnotationPresent(CustomBean.class)) {
//                        Object instance = field.getClass().getDeclaredConstructor().newInstance();
//                        System.out.println("bean class : " + instance);
//
//                        // 프록시 생성
//                        Object proxy = createProxy(instance);
//
//                        // Bean 이름 설정
//                        CustomBean customBean = field.getAnnotation(CustomBean.class);
//                        System.out.println("customBean : " + customBean);
//                        String beanName = customBean.value().isEmpty() ? beanClass.getSimpleName() : customBean.value();
//
//                        // 인터페이스 타입으로도 저장
////                Class<?>[] interfaces = beanClass.getInterfaces();
////                if (interfaces.length > 0) {
////                    for (Class<?> iface : interfaces) {
////                        beans.put(iface.getSimpleName(), proxy);
////                    }
////                }
//
//                        beans.put(beanName, proxy);
//                        System.out.println("beans : " + beans);
//                        System.out.println("beanName : " + beanName + " proxy : " + proxy);
//                    }
//                }
//            }
//
//
//            // @CustomAutowired가 달린 필드에 의존성 주입
////            for (Object bean : beans.values()) {
////                injectDependencies(bean);
////            }
//
//            sce.getServletContext().setAttribute("beanConfig", this); // 서블릿 컨텍스트에 저장
//            System.out.println("init bean!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to initialize beans", e);
//        }
//    }

    public void injectDependencies(Object proxyBean) {
        // 실제 클래스를 프록시로부터 가져와서 주입
        Object realBean = getRealObject(proxyBean);
        Class<?> realClass = realBean.getClass();

        System.out.println("Injecting dependencies into: " + realClass.getName());

        for (Field field : realClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(CustomAutowired.class)) {
                String dependencyName = field.getType().getSimpleName();
                System.out.println("dependencyName : " + dependencyName);
                System.out.println("bean list : " + beans);
                Object dependency = beans.get(dependencyName);

                System.out.println("Field: " + field.getName() + ", Type: " + field.getType() + ", Dependency found: " + dependency);

                if (dependency != null) {
                    try {
                        field.setAccessible(true);
                        field.set(realBean, dependency);
                        System.out.println("Successfully injected: " + dependency + " into " + field.getName());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to inject dependency", e);
                    }
                } else {
                    System.out.println("No dependency found for: " + dependencyName);
                }
            }
        }
    }

    public void injectDependenciesComp(Object comp) {
        // 실제 클래스를 프록시로부터 가져와서 주입

        Class<?> realClass = comp.getClass();

        System.out.println("Injecting dependencies into: " + realClass.getName());

        for (Field field : realClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(CustomAutowired.class)) {
                String dependencyName = field.getType().getSimpleName();
                System.out.println("dependencyName : " + dependencyName);
                System.out.println("bean list : " + beans);
                Object dependency = beans.get(dependencyName);

                System.out.println("Field: " + field.getName() + ", Type: " + field.getType() + ", Dependency found: " + dependency);

                if (dependency != null) {
                    try {
                        field.setAccessible(true);
                        // CustomAutowired 애노테이션 달린 필드 인스턴스와 동일한 이름의 인스턴스가 Bean에 존재하면, 실제 객체를 주입.
                        Object realField = getRealObject(field);
                        field.set(realField, dependency); // 변경 후, 변경전
                        System.out.println("Successfully injected: " + dependency + " into " + field.getName());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to inject dependency", e);
                    }
                } else {
                    System.out.println("No dependency found for: " + dependencyName);
                }
            }
        }
    }


    //Map으로 받은 파라미터의 value값에 클래스 내에, @CustomAutowired()가 달린 필드에
    public void injectDependenciesComps(Map<String, Object> components) {
        for (Map.Entry<String, Object> entry : components.entrySet()) {
            Object component = entry.getValue();
            Class<?> realClass = component.getClass();
            System.out.println("inject componet : " + component);
            System.out.println("inject realClass : " + realClass);
            System.out.println("Injecting dependencies into: " + realClass.getName());

            for (Field field : realClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(CustomAutowired.class)) {
                    String dependencyName = field.getName();
                    System.out.println("field : " + field);
                    System.out.println("Dependency name: " + dependencyName);
                    System.out.println("Available beans: " + beans);
                    Object dependency = beans.get(dependencyName);

                    System.out.println("Field: " + field.getName() + ", Type: " + field.getType() + ", Dependency found: " + dependency);

                    if (dependency != null) {
                        try {
                            field.setAccessible(true);
                            Object realObject = getRealObject(component); // 프록시 확인 후 실제 객체 가져오기
                            field.set(realObject, dependency);
                            System.out.println("Successfully injected: " + dependency + " into " + field.getName());
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Failed to inject dependency", e);
                        }
                    } else {
                        System.out.println("No dependency found for: " + dependencyName);
                    }
                }
            }
        }
    }


    private Object createProxy(Object target) {
        Class<?>[] interfaces = target.getClass().getInterfaces();

        if (interfaces.length > 0) {
            // JDK 동적 프록시 생성
            System.out.println("Creating JDK proxy for: " + target.getClass().getName());
            return Proxy.newProxyInstance(
                    target.getClass().getClassLoader(),
                    interfaces,
                    new CustomInvocationHandler(target)
            );
        } else {
            // CGLIB 프록시 생성
            System.out.println("Creating CGLIB proxy for: " + target.getClass().getName());
            return createCglibProxy(target);
        }
    }

    private Object createCglibProxy(Object target) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new CustomInvocationHandlerCglib(target));
        return enhancer.create();
    }


    public void addBean(Object object) {
        try {
            // 프록시 생성
            Object proxy = instance.createProxy(object);

            // Bean 이름 설정 (클래스 이름을 기본으로 사용)
            String beanName = object.getClass().getSimpleName();

            // 저장
            instance.beans.put(beanName, proxy);

            // 추가된 Bean에 의존성 주입
            instance.injectDependencies(proxy);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add bean", e);
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
