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

@CustomConfiguration
public class BeanConfig implements InitApp<String>{

    public BeanConfig() {
    }
    // Singleton Instance
    public static final BeanConfig instance = new BeanConfig();

    // 객체를 저장할 맵
    private final Map<String, Object> beans = new HashMap<>();

    // Private Constructor (Singleton)

    // Get Singleton Instance
    public static BeanConfig getInstance() {
        return instance;
    }

    public Map<String, Object> getBeans(){
        return beans;
    }

    @Override
    public void init(String packageName, ServletContextEvent sce) {
        try {
            // CustomBean 애노테이션이 있는 클래스 검색
            Set<Class<?>> beanClasses = findClassesWithAnnotation(CustomBean.class, packageName);

            // 객체 생성 및 저장
            for (Class<?> beanClass : beanClasses) {
                Object instance = beanClass.getDeclaredConstructor().newInstance();

                // 프록시 생성
                Object proxy = createProxy(instance);

                // Bean 이름 설정
                CustomBean customBean = beanClass.getAnnotation(CustomBean.class);
                String beanName = customBean.value().isEmpty() ? beanClass.getSimpleName() : customBean.value();

                // 인터페이스 타입으로도 저장
                Class<?>[] interfaces = beanClass.getInterfaces();
                if (interfaces.length > 0) {
                    for (Class<?> iface : interfaces) {
                        beans.put(iface.getSimpleName(), proxy);
                    }
                }

                beans.put(beanName, proxy);
                System.out.println("beans : " + beans);
                System.out.println("beanName : " + beanName + " proxy : " + proxy);
            }


            // @CustomAutowired가 달린 필드에 의존성 주입
            for (Object bean : beans.values()) {
                injectDependencies(bean);
            }

            sce.getServletContext().setAttribute("beanConfig", this); // 서블릿 컨텍스트에 저장
            System.out.println("init comp!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize beans", e);
        }
    }

    public void injectDependencies(Object proxyBean) {
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
