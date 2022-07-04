package ru.otus.appcontainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.ContainerInitializationException;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws ContainerInitializationException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws ContainerInitializationException {
        List<Method> methods = getComponentsInitializationMethods(configClass);
        try {
            Object config = configClass.getConstructor().newInstance();
            for (Method method : methods) {
                String componentName = method.getAnnotation(AppComponent.class).name();
                if (appComponentsByName.containsKey(componentName)) {
                    logger.warn("Component with name {} already exists, skipping", componentName);
                    continue;
                }

                Object[] arguments = getMethodArguments(method);
                Object newInstance = method.invoke(config, arguments);
                appComponents.add(newInstance);
                appComponentsByName.put(componentName, newInstance);
            }
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            throw new ContainerInitializationException("Can't process config", e);
        }
    }

    private List<Method> getComponentsInitializationMethods(Class<?> configClass) {
        checkConfigClass(configClass);
        Method[] methods = configClass.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(method -> method.getAnnotation(AppComponent.class).order()))
                .toList();
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object[] getMethodArguments(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        int parameterTypesLength = parameterTypes.length;
        Object[] arguments = new Object[parameterTypesLength];
        for (int i = 0; i < parameterTypesLength; i++) {
            arguments[i] = getAppComponent(parameterTypes[i]);
        }
        return arguments;
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Object component : appComponents) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                return (C) component;
            }
        }
        throw new RuntimeException("Can't find component " + componentClass.getName());
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        } else throw new RuntimeException("Can't find component with name " + componentName);
    }
}
