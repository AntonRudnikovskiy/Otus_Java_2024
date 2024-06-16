package hw;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class LoggerAspect<T> implements InvocationHandler {
    private final T target;
    private final Map<Method, Boolean> logAnnotatedMethods = new HashMap<>();

    public LoggerAspect(T target) {
        this.target = target;

        for (Method method : target.getClass().getInterfaces()[0].getMethods()) {
            try {
                Method realMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
                logAnnotatedMethods.put(method, realMethod.isAnnotationPresent(Log.class));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (logAnnotatedMethods.getOrDefault(method, false)) {
            if (args != null) {
                for (Object arg : args) {
                    System.out.println(arg);
                }
            }
        }
        return method.invoke(target, args);
    }

    public static <T> T createProxy(T target) {
        Class<?>[] interfaces = target.getClass().getInterfaces();
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                interfaces,
                new LoggerAspect<>(target)
        );
    }
}
