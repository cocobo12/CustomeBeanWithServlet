package khj.config;

import jakarta.servlet.ServletContextEvent;

import khj.annotation.CustomAutowired;
import khj.annotation.CustomBean;
import khj.annotation.CustomConfiguration;
import khj.config.handler.CustomInvocationHandler;
import khj.config.handler.CustomInvocationHandlerCglib;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static khj.container.ClassPath.findClassesWithFieldAnnotation;


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


            System.out.println("beans : " + beans);

            // 서블릿 컨텍스트에 저장
            sce.getServletContext().setAttribute("beanConfig", this);
            System.out.println("Bean initialization completed!");

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize beans", e);
        }
    }


    //Map으로 받은 파라미터의 value값에 클래스 내에, @CustomAutowired()가 달린 필드에
    public void injectDependenciesComps(Map<String, Object> components) {
        for (Map.Entry<String, Object> entry : components.entrySet()) {
            Object component = entry.getValue();
            Class<?> realClass = component.getClass();
//            System.out.println("inject componet : " + component);
//            System.out.println("inject realClass : " + realClass);
            System.out.println("Injecting dependencies into: " + realClass.getName());

            for (Field field : realClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(CustomAutowired.class)) {
                    String dependencyName = field.getName();
//                    System.out.println("field : " + field);
//                    System.out.println("Dependency name: " + dependencyName);
//                    System.out.println("Available beans: " + beans);
                    Object dependency = beans.get(dependencyName);

                    System.out.println("Field: " + field.getName() + ", Type: " + field.getType() + ", Dependency found: " + dependency);

                    if (dependency != null) {
                        try {
                            field.setAccessible(true);
                            Object realObject = getRealObject(component); // 프록시 확인 후 실제 객체 가져오기
                            //System.out.println("realObject : " + realObject);
                            Object nestDependency = injectNestedDependencies(dependency);
                            //System.out.println("nestDependency : " + nestDependency);

                            field.set(realObject, nestDependency);

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


    /**
     * 의존성 주입된 객체의 내부 필드에서도 @CustomAutowired를 찾아 주입한 뒤, 객체를 반환하는 재귀 로직.
     */
    private Object injectNestedDependencies(Object dependency) {
        if (dependency == null) {
            return null;
        }

        Object nestReal = getRealObject(dependency);
        Class<?> dependencyClass = nestReal.getClass();

        System.out.println("Checking nested dependencies for: " + dependencyClass.getName());

        for (Field nestedField : dependencyClass.getDeclaredFields()) {
            if (nestedField.isAnnotationPresent(CustomAutowired.class)) {
                String nestedDependencyName = nestedField.getName();
                //System.out.println("Nested Field: " + nestedField);
                //System.out.println("Nested Dependency name: " + nestedDependencyName);
                Object nestedDependency = beans.get(nestedDependencyName);

                if (nestedDependency != null) {
                    try {
                        nestedField.setAccessible(true);
                        nestedField.set(nestReal, nestedDependency);
                        System.out.println("Successfully injected nested dependency: " + nestedDependency + " into " + nestedField.getName());

                        // **재귀 호출로 더 깊은 수준의 필드까지 탐색 및 주입**
                        injectNestedDependencies(nestedDependency);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to inject nested dependency", e);
                    }
                } else {
                    System.out.println("No nested dependency found for: " + nestedDependencyName);
                }
            }
        }

        return dependency;
    }


    // 의존성 주입 한번.
    public void injectDependencies(Object proxyBean) {
        // 실제 클래스를 프록시로부터 가져와서 주입
        Object realBean = getRealObject(proxyBean);
        Class<?> realClass = realBean.getClass();

        System.out.println("Injecting dependencies into: " + realClass.getName());

        for (Field field : realClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(CustomAutowired.class)) {
                String dependencyName = field.getType().getSimpleName();
                //System.out.println("dependencyName : " + dependencyName);
                //System.out.println("bean list : " + beans);
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
            instance.injectNestedDependencies(proxy);
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
