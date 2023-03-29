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
    public <C> List<Component<?>> getComponentsByClass(Class<C> componentClass) {
        List<Component<?>> components = new ArrayList<>();
        for (Component<?> component : appComponents) {
            if (componentClass.isAssignableFrom(component.getInterfaze())) {
                components.add(component);
            }
        }
        return components;
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
