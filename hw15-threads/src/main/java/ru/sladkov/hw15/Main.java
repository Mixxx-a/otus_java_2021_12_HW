package ru.sladkov.hw15;

public class Main {

    public static void main(String[] args) {
        Counter counter = new Counter();
        Thread thread1 = new Thread(() -> counter.action(true, 1));
        Thread thread2 = new Thread(() -> counter.action(false,2));

        thread1.start();
        thread2.start();
    }
}