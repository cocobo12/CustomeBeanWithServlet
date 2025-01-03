package main.java.khj.config.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// CustomInvocationHandler 정의
public class CustomInvocationHandler implements InvocationHandler {
    private final Object target;

    public CustomInvocationHandler(Object target) {
        this.target = target;
    }
    // 실제 객체 반환 메서드
    public Object getTarget() {
        return target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before method: " + method.getName());
        Object result = method.invoke(target, args); // 원래 객체의 메서드 호출
        System.out.println("After method: " + method.getName());
        return result;
    }


}

