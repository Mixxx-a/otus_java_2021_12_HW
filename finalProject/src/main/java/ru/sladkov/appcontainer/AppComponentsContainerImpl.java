package ru.sladkov.appcontainer;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sladkov.ComponentInitializationException;
import ru.sladkov.ContainerInitializationException;
import ru.sladkov.appcontainer.annotations.AppComponent;
import ru.sladkov.appcontainer.annotations.Reference;
import ru.sladkov.appcontainer.api.AppComponentsContainer;
import ru.sladkov.appcontainer.api.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);
    private static final long INITIAL_ID = 0;

    private final ComponentContextImpl componentContext;
    private final Queue<Component<?>> startQueue = new LinkedList<>();
    private long currentComponentId = INITIAL_ID;

    public AppComponentsContainerImpl(String packageName) throws ContainerInitializationException {
        componentContext = new ComponentContextImpl();
        Component<AppComponentsContainer> containerComponent = new ComponentImpl<>(currentComponentId,
                "AppComponentsContainer", AppComponentsContainer.class, AppComponentsContainerImpl.class,
                this, Collections.emptyList());
        currentComponentId++;
        containerComponent.setState(State.ACTIVE);
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

            Component<T> component = new ComponentImpl<>(id, name, interfaze, clazz, instance, referenceFields);

            componentContext.addComponent(id, component);
            logger.info("Registered component {}", component);
            return component;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            logger.error("Unable to init component " + clazz, e);
        }
        return null;
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
                    if (component.getState() == State.SATISFIED) {
                        activateComponent(component);
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

    private <T> void resolveDependencies(Component<T> component) {
        try {
            boolean isSatisfied = satisfyAvailableDependencies(component);
            if (isSatisfied) {
                component.setState(State.SATISFIED);
                logger.info("Dependencies SATISFIED for component {}", component);
            } else {
                component.setState(State.UNSATISFIED);
                logger.info("Dependencies UNSATISFIED for component {}", component);
            }
        } catch (ComponentInitializationException | IllegalAccessException e) {
            logger.error("Unable to resolve dependencies of component " + component);
        }
    }

    private boolean satisfyAvailableDependencies(Component<?> component) throws ComponentInitializationException, IllegalAccessException {
        Map<Field, Component<?>> references = component.getReferences();
        boolean allReferencesSatisfiedFlag = true;
        for (Field referenceField : references.keySet()) {
            if (references.get(referenceField) == null) {
                Class<?> referenceType = referenceField.getType();
                List<Component<?>> referenceComponents = componentContext.getComponentsByClass(referenceType);
                boolean localReferenceSatisfied = false;
                for (Component<?> referenceComponent : referenceComponents) {
                    if (referenceComponent.getState() == State.ACTIVE) {
                        referenceField.setAccessible(true);
                        referenceField.set(component.getInstance(), referenceComponent.getInstance());
                        referenceField.setAccessible(false);
                        references.put(referenceField, referenceComponent);
                        localReferenceSatisfied = true;
                        break;
                    }
                }
                if (!localReferenceSatisfied) {
                    allReferencesSatisfiedFlag = false;
                }
            }
        }
        return allReferencesSatisfiedFlag;
    }

    private void activateComponent(Component<?> component) {
        component.start();
        component.setState(State.ACTIVE);
        logger.info("ACTIVATED component {}", component);
    }

    private void deactivateComponent(Component<?> component) {
        component.stop();
        component.setState(State.SATISFIED);
        logger.info("DEACTIVATED component {}", component);
    }

    @Override
    public <C> C getAppComponentById(long id) {
        Component<C> component = componentContext.getComponent(id);
        if (component.getState() != State.ACTIVE) {
            logger.error("Unable to get AppComponent because it is not in active state: " + component);
            return null;
        }
        return component.getInstance();
    }

    @Override
    public void startComponent(long id) {
        Component<?> component = componentContext.getComponent(id);
        activateComponent(component);
        setNewInstance(component);
        refreshReferences();
        injectReferences(component);
    }

    private <T> void setNewInstance(Component<T> component) {
        Class<?> clazz = component.getImplementationClazz();
        try {
            T newInstance = (T) clazz.getDeclaredConstructor().newInstance();
            component.setInstance(newInstance);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            logger.error("Unable to set new instance for component " + component);
        }
    }

    @Override
    public void stopComponent(long id) {
        Component<?> component = componentContext.getComponent(id);
        deactivateComponent(component);
        component.setInstance(null);
        refreshReferences();
    }

    @Override
    public void printAllComponents() {
        for (Component<?> component : componentContext.getAllComponents()) {
            System.out.println(component.getInfo());
        }
    }

    private void refreshReferences() {
        for (Component<?> component : componentContext.getAllComponents()) {
            Map<Field, Component<?>> references = component.getReferences();
            for (Map.Entry<Field, Component<?>> entry : references.entrySet()) {
                Field referenceField = entry.getKey();
                Component<?> referenceComponent = entry.getValue();
                if (referenceComponent == null) {
                    //Проверка на доступность компонента в контексте, добавление
                    Class<?> referenceType = referenceField.getType();
                    List<Component<?>> referenceComponents = componentContext.getComponentsByClass(referenceType);
                    boolean localReferenceSatisfied = false;
                    for (Component<?> dependency : referenceComponents) {
                        if (dependency.getState() == State.ACTIVE) {
                            try {
                                referenceField.setAccessible(true);
                                referenceField.set(component.getInstance(), dependency.getInstance());
                                referenceField.setAccessible(false);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                            references.put(referenceField, dependency);
                            localReferenceSatisfied = true;
                            break;
                        }
                    }
                    logger.info("RefreshReferences: Satisfy reference for field {}", entry.getKey());
                } else if (referenceComponent.getState() != State.ACTIVE) {
                    //Удаление референса
                    entry.setValue(null);
                    try {
                        referenceField.setAccessible(true);
                        referenceField.set(component.getInstance(), null);
                        referenceField.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        logger.error("Can't set referenceField", e);
                    }
                    component.setState(State.UNSATISFIED);
                    logger.info("RefreshReferences: Unsatisfied reference for field {}", entry.getKey());
                    logger.info("Trying to satisfy dependencies...");
                    resolveDependencies(component);
                }
            }
        }
    }

    private <T> void injectReferences(Component<T> component) {
        Map<Field, Component<?>> references = component.getReferences();
        for (Map.Entry<Field, Component<?>> entry : references.entrySet()) {
            Field referenceField = entry.getKey();
            Component<?> referenceComponent = entry.getValue();
            if (referenceComponent != null) {
                try {
                    referenceField.setAccessible(true);
                    referenceField.set(component.getInstance(), referenceComponent.getInstance());
                    referenceField.setAccessible(false);
                } catch (IllegalAccessException e) {
                    logger.error("Can't set referenceField", e);
                }
            }
        }
    }
}
