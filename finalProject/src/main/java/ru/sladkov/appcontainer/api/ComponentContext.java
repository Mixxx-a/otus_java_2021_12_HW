package ru.sladkov.appcontainer.api;

import java.util.List;

public interface ComponentContext {
    <C> void addComponent(long id, Component<C> component);
    <C> Component<C> getComponent(Class<C> componentClass);
    <C> Component<C> getComponent(long id);
    List<Component<?>> getAllComponents();


}
