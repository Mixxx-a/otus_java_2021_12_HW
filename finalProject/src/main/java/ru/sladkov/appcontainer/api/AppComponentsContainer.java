package ru.sladkov.appcontainer.api;

public interface AppComponentsContainer {
    <C> C getAppComponent(Class<C> componentClass);
    <C> C getAppComponent(String componentName);
    void activateComponent(long id);
    void deactivateComponent(long id);
    void printAllComponents();

}
