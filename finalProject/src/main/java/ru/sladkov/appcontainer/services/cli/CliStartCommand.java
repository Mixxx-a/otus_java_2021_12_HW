package ru.sladkov.appcontainer.services.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "start")
public class CliStartCommand implements Runnable {

    @CommandLine.Parameters()
    long componentId;


    @Override
    public void run() {

    }
}
