package ru.sladkov.services.impl;

import ru.sladkov.appcontainer.annotations.Activate;
import ru.sladkov.appcontainer.annotations.AppComponent;
import ru.sladkov.appcontainer.annotations.Deactivate;
import ru.sladkov.services.HelloService;

@AppComponent(name = "helloService", priority = 5, interfaze = HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public void hello() {
        System.out.println("Hello!");
    }

    @Activate
    public void start() {
        System.out.println("Hello Service started!");
    }

    @Deactivate
    public void stop() {
        System.out.println("Hello Service stopped!");
    }
}
