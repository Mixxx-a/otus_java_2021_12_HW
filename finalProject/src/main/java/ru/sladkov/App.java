package ru.sladkov;

import ru.sladkov.appcontainer.AppComponentsContainerImpl;
import ru.sladkov.appcontainer.api.AppComponentsContainer;
import ru.sladkov.services.HelloService;

public class App {

    public static void main(String[] args) throws Exception {
//        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);
        AppComponentsContainer container = new AppComponentsContainerImpl("ru.sladkov.services.impl");

        HelloService helloService = container.getAppComponent(HelloService.class);
        helloService.hello();
        container.printAllComponents();

//      ContainerCLI cli = container.getAppComponent(Cli.class);
//      cli.run();


//        Scanner in = new Scanner(System.in);
//        String command = "";
//        while ("exit".equals(command)) {
//            System.out.println("Input command");
//            command = in.next();
//        }



        // Приложение должно работать в каждом из указанных ниже вариантов
//        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
        //GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
        //GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

//        gameProcessor.startGame();
    }
}
