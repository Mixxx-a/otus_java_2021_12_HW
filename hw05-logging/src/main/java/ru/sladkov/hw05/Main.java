package ru.sladkov.hw05;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
        TestLoggingInterface testLogging = TestLoggingLoader.createClass(new TestLoggingImpl());
        testLogging.calculation(6);
        testLogging.calculation(7, 3);
        testLogging.calculation(8, 2, "abc");
    }
}
