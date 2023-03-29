package ru.sladkov.appcontainer.api;

public interface AppComponentsContainer {
    //    <C> C getAppComponent(Class<C> componentClass);
//    <C> C getAppComponent(String componentName);
    <C> C getAppComponentById(long id);

    void startComponent(long id);

    void stopComponent(long id);

    void printAllComponents();

}
