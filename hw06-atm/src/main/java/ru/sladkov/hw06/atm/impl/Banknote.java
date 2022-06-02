package ru.sladkov.hw06.atm.impl;

public class Banknote {

    private final String name;
    private final int value;

    public Banknote(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
