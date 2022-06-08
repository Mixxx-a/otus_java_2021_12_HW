package ru.sladkov.hw06.atm;

import ru.sladkov.hw06.atm.impl.Banknote;

public interface AtmOperations {

    void putMoney(Banknote banknote, int numberOfBanknotes);

    void withdrawMoney(int amount);

    void printBalance();


}
