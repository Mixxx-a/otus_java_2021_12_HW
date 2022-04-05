package ru.sladkov.hw03;

import ru.sladkov.hw03.tests.*;

import static ru.sladkov.hw03.TestsExecutor.executeTests;

public class Main {

    public static void main(String[] args) {
        executeTests(SampleTest.class);
        executeTests(ExceptionInBeforeTest.class);
        executeTests(ExceptionInAfterTest.class);
        executeTests(ExceptionInBeforeAndAfterTest.class);
        executeTests(AbstractClassTest.class);
        executeTests(ConstuctorWithParametersTest.class);
    }
}
