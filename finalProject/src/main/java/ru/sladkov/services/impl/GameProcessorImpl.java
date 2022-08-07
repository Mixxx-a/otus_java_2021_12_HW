package ru.sladkov.services.impl;

import ru.sladkov.appcontainer.annotations.AppComponent;
import ru.sladkov.appcontainer.annotations.Reference;
import ru.sladkov.model.Equation;
import ru.sladkov.model.GameResult;
import ru.sladkov.model.Player;
import ru.sladkov.services.EquationPreparer;
import ru.sladkov.services.GameProcessor;
import ru.sladkov.services.IOService;
import ru.sladkov.services.PlayerService;

import java.util.List;

@AppComponent(name = "gameProcessor", priority = 10, interfaze = GameProcessor.class)
public class GameProcessorImpl implements GameProcessor {

    private static final String MSG_HEADER = "Проверка знаний таблицы умножения";
    private static final String MSG_INPUT_BASE = "Введите цифру от 1 до 10";
    private static final String MSG_RIGHT_ANSWER = "Верно\n";
    private static final String MSG_WRONG_ANSWER = "Не верно\n";

    @Reference
    private IOService ioService;
    @Reference
    private EquationPreparer equationPreparer;
    @Reference
    private PlayerService playerService;

    @Override
    public void startGame() {
        ioService.out(MSG_HEADER);
        ioService.out("---------------------------------------");
        Player player = playerService.getPlayer();
        GameResult gameResult = new GameResult(player);

        int base = ioService.readInt(MSG_INPUT_BASE);
        List<Equation> equations = equationPreparer.prepareEquationsFor(base);
        equations.forEach(e -> {
            boolean isRight = ioService.readInt(e.toString()) == e.getResult();
            gameResult.incrementRightAnswers(isRight);
            ioService.out(isRight ? MSG_RIGHT_ANSWER : MSG_WRONG_ANSWER);
        });
        ioService.out(gameResult.toString());
    }
}
