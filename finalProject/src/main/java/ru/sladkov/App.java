package ru.sladkov;

import ru.sladkov.appcontainer.AppComponentsContainerImpl;
import ru.sladkov.appcontainer.api.AppComponentsContainer;
import ru.sladkov.services.StringService;

public class App {

    public static void main(String[] args) throws Exception {
        AppComponentsContainer container = new AppComponentsContainerImpl("ru.sladkov.services.impl");

//        HelloService helloService = container.getAppComponent(HelloService.class);
//        helloService.hello();
        container.printAllComponents();

        StringService stringService = container.getAppComponentById(5);
        stringService.printString();
        stringService.printString();

        container.stopComponent(6);
        container.printAllComponents();

        stringService.printString();
        stringService.printString();

        container.stopComponent(5);
        container.startComponent(6);
        container.startComponent(5);
        container.printAllComponents();

        stringService = container.getAppComponentById(5);
        stringService.printString();
        stringService.printString();


//      ContainerCLI cli = container.getAppComponent(Cli.class);
//      cli.run();


        // Приложение должно работать в каждом из указанных ниже вариантов
//        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
        //GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
        //GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

//        gameProcessor.startGame();
    }
}
