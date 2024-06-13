package hw;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LoggerAspect implements InvocationHandler {
    private final TestLoggingInterface testLoggingInterface;

    public LoggerAspect(TestLoggingInterface testLoggingInterface) {
        this.testLoggingInterface = testLoggingInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method realMethod = testLoggingInterface.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (realMethod.isAnnotationPresent(Log.class)) {
            if (args != null) {
                for (Object arg : args) {
                    System.out.println(arg);
                }
            }
        }
        return method.invoke(testLoggingInterface, args);
    }

    public static TestLoggingInterface createProxy(TestLoggingInterface testLoggingInterface) {
        return (TestLoggingInterface) Proxy.newProxyInstance(LoggerAspect.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, new LoggerAspect(testLoggingInterface));
    }
}
