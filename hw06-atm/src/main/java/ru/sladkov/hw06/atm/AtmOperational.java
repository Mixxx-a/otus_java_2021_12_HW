package ru.sladkov.hw06.atm;

import ru.sladkov.hw06.atm.impl.Banknote;

public interface AtmOperational {

    void putMoney(Banknote banknote, int numberOfBanknotes);

    void withdrawMoney(int amount);

    void printBalance();


}
