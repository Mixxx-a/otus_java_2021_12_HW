package ru.sladkov.appcontainer.services.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "appcontainer", subcommands = {
        CliLcCommand.class,
        CliStartCommand.class,
        CliStopCommand.class
})
public class CliCommands {
}
