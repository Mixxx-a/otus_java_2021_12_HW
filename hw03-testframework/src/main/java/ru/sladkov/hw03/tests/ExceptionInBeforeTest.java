package ru.sladkov.hw03.tests;

import ru.sladkov.hw03.annotations.After;
import ru.sladkov.hw03.annotations.Before;
import ru.sladkov.hw03.annotations.Test;

public class ExceptionInBeforeTest {

    @Before
    public void setUp() {
        System.out.println("--- @Before called in test class " + this.toString() + " ---");
        throw new RuntimeException();
    }

    @Test
    public void passingTest1() {
        System.out.println("--- passingTest1 called in test class " + this.toString() + " ---");
    }

    @After
    public void tearDown() {
        System.out.println("--- @After called in test class " + this.toString() + " ---");
    }
}
