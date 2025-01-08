package khj.config.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


// 동작 시간 측정
public class TimeInvocationHandler implements InvocationHandler {

    private final Object target;

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy 주입");
        long loginTime = System.currentTimeMillis();
        System.out.println("time : " + loginTime);

        Object result = method.invoke(target, args, loginTime);
        return result;
    }
}
