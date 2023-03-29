package ru.sladkov.appcontainer.api;

import ru.sladkov.appcontainer.State;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface Component<T> {
    long getId();

    State getState();

    void setState(State state);

    String getName();

    Class<?> getInterfaze();

    Class<?> getImplementationClazz();

    void start();

    void stop();

    T getInstance();

    void setInstance(T object);

    String getInfo();

    Map<Field, Component<?>> getReferences();
}
