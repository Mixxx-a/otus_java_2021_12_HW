package ru.otus;

public class ContainerInitializationException extends Exception {

    public ContainerInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContainerInitializationException(String message) {
        super(message);
    }
}
