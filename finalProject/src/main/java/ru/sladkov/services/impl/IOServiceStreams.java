package ru.sladkov.services.impl;

import ru.sladkov.appcontainer.annotations.AppComponent;
import ru.sladkov.services.IOService;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@AppComponent(name = "ioService", interfaze = IOService.class)
public class IOServiceStreams implements IOService {
    private final PrintStream out;
    private final Scanner in;

    public IOServiceStreams() {
        this.out = System.out;
        this.in = new Scanner(System.in);
    }


    @Override
    public void out(String message) {
        out.println(message);
    }

    @Override
    public String readLn(String prompt) {
        out(prompt);
        return in.next();
    }

    @Override
    public int readInt(String prompt) {
        out(prompt);
        return in.nextInt();
    }
}
