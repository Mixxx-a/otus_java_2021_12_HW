package ru.sladkov.hw06.atm;

public interface BanknotePack {

    String getBanknoteName();

    int getBanknoteValue();

    int getAmount();

    void addBanknotes(int quantity);

    void removeBanknotes(int quantity);


}
