package ru.sladkov.services.impl;

import ru.sladkov.appcontainer.annotations.AppComponent;
import ru.sladkov.services.PrintService;

@AppComponent(name = "Print Service #1", interfaze = PrintService.class)
public class PrintServiceImpl1 implements PrintService {

    @Override
    public void print(String string) {
        System.out.println("From PrintServiceImpl1: " + string);
    }
}