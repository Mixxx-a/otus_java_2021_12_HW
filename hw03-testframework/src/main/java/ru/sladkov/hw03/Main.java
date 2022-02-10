package ru.sladkov.hw03;

import ru.sladkov.hw03.tests.ExceptionInAfterTest;
import ru.sladkov.hw03.tests.ExceptionInBeforeAndAfterTest;
import ru.sladkov.hw03.tests.ExceptionInBeforeTest;
import ru.sladkov.hw03.tests.SampleTest;

import static ru.sladkov.hw03.TestsExecutor.executeTests;

public class Main {

    public static void main(String[] args) {
        executeTests(SampleTest.class);
        executeTests(ExceptionInBeforeTest.class);
        executeTests(ExceptionInAfterTest.class);
        executeTests(ExceptionInBeforeAndAfterTest.class);
    }
}
