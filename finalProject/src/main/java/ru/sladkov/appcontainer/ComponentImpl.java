package ru.sladkov.appcontainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sladkov.appcontainer.api.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentImpl<T> implements Component<T> {
    private static final Logger logger = LoggerFactory.getLogger(ComponentImpl.class);
    private final long id;
    private final String name;
    private final Class<?> interfaze;
    private final Class<?> implementationClazz;
    private final Map<Field, Component<?>> references = new HashMap<>();
    private T instance;
    private State state;

    public ComponentImpl(long id, String name, Class<?> interfaze, Class<?> implementationClazz, T instance, List<Field> referenceFields) {
        this.id = id;
        this.name = name;
        this.interfaze = interfaze;
        this.implementationClazz = implementationClazz;
        this.instance = instance;
        this.state = State.UNSATISFIED;
        referenceFields.forEach(field -> references.put(field, null));
    }

    @Override
    public Map<Field, Component<?>> getReferences() {
        return references;
    }

    public Class<?> getInterfaze() {
        return this.interfaze;
    }

    @Override
    public Class<?> getImplementationClazz() {
        return this.implementationClazz;
    }

    public T getInstance() {
        return instance;
    }

    public void setInstance(T instance) {
        this.instance = instance;
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
//        StringBuilder dependenciesInfo = new StringBuilder();
//        for (Component<?> dependency : this.getDependencies()) {
//            dependenciesInfo.append(dependency.getInterfaze().getSimpleName())
//                    .append(",");
//        }
//        dependenciesInfo.setLength(50);
//        StringBuilder referencesInfo = new StringBuilder();
//        for (Component<?> reference : this.getReferences()) {
//            referencesInfo.append(reference.getInterfaze().getSimpleName())
//                    .append(",");
//        }
//        referencesInfo.setLength(30);

        return idInfo +
                "|" +
                nameInfo +
                "|" +
                stateInfo +
                "|" +
                clazzInfo;
//                "|" +
//                dependenciesInfo +
//                "|" +
//                referencesInfo;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
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
        this.state = State.SATISFIED;
    }

    @Override
    public String toString() {
        return "Component{"
                + "id = " + this.getId()
                + ", name = " + this.getName()
                + ", state = " + this.getState()
                + "}";
    }

}

