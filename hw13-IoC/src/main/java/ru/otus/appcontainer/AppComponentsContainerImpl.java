package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... configClasses) {
        processConfigs(configClasses);
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        processConfigs(configClasses.toArray(new Class<?>[0]));
    }

    private void processConfigs(Class<?>[] configClasses) {
        List<Class<?>> sortedConfigClasses = Stream.of(configClasses)
                .sorted(Comparator.comparingInt(
                        c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .toList();

        for (Class<?> configClass : sortedConfigClasses) {
            processConfig(configClass);
        }
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        try {
            Object configInstance = configClass.getDeclaredConstructor().newInstance();
            Method[] methods = configClass.getDeclaredMethods();
            List<Method> sortedMethods = Stream.of(methods)
                    .filter(method -> method.isAnnotationPresent(AppComponent.class))
                    .sorted(Comparator.comparingInt(c -> c.getAnnotation(AppComponent.class).order()))
                    .toList();

            for (Method method : sortedMethods) {
                AppComponent appComponent = method.getAnnotation(AppComponent.class);
                String componentName = appComponent.name();

                if (appComponentsByName.containsKey(componentName)) {
                    throw new IllegalArgumentException(String.format("Component with name %s already exists", componentName));
                }

                Object component = createComponent(method, configInstance);
                appComponents.add(component);
                appComponentsByName.put(componentName, component);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to process config class: " + configClass.getName(), e);
        }
    }

    private Object createComponent(Method method, Object configInstance) throws Exception {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = Stream.of(parameterTypes)
                .map(this::getAppComponent)
                .toArray();
        return method.invoke(configInstance, args);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<C> components = appComponents.stream()
                .filter(componentClass::isInstance)
                .map(componentClass::cast)
                .toList();

        if (components.size() != 1) {
            throw new RuntimeException(String.format(
                    "Expected one component of type %s, but found %d", componentClass.getName(), components.size()));
        }

        return components.getFirst();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        C component = (C) appComponentsByName.get(componentName);
        if (component == null) {
            throw new RuntimeException(String.format("Component with name %s not found", componentName));
        }
        return component;
    }
}
