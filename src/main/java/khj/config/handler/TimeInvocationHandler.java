package main.java.khj.config.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TimeInvocationHandler implements InvocationHandler {

    private final Object target;

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Time proxy 실행");
        long loginTime = System.currentTimeMillis();
        System.out.println("time : " + loginTime);

        Object result = method.invoke(target, args, loginTime);
        return result;
    }
}
