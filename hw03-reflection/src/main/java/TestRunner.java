import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static void runTests(String className) {
        try {
            Class<?> testClass = Class.forName(className);
            Method[] methods = testClass.getDeclaredMethods();

            List<Method> beforeMethods = new ArrayList<>();
            List<Method> testMethods = new ArrayList<>();
            List<Method> afterMethods = new ArrayList<>();

            for (Method method : methods) {
                if (method.isAnnotationPresent(Before.class)) {
                    beforeMethods.add(method);
                } else if (method.isAnnotationPresent(Test.class)) {
                    testMethods.add(method);
                } else if (method.isAnnotationPresent(After.class)) {
                    afterMethods.add(method);
                }
            }

            int passed = 0, failed = 0;

            for (Method testMethod : testMethods) {
                Object testInstance = testClass.getDeclaredConstructor().newInstance();
                try {
                    for (Method beforeMethod : beforeMethods) {
                        beforeMethod.invoke(testInstance);
                    }
                    testMethod.invoke(testInstance);
                    passed++;
                } catch (Exception e) {
                    failed++;
                    System.out.println("Test " + testMethod.getName() + " failed: " + e.getCause());
                } finally {
                    for (Method afterMethod : afterMethods) {
                        afterMethod.invoke(testInstance);
                    }
                }
            }

            System.out.println("Passed: " + passed);
            System.out.println("Failed: " + failed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runTests("MyTests");
    }
}

