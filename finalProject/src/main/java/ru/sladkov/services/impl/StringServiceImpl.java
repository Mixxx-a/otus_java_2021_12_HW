package ru.sladkov.services.impl;

import ru.sladkov.appcontainer.annotations.AppComponent;
import ru.sladkov.appcontainer.annotations.Reference;
import ru.sladkov.services.PrintService;
import ru.sladkov.services.StringService;

@AppComponent(name = "String Service", interfaze = StringService.class)
public class StringServiceImpl implements StringService {

    private int counter = 0;

    @Reference
    private PrintService printService;

    @Override
    public void printString() {
        printService.print("Counter - " + counter);
        counter++;
    }
}
