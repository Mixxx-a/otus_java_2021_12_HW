package ru.sladkov.appcontainer;

import ru.sladkov.appcontainer.annotations.AppComponent;
import ru.sladkov.appcontainer.api.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentImpl<T> implements Component<T> {
    private final long id;
    private final String name;
    private final Class<?> interfaze;
    private T instance;
    private State state;
    private List<Field> referenceFields = new ArrayList<>();
    Map<Field, Component<?>> referenc111 = new HashMap<>();
    private List<Component<?>> dependencies = new ArrayList<>();
    private List<Component<?>> references = new ArrayList<>();

    public ComponentImpl(long id, String name, Class<?> interfaze, T instance) {
        this.id = id;
        this.name = name;
        this.interfaze = interfaze;
        this.instance = instance;
        this.state = State.UNSATISFIED;
    }

    public List<Field> getReferenceFields() {
        return referenceFields;
    }

    public void setReferenceFields(List<Field> referenceFields) {
        this.referenceFields = referenceFields;
    }

    public Class<?> getInterfaze() {
        return this.interfaze;
    }

    public T getInstance() {
        return instance;
    }

    @Override
    public String getInfo() {
        StringBuilder idInfo = new StringBuilder();
        idInfo.append(this.getId()).setLength(2);
        StringBuilder nameInfo = new StringBuilder(this.name);
        nameInfo.setLength(20);
        StringBuilder stateInfo = new StringBuilder(this.getState().toString());
        stateInfo.setLength(10);
        StringBuilder clazzInfo = new StringBuilder(this.getInterfaze().getSimpleName());
        clazzInfo.setLength(20);
        StringBuilder dependenciesInfo = new StringBuilder();
        for (Component<?> dependency : this.getDependencies()) {
            dependenciesInfo.append(dependency.getInterfaze().getSimpleName())
                    .append(",");
        }
        dependenciesInfo.setLength(50);
        StringBuilder referencesInfo = new StringBuilder();
        for (Component<?> reference : this.getReferences()) {
            referencesInfo.append(reference.getInterfaze().getSimpleName())
                    .append(",");
        }
        referencesInfo.setLength(30);

        return idInfo +
                "|" +
                nameInfo +
                "|" +
                stateInfo +
                "|" +
                clazzInfo +
                "|" +
                dependenciesInfo +
                "|" +
                referencesInfo;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public List<Component<?>> getReferences() {
        return this.references;
    }

    @Override
    public void setReferences(List<Component<?>> references) {
        this.references = references;
    }

//    public List<Class<?>> getInterfaces() {
//        return interfaces;
//    }
//
//    public void setInterfaces(List<Class<?>> interfaces) {
//        this.interfaces = interfaces;
//    }

    public List<Component<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public void addReference(Component<?> reference) {
        this.references.add(reference);
    }

    public void setDependencies(List<Component<?>> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void start() {
//        List<Method> activateMethods = Arrays.stream(this.clazz.getDeclaredMethods())
//                .filter(method -> method.isAnnotationPresent(Activate.class))
//                .toList();
//        for(Method activateMethod : activateMethods) {
//            activateMethod.invoke()
//        }
        this.state = State.ACTIVE;
    }

    @Override
    public void stop() {
        this.state = State.STOPPING;
    }

    @Override
    public String toString() {
        return "Component{"
                + "id = " + this.getId()
                + ", name = " + this.getName()
                + ", state = " + this.getState()
                + "}";
    }


//    public static class Builder {
//        private long id;
//        private String name;
//        private Class<T> clazz;
//        private Method initMethod;
//        private boolean isStartComponent;
//
//        Builder
//
//    }


}

