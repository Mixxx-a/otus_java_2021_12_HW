package ru.sladkov.hw03;

import ru.sladkov.hw03.annotations.After;
import ru.sladkov.hw03.annotations.Before;
import ru.sladkov.hw03.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SampleTest {

    @Before
    public void setUp() {
//        System.out.println("--- @Before called in test class " + this.toString() + " ---");
        throw new RuntimeException();
    }

    @Test
    public void passingTest1() {
//        System.out.println("--- passingTest1 called in test class " + this.toString() + " ---");
    }

    @Test
    public void passingTest2() {
//        System.out.println("--- passingTest2 called in test class " + this.toString() + " ---");
    }

    @Test
    public void failingTest1() {
//        System.out.println("--- failingTest1 called in test class " + this.toString() + " ---");
        throw new NullPointerException();
    }

    @Test
    public void failingTest2() {
//        System.out.println("--- failingTest2 called in test class " + this.toString() + " ---");
        assertThat(1 + 1).isEqualTo(3);
    }

    @After
    public void tearDown() {
//        System.out.println("--- @After called in test class " + this.toString() + " ---");
    }
}
