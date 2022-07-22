package ru.sladkov.services.impl;

import ru.sladkov.appcontainer.annotations.AppComponent;
import ru.sladkov.appcontainer.annotations.Reference;
import ru.sladkov.model.Player;
import ru.sladkov.services.IOService;
import ru.sladkov.services.PlayerService;

@AppComponent(name = "playerService", priority = 10, interfaze = PlayerService.class)
public class PlayerServiceImpl implements PlayerService {

    @Reference
    private IOService ioService;

    @Override
    public Player getPlayer() {
        ioService.out("Представьтесь пожалуйста");
        String playerName = ioService.readLn("Введите имя: ");
        return new Player(playerName);
    }
}
