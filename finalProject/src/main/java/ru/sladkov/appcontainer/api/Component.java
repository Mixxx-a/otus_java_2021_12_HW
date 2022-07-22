package ru.sladkov.appcontainer.api;

import ru.sladkov.appcontainer.State;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public interface Component<T> {
    long getId();
    State getState();
    String getName();
    Class<?> getInterfaze();
    void setInstance(T object);
    void start();
    void stop();
    void setState(State state);
    List<Component<?>> getReferences();
    void setReferences(List<Component<?>> references);
    T getInstance();
    String getInfo();

    void setDependencies(List<Component<?>> dependencies);
    List<Component<?>> getDependencies();

    void addReference(Component<?> reference);

    List<Field> getReferenceFields();
    void setReferenceFields(List<Field> referenceFields);
}
