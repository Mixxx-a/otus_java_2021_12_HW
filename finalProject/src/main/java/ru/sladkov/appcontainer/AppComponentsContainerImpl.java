package ru.sladkov.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sladkov.App;
import ru.sladkov.ComponentInitializationException;
import ru.sladkov.ContainerInitializationException;
import ru.sladkov.appcontainer.annotations.AppComponent;
import ru.sladkov.appcontainer.annotations.Reference;
import ru.sladkov.appcontainer.api.AppComponentsContainer;
import ru.sladkov.appcontainer.api.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);
    private static final long INITIAL_ID = 0;

    private final ComponentContextImpl componentContext;
    private final Queue<Component<?>> startQueue = new LinkedList<>();
    private long currentComponentId = INITIAL_ID;
//    private final ComponentStateProducer

//    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws ContainerInitializationException {
//        try {
//            componentContext = new ComponentContextImpl();
//            configObject = initialConfigClass.getConstructor().newInstance();
//            Component<AppComponentsContainer> containerComponent = new ComponentImpl<>(currentComponentId, null);
//            currentComponentId++;
//            containerComponent.setInstance(this);
//            containerComponent.setState(State.ACTIVE);
//            processConfig(initialConfigClass);
//        } catch (Exception e) {
//            throw new ContainerInitializationException("Can't initialize container", e);
//        }
//    }

    public AppComponentsContainerImpl(String packageName) throws ContainerInitializationException {
        componentContext = new ComponentContextImpl();
        Set<Class<?>> classes = getClasses(packageName);
        processClasses(classes);
    }

    private Set<Class<?>> getClasses(String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(AppComponent.class);
    }


    private void processClasses(Set<Class<?>> classes) throws ContainerInitializationException {
        List<Class<?>> sortedClasses = classes.stream()
                .sorted(Comparator.comparing(clazz -> clazz.getAnnotation(AppComponent.class).priority()))
                .toList();

        try {
            for (Class<?> clazz : sortedClasses) {
                Component<?> component = initComponent(currentComponentId, clazz);
                startQueue.offer(component);
                currentComponentId++;
            }
            startComponents();
        } catch (ComponentInitializationException e) {
            throw new ContainerInitializationException("Can't process config", e);
        }
    }

    private <T> Component<T> initComponent(long id, Class<T> clazz) {
        try {
            AppComponent appAnnotation = clazz.getDeclaredAnnotation(AppComponent.class);
            String name = appAnnotation.name();
            Class<?> interfaze = appAnnotation.interfaze();
            T instance = clazz.getDeclaredConstructor().newInstance();
            List<Field> referenceFields = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Reference.class))
                    .toList();

            Component<T> component = new ComponentImpl<>(id, name, interfaze, instance);
            component.setReferenceFields(referenceFields);

            componentContext.addComponent(id, component);
            logger.info("Registered component {}", component);
            return component;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    private <T> void resolveDependencies(Component<T> component) {
        List<Component<?>> dependencies;
        try {
            dependencies = getComponentDependencies(component);
        } catch (ComponentInitializationException | IllegalAccessException e) {
            component.setState(State.UNRESOLVED);
            return;
        }
        component.setDependencies(dependencies);
        component.setState(State.RESOLVED);
        logger.info("Dependencies RESOLVED for component {}", component);
    }

    private <T> void startComponent(Component<T> component) {
        component.start();
        component.setState(State.ACTIVE);
        logger.info("STARTED component {}", component);
    }

    private List<Component<?>> getComponentDependencies(Component<?> component) throws ComponentInitializationException, IllegalAccessException {
        List<Field> referenceFields = component.getReferenceFields();
        List<Component<?>> components = new ArrayList<>();
        for (Field referenceField : referenceFields) {
            Class<?> referenceType = referenceField.getType();
            Component<?> dependency = componentContext.getComponent(referenceType);
            if (dependency != null && dependency.getState() == State.ACTIVE) {
                referenceField.setAccessible(true);
                referenceField.set(component.getInstance(), dependency.getInstance());
                referenceField.setAccessible(false);
                components.add(dependency);
            } else throw new ComponentInitializationException("Can't get argument " + referenceType.getName()
                    + " for initializing component ");
            //++++++COMPONENT!!!!
        }
        return components;
    }

    private void startComponents() throws ComponentInitializationException {
        boolean finishFlag = false;
        while (!finishFlag) {
            finishFlag = true;
            int initialQueueSize = startQueue.size();
            for (int i = 0; i < initialQueueSize; i++) {
                Component<?> component = startQueue.poll();
                if (component != null) {
                    resolveDependencies(component);
                    if (component.getState() == State.RESOLVED) {
                        startComponent(component);
                        finishFlag = false;
                    } else {
                        startQueue.offer(component);
                    }
                }
            }
        }
        if (!startQueue.isEmpty()) {
            logger.error("Unable to initialize next components:");
            for (Component<?> component : startQueue) {
                logger.error(component.toString());
            }
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return componentContext.getComponent(componentClass).getInstance();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return null;
    }

    @Override
    public void activateComponent(long id) {
        Component<?> component = componentContext.getComponent(id);
//        activateComponent(component);

    }

    @Override
    public void deactivateComponent(long id) {
        Component<?> component = componentContext.getComponent(id);
        component.stop();
        component.setInstance(null);
        component.setState(State.UNRESOLVED);
        List<Component<?>> references = component.getReferences();
//        for (Component<?> reference : references) {
//
//        }
        refreshReferences();
    }

    @Override
    public void printAllComponents() {
        for (Component<?> component : componentContext.getAllComponents()) {
            System.out.println(component.getInfo());
        }
    }

    private void refreshReferences() {

    }
}
