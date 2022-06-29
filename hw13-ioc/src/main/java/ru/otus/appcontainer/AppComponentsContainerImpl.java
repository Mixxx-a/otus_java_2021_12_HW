package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        List<Method> methods = getComponentsInitializationMethods(configClass);
        try {
            Object config = configClass.getConstructor().newInstance();
            for (Method method : methods) {
                List<Object> arguments = new ArrayList<>();
                Class<?>[] parameterTypes = method.getParameterTypes();
                for (Class<?> clazz : parameterTypes) {
                    Object appComponent = getAppComponent(clazz);
                    arguments.add(appComponent);
                }
                Object newInstance = method.invoke(config, arguments.toArray());
                appComponents.add(newInstance);
                appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), newInstance);
            }
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private List<Method> getComponentsInitializationMethods(Class<?> configClass) {
        List<Method> componentsInitializationMethods = new ArrayList<>();
        checkConfigClass(configClass);
        Method[] methods = configClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                componentsInitializationMethods.add(method);
            }
        }
        componentsInitializationMethods.sort((leftMethod, rightMethod) -> {
            int orderLeft = leftMethod.getAnnotation(AppComponent.class).order();
            int orderRight = rightMethod.getAnnotation(AppComponent.class).order();
            return orderLeft - orderRight;
        });

        return componentsInitializationMethods;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Object component : appComponents) {
            if (componentClass.equals(component.getClass())) {
                return (C) component;
            }
            Class<?>[] implementedInterfaces = component.getClass().getInterfaces();
            Optional<Class<?>> componentClassInterface = Arrays.stream(implementedInterfaces)
                    .filter(interfaze -> interfaze.equals(componentClass)).findFirst();
            if (componentClassInterface.isPresent()) {
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
