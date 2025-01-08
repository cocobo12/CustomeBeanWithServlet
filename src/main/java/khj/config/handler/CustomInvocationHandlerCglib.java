package khj.config.handler;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CustomInvocationHandlerCglib implements MethodInterceptor {

    private final Object target;

    public CustomInvocationHandlerCglib(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        // 전처리 로직
        System.out.println("cglib Before method: " + method.getName());

        // 원래 메서드 호출
        Object result = method.invoke(target, args);

        // 후처리 로직
        System.out.println("cglib After method: " + method.getName());
        return result;
    }

    public Object getTarget() {
        return target;
    }
}
