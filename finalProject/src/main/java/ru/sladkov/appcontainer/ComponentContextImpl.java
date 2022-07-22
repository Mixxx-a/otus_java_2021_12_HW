package ru.sladkov.appcontainer;

import ru.sladkov.appcontainer.api.Component;
import ru.sladkov.appcontainer.api.ComponentContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentContextImpl implements ComponentContext {

    private final List<Component<?>> appComponents = new ArrayList<>();
    private final Map<Long, Component<?>> appComponentsById = new HashMap<>();

    @Override
    public <C> void addComponent(long id, Component<C> component) {
        appComponents.add(component);
        appComponentsById.put(id, component);
    }

    @Override
    public  <C> Component<C> getComponent(Class<C> componentClass) {
        for (Component<?> component : appComponents) {
            if (componentClass.isAssignableFrom(component.getInterfaze())) {
                return (Component<C>) component;
            }
        }
        return null;
    }

    @Override
    public <C> Component<C> getComponent(long id) {
        return (Component<C>) appComponentsById.get(id);
    }

    @Override
    public List<Component<?>> getAllComponents() {
        return appComponents;
    }
}
