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

        //Получение StringService, в зависимостях у которого есть PrintService
        StringService stringService = container.getAppComponentById(5);
        stringService.printString();
        stringService.printString();

        //Остановка PrintService 1
        container.stopComponent(6);

        //В StringService динамически подгужается другой компонент PrintService 2, без пересоздания инстанса
        stringService.printString();
        stringService.printString();

        //Остановка и запуск StringService. Идёт пересоздания инстанса.
        container.stopComponent(5);
        container.startComponent(5);

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
