package ru.sladkov.hw05;

public class Main {
    public static void main(String[] args) {
        TestLoggingInterface testLogging = TestLoggingLoader.createClass();
        testLogging.calculation(6);
        testLogging.calculation(7, 3);
        testLogging.calculation(8, 2, "abc");
    }
}
