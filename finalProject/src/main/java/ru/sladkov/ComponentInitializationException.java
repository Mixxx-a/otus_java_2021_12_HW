package ru.sladkov;

public class ComponentInitializationException extends Exception {

    public ComponentInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentInitializationException(String message) {
        super(message);
    }
}