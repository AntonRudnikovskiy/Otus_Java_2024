package hw;

import static hw.LoggerAspect.createProxy;

public class Demo {
    public static void main(String[] args) {
        TestLoggingInterface testLogging = new TestLoggingImpl();
        TestLoggingInterface loggerAspect = createProxy(testLogging);
        loggerAspect.calculation(6);
        loggerAspect.calculation(6, 3);
    }
}
